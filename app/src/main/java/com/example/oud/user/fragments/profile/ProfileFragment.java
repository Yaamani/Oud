package com.example.oud.user.fragments.profile;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oud.Constants;
import com.example.oud.OptionsFragment;
import com.example.oud.R;
import com.example.oud.RenameFragment;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView profileDisplaynameTextView;
    private BottomNavigationView navigationView;
    private ImageButton renameButton;
    private ImageButton optionsButton;


    private ProfileViewModel mViewModel;
    private String userId;

    ProfilePlaylistsFragment profilePlaylistsFragment;
    ProfileFollowersFragment profileFollowersFragment;




    private final int RESULT_LOAD_IMG=111;


    public static ProfileFragment newInstance(String userId) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUserId(userId);
        return profileFragment;
    }

    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            String userId) {

        FragmentManager manager = activity.getSupportFragmentManager();
        ProfileFragment profileFragment = ProfileFragment.newInstance(userId);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, profileFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(v);
        setButtonsOnClickListener();
        profilePlaylistsFragment= ProfilePlaylistsFragment.newInstance(userId);
        profileFollowersFragment= ProfileFollowersFragment.newInstance(userId);
        navigationView.setSelectedItemId(R.id.navigation_profile_playlists);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        if(userId!=null) {
            SharedPreferences prefs = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
            String token = prefs.getString("token","000000");
            mViewModel.getProfile(userId,token).observe(getViewLifecycleOwner(), new Observer<ProfilePreview>() {
                @Override
                public void onChanged(ProfilePreview profilePreview) {
                    if(profilePreview !=null){
                    profileDisplaynameTextView.setText(profilePreview.getDisplayName());
                    if(profilePreview.getImages().length > 0)
                        Glide.with(getContext()).asBitmap().load(profilePreview.getImages()[0]).into(profileImageView);
                }}
            });

        }

        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                mViewModel.updateProfileImage(token,imageUri);

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
                RenameFragment.showRenameFragment(getActivity(),R.id.nav_host_fragment,profileDisplaynameTextView.getText().toString(),profileDisplaynameTextView);
            }
        };

        profileImageView.setOnClickListener(updateImageOnClickListener);
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
                        openFragment(ProfileFollowingFragment.newInstance(userId));
                }

                return false;
            }
        });


    }



    public void setUserId(String userId) {
        this.userId = userId;
    }
    private void uploadProfileImage(Bitmap newImage){

    }

    private void openFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction().replace(R.id.fragment_host_profile,fragment).commit();
    }


}
