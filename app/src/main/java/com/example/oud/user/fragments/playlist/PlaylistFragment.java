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

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {

    private PlaylistViewModel mViewModel;

    private Constants.PlaylistFragmentType type;
    private String id;

    public static PlaylistFragment newInstance(Constants.PlaylistFragmentType type, String id) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        Bundle args = new Bundle();
        args.putSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY, type);
        args.putString(Constants.ID_KEY, id);
        playlistFragment.setArguments(args);

        return playlistFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        Bundle args = getArguments();
        if (args != null) {
            type = (Constants.PlaylistFragmentType) args.getSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY);
            id = args.getString(Constants.ID_KEY);
        } else
            throw new RuntimeException("Instead of calling new " + PlaylistFragment.class.getSimpleName() + "()" +
                    ", call " + PlaylistFragment.class.getSimpleName() + ".newInstance(type, id)" +
                    " to pass the arguments to the fragment.");





        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_playlist_tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> trackNames = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            trackNames.add("Track"+i);
        }
        PlaylistRecyclerViewAdapter adapter = new PlaylistRecyclerViewAdapter(trackNames);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*mViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        // TODO: Use the ViewModel*/


    }

}
