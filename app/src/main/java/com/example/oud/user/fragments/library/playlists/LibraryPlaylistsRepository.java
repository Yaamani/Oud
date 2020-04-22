package com.example.oud.user.fragments.library.playlists;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.FollowingPublicityPayload;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.PlaylistDetailsPayload;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class LibraryPlaylistsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryPlaylistsRepository.class.getSimpleName();

    private static final LibraryPlaylistsRepository ourInstance = new LibraryPlaylistsRepository();

    private LibraryPlaylistsRepository() {}

    public static LibraryPlaylistsRepository getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param token
     * @param offset The index which the returned items will start at.
     * @param limit Number of items returned by the server.
     * @return A {@link MutableLiveData} containing the playlists followed by the current user.
     */
    public MutableLiveData<OudList<Playlist>> getPlaylistsFollowedByCurrentUser(String token, Integer limit, Integer offset) {
        MutableLiveData<OudList<Playlist>> playlistsLiveData = new MutableLiveData<>();

        Call<OudList<Playlist>> playlistsCall = oudApi.getPlaylistsFollowedByCurrentUser(token, limit, offset);
        addCall(playlistsCall).enqueue(new FailureSuccessHandledCallback<OudList<Playlist>>(this) {
            @Override
            public void onResponse(Call<OudList<Playlist>> call, Response<OudList<Playlist>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                playlistsLiveData.setValue(response.body());
            }
        });

        return playlistsLiveData;
    }

    public void unfollowPlaylist(String token, String playlistId, ConnectionStatusListener undoUiAndUpdateLiveData) {

        Call<ResponseBody> unfollowCall = oudApi.unfollowPlaylist(token, playlistId);
        addCall(unfollowCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this, undoUiAndUpdateLiveData) {
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
     * Create a playlist with the supplied details.
     * @param token
     * @param loggedInUserId
     * @param playlistDetailsPayload Playlist details.
     * @param playlistCreationListener Listener to react when the playlist is created or when there's an error.
     */
    public void createPlaylist(String token, String loggedInUserId, PlaylistDetailsPayload playlistDetailsPayload, PlaylistCreationListener playlistCreationListener) {

        Call<Playlist> createPlaylistCall = oudApi.createPlaylist(token, loggedInUserId, playlistDetailsPayload);
        addCall(createPlaylistCall).enqueue(new FailureSuccessHandledCallback<Playlist>(this) {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    playlistCreationListener.onCreationFailure(PlaylistCreationListener.PlaylistCreationFailureState.CREATION_FAILURE);
                    return;
                }

                Playlist playlist = response.body();
                follow(token, playlist, playlistCreationListener);
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                super.onFailure(call, t);
                playlistCreationListener.onCreationFailure(PlaylistCreationListener.PlaylistCreationFailureState.CREATION_FAILURE);

            }
        });
    }

    private void follow(String token, Playlist playlist, PlaylistCreationListener playlistCreationListener) {
        Call<ResponseBody> followCall = oudApi.followPlaylist(token, playlist.getId(), new FollowingPublicityPayload(true));
        addCall(followCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(LibraryPlaylistsRepository.this) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    playlistCreationListener.onCreationFailure(PlaylistCreationListener.PlaylistCreationFailureState.SUCCESSFUL_CREATION_FOLLOWING_FAILURE);
                    return;
                }

                playlistCreationListener.onSuccessfulCreation(playlist);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                playlistCreationListener.onCreationFailure(PlaylistCreationListener.PlaylistCreationFailureState.SUCCESSFUL_CREATION_FOLLOWING_FAILURE);
            }
        });
    }

    public interface PlaylistCreationListener {

        enum PlaylistCreationFailureState {CREATION_FAILURE, SUCCESSFUL_CREATION_FOLLOWING_FAILURE}

        void onSuccessfulCreation(Playlist playlist);
        void onCreationFailure(PlaylistCreationFailureState playlistCreationFailureState);
    }

}























