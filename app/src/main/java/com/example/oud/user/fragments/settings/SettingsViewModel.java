package com.example.oud.user.fragments.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.Constants;
import com.example.oud.api.LoggedInUser;
import com.example.oud.api.ProfilePreview;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class SettingsViewModel extends ConnectionAwareViewModel<SettingsRepository> {
    private MutableLiveData<LoggedInUser> fullProfile;
    private MutableLiveData<ProfilePreview> profile;

    public SettingsViewModel(){
        super(new SettingsRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<LoggedInUser> getFullProfile(String token){
        if(fullProfile == null){
            fullProfile = mRepo.getCurrentUser(token);
        }
        return fullProfile;
    }

    public MutableLiveData<ProfilePreview> getProfile(String userId,String token){
        if(profile ==null){
            profile = mRepo.loadProfile(userId, token);
        }
        return  profile;
    }


    @Override
    public void clearData() {
        fullProfile= null;
    }
}
