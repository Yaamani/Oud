package com.example.oud.user.fragments.library.likedtracks;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import com.example.oud.user.TrackListRecyclerViewAdapter;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibraryLikedTracksViewModel extends ConnectionAwareViewModel<LibraryLikedTracksRepository> {

    /*public enum UserLibraryLikedTracksOperation {
        REMOVE_TRACK_FROM_LIKED_TRACKS,
    }*/

    //private UserLibraryLikedTracksOperation currentOperation = null;


    private MutableLiveData<OudList<LikedTrack>> lastSetOfLoadedTracks;
    private ArrayList<MutableLiveData<LikedTrack>> loadedLikedTracks = new ArrayList<>();


    private int removedLikedTrackPosition;


    public LibraryLikedTracksViewModel() {
        super(LibraryLikedTracksRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     *
     * <p>Asks {@link LibraryLikedTracksRepository} to fetch for a new set of liked track starting from the last one found in {@link #loadedLikedTracks}.</p>
     * <p>The number of tracks fetched equals {@link Constants#USER_LIBRARY_SINGLE_FETCH_LIMIT}.</p>
     * @param token
     * @return A {@link MutableLiveData} containing a list of the newly loaded tracks.
     */
    public MutableLiveData<OudList<LikedTrack>> loadMoreTracks(String token) {
        if (lastSetOfLoadedTracks == null)
            lastSetOfLoadedTracks = mRepo.getLikedTrackByCurrentUser(token, Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0);
        else {
            int prevOffset = lastSetOfLoadedTracks.getValue().getOffset();
            int prevLimit = lastSetOfLoadedTracks.getValue().getLimit();

            int offset = prevOffset+prevLimit, limit = Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

            lastSetOfLoadedTracks = mRepo.getLikedTrackByCurrentUser(token, limit, offset);
        }

        return lastSetOfLoadedTracks;
    }

    /**
     *
     * @param token
     * @param id The track you want the current user not to like.
     * @param position The track position in {@link TrackListRecyclerViewAdapter}.
     */
    public void removeTrackFromLikedTracks(String token,
                                           String id,
                                           int position,
                                           ConnectionStatusListener undoUiAndUpdateLiveData) {
        if (loadedLikedTracks.isEmpty()) return;

        /*setCurrentOperation(UserLibraryLikedTracksOperation.REMOVE_TRACK_FROM_LIKED_TRACKS);*/

        removedLikedTrackPosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s, undoUiAndUpdateLiveData);
    }

    public ArrayList<MutableLiveData<LikedTrack>> getLoadedLikedTracks() {
        return loadedLikedTracks;
    }

    /*public UserLibraryLikedTracksOperation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(UserLibraryLikedTracksOperation currentOperation) {
        this.currentOperation = currentOperation;
    }*/

    public void updateLiveDataUponRemovingTrackFromLikedTracks() {
        loadedLikedTracks.remove(removedLikedTrackPosition);
    }

    public void clearLoadedTracks() {
        loadedLikedTracks = new ArrayList<>();
    }

    public void clearLastSetOfLoadedTracks() {
        lastSetOfLoadedTracks = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearLastSetOfLoadedTracks();
        clearLoadedTracks();
    }

    @Override
    public void clearData() {
        clearLastSetOfLoadedTracks();
        clearLoadedTracks();
    }
}
