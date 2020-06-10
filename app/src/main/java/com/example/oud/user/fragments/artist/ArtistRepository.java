package com.example.oud.user.fragments.artist;

import android.util.Log;

import com.example.oud.NotificationUtils;
import com.example.oud.OudUtils;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.BooleanIdsResponse;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import com.example.oud.Constants;

public class ArtistRepository extends ConnectionAwareRepository {

    private static final String TAG = ArtistRepository.class.getSimpleName();

    private static final ArtistRepository ourInstance = new ArtistRepository();

    private ArtistRepository() {}

    public static ArtistRepository getInstance() {
        return ourInstance;
    }

    /**
     * Get artist data from server.
     * @param token
     * @param artistId
     * @return {@link MutableLiveData} that will hold all the artist info when the server successfully responds.
     */
    public MutableLiveData<Artist> fetchArtist(String token, String artistId) {
        MutableLiveData<Artist> artistMutableLiveData = new MutableLiveData<>();
        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<Artist> artistCall = oudApi.artist(token, artistId);

        addCall(artistCall).enqueue(new FailureSuccessHandledCallback<Artist>(this) {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                Artist artist = response.body();
                artistMutableLiveData.setValue(artist);
            }
        });


        return artistMutableLiveData;
    }

    /**
     *
     * @param token
     * @param type {@link Constants#API_ARTIST} or {@link Constants#API_USER}.
     * @param ids ids for users or artists you wanna know whether the current user follows or not.
     * @return {@link MutableLiveData} that will hold, when the server successfully responds, an array of booleans.
     */
    public MutableLiveData<BooleanIdsResponse> doesUserFollowTheseArtistsOrUsers(String token, String type, ArrayList<String> ids) {
        MutableLiveData<BooleanIdsResponse> doesUserFollowArtistsLiveData = new MutableLiveData<>();

        Call<BooleanIdsResponse> doesUserFollowArtistsCall = oudApi.doesCurrentUserFollowsArtistsOrUsers(token, type, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(doesUserFollowArtistsCall).enqueue(new FailureSuccessHandledCallback<BooleanIdsResponse>(this) {
            @Override
            public void onResponse(Call<BooleanIdsResponse> call, Response<BooleanIdsResponse> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                doesUserFollowArtistsLiveData.setValue(response.body());
            }
        });

        return doesUserFollowArtistsLiveData;
    }

    /**
     *
     * @param token
     * @param type {@link Constants#API_ARTIST} or {@link Constants#API_USER}.
     * @param ids ids of users or artists to follow.
     */
    public void followArtistsOrUsers(String token, String type, ArrayList<String> ids) {

        Call<ResponseBody> followCall = oudApi.followArtistsOrUsers(token, type, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(followCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                NotificationUtils.subscribeToFollowedArtistTopic(ids.get(0));
            }
        });
    }

    /**
     *
     * @param token
     * @param type {@link Constants#API_ARTIST} or {@link Constants#API_USER}.
     * @param ids ids of users or artists to unfollow.
     */
    public void unfollowArtistsOrUsers(String token, String type, ArrayList<String> ids) {

        Call<ResponseBody> unfollowCall = oudApi.unfollowArtistsOrUsers(token, type, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(unfollowCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                NotificationUtils.unsubscribeFromFollowedArtistTopic(ids.get(0));
            }
        });

    }

    /**
     *
     * @param token
     * @param artistId Id for the artist you wanna get the albums for.
     * @param offset The index which the returned albums will start at.
     * @param limit Number of albums returned by the server.
     * @return {@link MutableLiveData} that will hold the requested albums when the server successfully responds.
     */
    public MutableLiveData<OudList<Album>> fetchSomeAlbums(String token, String artistId, Integer offset, Integer limit) {
        MutableLiveData<OudList<Album>> albumsMutableLiveData = new MutableLiveData<>();

        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<OudList<Album>> albumsCall = oudApi.artistAlbums(token, artistId, offset, limit);

        addCall(albumsCall).enqueue(new FailureSuccessHandledCallback<OudList<Album>>(this) {
            @Override
            public void onResponse(Call<OudList<Album>> call, Response<OudList<Album>> response) {
                super.onResponse(call, response);
                albumsMutableLiveData.setValue(response.body());
            }
        });

        return albumsMutableLiveData;
    }

    /**
     * 
     * @param token
     * @param artistId Id for the artist you wanna get the similar artists for.
     * @return {@link MutableLiveData} that will hold the requested artists when the server successfully responds.
     */
    public MutableLiveData<RelatedArtists> fetchSimilarArtists(String token, String artistId) {
        MutableLiveData<RelatedArtists> similarArtistsLiveData = new MutableLiveData<>();


        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<RelatedArtists> similarArtistsCall = oudApi.similarArtists(token, artistId);

        addCall(similarArtistsCall).enqueue(new FailureSuccessHandledCallback<RelatedArtists>(this) {
            @Override
            public void onResponse(Call<RelatedArtists> call, Response<RelatedArtists> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                similarArtistsLiveData.setValue(response.body());
            }
        });


        return similarArtistsLiveData;
    }

    /**
     *
     * @param token
     * @param ids Ids for tracks you wanna know whether they're liked by the current user or not.
     * @return {@link MutableLiveData} that wil hold, when the server successfully responds, an array of booleans.
     */
    public MutableLiveData<ArrayList<Boolean>> areTracksLiked(String token, ArrayList<String> ids) {
        MutableLiveData<ArrayList<Boolean>> savedTracksMutableLiveData = new MutableLiveData<>();

        Call<ArrayList<Boolean>> areTracksSavedCall = oudApi.getAreTheseTracksLiked(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(areTracksSavedCall).enqueue(new FailureSuccessHandledCallback<ArrayList<Boolean>>(this) {
            @Override
            public void onResponse(Call<ArrayList<Boolean>> call, Response<ArrayList<Boolean>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                savedTracksMutableLiveData.setValue(response.body());
            }
        });

        return savedTracksMutableLiveData;
    }

    /**
     *
     * @param token
     * @param ids Ids for tracks you want the current user to like.
     */
    public void addTheseTracksToLikedTracks(String token, ArrayList<String> ids) {

        Call<ResponseBody> addTheseTracksToLikedTracksCall = oudApi.addTheseTracksToLikedTracks(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(addTheseTracksToLikedTracksCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
            }
        });
    }

    /**
     *
     * @param token
     * @param ids Ids for tracks you want the current user to not like anymore.
     */
    public void removeTheseTracksFromLikedTracks(String token, ArrayList<String> ids) {

        Call<ResponseBody> removeTheseTracksFromLikedTracksCall = oudApi.removeTheseTracksFromLikedTracks(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(removeTheseTracksFromLikedTracksCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
            }
        });
    }

}
