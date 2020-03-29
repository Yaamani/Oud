package com.example.oud;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureSuccessHandledCallback<T> implements Callback<T> {

    private static final String TAG = FailureSuccessHandledCallback.class.getSimpleName();

    private static int counter = 0;

    private ConnectionStatusListener connectionStatusListener;

    public FailureSuccessHandledCallback(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        connectionStatusListener.onConnectionSuccess();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        connectionStatusListener.onConnectionFailure();
        counter++;

        Log.i(TAG, "onFailure: failure counter = " + counter);
    }



}
