package com.example.oud.user.fragments.library.artists;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.NotificationShareUtils;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class LibraryArtistsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryArtistsRepository.class.getSimpleName();

    private static final LibraryArtistsRepository ourInstance = new LibraryArtistsRepository();

    private LibraryArtistsRepository() {}

    public static LibraryArtistsRepository getInstance() {
        return ourInstance;
    }




    /**
     *
     * @param token
     * @param offset The index which the returned items will start at.
     * @param limit Number of items returned by the server.
     * @return A {@link MutableLiveData} containing the artists followed by the current user.
     */
    public MutableLiveData<OudList<ArtistPreview>> getArtistsFollowedByCurrentUser(String token, Integer limit, Integer offset) {
        MutableLiveData<OudList<ArtistPreview>> ArtistsLiveData = new MutableLiveData<>();

        Call<OudList<ArtistPreview>> ArtistsCall = oudApi.getArtistsFollowedByCurrentUser(token, limit, offset);
        addCall(ArtistsCall).enqueue(new FailureSuccessHandledCallback<OudList<ArtistPreview>>(this) {
            @Override
            public void onResponse(Call<OudList<ArtistPreview>> call, Response<OudList<ArtistPreview>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                ArtistsLiveData.setValue(response.body());
            }
        });

        return ArtistsLiveData;
    }

    public void unfollowArtist(String token, String ArtistId, ConnectionStatusListener undoUiAndUpdateLiveData) {

        Call<ResponseBody> unfollowCall = oudApi.unfollowArtistsOrUsers(token, Constants.API_ARTIST, ArtistId);
        addCall(unfollowCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this, undoUiAndUpdateLiveData) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
                NotificationShareUtils.unsubscribeFromFollowedArtistTopic(ArtistId);
            }
        });
    }

}
