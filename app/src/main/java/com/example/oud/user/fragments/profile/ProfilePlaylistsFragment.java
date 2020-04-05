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

import java.util.ArrayList;
import java.util.List;

public class ProfilePlaylistsFragment extends Fragment {

    private ProfilePlaylistsViewModel mViewModel;

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

    public static ProfilePlaylistsFragment newInstance(String id) {
        ProfilePlaylistsFragment fragment =  new ProfilePlaylistsFragment();
        fragment.setUserId(id);
        return  fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_playlists, container, false);
        recyclerView = v.findViewById(R.id.recycler_view_profile_playlists);
        loadMoreButton = v.findViewById(R.id.btn_profile_playlist_load_more);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.loadMorePlaylists(userId);
            }
        });
        mViewModel = ViewModelProviders.of(this).get(ProfilePlaylistsViewModel.class);
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
                initRecyclerView();

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

    private void initRecyclerView(){
        Log.d("TAG", "initRecyclerView: init recyclerview.");

        adapter = new ProfilePlaylistRecyclerViewAdapter(getContext(), playlistNames,playlistImageUrls,playlistIds);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
