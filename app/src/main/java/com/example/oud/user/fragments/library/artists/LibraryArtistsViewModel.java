package com.example.oud.user.fragments.library.artists;

import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksRepository;

public class LibraryArtistsViewModel extends ConnectionAwareViewModel<LibraryArtistsRepository> {


    public LibraryArtistsViewModel() {
        super(LibraryArtistsRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }



    @Override
    public void clearData() {

    }
}
