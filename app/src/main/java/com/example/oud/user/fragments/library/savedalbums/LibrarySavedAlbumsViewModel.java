package com.example.oud.user.fragments.library.savedalbums;

import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsRepository;

public class LibrarySavedAlbumsViewModel extends ConnectionAwareViewModel<LibrarySavedAlbumsRepository> {


    public LibrarySavedAlbumsViewModel() {
        super(LibrarySavedAlbumsRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }



    @Override
    public void clearData() {

    }
}
