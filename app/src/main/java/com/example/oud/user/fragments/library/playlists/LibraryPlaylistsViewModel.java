package com.example.oud.user.fragments.library.playlists;

import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class LibraryPlaylistsViewModel extends ConnectionAwareViewModel<LibraryPlaylistsRepository> {


    public LibraryPlaylistsViewModel() {
        super(LibraryPlaylistsRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }



    @Override
    public void clearData() {

    }
}
