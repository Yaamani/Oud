package com.example.oud.user.fragments.playlist;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.oud.api.Playlist;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistRepository {

    private static final PlaylistRepository ourInstance = new PlaylistRepository();


    private String baseUrl;
    private ConnectionStatusListener connectionStatusListener;




    public static PlaylistRepository getInstance() {
        return ourInstance;
    }

    private PlaylistRepository() {
        this.baseUrl = Constants.BASE_URL;
    }

    public MutableLiveData<Playlist> fetchPlaylist(String playlistId) {
        MutableLiveData<Playlist> playlistMutableLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Playlist> playlistCall = oudApi.playlist(playlistId);





        return playlistMutableLiveData;
    }

    private OudApi instantiateRetrofitOudApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OudApi.class);

    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ConnectionStatusListener getConnectionStatusListener() {
        return connectionStatusListener;
    }

    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }
}
