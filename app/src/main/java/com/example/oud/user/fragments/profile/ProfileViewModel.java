package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserPlaylistsResponse;

import java.util.List;

public class ProfileViewModel extends ViewModel implements ConnectionStatusListener {

    private ProfileRepository repository;
    private MutableLiveData<Boolean> connectionSuccessful;
    private MutableLiveData<ProfilePreview> profile;
    private MutableLiveData<List<PlaylistPreview>> playlists;

    public MutableLiveData<ProfilePreview> getProfile(String userId){
        if(profile ==null)
            profile = repository.loadProfile(userId);
        return  profile;
    }

    public MutableLiveData<List<PlaylistPreview>> getUserPlaylists(String userId){
        if(playlists == null)
            playlists = repository.loadUserPlaylists(userId);

        return playlists;
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
