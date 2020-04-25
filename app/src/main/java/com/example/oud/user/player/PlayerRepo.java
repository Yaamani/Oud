package com.example.oud.user.player;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.CurrentPlayback;
import com.example.oud.api.StartOrResumePlayback;
import com.example.oud.api.StatusMessageResponse;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerRepo extends ConnectionAwareRepository {


    public static final String TAG = PlayerRepo.class.getSimpleName();

    public static PlayerRepo ourInstance = new PlayerRepo();
    private MutableLiveData<CurrentPlayback> trackMutableLiveData ;

    private PlayerRepo(){}

    public MutableLiveData<CurrentPlayback> getCurrentlyPlayback(String token){

        trackMutableLiveData = new MutableLiveData<>();

        Call<CurrentPlayback> currentlyPlaybackCall = oudApi.getCurrentPlayback(token);
        addCall(currentlyPlaybackCall).enqueue(new Callback<CurrentPlayback>() {

            CurrentPlayback currentPlayback;
            @Override
            public void onResponse(Call<CurrentPlayback> call, Response<CurrentPlayback> response) {
                /*super.onResponse(call, response);*/

                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }
                currentPlayback = response.body();
                trackMutableLiveData.setValue(currentPlayback);
            }

            @Override
            public void onFailure(Call<CurrentPlayback> call, Throwable t) {
                /*super.onFailure(call, t);*/
                Log.e(TAG ,t.getMessage());
            }
        });

        return trackMutableLiveData;
    }

    public void putStartPlayback(String contextId, Integer offset, String token){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        /*StartOrResumePlayback startOrResumePlayback = new StartOrResumePlayback(contextId, offset);*/
        StartOrResumePlayback startOrResumePlayback = new StartOrResumePlayback(contextId);

        Call<StatusMessageResponse> call = oudApi.startOrResumeTrack(token,  startOrResumePlayback);
        addCall(call).enqueue(new Callback<StatusMessageResponse>()
            {

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                /*super.onResponse(call, response);*/

                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                /*super.onFailure(call, t);*/

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    //For PlayList does't have id
    public void putStartPlayback(List<String> uris, String token){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        StartOrResumePlayback startOrResumePlayback = new StartOrResumePlayback(uris);

        Call<StatusMessageResponse> call = oudApi.startOrResumeTrack(token,  startOrResumePlayback);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(this){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void putResumePlayback(Long positionMs, String token){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        StartOrResumePlayback startOrResumePlayback = new StartOrResumePlayback(positionMs);

        Call<StatusMessageResponse> call = oudApi.startOrResumeTrack(token,  startOrResumePlayback);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(this){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void postSkipToNext(String token){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        Call<StatusMessageResponse> call = oudApi.skipToNextTrack(token);

        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(this){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void postSkipToPrev(String token){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        Call<StatusMessageResponse> call = oudApi.skipToPreviousTrack(token);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(connectionStatusListener){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void putSeekTo(String token, Long positionMs){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        Call<StatusMessageResponse> call = oudApi.seekTo(token, positionMs);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(connectionStatusListener){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void putRepeatMode(String token, String repeatMode){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        Call<StatusMessageResponse> call = oudApi.putRepeatMode(token, repeatMode);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(connectionStatusListener){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }

    public void putShuffleEnable(String token, boolean shuffleEnable){

        final StatusMessageResponse[] statusMessageResponse = new StatusMessageResponse[1];

        Call<StatusMessageResponse> call = oudApi.enableShuffle(token, shuffleEnable);
        addCall(call).enqueue(new FailureSuccessHandledCallback<StatusMessageResponse>(connectionStatusListener){

            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                super.onResponse(call, response);


                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                statusMessageResponse[0] = response.body();

                Log.d(TAG, statusMessageResponse[0].getMessage());
                Log.d(TAG, statusMessageResponse[0].getStatus());
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());

            }
        });

    }


}
