package com.example.oud.user.fragments.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oud.R;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFollowersFragment extends ConnectionAwareFragment<ProfileFollowersViewModel> {

    private String userId;

    private ArrayList<String> followersNames = new ArrayList<>();
    private ArrayList<String> followersImageUrls = new ArrayList<>();
    private ArrayList<String> followersIds = new ArrayList<>();
    private ArrayList<String> followersTypes = new ArrayList<>();

    private RecyclerView recyclerView;
    private Button loadMoreButton;

    public static ProfileFollowersFragment newInstance(String id,Activity activity) {
        ProfileFollowersFragment fragment = new ProfileFollowersFragment(activity);
        fragment.setUserId(id);
        return fragment;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProfileFollowersFragment(Activity activity){
        super(ProfileFollowersViewModel.class,
                R.layout.fragment_profile_followers,
                activity.findViewById(R.id.progress_bar_user_activity),
                activity.findViewById(R.id.block_view),
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_profile_followers);
        loadMoreButton = view.findViewById(R.id.btn_profile_followers_load_more);

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loadMorePlaylists(userId);
            }
        });

        mViewModel.getUserFollowers(userId).observe(getViewLifecycleOwner(), new Observer<List<UserOrArtistPreview>>() {
            @Override
            public void onChanged(List<UserOrArtistPreview> userOrArtistPreviews) {
                followersIds.clear();
                followersImageUrls.clear();
                followersNames.clear();
                followersTypes.clear();
                for(int position =0 ;position<userOrArtistPreviews.size();position++){
                    followersIds.add(userOrArtistPreviews.get(position).getId());
                    followersNames.add(userOrArtistPreviews.get(position).getName());
                    followersImageUrls.add(userOrArtistPreviews.get(position).getImageUrl());
                    followersTypes.add(userOrArtistPreviews.get(position).getType());
                }
                setRecyclerView();
                if(mViewModel.getTotalNumberOfFollowers().getValue()>userOrArtistPreviews.size() && userOrArtistPreviews.size()>5){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreButton.setVisibility(View.VISIBLE);
                        }
                    });

                }else
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreButton.setVisibility(View.GONE);
                        }
                    });
            }
        });


    }

    private void setRecyclerView(){

        ProfileFollowersRecyclerViewAdapter adapter = new ProfileFollowersRecyclerViewAdapter(getContext(),followersNames,followersImageUrls,followersIds,followersTypes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
