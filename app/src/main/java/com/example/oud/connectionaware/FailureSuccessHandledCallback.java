package com.example.oud.connectionaware;

import android.util.Log;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FailureSuccessHandledCallback<T> implements Callback<T> {

    private static final String TAG = FailureSuccessHandledCallback.class.getSimpleName();

    private static int counter = 0;

    private ConnectionAwareRepository repo;
    private ConnectionStatusListener connectionStatusListener;
    private ConnectionStatusListener connectionStatusListenerForUndo;

    public FailureSuccessHandledCallback(ConnectionAwareRepository repo,ConnectionStatusListener connectionStatusListenerForUndo) {
        this.repo = repo;
        this.connectionStatusListener = repo.getConnectionStatusListener();
        this.connectionStatusListenerForUndo = connectionStatusListenerForUndo;
    }

    public FailureSuccessHandledCallback(ConnectionAwareRepository repo) {
        this.repo = repo;
        this.connectionStatusListener = repo.getConnectionStatusListener();
    }

    public FailureSuccessHandledCallback(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG, "onResponse: " + "SUCCESS");
        connectionStatusListener.onConnectionSuccess();
        if(connectionStatusListenerForUndo!=null) {
            if (Constants.MOCK) {
                    connectionStatusListenerForUndo.onConnectionSuccess();
            } else {
                if (response.isSuccessful())
                    connectionStatusListenerForUndo.onConnectionSuccess();
                else
                    connectionStatusListenerForUndo.onConnectionFailure();
            }
        }
        if (repo != null)
            repo.calls.remove(call);

        clear();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "onFailure: request: " + call.request());

        t.printStackTrace();
        //Log.e(TAG, "onFailure: " + t.getMessage());

        if(connectionStatusListenerForUndo!=null)
            connectionStatusListenerForUndo.onConnectionFailure();

        if (!call.isCanceled()) {
            cancelAllRequests();

            connectionStatusListener.onConnectionFailure();
        }

        counter++;

        clear();

        Log.i(TAG, "onFailure: failure counter = " + counter);
    }

    private void cancelAllRequests() {
        if (repo == null)
            return;

        for (Call c : repo.calls) {
            c.cancel();
        }
        repo.calls.clear();
    }

    private void clear() {
        repo = null;
        connectionStatusListener = null;
    }

}
