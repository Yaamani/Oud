package com.example.oud.user.fragments.premium.offlinetracks;

import android.util.Log;

import com.example.oud.OudUtils;
import com.example.oud.api.CouponPayload;
import com.example.oud.api.Profile;
import com.example.oud.api.StatusMessageResponse;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.google.gson.Gson;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class PremiumOfflineTracksRepository extends ConnectionAwareRepository {

    private static final String TAG = PremiumOfflineTracksRepository.class.getSimpleName();

    public static final PremiumOfflineTracksRepository ourInstance = new PremiumOfflineTracksRepository();

    public static PremiumOfflineTracksRepository getInstance() {
        return ourInstance;
    }

    private PremiumOfflineTracksRepository() {

    }

    /**
     *
     * @param token
     * @return {@link MutableLiveData} that will hold a {@link Profile} object containing all the user info.
     */
    public MutableLiveData<Profile> getProfileOfCurrentUser(String token) {
        MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();

        Call<Profile> profileCall = oudApi.getProfileOfCurrentUser(token);
        addCall(profileCall).enqueue(new FailureSuccessHandledCallback<Profile>(this) {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                profileMutableLiveData.postValue(response.body());
            }
        });



        return profileMutableLiveData;
    }
}
