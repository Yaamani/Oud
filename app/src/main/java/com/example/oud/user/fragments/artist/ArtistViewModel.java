package com.example.oud.user.fragments.artist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.playlist.PlaylistViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class ArtistViewModel extends ConnectionAwareViewModel<ArtistRepository> {

    public enum UserArtistOperation {
        ADD_TRACK_TO_LIKED_TRACKS,
        REMOVE_TRACK_FROM_LIKED_TRACKS
    }

    private UserArtistOperation currentOperation = null;


    private MutableLiveData<Artist> artistMutableLiveData;

    private MutableLiveData<OudList<Album>> lastSetOfLoadedAlbums;
    private ArrayList<MutableLiveData<Album>> loadedAlbums = new ArrayList<>();

    private MutableLiveData<IsFoundResponse> areTracksLikedLiveData;


    private MutableLiveData<RelatedArtists> similarArtistsMutableLiveData;



    private int trackLikePosition;


    public ArtistViewModel() {
        super(ArtistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Artist> getArtistMutableLiveData(String token, String artistId) {
        if (artistMutableLiveData == null) {
            artistMutableLiveData = mRepo.fetchArtist(token, artistId);
        } else {
            String currentId = artistMutableLiveData.getValue().get_id();
            if (currentId.equals(artistId)) {
                return artistMutableLiveData;
            } else {
                // Fetch
                clearData();
                artistMutableLiveData = mRepo.fetchArtist(token, artistId);
            }
        }
        return artistMutableLiveData;
    }

    public MutableLiveData<IsFoundResponse> getAreTracksLikedLiveData(String token, ArrayList<String> ids) {
        if (areTracksLikedLiveData == null)
            areTracksLikedLiveData = mRepo.areTracksLiked(token, ids);
        return areTracksLikedLiveData;
    }

    public void addTrackToLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(UserArtistOperation.ADD_TRACK_TO_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.addTheseTracksToLikedTracks(token, s);
    }

    public void removeTrackFromLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(UserArtistOperation.REMOVE_TRACK_FROM_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s);
    }


    public MutableLiveData<OudList<Album>> getLastSetOfLoadedAlbums(String token, String artistId) {
        if (lastSetOfLoadedAlbums == null) {
            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        }

        return lastSetOfLoadedAlbums;
    }

    public void loadMoreAlbums(String token, String artistId) {
        lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, lastSetOfLoadedAlbums.getValue().getLimit(), Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
    }

    public ArrayList<MutableLiveData<Album>> getLoadedAlbums() {
        return loadedAlbums;
    }

    public MutableLiveData<RelatedArtists> getSimilarArtistsMutableLiveData(String token, String artistId) {
        if (similarArtistsMutableLiveData == null) {
            similarArtistsMutableLiveData = mRepo.fetchSimilarArtists(token, artistId);
        }
        return similarArtistsMutableLiveData;
    }

    public void setCurrentOperation(UserArtistOperation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public UserArtistOperation getCurrentOperation() {
        return currentOperation;
    }

    private void updateLiveDataUponAddingTrackToLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, true);
    }

    private void updateLiveDataUponRemovingTrackFromLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, false);
    }

    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();

        if (currentOperation != null)
            switch (currentOperation) {
                case ADD_TRACK_TO_LIKED_TRACKS: updateLiveDataUponAddingTrackToLikedTracks();
                    break;
                case REMOVE_TRACK_FROM_LIKED_TRACKS: updateLiveDataUponRemovingTrackFromLikedTracks();
                    break;
            }

        currentOperation = null;

    }



    @Override
    public void clearData() {
        artistMutableLiveData = null;

        similarArtistsMutableLiveData = null;

        areTracksLikedLiveData = null;
    }
}
