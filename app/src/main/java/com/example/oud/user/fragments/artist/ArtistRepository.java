package com.example.oud.user.fragments.artist;

import android.util.Log;

import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
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


        OudApi oudApi = instantiateRetrofitOudApi();
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

    public MutableLiveData<OudList<Album>> fetchSomeAlbums(String token, String artistId, Integer offset, Integer limit) {
        MutableLiveData<OudList<Album>> albumsMutableLiveData = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi();
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

    public MutableLiveData<RelatedArtists> fetchSimilarArtists(String token, String artistId) {
        MutableLiveData<RelatedArtists> similarArtistsLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
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

}
