package com.example.oud.user.fragments.artist;

import android.util.Log;

import com.example.oud.OudUtils;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ArtistRepository extends ConnectionAwareRepository {

    private static final String TAG = ArtistRepository.class.getSimpleName();

    private static final ArtistRepository ourInstance = new ArtistRepository();


    public static ArtistRepository getInstance() {
        return ourInstance;
    }

    public MutableLiveData<Artist> fetchArtist(String token, String artistId) {
        MutableLiveData<Artist> artistMutableLiveData = new MutableLiveData<>();


        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<Artist> artistCall = oudApi.artist("Bearer "+ token, artistId);

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

    public MutableLiveData<OudList<Album>> fetchSomeAlbums(String token, String artistId, Integer offset, Integer limit) {
        MutableLiveData<OudList<Album>> albumsMutableLiveData = new MutableLiveData<>();

        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<OudList<Album>> albumsCall = oudApi.artistAlbums("Bearer "+ token, artistId, offset, limit);

        addCall(albumsCall).enqueue(new FailureSuccessHandledCallback<OudList<Album>>(this) {
            @Override
            public void onResponse(Call<OudList<Album>> call, Response<OudList<Album>> response) {
                super.onResponse(call, response);
                albumsMutableLiveData.setValue(response.body());
            }
        });

        return albumsMutableLiveData;
    }

    public MutableLiveData<RelatedArtists> fetchSimilarArtists(String token, String artistId) {
        MutableLiveData<RelatedArtists> similarArtistsLiveData = new MutableLiveData<>();


        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<RelatedArtists> similarArtistsCall = oudApi.similarArtists("Bearer "+ token, artistId);

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

    public MutableLiveData<IsFoundResponse> areTracksLiked(String token, ArrayList<String> ids) {
        MutableLiveData<IsFoundResponse> savedTracksMutableLiveData = new MutableLiveData<>();

        Call<IsFoundResponse> areTracksSavedCall = oudApi.getAreTheseTracksLiked("Bearer " + token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(areTracksSavedCall).enqueue(new FailureSuccessHandledCallback<IsFoundResponse>(this) {
            @Override
            public void onResponse(Call<IsFoundResponse> call, Response<IsFoundResponse> response) {
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

    public void addTheseTracksToLikedTracks(String token, ArrayList<String> ids) {

        Call<ResponseBody> addTheseTracksToLikedTracksCall = oudApi.addTheseTracksToLikedTracks("Bearer" + token, OudUtils.commaSeparatedListQueryParameter(ids));
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

    public void removeTheseTracksFromLikedTracks(String token, ArrayList<String> ids) {

        Call<ResponseBody> removeTheseTracksFromLikedTracksCall = oudApi.removeTheseTracksFromLikedTracks("Bearer" + token, OudUtils.commaSeparatedListQueryParameter(ids));
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
