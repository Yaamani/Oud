package com.example.oud.user.player.smallplayer;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.Constants;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class SmallPlayerViewModel extends ConnectionAwareViewModel<SmallPlayerRepo> {

    private MutableLiveData<Track> trackMutableLiveData ;
    public SmallPlayerViewModel() {

        super(SmallPlayerRepo.ourInstance , Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Track> getTrackMutableLiveData(String id) {

        if(trackMutableLiveData == null){

            trackMutableLiveData = repo.fetchTrack(id);
        }
        return trackMutableLiveData;
    }

    @Override
    public void clearData() {

      trackMutableLiveData = null;
    }
}
