package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.Playlist;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserPlaylistsResponse;


import java.util.Arrays;
import java.util.List;

public class ProfilePlaylistsViewModel extends ViewModel implements ConnectionStatusListener {
    private ProfileRepository repository;
    private MutableLiveData<Boolean> connectionSuccessful = new MutableLiveData<>();
    private MutableLiveData<List<PlaylistPreview>> playlists;
    private MutableLiveData<Integer> totalNumberOfPlaylists;

    public boolean isAllLoaded() {
        return allLoaded;
    }

    private boolean allLoaded=false;

    public MutableLiveData<List<PlaylistPreview>> getUserPlaylists(String userId){
        MutablePlaylistWithTotal result;
        if(playlists == null){
            result = repository.loadUserPlaylists(userId);
            playlists = result.getPlaylists();
            totalNumberOfPlaylists = result.getTotal();

        }
        return playlists;
    }
    public MutableLiveData<Integer> getTotalNumberOfPlaylists(){
        return totalNumberOfPlaylists;
    }
    public void loadMorePlaylists(String userId){
        if(playlists == null)
            return;
        repository.loadMoreUserPlaylists(userId,playlists.getValue().size()-1,playlists);

    }


    public ProfilePlaylistsViewModel() {
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
