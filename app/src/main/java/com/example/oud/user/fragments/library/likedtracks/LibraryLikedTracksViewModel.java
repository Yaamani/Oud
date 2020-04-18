package com.example.oud.user.fragments.library.likedtracks;

import com.example.oud.Constants;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibraryLikedTracksViewModel extends ConnectionAwareViewModel<LibraryLikedTracksRepository> {

    private MutableLiveData<OudList<LikedTrack>> lastSetOfLoadedTracks;
    private ArrayList<MutableLiveData<LikedTrack>> loadedLikedTracks;

    public LibraryLikedTracksViewModel() {
        super(LibraryLikedTracksRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     *
     * <p>Asks {@link LibraryLikedTracksRepository} to fetch for a new set of liked track starting from the last one found in {@link #loadedLikedTracks}.</p>
     * <p>The number of tracks fetched equals {@link Constants#USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT}.</p>
     * @param token
     * @return A {@link MutableLiveData} containing a list of the newly loaded tracks.
     */
    public MutableLiveData<OudList<LikedTrack>> loadMoreTracks(String token) {
        if (lastSetOfLoadedTracks == null)
            lastSetOfLoadedTracks = mRepo.getLikedTrackByCurrentUser(token, Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT, 0);
        else {
            int prevOffset = lastSetOfLoadedTracks.getValue().getOffset();
            int prevLimit = lastSetOfLoadedTracks.getValue().getLimit();

            int offset = prevOffset+prevLimit, limit = Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT;

            lastSetOfLoadedTracks = mRepo.getLikedTrackByCurrentUser(token, limit, offset);
        }

        return lastSetOfLoadedTracks;
    }

    public ArrayList<MutableLiveData<LikedTrack>> getLoadedLikedTracks() {
        return loadedLikedTracks;
    }

    @Override
    public void clearData() {

    }
}
