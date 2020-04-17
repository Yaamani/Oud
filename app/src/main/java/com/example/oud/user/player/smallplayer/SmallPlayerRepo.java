package com.example.oud.user.player.smallplayer;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

/*import com.example.oud.FailureSuccessHandledCallback;*/
import com.example.oud.api.Album;
import com.example.oud.api.OudApi;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import retrofit2.Call;
import retrofit2.Response;

public class SmallPlayerRepo extends ConnectionAwareRepository {

    private final String TAG = "SmallPlayerRepo";
    public static SmallPlayerRepo ourInstance = new SmallPlayerRepo();

    private MutableLiveData<Track> mutableLiveData ;
    private MutableLiveData<Boolean> isLoadingTrackMutableLiveData;
    private MutableLiveData<Album> albumMutableLiveData;

    private SmallPlayerRepo(){}
    public MutableLiveData<Track> fetchTrack(String trackId) {

        mutableLiveData = new MutableLiveData<>();
        isLoadingTrackMutableLiveData = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi();

        Call<Track> trackCall = oudApi.getTrack(trackId);
        trackCall.enqueue(new FailureSuccessHandledCallback<Track>(connectionStatusListener){

            Track track;
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                super.onResponse(call, response);

                if(!response.isSuccessful()){

                    isLoadingTrackMutableLiveData.setValue(false);
                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }

                isLoadingTrackMutableLiveData.setValue(true);
                track = response.body();
                mutableLiveData.setValue(track);

            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                super.onFailure(call, t);

                isLoadingTrackMutableLiveData.setValue(false);
                Log.e(TAG ,t.getMessage());
            }
        });

        return  mutableLiveData;

    }

    public MutableLiveData<Album> getAlbumImage(String albumId){

        albumMutableLiveData = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi();

        Call<Album> albumCall = oudApi.album("token",albumId);
        albumCall.enqueue(new FailureSuccessHandledCallback<Album>(connectionStatusListener){

            Album album;

            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                super.onResponse(call, response);

                if(!response.isSuccessful()){

                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }
                album = response.body();
                albumMutableLiveData.setValue(album);
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                super.onFailure(call, t);

                Log.e(TAG ,t.getMessage());
            }
        });

        return  albumMutableLiveData;


    }

    public MutableLiveData<Boolean> isLoadingTrack(){


        return isLoadingTrackMutableLiveData;

    }
}
