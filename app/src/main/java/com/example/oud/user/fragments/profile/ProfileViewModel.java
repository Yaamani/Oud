package com.example.oud.user.fragments.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;

import com.example.oud.Constants;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserPlaylistsResponse;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class ProfileViewModel extends ConnectionAwareViewModel<ProfileRepository> {

    public ProfileViewModel(){
        super(new ProfileRepository(), Constants.YAMANI_MOCK_BASE_URL);

    }
    private MutableLiveData<ProfilePreview> profile;


    public MutableLiveData<ProfilePreview> getProfile(String userId,String token){
        if(profile ==null){

            profile = mRepo.loadProfile(userId, token);

        }
        return  profile;
    }


    public void updateProfileImage(String token, Uri image, Bitmap bitmap, Context context){
        mRepo.setProfileImage(token,image,bitmap,context);
    }


    @Override
    public void clearData() {
        profile = null;
    }



}
