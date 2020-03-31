package com.example.oud.user.fragments.playlist;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.home.HomeViewModel;

import java.util.ArrayList;

public class PlaylistFragment extends ConnectionAwareFragment<PlaylistViewModel> {

    //private PlaylistViewModel mViewModel;

    private Constants.PlaylistFragmentType type;
    private String id;

    public PlaylistFragment() {
        super(PlaylistViewModel.class,
                R.layout.fragment_playlist,
                R.id.progress_playlist,
                null);
    }

    public static PlaylistFragment newInstance(Constants.PlaylistFragmentType type, String id) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        Bundle args = new Bundle();
        args.putSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY, type);
        args.putString(Constants.ID_KEY, id);
        playlistFragment.setArguments(args);

        return playlistFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle args = getArguments();
        if (args != null) {
            type = (Constants.PlaylistFragmentType) args.getSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY);
            id = args.getString(Constants.ID_KEY);
        } else
            throw new RuntimeException("Instead of calling new " + PlaylistFragment.class.getSimpleName() + "()" +
                    ", call " + PlaylistFragment.class.getSimpleName() + ".newInstance(type, id)" +
                    " to pass the arguments to the fragment.");




        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_playlist_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> trackNames = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            trackNames.add("Track"+i);
        }
        PlaylistRecyclerViewAdapter adapter = new PlaylistRecyclerViewAdapter(trackNames);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();


    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();
    }
}
