package com.example.oud.user.fragments.playlist;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.oud.api.Playlist;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistRepository extends ConnectionAwareRepository {

    private static final PlaylistRepository ourInstance = new PlaylistRepository();


    public static PlaylistRepository getInstance() {
        return ourInstance;
    }


    public MutableLiveData<Playlist> fetchPlaylist(String playlistId) {
        MutableLiveData<Playlist> playlistMutableLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Playlist> playlistCall = oudApi.playlist(playlistId);





        return playlistMutableLiveData;
    }
}
