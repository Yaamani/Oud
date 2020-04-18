package com.example.oud.user.fragments.library.artists;

import android.os.Bundle;
import android.view.View;

import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LibraryArtistsFragment extends ConnectionAwareFragment<LibraryArtistsViewModel> {

    public LibraryArtistsFragment() {
        // Required empty public constructor
        super(LibraryArtistsViewModel.class,
                R.layout.fragment_library_artists,
                R.id.progress_library_artists,
                R.id.view_block_ui_input_library_artists,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
