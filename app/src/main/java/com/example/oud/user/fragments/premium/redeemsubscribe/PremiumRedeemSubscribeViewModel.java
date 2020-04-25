package com.example.oud.user.fragments.premium.redeemsubscribe;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.Profile;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PremiumRedeemSubscribeViewModel extends ConnectionAwareViewModel<PremiumRedeemSubscribeRepository> {


    private MutableLiveData<Profile> profileLiveData;


    public PremiumRedeemSubscribeViewModel() {
        super(PremiumRedeemSubscribeRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     *
     * @param token
     * @return {@link LiveData} that will hold a {@link Profile} object containing all the user info.
     */
    public LiveData<Profile> getProfileLiveData(String token) {
        if (profileLiveData == null) {
            profileLiveData = mRepo.getProfileOfCurrentUser(token);
        }
        return profileLiveData;
    }

    /**
     * <p>Redeem the coupon to get more credit.</p>
     * <p>You can call {@link #getProfileLiveData(String)} and observe it to get the new profile data when the server successfully responds.</p>
     * @param token
     * @param couponId
     */
    public void redeemCoupon(String token, String couponId, OudUtils.ServerFailureResponseListener serverFailureResponseListener) {
        OudUtils.ServerSuccessResponseListener<Profile> serverSuccessResponseListener = (code, responseBody) -> {
            if (profileLiveData == null)
                profileLiveData = new MutableLiveData<>();
            profileLiveData.postValue(responseBody);
        };

        mRepo.redeemCoupon(token, couponId, serverSuccessResponseListener, serverFailureResponseListener);
    }

    /**
     * <p>Subscribe to premium plan or extend your current plan if you're already subscribed.</p>
     * <p>You can call {@link #getProfileLiveData(String)} and observe it to get the new profile data when the server successfully responds.</p>
     * @param token
     */
    public void subscribeToPremiumOrExtendCurrentPlan(String token, OudUtils.ServerFailureResponseListener serverFailureResponseListener) {
        OudUtils.ServerSuccessResponseListener<Profile> serverSuccessResponseListener = (code, responseBody) -> {
            if (profileLiveData == null)
                profileLiveData = new MutableLiveData<>();
            profileLiveData.postValue(responseBody);
        };

        mRepo.subscribeToPremiumOrExtendCurrentPlan(token, serverSuccessResponseListener, serverFailureResponseListener);
    }

    public void clearProfileData() {
        profileLiveData = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearProfileData();
    }

    @Override
    public void clearData() {
        clearProfileData();
    }
}
