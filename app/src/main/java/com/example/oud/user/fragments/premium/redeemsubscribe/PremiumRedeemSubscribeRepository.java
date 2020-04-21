package com.example.oud.user.fragments.premium.redeemsubscribe;

import android.util.Log;

import com.example.oud.api.Profile;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

public class PremiumRedeemSubscribeRepository extends ConnectionAwareRepository {

    private static final String TAG = PremiumRedeemSubscribeRepository.class.getSimpleName();

    public static final PremiumRedeemSubscribeRepository ourInstance = new PremiumRedeemSubscribeRepository();

    public static PremiumRedeemSubscribeRepository getInstance() {
        return ourInstance;
    }

    private PremiumRedeemSubscribeRepository() {

    }

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
