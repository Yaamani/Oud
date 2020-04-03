package com.example.oud.user.fragments.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oud.R;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView profileDisplaynameTextView;
    RecyclerView recyclerView;


    private ProfileViewModel mViewModel;
    private String userId;
    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<String> playlistImageUrls = new ArrayList<>();
    private ArrayList<String> playlistIds = new ArrayList<>();


    public static ProfileFragment newInstance(String userId) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setUserId(userId);
        return profileFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(v);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        if(userId!=null) {
            mViewModel.getProfile(userId).observe(getViewLifecycleOwner(), new Observer<ProfilePreview>() {
                @Override
                public void onChanged(ProfilePreview profilePreview) {
                    profileDisplaynameTextView.setText(profilePreview.getDisplayName());
                    if(profilePreview.getImages().length > 0)
                        Glide.with(getContext()).asBitmap().load(profilePreview.getImages()[0]).into(profileImageView);
                }
            });
            mViewModel.getUserPlaylists(userId).observe(getViewLifecycleOwner(), new Observer<List<PlaylistPreview>>() {
                @Override
                public void onChanged(List<PlaylistPreview> playlistPreviews) {
                    for(int position=0 ;position < playlistPreviews.size();position++){
                        playlistNames.add(playlistPreviews.get(position).getName());
                        playlistImageUrls.add(playlistPreviews.get(position).getImageUrl());
                        playlistIds.add(playlistPreviews.get(position).getId());
                    }
                    initRecyclerView();
                }
            });
        }

        // TODO: Use the ViewModel
    }

    private void initializeViews(View v){
        profileImageView = v.findViewById(R.id.image_profile_fragment);
        profileDisplaynameTextView = v.findViewById(R.id.text_item_playlist_name);
        recyclerView = v.findViewById(R.id.recycler_view_profile);
    }

    private void initRecyclerView(){
        Log.d("TAG", "initRecyclerView: init recyclerview.");

        ProfilePlaylistRecyclerViewAdapter adapter = new ProfilePlaylistRecyclerViewAdapter(getContext(), playlistNames,playlistImageUrls,playlistIds);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
