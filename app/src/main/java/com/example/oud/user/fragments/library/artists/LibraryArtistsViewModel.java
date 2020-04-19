package com.example.oud.user.fragments.library.artists;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksRepository;

import androidx.lifecycle.MutableLiveData;

public class LibraryArtistsViewModel extends LibrarySubFragmentViewModel<LibraryArtistsRepository, ArtistPreview> {


    public LibraryArtistsViewModel() {
        super(LibraryArtistsRepository.getInstance());
    }


    @Override
    protected MutableLiveData<OudList<ArtistPreview>> repoFetchItems(String token, int limit, int offset) {
        return mRepo.getArtistsFollowedByCurrentUser(token, limit, offset);
    }

    @Override
    public void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData) {
        mRepo.unfollowArtist(token, id, undoUiAndUpdateLiveData);
    }
}
