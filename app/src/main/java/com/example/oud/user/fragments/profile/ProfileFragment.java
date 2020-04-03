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

import com.example.oud.R;
import com.example.oud.api.ProfilePreview;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        if(userId!=null) {
            mViewModel.getProfile(userId).observe(getViewLifecycleOwner(), new Observer<ProfilePreview>() {
                @Override
                public void onChanged(ProfilePreview profilePreview) {

                }
            });
        }
        initRecyclerView();
        // TODO: Use the ViewModel
    }

    private void initializeViews(View v){


    }

    private void initRecyclerView(){
        Log.d("TAG", "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_profile);
        ProfilePlaylistRecyclerViewAdapter adapter = new ProfilePlaylistRecyclerViewAdapter(getContext(), mNames, mImageUrls,mNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
