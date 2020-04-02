package com.example.oud.user.fragments.playlist;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.OudApi;
import com.example.oud.api.Playlist;
import com.example.oud.api.ReorderPlaylistPayload;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaylistRepository extends ConnectionAwareRepository {

    private static final String TAG = PlaylistRepository.class.getSimpleName();

    private static final PlaylistRepository ourInstance = new PlaylistRepository();


    public static PlaylistRepository getInstance() {
        return ourInstance;
    }


    public MutableLiveData<Playlist> fetchPlaylist(String playlistId) {
        MutableLiveData<Playlist> playlistMutableLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Playlist> playlistCall = oudApi.playlist(playlistId);

        playlistCall.enqueue(new FailureSuccessHandledCallback<Playlist>(connectionStatusListener) {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                Playlist playlist = response.body();
                playlistMutableLiveData.setValue(playlist);
            }
        });


        return playlistMutableLiveData;
    }

    public MutableLiveData<Album> fetchAlbum(String albumId) {
        MutableLiveData<Album> albumMutableLiveData = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Album> albumCall = oudApi.album(albumId);

        albumCall.enqueue(new FailureSuccessHandledCallback<Album>(connectionStatusListener) {

            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                Album album = response.body();
                albumMutableLiveData.setValue(album);
            }
        });


        return albumMutableLiveData;
    }

    public void reorderTrack(String playlistId, int fromPosition, int toPosition) {
        OudApi oudApi = instantiateRetrofitOudApi();

        ReorderPlaylistPayload reorderPlaylistPayload = new ReorderPlaylistPayload(fromPosition, 1, toPosition);

        Call reorderCall = oudApi.reorderPlaylistTracks(playlistId, reorderPlaylistPayload);

        reorderCall.enqueue(new FailureSuccessHandledCallback(connectionStatusListener));


    }
}
