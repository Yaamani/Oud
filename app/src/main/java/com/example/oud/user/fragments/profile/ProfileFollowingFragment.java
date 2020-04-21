package com.example.oud.user.fragments.profile;

import androidx.lifecycle.Observer;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileFollowingFragment extends ConnectionAwareFragment<ProfileFollowingViewModel> {

    private String userId;
    private RecyclerView recyclerView;
    private Button loadMoreButton;
    private ArrayList<String> followedArtistsNames = new ArrayList<>();
    private ArrayList<String> followedArtistsImageUrls = new ArrayList<>();
    private ArrayList<String> followedArtistsIds = new ArrayList<>();
    private ArrayList<String> followedArtistsTypes = new ArrayList<>();


    private ArrayList<String> followedUsersNames = new ArrayList<>();
    private ArrayList<String> followedUsersImageUrls = new ArrayList<>();
    private ArrayList<String> followedUsersIds = new ArrayList<>();
    private ArrayList<String> followedUsersTypes = new ArrayList<>();



    public static ProfileFollowingFragment newInstance(String id,Activity activity) {
        ProfileFollowingFragment fragment = new ProfileFollowingFragment(activity);
        fragment.setUserId(id);
        return fragment;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProfileFollowingFragment(Activity activity){
        super(ProfileFollowingViewModel.class,
                R.layout.fragment_profile_following,
                activity.findViewById(R.id.progress_bar_user_activity),
                activity.findViewById(R.id.block_view),
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_profile_following);
        loadMoreButton = view.findViewById(R.id.btn_profile_following_load_more);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followedArtistsIds.size()==mViewModel.getTotalNumberOfFollowedArtists().getValue())
                    mViewModel.loadMoreFollowedUsers(userId);
                mViewModel.loadMoreFollowedArtists(userId);
            }
        });
        mViewModel.getUserFollowedArtists(userId).observe(getViewLifecycleOwner(), new Observer<List<UserOrArtistPreview>>() {
            @Override
            public void onChanged(List<UserOrArtistPreview> userOrArtistPreviews) {

                followedArtistsIds.clear();
                followedArtistsImageUrls.clear();
                followedArtistsNames.clear();
                followedArtistsTypes.clear();
                for(int position =0 ;position<userOrArtistPreviews.size();position++){
                    followedArtistsIds.add(userOrArtistPreviews.get(position).getId());
                    followedArtistsNames.add(userOrArtistPreviews.get(position).getName());
                    followedArtistsImageUrls.add(userOrArtistPreviews.get(position).getImageUrl());
                    followedArtistsTypes.add(userOrArtistPreviews.get(position).getType());
                }
                setRecyclerView();

                if(mViewModel.getTotalNumberOfFollowedArtists().getValue()==userOrArtistPreviews.size())
                    loadUserFollowedUsers();

                if(mViewModel.getTotalNumberOfFollowedArtists().getValue()>userOrArtistPreviews.size() && userOrArtistPreviews.size()>5){
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void loadUserFollowedUsers(){
        mViewModel.getUserFollowedUsers(userId).observe(getViewLifecycleOwner(), new Observer<List<UserOrArtistPreview>>() {
            @Override
            public void onChanged(List<UserOrArtistPreview> userOrArtistPreviews) {
                followedUsersIds.clear();
                followedUsersImageUrls.clear();
                followedUsersNames.clear();
                followedUsersTypes.clear();
                for(int position =0 ;position<userOrArtistPreviews.size();position++){
                    followedUsersIds.add(userOrArtistPreviews.get(position).getId());
                    followedUsersNames.add(userOrArtistPreviews.get(position).getName());
                    followedUsersImageUrls.add(userOrArtistPreviews.get(position).getImageUrl());
                    followedUsersTypes.add(userOrArtistPreviews.get(position).getType());
                }
                setRecyclerView();
                if(mViewModel.getTotalNumberOfFollowedUsers().getValue()>userOrArtistPreviews.size() && userOrArtistPreviews.size()>5){
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
        ArrayList<String> followedArtistsAndUsersNames = new ArrayList<>();
        followedArtistsAndUsersNames.addAll(followedArtistsNames);
        followedArtistsAndUsersNames.addAll(followedUsersNames);

        ArrayList<String> followedArtistsAndUsersImageUrls = new ArrayList<>();
        followedArtistsAndUsersImageUrls.addAll(followedArtistsImageUrls);
        followedArtistsAndUsersImageUrls.addAll(followedUsersImageUrls);


        ArrayList<String> followedArtistsAndUsersIds = new ArrayList<>();
        followedArtistsAndUsersIds.addAll(followedArtistsIds);
        followedArtistsAndUsersIds.addAll(followedUsersIds);

        ArrayList<String> followedArtistsAndUsersTypes = new ArrayList<>();
        followedArtistsAndUsersTypes.addAll(followedArtistsTypes);
        followedArtistsAndUsersTypes.addAll(followedUsersTypes);



        ProfileFollowingRecyclerViewAdapter adapter = new ProfileFollowingRecyclerViewAdapter(getContext(),
                followedArtistsAndUsersNames,
                followedArtistsAndUsersImageUrls,
                followedArtistsAndUsersIds,
                followedArtistsAndUsersTypes,getViewLifecycleOwner());

        //ProfileFollowersRecyclerViewAdapter adapter = new ProfileFollowersRecyclerViewAdapter(getContext(),followedArtistsNames,followedArtistsImageUrls,followedArtistsTypes, userId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}

