package com.example.oud.user.fragments.library.likedtracks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.LoadMoreAdapter;
import com.example.oud.user.fragments.artist.ArtistViewModel;


public class LibraryLikedTracksFragment extends ConnectionAwareFragment<LibraryLikedTracksViewModel> {

    private String token;

    private RecyclerView mRecyclerViewLikedTracks;
    private LoadMoreAdapter mLikedTracksAdapter;

    public LibraryLikedTracksFragment() {
        // Required empty public constructor
        super(LibraryLikedTracksViewModel.class,
                R.layout.fragment_library_liked_tracks,
                R.id.progress_library_liked_tracks,
                R.id.view_block_ui_input_library_liked_tracks,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleToken();

        initializeUiStuff(view);

        handleLikedTracks();
    }

    private void handleToken() {
        token = OudUtils.getToken(getContext());
    }

    private void initializeUiStuff(@NonNull View view) {
        mRecyclerViewLikedTracks = view.findViewById(R.id.recycler_view_library_liked_tracks);
    }

    /**
     * Handles the data and the logic behind liked tracks.
     */
    private void handleLikedTracks() {

        if (mViewModel.getLoadedLikedTracks().size() < Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT)
            loadMoreTracks();
        else
            observerLoadedTracks();

    }

    /**
     * Observes loadedLikedTracks in {@link LibraryLikedTracksViewModel} and adds the newly loaded tracks to the loaded ones.
     */
    private void loadMoreTracks() {

    }

    /**
     * Observes loadedLikedTracks in {@link LibraryLikedTracksViewModel}. and populates {@link #mLikedTracksAdapter}.
     */
    private void observerLoadedTracks() {

    }
}
