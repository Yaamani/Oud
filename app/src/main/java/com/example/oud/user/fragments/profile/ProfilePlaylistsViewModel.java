package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.Playlist;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserPlaylistsResponse;
import com.example.oud.connectionaware.ConnectionAwareViewModel;


import java.util.List;

public class ProfilePlaylistsViewModel extends ConnectionAwareViewModel<ProfilePlaylistRepository> {
    private MutableLiveData<List<PlaylistPreview>> playlists;
    private MutableLiveData<Integer> totalNumberOfPlaylists;


    public MutableLiveData<List<PlaylistPreview>> getUserPlaylists(String userId){
        MutablePlaylistWithTotal result;
        if(playlists == null){
            result = mRepo.loadUserPlaylists(userId);
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
        mRepo.loadMoreUserPlaylists(userId,playlists.getValue().size()-1,playlists);

    }


    public ProfilePlaylistsViewModel() {
        super(new ProfilePlaylistRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }

    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();
    }

    @Override
    public void onConnectionFailure() {
        super.onConnectionFailure();
    }

    @Override
    public void clearData() {
        playlists = null;
        totalNumberOfPlaylists = null;

    }

    public void followPlaylist(String token, String playlistId,ConnectionStatusListener connectionStatusListener){
        mRepo.followPlaylist(token ,playlistId,connectionStatusListener);
    }

    public void unFollowPlaylist(String token, String playlistId,ConnectionStatusListener connectionStatusListener){
        mRepo.unFollowPlaylist(token,playlistId,connectionStatusListener);
    }

    public MutableLiveData<Boolean> checkIfIFollowThisPlaylist(String token,String playlistId,String userId){
        MutableLiveData<Boolean> iFollowThisPlaylist= new MutableLiveData<>();
        mRepo.checkIfIFollowThisPlaylist(token,playlistId,userId,iFollowThisPlaylist);

        return iFollowThisPlaylist;
    }



}
