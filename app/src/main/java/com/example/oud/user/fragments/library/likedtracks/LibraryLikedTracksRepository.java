package com.example.oud.user.fragments.library.likedtracks;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.OudUtils;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudList;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class LibraryLikedTracksRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryLikedTracksRepository.class.getSimpleName();

    private static final LibraryLikedTracksRepository ourInstance = new LibraryLikedTracksRepository();

    private LibraryLikedTracksRepository() {}

    public static LibraryLikedTracksRepository getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param token
     * @param offset The index which the returned items will start at.
     * @param limit Number of items returned by the server.
     * @return A {@link MutableLiveData} containing the tracks liked by the current user.
     */
    public MutableLiveData<OudList<LikedTrack>> getLikedTrackByCurrentUser(String token, Integer limit, Integer offset) {
        MutableLiveData<OudList<LikedTrack>> likedTracksLiveData = new MutableLiveData<>();

        Call<OudList<LikedTrack>> likedTracksCall = oudApi.getLikedTrackByCurrentUser(token, limit, offset);
        addCall(likedTracksCall).enqueue(new FailureSuccessHandledCallback<OudList<LikedTrack>>(this) {
            @Override
            public void onResponse(Call<OudList<LikedTrack>> call, Response<OudList<LikedTrack>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                likedTracksLiveData.setValue(response.body());
            }
        });

        return likedTracksLiveData;
    }

    /**
     *
     * @param token
     * @param ids Ids for tracks you want the current user to not like anymore.
     */
    public void removeTheseTracksFromLikedTracks(String token,
                                                 ArrayList<String> ids,
                                                 ConnectionStatusListener undoUiAndUpdateLiveData) {

        Call<ResponseBody> removeTheseTracksFromLikedTracksCall = oudApi.removeTheseTracksFromLikedTracks(token, OudUtils.commaSeparatedListQueryParameter(ids));
        addCall(removeTheseTracksFromLikedTracksCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this, undoUiAndUpdateLiveData) {
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
