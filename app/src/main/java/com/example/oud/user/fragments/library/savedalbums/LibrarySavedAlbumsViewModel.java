package com.example.oud.user.fragments.library.savedalbums;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudList;
import com.example.oud.api.SavedAlbum;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsRepository;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibrarySavedAlbumsViewModel extends LibrarySubFragmentViewModel<LibrarySavedAlbumsRepository, SavedAlbum> {


    public LibrarySavedAlbumsViewModel() {
        super(LibrarySavedAlbumsRepository.getInstance());
    }


    @Override
    protected MutableLiveData<OudList<SavedAlbum>> repoFetchItems(String token, int limit, int offset) {
        return mRepo.getSavedAlbumsByCurrentUser(token, limit, offset);
    }

    @Override
    public void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData) {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(id);

        mRepo.unsaveAlbum(token, ids, undoUiAndUpdateLiveData);
    }
}
