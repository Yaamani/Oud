package com.example.oud.user.player;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareRepository;

public abstract class ConnectionAwareService <ConnectionAwareRepo extends ConnectionAwareRepository> implements ConnectionStatusListener {

    protected ConnectionAwareRepo mRepo;

    private MutableLiveData<Constants.ConnectionStatus> connectionStatus;

    public ConnectionAwareService(ConnectionAwareRepo repoInstance , String MOCK_SERVER_URL){

        mRepo = repoInstance;
        mRepo.setConnectionStatusListener(this);

        if (Constants.MOCK)
            mRepo.setBaseUrl(MOCK_SERVER_URL);

    }

    @Override
    public void onConnectionSuccess() {
        connectionStatus.setValue(Constants.ConnectionStatus.SUCCESSFUL);
        //Log.i(TAG, "onConnectionSuccess: ");
    }

    @Override
    public void onConnectionFailure() {
        connectionStatus.setValue(Constants.ConnectionStatus.FAILED);
        //Log.i(TAG, "onConnectionSuccess: ");
        clearData();
    }

    protected abstract void clearData();
}
