package com.example.oud.user.fragments.profile;

import androidx.annotation.IdRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OptionsFragment;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.RenameFragment;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.authentication.MainActivity;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends ConnectionAwareFragment<ProfileViewModel> {

    private ImageView profileImageView;
    private TextView profileDisplaynameTextView;
    private BottomNavigationView navigationView;
    private ImageButton renameButton;
    private ImageButton optionsButton;
    private Button followButton;
    private Button unFollowButton;




    private String userId;
    private String displayName;

    ProfilePlaylistsFragment profilePlaylistsFragment;
    ProfileFollowersFragment profileFollowersFragment;
    ProfileFollowingFragment profileFollowingFragment;

    Bitmap oldImage;

    private boolean isMyProfile;



    private final int RESULT_LOAD_IMG=111;


    public ProfileFragment(Activity activity){
        super(ProfileViewModel.class,
                R.layout.fragment_profile,
                activity.findViewById(R.id.progress_bar_user_activity),
                activity.findViewById(R.id.block_view),
                null);
    }

    public static ProfileFragment newInstance(String userId,Activity activity) {
        ProfileFragment profileFragment = new ProfileFragment(activity);
        profileFragment.setUserId(userId);
        return profileFragment;
    }



    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            String userId) {

        FragmentManager manager = activity.getSupportFragmentManager();

        ProfileFragment profileFragment = ProfileFragment.newInstance(userId,activity);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, profileFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setButtonsOnClickListener();

        profilePlaylistsFragment= ProfilePlaylistsFragment.newInstance(userId,getActivity());
        profileFollowersFragment= ProfileFollowersFragment.newInstance(userId,getActivity());
        profileFollowingFragment= ProfileFollowingFragment.newInstance(userId,getActivity());

        navigationView.setSelectedItemId(R.id.navigation_profile_playlists);
        handleAuthorization();
        handleFollowChecks();




        if(userId!=null) {

            String token = OudUtils.getToken(getContext());
            mViewModel.getProfile(userId,token).observe(getViewLifecycleOwner(), new Observer<ProfilePreview>() {
                @Override
                public void onChanged(ProfilePreview profilePreview) {
                    if(profilePreview !=null){

                        profileDisplaynameTextView.setText(profilePreview.getDisplayName());
                            Log.e("profile fragment","number of images :"+profilePreview.getImages().length);
                            String imageUrl = OudUtils.convertImageToFullUrl(profilePreview.getImages()[0]);
                            Glide.with(getContext()).asBitmap().load(imageUrl).into(profileImageView);
                            Log.e("profile fragment",imageUrl);


                    }}
            });

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        oldImage = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();

        ConnectionStatusListener undoUpdateImage = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                Toast.makeText(getContext(),"image updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionFailure() {
                Glide.with(getContext())
                        .asBitmap()
                        .load(oldImage)
                        .into(profileImageView);
                oldImage=null;
            }
        };

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                Log.e("Profile Fragment",imageUri.getPath());

                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                profileImageView.setImageBitmap(selectedImage);

                Glide.with(getContext())
                        .asBitmap()
                        .load(imageUri)
                        .into(profileImageView);

                String token = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE).getString("token","0000");
                Context context = ((Activity)getActivity()).getApplicationContext();
                mViewModel.updateProfileImage(token,imageUri,selectedImage,context,undoUpdateImage);

            } catch (FileNotFoundException e) {
                Log.e("Profile Fragment",e.getMessage());
                e.printStackTrace();

            }

        }else {

        }

    }

    private void initializeViews(View v){
        profileImageView = v.findViewById(R.id.image_profile_fragment);
        profileDisplaynameTextView = v.findViewById(R.id.text_profile_fragment_display_name);
        navigationView = v.findViewById(R.id.navigation_bar_profile);
        renameButton = v.findViewById(R.id.btn_rename_profile);
        optionsButton = v.findViewById(R.id.btn_profile_options);
        followButton = v.findViewById(R.id.btn_profile_follow);
        unFollowButton = v.findViewById(R.id.btn_profile_unfollow);

        followButton.setEnabled(false);
    }

    private void setButtonsOnClickListener(){
        View.OnClickListener updateImageOnClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }
        };


        View.OnClickListener renameOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RenameFragment.showRenameFragment(getActivity(),R.id.nav_host_fragment,profileDisplaynameTextView.getText().toString(),profileDisplaynameTextView);

                //RenameDisplayNameWithPasswordFragment fragment = RenameDisplayNameWithPasswordFragment.newInstance(getActivity(),)
            }
        };
        renameButton.setOnClickListener(renameOnClickListener);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OptionsFragment.builder(getActivity())
                        .addItem(R.drawable.ic_camera,"Update profile Picture",updateImageOnClickListener)
                        .addItem(R.drawable.ic_rename, "Change displayName",renameOnClickListener)
                        .show();
            }
        });


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_profile_playlists:
                        openFragment(profilePlaylistsFragment);
                        return true;
                    case R.id.navigation_profile_followers:
                        openFragment(profileFollowersFragment);
                        return true;
                    case R.id.navigation_profile_following:
                        openFragment(profileFollowingFragment);
                }

                return false;
            }
        });

        setFollowButtonClickListener();
        setUnFollowButtonClickListener();
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }


    private void openFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_host_profile,fragment).addToBackStack(null).commit();
    }

    private void handleAuthorization(){
        String myUserId =OudUtils.getUserId(getContext());
        if(!userId.equals(myUserId)) {
            isMyProfile=false;
            renameButton.setVisibility(View.GONE);
            optionsButton.setVisibility(View.GONE);
            followButton.setVisibility(View.VISIBLE);
            unFollowButton.setVisibility(View.INVISIBLE);
        }else {
            isMyProfile=true;
            followButton.setVisibility(View.GONE);
            unFollowButton.setVisibility(View.GONE);
        }



    }

    private void handleFollowChecks() {
        if (!isMyProfile) {
            mViewModel.checkIfIFollowThisUser(OudUtils.getToken(getContext()), userId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean iFollowThisUser) {
                    if (iFollowThisUser) {
                        followButton.setVisibility(View.INVISIBLE);
                        unFollowButton.setVisibility(View.VISIBLE);
                        unFollowButton.setEnabled(true);
                    } else {
                        unFollowButton.setVisibility(View.INVISIBLE);
                        followButton.setVisibility(View.VISIBLE);
                        followButton.setEnabled(true);
                    }
                }
            });


        }
    }

    private void setFollowButtonClickListener(){
        ConnectionStatusListener followButtonConnectionListener = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                unFollowButton.setEnabled(true);
            }
            @Override
            public void onConnectionFailure() {
                followButton.setEnabled(true);
                followButton.setVisibility(View.VISIBLE);
                unFollowButton.setVisibility(View.INVISIBLE);
            }
        };

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followButton.setVisibility(View.INVISIBLE);
                unFollowButton.setVisibility(View.VISIBLE);
                unFollowButton.setEnabled(false);
                mViewModel.followUser(OudUtils.getToken(getContext()),userId,followButtonConnectionListener);

            }
        });


    }

    private void setUnFollowButtonClickListener(){
        ConnectionStatusListener unFollowButtonConnectionListener = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                followButton.setEnabled(true);
            }
            @Override
            public void onConnectionFailure() {
                unFollowButton.setEnabled(true);
                unFollowButton.setVisibility(View.VISIBLE);
                followButton.setVisibility(View.INVISIBLE);
            }
        };

        unFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followButton.setVisibility(View.VISIBLE);
                unFollowButton.setVisibility(View.INVISIBLE);
                followButton.setEnabled(false);
                mViewModel.unFollowUser(OudUtils.getToken(getContext()),userId,unFollowButtonConnectionListener);

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }
    }




    }
