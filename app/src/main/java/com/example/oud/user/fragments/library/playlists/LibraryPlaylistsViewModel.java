package com.example.oud.user.fragments.library.playlists;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.PlaylistDetailsPayload;
import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;
import com.example.oud.user.fragments.playlist.PlaylistViewModel;

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

    /**
     * Create a new playlist.
     * @param token
     * @param loggedInUserId
     * @param playlistCreationListener Listener to react when the playlist is created or when there's an error.
     */
    public void createPlaylist(String token, String loggedInUserId, LibraryPlaylistsRepository.PlaylistCreationListener playlistCreationListener) {
        PlaylistDetailsPayload playlistDetailsPayload = new PlaylistDetailsPayload("New playlist",
                true,
                false,
                "New playlist.",
                null);

        mRepo.createPlaylist(token, loggedInUserId, playlistDetailsPayload, playlistCreationListener);
        //mRepo.followPlaylist(token, id, true);

    }

}
