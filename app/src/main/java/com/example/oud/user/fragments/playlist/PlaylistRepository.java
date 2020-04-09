package com.example.oud.user.fragments.playlist;

import android.util.Log;

import com.example.oud.api.ChangePlaylistDetailsPayload;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.OudApi;
import com.example.oud.api.Playlist;
import com.example.oud.api.ReorderPlaylistPayload;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class PlaylistRepository extends ConnectionAwareRepository {

    private static final String TAG = PlaylistRepository.class.getSimpleName();

    private static final PlaylistRepository ourInstance = new PlaylistRepository();


    public static PlaylistRepository getInstance() {
        return ourInstance;
    }


    public MutableLiveData<Playlist> fetchPlaylist(String token, String playlistId) {
        MutableLiveData<Playlist> playlistMutableLiveData = new MutableLiveData<>();


        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Playlist> playlistCall = oudApi.playlist(token, playlistId);

        addCall(playlistCall).enqueue(new FailureSuccessHandledCallback<Playlist>(this) {
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

    public MutableLiveData<Album> fetchAlbum(String token, String albumId) {
        MutableLiveData<Album> albumMutableLiveData = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi();
        Call<Album> albumCall = oudApi.album(token, albumId);

        addCall(albumCall).enqueue(new FailureSuccessHandledCallback<Album>(this) {

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

    public void reorderTrack(String token, String playlistId, int fromPosition, int toPosition) {
        OudApi oudApi = instantiateRetrofitOudApi();

        ReorderPlaylistPayload reorderPlaylistPayload = new ReorderPlaylistPayload(fromPosition, 1, toPosition);

        Call reorderCall = oudApi.reorderPlaylistTracks(token, playlistId, reorderPlaylistPayload);

        addCall(reorderCall).enqueue(new FailureSuccessHandledCallback(this) {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
            }
        });
    }

    public void renamePlaylist(String token, String playlistId, String newName) {
        OudApi oudApi = instantiateRetrofitOudApi();

        ChangePlaylistDetailsPayload changePlaylistDetailsPayload = new ChangePlaylistDetailsPayload(newName, null, null, null, null);

        Call changeDetailsCall = oudApi.changePlaylistDetails(token, playlistId, changePlaylistDetailsPayload);

        addCall(changeDetailsCall).enqueue(new FailureSuccessHandledCallback(this) {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
            }
        });
    }


}
