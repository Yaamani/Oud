package com.example.oud.user.fragments.library;

import com.example.oud.user.fragments.library.artists.LibraryArtistsFragment;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksFragment;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsFragment;
import com.example.oud.user.fragments.library.savedalbums.LibrarySavedAlbumsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LibraryFragmentAdapter extends FragmentStateAdapter {

    private LibraryLikedTracksFragment libraryLikedTracksFragment;
    private LibraryPlaylistsFragment libraryPlaylistsFragment;
    private LibraryArtistsFragment libraryArtistsFragment;
    private LibrarySavedAlbumsFragment librarySavedAlbumsFragment;

    public LibraryFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);

        libraryLikedTracksFragment = new LibraryLikedTracksFragment();
        libraryPlaylistsFragment = new LibraryPlaylistsFragment();
        libraryArtistsFragment = new LibraryArtistsFragment();
        librarySavedAlbumsFragment = new LibrarySavedAlbumsFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0: return libraryLikedTracksFragment;
            case 1: return libraryPlaylistsFragment;
            case 2: return libraryArtistsFragment;
            case 3: return librarySavedAlbumsFragment;
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
