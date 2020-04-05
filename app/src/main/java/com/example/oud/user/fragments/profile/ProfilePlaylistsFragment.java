package com.example.oud.user.fragments.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.oud.R;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfilePlaylistsFragment extends ConnectionAwareFragment<ProfilePlaylistsViewModel> {



    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<String> playlistImageUrls = new ArrayList<>();
    private ArrayList<String> playlistIds = new ArrayList<>();

    ProfilePlaylistRecyclerViewAdapter adapter;

    private String userId;

    private RecyclerView recyclerView;
    private Button loadMoreButton;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ProfilePlaylistsFragment(){
        super(ProfilePlaylistsViewModel.class,
                R.layout.fragment_profile_playlists,
                R.id.progress_bar_profile_playlist,
                null);
    }

    public static ProfilePlaylistsFragment newInstance(String id) {

        ProfilePlaylistsFragment fragment =  new ProfilePlaylistsFragment();
        fragment.setUserId(id);
        return  fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_profile_playlists);
        loadMoreButton = view.findViewById(R.id.btn_profile_playlist_load_more);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loadMorePlaylists(userId);
            }
        });


        mViewModel.getUserPlaylists(userId).observe(getViewLifecycleOwner(), new Observer<List<PlaylistPreview>>() {
            @Override
            public void onChanged(List<PlaylistPreview> playlistPreviews) {
                playlistNames.clear();
                playlistIds.clear();
                playlistImageUrls.clear();
                for(int position=0 ;position < playlistPreviews.size();position++){
                    playlistNames.add(playlistPreviews.get(position).getName());
                    playlistImageUrls.add(playlistPreviews.get(position).getImageUrl());
                    playlistIds.add(playlistPreviews.get(position).getId());
                }
                setRecyclerView();

                if(mViewModel.getTotalNumberOfPlaylists().getValue()>playlistPreviews.size() && playlistPreviews.size()>5){
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

    private void setRecyclerView(){


        adapter = new ProfilePlaylistRecyclerViewAdapter(getContext(), playlistNames,playlistImageUrls,playlistIds,userId);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
