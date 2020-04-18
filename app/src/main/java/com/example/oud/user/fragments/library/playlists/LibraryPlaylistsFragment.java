package com.example.oud.user.fragments.library.playlists;

import android.os.Bundle;
import android.view.View;

import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LibraryPlaylistsFragment extends ConnectionAwareFragment<LibraryPlaylistsViewModel> {

    public LibraryPlaylistsFragment() {
        // Required empty public constructor
        super(LibraryPlaylistsViewModel.class,
                R.layout.fragment_library_playlists,
                R.id.progress_library_playlists,
                R.id.view_block_ui_input_library_playlists,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
