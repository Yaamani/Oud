package com.example.oud.user.fragments.library.playlists;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.GenericVerticalRecyclerViewAdapter;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibraryPlaylistsViewModel extends ConnectionAwareViewModel<LibraryPlaylistsRepository> {

    private MutableLiveData<OudList<Playlist>> lastSetOfLoadedPlaylists;
    private ArrayList<MutableLiveData<Playlist>> loadedPlaylists = new ArrayList<>();

    private int unfollowedPlaylistPosition;


    public LibraryPlaylistsViewModel() {
        super(LibraryPlaylistsRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     *
     * <p>Asks {@link LibraryPlaylistsRepository} to fetch for a new set of followed playlists starting from the last one found in {@link #loadedPlaylists}.</p>
     * <p>The number of playlists fetched equals {@link Constants#USER_LIBRARY_SINGLE_FETCH_LIMIT}.</p>
     * @param token
     * @return A {@link MutableLiveData} containing a list of the newly loaded playlists.
     */
    public MutableLiveData<OudList<Playlist>> loadMorePlaylists(String token) {
        if (lastSetOfLoadedPlaylists == null)
            lastSetOfLoadedPlaylists = mRepo.getPlaylistsFollowedByCurrentUser(token, Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0);
        else {
            int prevOffset = lastSetOfLoadedPlaylists.getValue().getOffset();
            int prevLimit = lastSetOfLoadedPlaylists.getValue().getLimit();

            int offset = prevOffset+prevLimit, limit = Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

            lastSetOfLoadedPlaylists = mRepo.getPlaylistsFollowedByCurrentUser(token, limit, offset);
        }

        return lastSetOfLoadedPlaylists;
    }

    /**
     *
     * @param token
     * @param id The playlist you want the current user to unfollow.
     * @param position The playlist position in {@link GenericVerticalRecyclerViewAdapter}.
     */
    public void unfollowPlaylist(String token,
                                   String id,
                                   int position,
                                   ConnectionStatusListener undoUiAndUpdateLiveData) {
        if (loadedPlaylists.isEmpty()) return;


        unfollowedPlaylistPosition = position;

        mRepo.unfollowPlaylist(token, id, undoUiAndUpdateLiveData);
    }

    public ArrayList<MutableLiveData<Playlist>> getLoadedPlaylists() {
        return loadedPlaylists;
    }

    public void updateLiveDataUponUnfollowingPlaylist() {
        loadedPlaylists.remove(unfollowedPlaylistPosition);
    }

    public void clearLoadedPlaylists() {
        loadedPlaylists = new ArrayList<>();
    }

    public void clearLastSetOfLoadedPlaylists() {
        lastSetOfLoadedPlaylists = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearLastSetOfLoadedPlaylists();
        clearLoadedPlaylists();
    }

    @Override
    public void clearData() {
        clearLastSetOfLoadedPlaylists();
        clearLoadedPlaylists();
    }
}
