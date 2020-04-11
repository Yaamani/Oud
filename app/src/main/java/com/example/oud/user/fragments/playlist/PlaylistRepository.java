package com.example.oud.user.fragments.playlist;

import android.util.Log;

import com.example.oud.api.ChangePlaylistDetailsPayload;
import com.example.oud.api.RemovePlaylistTracksPayload;
import com.example.oud.api.UserAreTracksLiked;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.api.ReorderPlaylistPayload;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
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


        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<Playlist> playlistCall = oudApi.playlist("Bearer "+ token, playlistId);

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

        //OudApi oudApi = instantiateRetrofitOudApi();
        Call<Album> albumCall = oudApi.album("Bearer "+ token, albumId);

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
        //OudApi oudApi = instantiateRetrofitOudApi();

        ReorderPlaylistPayload reorderPlaylistPayload = new ReorderPlaylistPayload(fromPosition, 1, toPosition);

        Call reorderCall = oudApi.reorderPlaylistTracks("Bearer " + token, playlistId, reorderPlaylistPayload);

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
        //OudApi oudApi = instantiateRetrofitOudApi();

        ChangePlaylistDetailsPayload changePlaylistDetailsPayload = new ChangePlaylistDetailsPayload(newName, null, null, null, null);

        Call changeDetailsCall = oudApi.changePlaylistDetails("Bearer " + token, playlistId, changePlaylistDetailsPayload);

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

    public void deleteTrack(String token, String playlistId, String trackId) {

        RemovePlaylistTracksPayload removePlaylistTracksPayload = new RemovePlaylistTracksPayload(trackId);
        Call removeTrackCall = oudApi.removePlaylistTracks("Bearer " + token, playlistId, removePlaylistTracksPayload);

        addCall(removeTrackCall).enqueue(new FailureSuccessHandledCallback(this) {
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

    public MutableLiveData<UserAreTracksLiked> areTracksLiked(String token, ArrayList<String> ids) {
        MutableLiveData<UserAreTracksLiked> savedTracksMutableLiveData = new MutableLiveData<>();

        Call<UserAreTracksLiked> areTracksSavedCall = oudApi.getAreTheseTracksLiked("Bearer " + token, ids);
        addCall(areTracksSavedCall).enqueue(new FailureSuccessHandledCallback<UserAreTracksLiked>(this) {
            @Override
            public void onResponse(Call<UserAreTracksLiked> call, Response<UserAreTracksLiked> response) {
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

        Call<ResponseBody> addTheseTracksToLikedTracksCall = oudApi.addTheseTracksToLikedTracks("Bearer" + token, ids);
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

        Call<ResponseBody> removeTheseTracksFromLikedTracksCall = oudApi.removeTheseTracksFromLikedTracks("Bearer" + token, ids);
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
