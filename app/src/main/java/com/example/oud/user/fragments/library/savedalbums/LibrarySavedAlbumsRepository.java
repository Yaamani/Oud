package com.example.oud.user.fragments.library.savedalbums;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.OudUtils;
import com.example.oud.api.OudList;
import com.example.oud.api.SavedAlbum;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class LibrarySavedAlbumsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibrarySavedAlbumsRepository.class.getSimpleName();

    private static final LibrarySavedAlbumsRepository ourInstance = new LibrarySavedAlbumsRepository();

    private LibrarySavedAlbumsRepository() {}

    public static LibrarySavedAlbumsRepository getInstance() {
        return ourInstance;
    }

    /**
     *
     * @param token
     * @param offset The index which the returned items will start at.
     * @param limit Number of items returned by the server.
     * @return A {@link MutableLiveData} containing the SavedAlbums followed by the current user.
     */
    public MutableLiveData<OudList<SavedAlbum>> getSavedAlbumsByCurrentUser(String token, Integer limit, Integer offset) {
        MutableLiveData<OudList<SavedAlbum>> SavedAlbumsLiveData = new MutableLiveData<>();

        Call<OudList<SavedAlbum>> SavedAlbumsCall = oudApi.getSavedAlbumsByCurrentUser(token, limit, offset);
        addCall(SavedAlbumsCall).enqueue(new FailureSuccessHandledCallback<OudList<SavedAlbum>>(this) {
            @Override
            public void onResponse(Call<OudList<SavedAlbum>> call, Response<OudList<SavedAlbum>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                SavedAlbumsLiveData.setValue(response.body());
            }
        });

        return SavedAlbumsLiveData;
    }

    public void unsaveAlbum(String token, ArrayList<String> savedAlbumIds, ConnectionStatusListener undoUiAndUpdateLiveData) {

        Call<ResponseBody> unsaveCall = oudApi.unsaveTheseAlbumsForTheCurrentUser(token, OudUtils.commaSeparatedListQueryParameter(savedAlbumIds));
        addCall(unsaveCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this, undoUiAndUpdateLiveData) {
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
