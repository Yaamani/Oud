package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.Constants;
import com.example.oud.api.LoggedInUser;
import com.example.oud.api.UpdateProfileData;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class RenameDisplayNameWithPasswordViewModel extends ConnectionAwareViewModel<RenameDisplayNameWithPasswordRepository> {
    private MutableLiveData<LoggedInUser> profile;

    public RenameDisplayNameWithPasswordViewModel(){
        super(new RenameDisplayNameWithPasswordRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<LoggedInUser> getProfile(String token){
        if(profile==null)
            profile = mRepo.getCurrentUser(token);

        return profile;
    }
    public void updateProfile(String token, UpdateProfileData data,MutableLiveData<String> errorMessage){
        mRepo.updateProfile(token,data,errorMessage);
    }




    @Override
    public void clearData() {

    }
}
