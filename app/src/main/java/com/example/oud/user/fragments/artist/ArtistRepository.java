package com.example.oud.user.fragments.artist;

import android.util.Log;

import com.example.oud.api.Artist;
import com.example.oud.api.OudApi;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class ArtistRepository extends ConnectionAwareRepository {

    private static final String TAG = ArtistRepository.class.getSimpleName();

    private static final ArtistRepository ourInstance = new ArtistRepository();


    public static ArtistRepository getInstance() {
        return ourInstance;
    }

    public MutableLiveData<Artist> fetchArtist(String artistId) {
        MutableLiveData<Artist> artistMutableLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Artist> artistCall = oudApi.artist(artistId);

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



}
