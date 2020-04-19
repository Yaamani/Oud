package com.example.oud.user.fragments.library.playlists;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;

import androidx.lifecycle.MutableLiveData;

public class LibraryPlaylistsViewModel extends LibrarySubFragmentViewModel<LibraryPlaylistsRepository, Playlist> {
    public LibraryPlaylistsViewModel() {
        super(LibraryPlaylistsRepository.getInstance());
    }

    @Override
    protected MutableLiveData<OudList<Playlist>> repoFetchItems(String token, int limit, int offset) {
        return mRepo.getPlaylistsFollowedByCurrentUser(token, limit, offset);
    }

    @Override
    public void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData) {
        mRepo.unfollowPlaylist(token, id, undoUiAndUpdateLiveData);
    }
}
