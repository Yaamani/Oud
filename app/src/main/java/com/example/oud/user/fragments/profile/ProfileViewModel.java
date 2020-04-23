package com.example.oud.user.fragments.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;

import com.example.oud.Constants;
import com.example.oud.OudUtils;

import com.example.oud.api.LoggedInUser;
import com.example.oud.api.ProfilePreview;

import com.example.oud.connectionaware.ConnectionAwareViewModel;


public class ProfileViewModel extends ConnectionAwareViewModel<ProfileRepository> {

    private MutableLiveData<ProfilePreview> profile;
    private MutableLiveData<Boolean> iFollowThisUser = new MutableLiveData<>();

    public ProfileViewModel(){
        super(new ProfileRepository(), Constants.YAMANI_MOCK_BASE_URL);

    }


    public MutableLiveData<ProfilePreview> getProfile(String userId,String token){
        if(profile ==null){

            profile = mRepo.loadProfile(userId, token);

        }
        return  profile;
    }

    public MutableLiveData<Boolean> checkIfIFollowThisUser(String token,String userId){
        String []list = new String[1];
        list[0]= userId;

        String commaSeparatedUserId = OudUtils.commaSeparatedListQueryParameter(list);
        mRepo.checkIfIFollowThisUser(token,commaSeparatedUserId,iFollowThisUser);

        return iFollowThisUser;
    }


    public void updateProfileImage(String token, Uri image, Bitmap bitmap, Context context,ConnectionStatusListener connectionStatusListenerUndo){
        mRepo.setProfileImage(token,image,bitmap,context,connectionStatusListenerUndo);
    }

    public void followUser(String token,String userid,ConnectionStatusListener connectionStatusListener){
        mRepo.followUser(token,userid,connectionStatusListener);
    }

    public void unFollowUser(String token,String userid,ConnectionStatusListener connectionStatusListener){
        mRepo.unFollowUser(token,userid,connectionStatusListener);
    }

    public MutableLiveData<LoggedInUser> getFullProfile(String token){
        MutableLiveData<LoggedInUser> fullProfile = mRepo.getCurrentUser(token);
        return fullProfile;
    }



    @Override
    public void clearData() {
        profile = null;
    }



}
