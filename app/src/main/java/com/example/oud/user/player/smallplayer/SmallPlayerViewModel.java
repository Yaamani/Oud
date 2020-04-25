package com.example.oud.user.player.smallplayer;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class SmallPlayerViewModel extends ConnectionAwareViewModel<SmallPlayerRepo> {

    private MutableLiveData<Track> trackMutableLiveData;
    private MutableLiveData<Album> albumMutableLiveData;
    private MutableLiveData<Boolean> isLoadingTrackMutableLiveData;

    public SmallPlayerViewModel() {

        super(SmallPlayerRepo.ourInstance, Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Track> getTrackMutableLiveData(String id) {

        if (trackMutableLiveData == null) {

            trackMutableLiveData = mRepo.fetchTrack(id);
        }
        return trackMutableLiveData;
    }

    public MutableLiveData<Boolean> checkIsLoadingTrack() {

        if(isLoadingTrackMutableLiveData == null) {
            isLoadingTrackMutableLiveData = mRepo.isLoadingTrack();
        }

        return isLoadingTrackMutableLiveData;

    }

    public MutableLiveData<Album> getAlbumImageMutableLiveData(String albumId){

        if(albumMutableLiveData == null){

            albumMutableLiveData = mRepo.getAlbumImage(albumId);

        }

       return albumMutableLiveData;
    }

    @Override
    public void clearData() {

        trackMutableLiveData = null;
        isLoadingTrackMutableLiveData = null;
        albumMutableLiveData = null;
    }
}
