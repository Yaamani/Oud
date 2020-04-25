package com.example.oud.user.fragments.premium.offlinetracks;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.Profile;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.premium.redeemsubscribe.PremiumRedeemSubscribeRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PremiumOfflineTracksViewModel extends ConnectionAwareViewModel<PremiumOfflineTracksRepository> {


    private MutableLiveData<Profile> profileLiveData;


    public PremiumOfflineTracksViewModel() {
        super(PremiumOfflineTracksRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
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
