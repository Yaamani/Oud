package com.example.oud.user.fragments.profile;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserPlaylistsResponse;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class ProfileViewModel extends ViewModel implements ConnectionStatusListener {

    private ProfileRepository repository;
    private MutableLiveData<Boolean> connectionSuccessful = new MutableLiveData<>();
    private MutableLiveData<ProfilePreview> profile;
    private MutableLiveData<List<PlaylistPreview>> playlists;

    public MutableLiveData<ProfilePreview> getProfile(String userId){
        if(profile ==null)
            profile = repository.loadProfile(userId);
        return  profile;
    }


    public void updateProfileImage(String token, Uri image){
        repository.setProfileImage(token,image);
    }



    public ProfileViewModel() {
        repository = new ProfileRepository(this);
    }

    @Override
    public void onConnectionSuccess() {
        connectionSuccessful.setValue(true);
    }

    @Override
    public void onConnectionFailure() {
        connectionSuccessful.setValue(false);
    }

    public MutableLiveData<Boolean> getConnectionSuccessful() {
        return connectionSuccessful;
    }

}