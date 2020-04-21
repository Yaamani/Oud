package com.example.oud.user.fragments.premium.redeemsubscribe;

import com.example.oud.Constants;
import com.example.oud.api.Profile;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PremiumRedeemSubscribeViewModel extends ConnectionAwareViewModel<PremiumRedeemSubscribeRepository> {


    private MutableLiveData<Profile> profileLiveData;


    public PremiumRedeemSubscribeViewModel() {
        super(PremiumRedeemSubscribeRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public LiveData<Profile> getProfileLiveData(String token) {
        if (profileLiveData == null) {
            profileLiveData = mRepo.getProfileOfCurrentUser(token);
        }
        return profileLiveData;
    }

    @Override
    public void clearData() {
        profileLiveData = null;
    }
}
