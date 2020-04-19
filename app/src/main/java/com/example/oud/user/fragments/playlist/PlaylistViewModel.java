package com.example.oud.user.fragments.playlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Playlist;
import com.example.oud.api.Track;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class PlaylistViewModel extends ConnectionAwareViewModel<PlaylistRepository> {
    // TODO: Implement the ViewModel

    private static final String TAG = PlaylistViewModel.class.getSimpleName();

    public enum PlaylistOperation {RENAME,
        REORDER,
        DELETE,
        UPLOAD_IMAGE,
        ADD_TRACK_TO_LIKED_TRACKS,
        REMOVE_TRACK_FROM_LIKED_TRACKS,
        FOLLOW_PLAYLIST,
        UNFOLLOW_PLAYLIST,
        MAKE_PLAYLIST_PUBLIC,
        MAKE_PLAYLIST_PRIVATE,
        MAKE_PLAYLIST_COLLABORATIVE,
        MAKE_PLAYLIST_NON_COLLABORATIVE,
        SAVE_ALBUM,
        UNSAVE_ALBUM}

    private PlaylistOperation currentOperation = null;


    // Playlist
    private MutableLiveData<Playlist> playlistLiveData;
    //private ArrayList<MutableLiveData<Album>> eachTrackAlbumLiveData;
    private MutableLiveData<ArrayList<Boolean>> doesUserFollowThisPlaylist;
    private Drawable newPlaylistImageThatIsBeingUploadedNow = null;
    private Drawable newPlaylistImage = null;

    // Album
    private MutableLiveData<Album> albumLiveData;
    private MutableLiveData<IsFoundResponse> isThisAlbumSavedByUser;


    private MutableLiveData<IsFoundResponse> areTracksLikedLiveData;



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
                    clearData();
                    playlistLiveData = mRepo.fetchPlaylist(token, playlistId);
                }
            }
        }

        return playlistLiveData;
    }

    public Drawable getNewPlaylistImage() {
        return newPlaylistImage;
    }

    /*public MutableLiveData<Album> getTrackAlbumLiveData(String token, int position, String albumId) {
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
    }*/

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
                    clearData();
                    albumLiveData = mRepo.fetchAlbum(token, albumId);
                }
            }
        }

        return albumLiveData;
    }

    public MutableLiveData<ArrayList<Boolean>> getDoesUserFollowThisPlaylist(String token, String playlistId, String userId) {
        if (doesUserFollowThisPlaylist == null) {
            ArrayList<String> ids = new ArrayList<>();
            ids.add(userId);
            doesUserFollowThisPlaylist = mRepo.checkIfUsersFollowPlaylist(token, playlistId, ids);
        }
        return doesUserFollowThisPlaylist;
    }

    public MutableLiveData<IsFoundResponse> getIsThisAlbumSavedByUser(String token) {
        if (isThisAlbumSavedByUser == null) {
            ArrayList<String> albumId = new ArrayList<>();
            albumId.add(albumLiveData.getValue().get_id());
            isThisAlbumSavedByUser = mRepo.checkIfTheseAlbumsAreSavedByUser(token, albumId);
        }
        return isThisAlbumSavedByUser;
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
        mRepo.changePlaylistDetails(token, id, newName, null, null);
    }

    public void deleteTrack(String token, int deletionPosition) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.DELETE);

        this.deletionPosition = deletionPosition;

        String playlistId = playlistLiveData.getValue().getId();
        String trackId = playlistLiveData.getValue().getTracks().get(deletionPosition).get_id();
        mRepo.deleteTrack(token, playlistId, trackId);
    }

    public MutableLiveData<IsFoundResponse> getAreTracksLikedLiveData(String token, ArrayList<String> ids) {
        if (areTracksLikedLiveData == null)
            areTracksLikedLiveData = mRepo.areTracksLiked(token, ids);
        return areTracksLikedLiveData;
    }

    public void addTrackToLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(PlaylistOperation.ADD_TRACK_TO_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.addTheseTracksToLikedTracks(token, s);
    }

    public void removeTrackFromLikedTracks(String token, String id, int position) {
        if (areTracksLikedLiveData == null) return;

        setCurrentOperation(PlaylistOperation.REMOVE_TRACK_FROM_LIKED_TRACKS);

        trackLikePosition = position;

        ArrayList<String> s = new ArrayList<>();
        s.add(id);

        mRepo.removeTheseTracksFromLikedTracks(token, s);
    }

    public void followThisPlaylist(String token) {
        if (playlistLiveData == null) return;
        if (doesUserFollowThisPlaylist == null) return;

        setCurrentOperation(PlaylistOperation.FOLLOW_PLAYLIST);

        String id = playlistLiveData.getValue().getId();
        mRepo.followPlaylist(token, id, true);
    }

    public void unfollowThisPlaylist(String token) {
        if (playlistLiveData == null) return;
        if (doesUserFollowThisPlaylist == null) return;

        setCurrentOperation(PlaylistOperation.UNFOLLOW_PLAYLIST);

        String id = playlistLiveData.getValue().getId();
        mRepo.unfollowPlaylist(token, id);
    }

    public void makePlaylistPublic(String token) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.MAKE_PLAYLIST_PUBLIC);

        String id = playlistLiveData.getValue().getId();
        String name = playlistLiveData.getValue().getName();
        mRepo.changePlaylistDetails(token, id, name, true, null);
    }

    public void makePlaylistPrivate(String token) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.MAKE_PLAYLIST_PRIVATE);

        String id = playlistLiveData.getValue().getId();
        String name = playlistLiveData.getValue().getName();
        mRepo.changePlaylistDetails(token, id, name, false, null);
    }

    public void makePlaylistCollaborative(String token) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.MAKE_PLAYLIST_COLLABORATIVE);
        String id = playlistLiveData.getValue().getId();
        String name = playlistLiveData.getValue().getName();
        mRepo.changePlaylistDetails(token, id, name, null, true);
    }

    public void makePlaylistNonCollaborative(String token) {
        if (playlistLiveData == null) return;

        setCurrentOperation(PlaylistOperation.MAKE_PLAYLIST_NON_COLLABORATIVE);
        String id = playlistLiveData.getValue().getId();
        String name = playlistLiveData.getValue().getName();
        mRepo.changePlaylistDetails(token, id, name, null, false);
    }

    public void uploadPlaylistImage(String token, Context context, PlaylistFragment playlistFragment, Drawable before, Bitmap bitmap) {
        File sd = context.getCacheDir();
        File folder = new File(sd, "/myfolder/");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e(TAG, "Cannot create a directory!");
            } else {
                folder.mkdirs();
            }
        }

        File file = new File(folder,"mypic.jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(String.valueOf(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
            outputStream.close();
            Log.e(TAG,"image output stream");


        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            Log.e(TAG, e.getMessage());
            //Log.e(TAG,"first catch");
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e(TAG, e.getMessage());
            //Log.e(TAG,"second catch");
        }

        Log.i(TAG, ("file size :"+Integer.parseInt(String.valueOf(file.length()/1024))));


        setCurrentOperation(PlaylistOperation.UPLOAD_IMAGE);
        newPlaylistImageThatIsBeingUploadedNow = new BitmapDrawable(context.getResources(), bitmap);
        playlistFragment.setPlaylistImageBeforeUploadingTheNewOne(before);
        playlistFragment.blockUiAndWait();

        mRepo.uploadPlaylistImage(token, file);

    }

    public void saveAlbum(String token) {
        if (albumLiveData == null) return;
        if (isThisAlbumSavedByUser == null) return;

        setCurrentOperation(PlaylistOperation.SAVE_ALBUM);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(albumLiveData.getValue().get_id());
        mRepo.saveTheseAlbumsForTheCurrentUser(token, ids);
    }

    public void unsaveAlbum(String token) {
        if (albumLiveData == null) return;
        if (isThisAlbumSavedByUser == null) return;

        setCurrentOperation(PlaylistOperation.UNSAVE_ALBUM);

        ArrayList<String> ids = new ArrayList<>();
        ids.add(albumLiveData.getValue().get_id());
        mRepo.unsaveTheseAlbumsForTheCurrentUser(token, ids);
    }

    private void updateLiveDataUponReordering() {
        //Collections.swap(playlistLiveData.getValue().getTracks(), reorderingFromPosition, reorderingToPosition);
        Track track = playlistLiveData.getValue().getTracks().remove(reorderingFromPosition);
        playlistLiveData.getValue().getTracks().add(reorderingToPosition, track);
    }

    private void updateDataUponUploadingPlaylistImage() {
        this.newPlaylistImage = newPlaylistImageThatIsBeingUploadedNow;
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

    private void updateLiveDataUponFollowingPlaylist() {
        doesUserFollowThisPlaylist.getValue().set(0, true);
    }

    private void updateLiveDataUponUnFollowingPlaylist() {
        doesUserFollowThisPlaylist.getValue().set(0, false);
    }

    private void updateLiveDataUponMakingPlaylistPublic() {
        playlistLiveData.getValue().setPublicPlaylist(true);
    }

    private void updateLiveDataUponMakingPlaylistPrivate() {
        playlistLiveData.getValue().setPublicPlaylist(false);
    }

    private void updateLiveDataUponMakingPlaylistCollaborative() {
        playlistLiveData.getValue().setCollaborative(true);
    }

    private void updateLiveDataUponMakingPlaylistNonCollaborative() {
        playlistLiveData.getValue().setCollaborative(false);
    }

    private void updateLiveDataUponSavingAnAlbum() {
        isThisAlbumSavedByUser.getValue().getIsFound().set(0, true);
    }

    private void updateLiveDataUponUnSavingAnAlbum() {
        isThisAlbumSavedByUser.getValue().getIsFound().set(0, false);
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
                case UPLOAD_IMAGE: updateDataUponUploadingPlaylistImage();
                    break;
                case ADD_TRACK_TO_LIKED_TRACKS: updateLiveDataUponAddingTrackToLikedTracks();
                    break;
                case REMOVE_TRACK_FROM_LIKED_TRACKS: updateLiveDataUponRemovingTrackFromLikedTracks();
                    break;
                case FOLLOW_PLAYLIST: updateLiveDataUponFollowingPlaylist();
                    break;
                case UNFOLLOW_PLAYLIST: updateLiveDataUponUnFollowingPlaylist();
                    break;
                case MAKE_PLAYLIST_PUBLIC: updateLiveDataUponMakingPlaylistPublic();
                    break;
                case MAKE_PLAYLIST_PRIVATE: updateLiveDataUponMakingPlaylistPrivate();
                    break;
                case MAKE_PLAYLIST_COLLABORATIVE: updateLiveDataUponMakingPlaylistCollaborative();
                    break;
                case MAKE_PLAYLIST_NON_COLLABORATIVE: updateLiveDataUponMakingPlaylistNonCollaborative();
                    break;
                case SAVE_ALBUM: updateLiveDataUponSavingAnAlbum();
                    break;
                case UNSAVE_ALBUM:updateLiveDataUponUnSavingAnAlbum();
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

    public void clearDoesUserFollowThisPlaylistData() {
        doesUserFollowThisPlaylist = null;
    }

    public void clearIsThisAlbumSavedByUserData() {
        isThisAlbumSavedByUser = null;
    }

    public void clearAreTracksLikedData() {
        areTracksLikedLiveData = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearDoesUserFollowThisPlaylistData();
        clearIsThisAlbumSavedByUserData();
        clearAreTracksLikedData();
    }

    @Override
    public void clearData() {
        playlistLiveData = null;
        //eachTrackAlbumLiveData = null;
        clearDoesUserFollowThisPlaylistData();
        newPlaylistImage = null;

        albumLiveData = null;
        clearIsThisAlbumSavedByUserData();

        clearAreTracksLikedData();
    }
}
