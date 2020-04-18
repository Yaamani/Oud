package com.example.oud.user.fragments.artist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.BooleanIdsResponse;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.TrackListRecyclerViewAdapter;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class ArtistViewModel extends ConnectionAwareViewModel<ArtistRepository> {

    private static final String TAG = ArtistViewModel.class.getSimpleName();

    public enum UserArtistOperation {
        ADD_TRACK_TO_LIKED_TRACKS,
        REMOVE_TRACK_FROM_LIKED_TRACKS,
        FOLLOW_ARTIST,
        UNFOLLOW_ARTIST
    }

    /**
     * A data change on the server-side.
     */
    private UserArtistOperation currentOperation = null;


    private MutableLiveData<Artist> artistMutableLiveData;
    private MutableLiveData<BooleanIdsResponse> doesUserFollowThisArtist;

    private MutableLiveData<OudList<Album>> lastSetOfLoadedAlbums;
    private ArrayList<MutableLiveData<Album>> loadedAlbums = new ArrayList<>();

    private MutableLiveData<IsFoundResponse> areTracksLikedLiveData;


    private MutableLiveData<RelatedArtists> similarArtistsMutableLiveData;



    private int trackLikePosition;


    public ArtistViewModel() {
        super(ArtistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     * <p>If the {@link MutableLiveData} doesn't exist it will ask {@link ArtistRepository} to fetch for the data first.</p>
     * <p>If the existing data belongs to a different artist, it will call {@link #clearData()}
     * then ask {@link ArtistRepository} to fetch for the specified artist.</p>
     * @param token
     * @param artistId The id of the artist you wanna get the info for.
     * @return The {@link MutableLiveData} that holds all the artist info.
     */
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

    /**
     * <p>If the {@link MutableLiveData} doesn't exist it will ask {@link ArtistRepository} to fetch for the data first.</p>
     * @param token
     * @param artistId
     * @return The {@link MutableLiveData} that tell us whether the current user follow the specified artist or not.
     */
    public MutableLiveData<BooleanIdsResponse> getDoesUserFollowThisArtist(String token, String artistId) {
        if (doesUserFollowThisArtist == null) {
            ArrayList<String> ids = new ArrayList<>();
            ids.add(artistId);
            doesUserFollowThisArtist = mRepo.doesUserFollowTheseArtistsOrUsers(token, Constants.API_ARTIST, ids);
        }
        return doesUserFollowThisArtist;
    }

    /**
     * <p>Asks {@link ArtistRepository} to send follow request to the server</p>
     * @param token
     * @param artistId The artist you want the current user to follow.
     */
    public void followThisArtist(String token, String artistId) {
        if (doesUserFollowThisArtist == null) return;

        setCurrentOperation(UserArtistOperation.FOLLOW_ARTIST);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(artistId);
        mRepo.followArtistsOrUsers(token, Constants.API_ARTIST, ids);
    }

    /**
     * <p>Asks {@link ArtistRepository} to send unfollow request to the server</p>
     * @param token
     * @param artistId The artist you want the current user to unfollow.
     */
    public void unfollowThisArtist(String token, String artistId) {
        if (doesUserFollowThisArtist == null) return;

        setCurrentOperation(UserArtistOperation.UNFOLLOW_ARTIST);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(artistId);
        mRepo.unfollowArtistsOrUsers(token, Constants.API_ARTIST, ids);
    }

    /**
     *
     * @param token
     * @param ids
     * @return The {@link MutableLiveData} that tell us whether the user like the specified tracks or not (array of booleans).
     */
    public MutableLiveData<IsFoundResponse> getAreTracksLikedLiveData(String token, ArrayList<String> ids) {
        if (areTracksLikedLiveData == null)
            areTracksLikedLiveData = mRepo.areTracksLiked(token, ids);
        return areTracksLikedLiveData;
    }

    /**
     * <p>Asks {@link ArtistRepository} to send like request to the server</p>
     * @param token
     * @param id The track you want the current user to like.
     * @param position The track position in {@link TrackListRecyclerViewAdapter}.
     */
    public void addTrackToLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(UserArtistOperation.ADD_TRACK_TO_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.addTheseTracksToLikedTracks(token, s);
    }

    /**
     *
     * @param token
     * @param id The track you want the current user not to like.
     * @param position The track position in {@link TrackListRecyclerViewAdapter}.
     */
    public void removeTrackFromLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(UserArtistOperation.REMOVE_TRACK_FROM_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s);
    }


    /*public MutableLiveData<OudList<Album>> getLastSetOfLoadedAlbums(String token, String artistId) {
        if (lastSetOfLoadedAlbums == null) {
            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        }

        return lastSetOfLoadedAlbums;
    }*/

    /**
     * <p>Asks {@link ArtistRepository} to fetch for a new set of albums starting from the last one found in {@link #loadedAlbums}.</p>
     * <p>The number of albums fetched equals {@link Constants#USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT}.</p>
     * @param token
     * @param artistId
     * @return A {@link MutableLiveData} containing a list of the newly loaded albums.
     */
    public MutableLiveData<OudList<Album>> loadMoreAlbums(String token, String artistId) {
        if (lastSetOfLoadedAlbums == null) {
            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        } else {
            int prevOffset = lastSetOfLoadedAlbums.getValue().getOffset();
            int prevLimit = lastSetOfLoadedAlbums.getValue().getLimit();

            int offset = prevOffset+prevLimit, limit = Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT;

            // if (off)


            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, offset, limit);
        }

        return lastSetOfLoadedAlbums;
    }

    public ArrayList<MutableLiveData<Album>> getLoadedAlbums() {
        return loadedAlbums;
    }

    /**
     * <p>If the {@link MutableLiveData} doesn't exist it will ask {@link ArtistRepository} to fetch for the data first.</p>
     * @param token
     * @param artistId
     * @return A {@link MutableLiveData} containing a list of similar artists to the specified one.
     */
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

    /**
     * When the {@link #currentOperation} succeeds, update the {@link MutableLiveData} accordingly to match that on the server.
     */
    private void updateLiveDataUponAddingTrackToLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, true);
    }

    /**
     * When the {@link #currentOperation} succeeds, update the {@link MutableLiveData} accordingly to match that on the server.
     */
    private void updateLiveDataUponRemovingTrackFromLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, false);
    }

    /**
     * When the {@link #currentOperation} succeeds, update the {@link MutableLiveData} accordingly to match that on the server.
     */
    private void updateLiveDataUponFollowingArtist() {
        doesUserFollowThisArtist.getValue().getIds().set(0, true);
    }

    /**
     * When the {@link #currentOperation} succeeds, update the {@link MutableLiveData} accordingly to match that on the server.
     */
    private void updateLiveDataUponUnFollowingArtist() {
        /*System.out.println(doesUserFollowThisArtist + ", ");
        System.out.println(doesUserFollowThisArtist.getValue() + ", ");
        System.out.println(doesUserFollowThisArtist.getValue().getIds() + ", ");*/
        doesUserFollowThisArtist.getValue().getIds().set(0, false);
    }

    /**
     * When the {@link #currentOperation} succeeds, update the {@link MutableLiveData} accordingly to match that on the server.
     */
    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();

        if (currentOperation != null)
            switch (currentOperation) {
                case ADD_TRACK_TO_LIKED_TRACKS: updateLiveDataUponAddingTrackToLikedTracks();
                    break;
                case REMOVE_TRACK_FROM_LIKED_TRACKS: updateLiveDataUponRemovingTrackFromLikedTracks();
                    break;
                case FOLLOW_ARTIST: updateLiveDataUponFollowingArtist();
                    break;
                case UNFOLLOW_ARTIST: updateLiveDataUponUnFollowingArtist();
                    break;
            }

        currentOperation = null;

    }


    public void clearDoesUserFollowThisArtist() {
        doesUserFollowThisArtist = null;
    }

    public void clearAreTracksLikedData() {
        areTracksLikedLiveData = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearDoesUserFollowThisArtist();
        clearAreTracksLikedData();
    }

    @Override
    public void clearData() {
        artistMutableLiveData = null;
        clearDoesUserFollowThisArtist();
        loadedAlbums = new ArrayList<>();

        similarArtistsMutableLiveData = null;

        clearAreTracksLikedData();
    }
}
