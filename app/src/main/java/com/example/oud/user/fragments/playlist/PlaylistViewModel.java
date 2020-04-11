package com.example.oud.user.fragments.playlist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.api.Track;
import com.example.oud.api.UserAreTracksLiked;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class PlaylistViewModel extends ConnectionAwareViewModel<PlaylistRepository> {
    // TODO: Implement the ViewModel


    public enum PlaylistOperation {RENAME, REORDER, DELETE, UPLOAD_IMAGE, ADD_TRACK_TO_LIKED_TRACKS, REMOVE_TRACK_FROM_LIKED_TRACKS}
    private PlaylistOperation currentOperation = null;


    // Playlist
    private MutableLiveData<Playlist> playlistLiveData;
    private ArrayList<MutableLiveData<Album>> eachTrackAlbumLiveData;

    // Album
    private MutableLiveData<Album> albumLiveData;


    private MutableLiveData<UserAreTracksLiked> areTracksLikedLiveData;



    private int reorderingFromPosition;
    private int reorderingToPosition;

    private String newName;

    private int deletionPosition;

    private int trackLikePosition;




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
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.REORDER);

        reorderingFromPosition = fromPosition;
        reorderingToPosition = toPosition;
        // Server
        String id = playlistLiveData.getValue().getId();
        mRepo.reorderTrack(token, id, fromPosition, toPosition);
    }

    public void renamePlaylist(String token, String newName) {
        if (playlistLiveData == null) return;

        this.newName = newName;
        if (playlistLiveData.getValue().getName().equals(newName)) return;
        setCurrentOperation(PlaylistOperation.RENAME);
        // Server
        String id = playlistLiveData.getValue().getId();
        mRepo.renamePlaylist(token, id, newName);
    }

    public void deleteTrack(String token, int deletionPosition) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.DELETE);

        this.deletionPosition = deletionPosition;

        String playlistId = playlistLiveData.getValue().getId();
        String trackId = playlistLiveData.getValue().getTracks().get(deletionPosition).get_id();
        mRepo.deleteTrack(token, playlistId, trackId);
    }

    public MutableLiveData<UserAreTracksLiked> getAreTracksLikedLiveData(String token, ArrayList<String> ids) {
        if (areTracksLikedLiveData == null)
            areTracksLikedLiveData = mRepo.areTracksLiked(token, ids);
        return areTracksLikedLiveData;
    }

    public void addTrackToLikedTracks(String token, String id, int position) {

        setCurrentOperation(PlaylistOperation.ADD_TRACK_TO_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.addTheseTracksToLikedTracks(token, s);
    }

    public void removeTrackFromLikedTracks(String token, String id, int position) {

        setCurrentOperation(PlaylistOperation.REMOVE_TRACK_FROM_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s);
    }


    private void updateLiveDataUponReordering() {
        //Collections.swap(playlistLiveData.getValue().getTracks(), reorderingFromPosition, reorderingToPosition);
        Track track = playlistLiveData.getValue().getTracks().remove(reorderingFromPosition);
        playlistLiveData.getValue().getTracks().add(reorderingToPosition, track);
    }

    private void updateLiveDataUponRenaming() {
        playlistLiveData.getValue().setName(newName);
    }

    private void updateLiveDataUponDeletion() {
        playlistLiveData.getValue().getTracks().remove(deletionPosition);
    }

    private void updateLiveDataUponAddingTrackToLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, true);
    }

    private void updateLiveDataUponRemovingTrackFromLikedTracks() {
        areTracksLikedLiveData.getValue().getIsFound().set(trackLikePosition, false);
    }

    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();

        if (currentOperation != null)
            switch (currentOperation) {
                case DELETE: updateLiveDataUponDeletion();
                    break;
                case RENAME: updateLiveDataUponRenaming();
                    break;
                case REORDER: updateLiveDataUponReordering();
                    break;
                case UPLOAD_IMAGE:
                    break;
                case ADD_TRACK_TO_LIKED_TRACKS: updateLiveDataUponAddingTrackToLikedTracks();
                    break;
                case REMOVE_TRACK_FROM_LIKED_TRACKS: updateLiveDataUponRemovingTrackFromLikedTracks();
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
