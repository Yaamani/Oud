package com.example.oud.user.fragments.playlist;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.oud.OudUtils;
import com.example.oud.api.PlaylistDetailsPayload;
import com.example.oud.api.FollowingPublicityPayload;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.api.ReorderPlaylistPayload;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import java.io.File;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

        //OudApi oudApi = instantiateRetrofitOudApi();
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
        //OudApi oudApi = instantiateRetrofitOudApi();

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

    public void changePlaylistDetails(String token, String playlistId, String newName, Boolean _public, Boolean collaborative) {
        //OudApi oudApi = instantiateRetrofitOudApi();

        PlaylistDetailsPayload playlistDetailsPayload = new PlaylistDetailsPayload(newName, _public, collaborative, null, null);

        Call changeDetailsCall = oudApi.changePlaylistDetails(token, playlistId, playlistDetailsPayload);

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

        // RemovePlaylistTracksPayload removePlaylistTracksPayload = new RemovePlaylistTracksPayload(trackId);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(trackId);

        Call removeTrackCall = oudApi.removePlaylistTracks(token, playlistId, OudUtils.commaSeparatedListQueryParameter(ids));

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

    public MutableLiveData<ArrayList<Boolean>> checkIfUsersFollowPlaylist(String token, String playlistId, ArrayList<String> ids) {
        MutableLiveData<ArrayList<Boolean>> followingLiveData = new MutableLiveData<>();

        Call<ArrayList<Boolean>> followingCall = oudApi.checkIfUsersFollowPlaylist(token, playlistId, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(followingCall).enqueue(new FailureSuccessHandledCallback<ArrayList<Boolean>>(this) {
            @Override
            public void onResponse(Call<ArrayList<Boolean>> call, Response<ArrayList<Boolean>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                followingLiveData.setValue(response.body());
            }
        });

        return followingLiveData;
    }

    /**
     *
     * @param token
     * @param playlistId
     * @param followingPublicly When true, other users can see that you're following this playlist.
     */
    public void followPlaylist(String token, String playlistId, boolean followingPublicly) {

        FollowingPublicityPayload followingPublicityPayload = new FollowingPublicityPayload(followingPublicly);

        Call<ResponseBody> followCall = oudApi.followPlaylist(token, playlistId, followingPublicityPayload);
        addCall(followCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
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

    public void unfollowPlaylist(String token, String playlistId) {

        Call<ResponseBody> unfollowCall = oudApi.unfollowPlaylist(token, playlistId);
        addCall(unfollowCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
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

    public MutableLiveData<ArrayList<Boolean>> checkIfTheseAlbumsAreSavedByUser(String token, ArrayList<String> ids) {
        MutableLiveData<ArrayList<Boolean>> theseAlbumsSavedByUserLiveData = new MutableLiveData<>();

        Call<ArrayList<Boolean>> theseAlbumsSavedByUserCall = oudApi.checkIfTheseAlbumsAreSavedByUser(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(theseAlbumsSavedByUserCall).enqueue(new FailureSuccessHandledCallback<ArrayList<Boolean>>(this) {
            @Override
            public void onResponse(Call<ArrayList<Boolean>> call, Response<ArrayList<Boolean>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                theseAlbumsSavedByUserLiveData.setValue(response.body());
            }
        });

        return theseAlbumsSavedByUserLiveData;
    }

    public void saveTheseAlbumsForTheCurrentUser(String token, ArrayList<String> ids) {

        Call<ResponseBody> saveAlbumsCall = oudApi.saveTheseAlbumsForTheCurrentUser(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(saveAlbumsCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
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

    public void unsaveTheseAlbumsForTheCurrentUser(String token, ArrayList<String> ids) {

        Call<ResponseBody> unsaveAlbumsCall = oudApi.unsaveTheseAlbumsForTheCurrentUser(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(unsaveAlbumsCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
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

    public void uploadPlaylistImage(String token, Context context, String playlistId, File file, Uri uri) {

        RequestBody requestFile = RequestBody.create(file, MediaType.parse(context.getContentResolver().getType(uri)));

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        //MultipartBody.Part multipartBody = MultipartBody.Part.create(requestFile);

        Call<ResponseBody> uploadImageCall = oudApi.uploadPlaylistImage(token, playlistId, multipartBody);
        addCall(uploadImageCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this) {
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
