package com.example.oud.user.fragments.playlist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class PlaylistViewModel extends ConnectionAwareViewModel<PlaylistRepository> {
    // TODO: Implement the ViewModel

    // Playlist
    private MutableLiveData<Playlist> playlistLiveData;
    private ArrayList<MutableLiveData<Album>> eachTrackAlbum;

    // Album
    //private

    public PlaylistViewModel() {
        super(PlaylistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<Playlist> getPlaylistLiveData(String playlistId) {
        if (playlistLiveData == null) {
            // Fetch.
            playlistLiveData = mRepo.fetchPlaylist(playlistId);
        } else {

            String currentId = playlistLiveData.getValue().getId();
            if (currentId.equals(playlistId)) {
                return playlistLiveData;
            } else {
                // Fetch
                playlistLiveData = mRepo.fetchPlaylist(playlistId);
            }
        }

        return playlistLiveData;
    }

    public MutableLiveData<Album> getAlbumLiveData(int position, String albumId) {
        if (eachTrackAlbum == null)
            eachTrackAlbum = new ArrayList<>();

        if (position >= eachTrackAlbum.size()) {
            for (int i = eachTrackAlbum.size(); i <= position; i++) {
                eachTrackAlbum.add(null);
            }
        }

        if (eachTrackAlbum.get(position) == null)
            eachTrackAlbum.set(position, mRepo.fetchAlbum(albumId));
        else if (!eachTrackAlbum.get(position).getValue().get_id().equals(albumId))
            eachTrackAlbum.set(position, mRepo.fetchAlbum(albumId));

        return eachTrackAlbum.get(position);
    }

    @Override
    public void clearData() {
        playlistLiveData = null;

    }
}
