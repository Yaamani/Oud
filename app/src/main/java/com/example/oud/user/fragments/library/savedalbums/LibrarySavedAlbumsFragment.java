package com.example.oud.user.fragments.library.savedalbums;

import android.os.Bundle;
import android.view.View;

import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LibrarySavedAlbumsFragment extends ConnectionAwareFragment<LibrarySavedAlbumsViewModel> {

    public LibrarySavedAlbumsFragment() {
        // Required empty public constructor
        super(LibrarySavedAlbumsViewModel.class,
                R.layout.fragment_library_saved_albums,
                R.id.progress_library_saved_albums,
                R.id.view_block_ui_input_library_saved_albums,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
