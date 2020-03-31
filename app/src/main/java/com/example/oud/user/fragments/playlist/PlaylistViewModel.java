package com.example.oud.user.fragments.playlist;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.Playlist;
import com.example.oud.user.fragments.home.HomeRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaylistViewModel extends ViewModel implements ConnectionStatusListener {
    // TODO: Implement the ViewModel

    private PlaylistRepository playlistRepository;

    private MutableLiveData<Playlist> playlistLiveData;

    private MutableLiveData<Constants.ConnectionStatus> connectionStatus = new MutableLiveData<Constants.ConnectionStatus>();


    public PlaylistViewModel() {
        playlistRepository = PlaylistRepository.getInstance();
        playlistRepository.setConnectionStatusListener(this);
        if (Constants.MOCK)
            playlistRepository.setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Playlist> getPlaylistLiveData(String playlistId) {
        if (playlistLiveData == null) {
            // Fetch.
        } else {

            String currentId = playlistLiveData.getValue().getId();
            if (currentId.equals(playlistId)) {
                return playlistLiveData;
            } else {
                // Fetch
            }
        }

        return playlistLiveData;
    }

    @Override
    public void onConnectionSuccess() {
        connectionStatus.setValue(Constants.ConnectionStatus.SUCCESSFUL);
    }

    @Override
    public void onConnectionFailure() {
        connectionStatus.setValue(Constants.ConnectionStatus.FAILED);
    }
}
