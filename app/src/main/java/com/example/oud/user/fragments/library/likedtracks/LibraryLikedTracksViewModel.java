package com.example.oud.user.fragments.library.likedtracks;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudList;

import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibraryLikedTracksViewModel extends LibrarySubFragmentViewModel<LibraryLikedTracksRepository, LikedTrack> {


    public LibraryLikedTracksViewModel() {
        super(LibraryLikedTracksRepository.getInstance());
    }

    @Override
    protected MutableLiveData<OudList<LikedTrack>> repoFetchItems(String token, int limit, int offset) {
        return mRepo.getLikedTrackByCurrentUser(token, limit, offset);
    }

    @Override
    public void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData) {


        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s, undoUiAndUpdateLiveData);
    }

}
