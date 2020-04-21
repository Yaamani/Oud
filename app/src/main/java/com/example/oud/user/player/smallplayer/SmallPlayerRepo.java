package com.example.oud.user.player.smallplayer;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.OudApi;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareRepository;

public class SmallPlayerRepo extends ConnectionAwareRepository {

    public static SmallPlayerRepo ourInstance = new SmallPlayerRepo();

    private SmallPlayerRepo(){}
    public MutableLiveData<Track> fetchTrack(String trackId){

        MutableLiveData<Track> mutableLiveData = new MutableLiveData<>();
        // OudApi oudApi = instantiateRetrofitOudApi();
        //todo create fun to getTrack from Api

        return  mutableLiveData;
    }
}
