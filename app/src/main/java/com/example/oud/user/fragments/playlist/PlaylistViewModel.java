package com.example.oud.user.fragments.playlist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import androidx.lifecycle.MutableLiveData;

public class PlaylistViewModel extends ConnectionAwareViewModel<PlaylistRepository> {
    // TODO: Implement the ViewModel


    public enum PlaylistOperation {RENAME, REORDER, DELETE, UPLOAD_IMAGE}
    private PlaylistOperation currentOperation = null;


    // Playlist
    private MutableLiveData<Playlist> playlistLiveData;
    private ArrayList<MutableLiveData<Album>> eachTrackAlbumLiveData;

    // Album
    private MutableLiveData<Album> albumLiveData;





    private int reorderingFromPosition;
    private int reorderingToPosition;

    private String newName;





    public PlaylistViewModel() {
        super(PlaylistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<Playlist> getPlaylistLiveData(String token, String playlistId) {
        if (playlistLiveData == null) {
            // Fetch.
            playlistLiveData = mRepo.fetchPlaylist(token, playlistId);
        } else {

            if (playlistLiveData.getValue() != null) {
                String currentId = playlistLiveData.getValue().getId();
                if (currentId.equals(playlistId)) {
                    return playlistLiveData;
                } else {
                    // Fetch
                    playlistLiveData = mRepo.fetchPlaylist(token, playlistId);
                }
            }
        }

        return playlistLiveData;
    }

    public MutableLiveData<Album> getTrackAlbumLiveData(String token, int position, String albumId) {
        if (eachTrackAlbumLiveData == null)
            eachTrackAlbumLiveData = new ArrayList<>();

        if (position >= eachTrackAlbumLiveData.size()) {
            for (int i = eachTrackAlbumLiveData.size(); i <= position; i++) {
                eachTrackAlbumLiveData.add(null);
            }
        }

        if (eachTrackAlbumLiveData.get(position) == null)
            eachTrackAlbumLiveData.set(position, mRepo.fetchAlbum(token, albumId));
        else if (!eachTrackAlbumLiveData.get(position).getValue().get_id().equals(albumId))
            eachTrackAlbumLiveData.set(position, mRepo.fetchAlbum(token, albumId));

        return eachTrackAlbumLiveData.get(position);
    }

    public MutableLiveData<Album> getAlbumLiveData(String token, String albumId) {
        if (albumLiveData == null)
            albumLiveData = mRepo.fetchAlbum(token, albumId);
        else {
            if (albumLiveData.getValue() != null) {
                String currentId = albumLiveData.getValue().get_id();
                if (currentId.equals(albumId)) {
                    return albumLiveData;
                } else {
                    // Fetch
                    albumLiveData = mRepo.fetchAlbum(token, albumId);
                }
            }
        }

        return albumLiveData;
    }



    public void reorderTrack(String token, int fromPosition, int toPosition) {
        reorderingFromPosition = fromPosition;
        reorderingToPosition = toPosition;
        // Server
        String id = playlistLiveData.getValue().getId();
        mRepo.reorderTrack(token, id, fromPosition, toPosition);
    }

    public void renamePlaylist(String token, String newName) {
        this.newName = newName;
        if (playlistLiveData.getValue().getName().equals(newName)) return;
        // Server
        String id = playlistLiveData.getValue().getId();
        mRepo.renamePlaylist(token, id, newName);
    }



    private void updateLiveDataUponReordering() {
        //Collections.swap(playlistLiveData.getValue().getTracks(), reorderingFromPosition, reorderingToPosition);
        Track track = playlistLiveData.getValue().getTracks().remove(reorderingFromPosition);
        playlistLiveData.getValue().getTracks().add(reorderingToPosition, track);
    }

    private void updateLiveDataUponRenaming() {
        playlistLiveData.getValue().setName(newName);
    }

    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();

        if (currentOperation != null)
            switch (currentOperation) {
                /*case DELETE: undoDeletionRecyclerView(positionBeforeDeletion, trackImageBeforeDeletion, trackNameBeforeDeletion);
                    break;*/
                case RENAME: updateLiveDataUponRenaming();
                    break;
                case REORDER: updateLiveDataUponReordering();
                    break;
                case UPLOAD_IMAGE:
                    break;

        }

        currentOperation = null;

    }

    public void setCurrentOperation(PlaylistOperation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public PlaylistOperation getCurrentOperation() {
        return currentOperation;
    }

    @Override
    public void clearData() {
        playlistLiveData = null;
        eachTrackAlbumLiveData = null;
        albumLiveData = null;
    }
}
