package com.example.oud.user.fragments.playlist;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.Playlist;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.home.HomeRepository;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PlaylistViewModel extends ConnectionAwareViewModel<PlaylistRepository> {
    // TODO: Implement the ViewModel

    private MutableLiveData<Playlist> playlistLiveData;

    public PlaylistViewModel() {
        super(PlaylistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
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
    public void clearData() {
        playlistLiveData = null;
    }
}
