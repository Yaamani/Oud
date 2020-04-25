package com.example.oud.user.fragments.premium.redeemsubscribe;

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

public class PremiumRedeemSubscribeRepository extends ConnectionAwareRepository {

    private static final String TAG = PremiumRedeemSubscribeRepository.class.getSimpleName();

    public static final PremiumRedeemSubscribeRepository ourInstance = new PremiumRedeemSubscribeRepository();

    public static PremiumRedeemSubscribeRepository getInstance() {
        return ourInstance;
    }

    private PremiumRedeemSubscribeRepository() {

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

    /**
     * Redeem the coupon to get more credit.
     * @param token
     * @param couponId
     * @param serverSuccessResponseListener To get the returned {@link Profile} if it's a success response.
     * @param serverFailureResponseListener To get the failure code if it's a failure response.
     *
     */
    public void redeemCoupon(String token,
                             String couponId,
                             OudUtils.ServerSuccessResponseListener<Profile> serverSuccessResponseListener,
                             OudUtils.ServerFailureResponseListener serverFailureResponseListener) {

        CouponPayload couponPayload = new CouponPayload(couponId);
        Call<Profile> profileCall = oudApi.redeemCoupon(token, couponPayload);
        addCall(profileCall).enqueue(new FailureSuccessHandledCallback<Profile>(this) {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    StatusMessageResponse errorMessage = new StatusMessageResponse(String.valueOf(response.code()), "");
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        try {
                            errorMessage = gson.fromJson(response.errorBody().charStream(),StatusMessageResponse.class);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    serverFailureResponseListener.respondsWithFailureCode(response.code(), errorMessage);
                    return;
                }
                serverSuccessResponseListener.respondsWithSuccessCode(response.code(), response.body());
            }
        });

    }

    /**
     * Subscribe to premium plan or extend your current plan if you're already subscribed.
     * @param token
     * @param serverSuccessResponseListener To get the returned {@link Profile} if it's a success response.
     * @param serverFailureResponseListener To get the failure code if it's a failure response.
     */
    public void subscribeToPremiumOrExtendCurrentPlan(String token,
                                                      OudUtils.ServerSuccessResponseListener<Profile> serverSuccessResponseListener,
                                                      OudUtils.ServerFailureResponseListener serverFailureResponseListener) {

        Call<Profile> profileCall = oudApi.subscribeToPremiumOrExtendCurrentPlan(token);
        addCall(profileCall).enqueue(new FailureSuccessHandledCallback<Profile>(this) {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());

                    StatusMessageResponse errorMessage = new StatusMessageResponse(String.valueOf(response.code()), "");
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        try {
                            errorMessage = gson.fromJson(response.errorBody().charStream(),StatusMessageResponse.class);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    serverFailureResponseListener.respondsWithFailureCode(response.code(), errorMessage);
                    return;
                }
                serverSuccessResponseListener.respondsWithSuccessCode(response.code(), response.body());
            }
        });



    }

}
