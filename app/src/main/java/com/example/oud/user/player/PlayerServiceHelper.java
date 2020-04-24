package com.example.oud.user.player;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.Constants;
import com.example.oud.api.CurrentPlayback;
import com.example.oud.api.StatusMessageResponse;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class PlayerServiceHelper extends ConnectionAwareService<PlayerRepo> {

    private MutableLiveData<CurrentPlayback> trackMutableLiveData;

    public PlayerServiceHelper() {

        super(PlayerRepo.ourInstance, Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<CurrentPlayback> getTrackMutableLiveData(String token) {

        if (trackMutableLiveData == null) {

            trackMutableLiveData = mRepo.getCurrentlyPlayback(token);

        }
        return trackMutableLiveData;
    }

    public void putStartTrack(String contextId, Integer offset, String token){

        mRepo.putStartPlayback(contextId, offset, token);
    }

    public void putStartTrack(ArrayList<String> uris, String token){

        mRepo.putStartPlayback(uris, token);
    }

    public void putResumePlayback(Long positionMs, String token){

        mRepo.putResumePlayback(positionMs, token);
    }

    public void postSkipToNext(String token){

      mRepo.postSkipToNext(token);

    }

    public void postSkipToPrev(String token){

        mRepo.postSkipToPrev(token);

    }

    public void putSeekTo(String token, Long positionMs){

        mRepo.putSeekTo(token, positionMs);

    }

    public void putRepeatMode(String token, String repeatMode){

        mRepo.putRepeatMode(token, repeatMode);

    }

    public void putShuffleEnable(String token, boolean shuffleEnable ){

        mRepo.putShuffleEnable(token, shuffleEnable);

    }

    @Override
    protected void clearData() {

        trackMutableLiveData = null;
    }
}
