package com.example.oud.connectionaware;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class ConnectionAwareViewModel<ConnectionAwareRepo extends ConnectionAwareRepository> extends ViewModel implements ConnectionStatusListener {

    private static final String TAG = ConnectionAwareViewModel.class.getSimpleName();

    protected ConnectionAwareRepo repo;

    private MutableLiveData<Constants.ConnectionStatus> connectionStatus;

    public ConnectionAwareViewModel(ConnectionAwareRepo repoInstance, String MOCK_SERVER_URL) {
        repo = repoInstance;
        repo.setConnectionStatusListener(this);
        if (Constants.MOCK)
            repo.setBaseUrl(MOCK_SERVER_URL);
    }

    public MutableLiveData<Constants.ConnectionStatus> getConnectionStatus() {
        if (connectionStatus == null)
            connectionStatus = new MutableLiveData<>();
        return connectionStatus;
    }

    @Override
    public void onConnectionSuccess() {
        connectionStatus.setValue(Constants.ConnectionStatus.SUCCESSFUL);
        Log.i(TAG, "onConnectionSuccess: ");
    }

    @Override
    public void onConnectionFailure() {
        connectionStatus.setValue(Constants.ConnectionStatus.FAILED);
        Log.i(TAG, "onConnectionSuccess: ");
        clearData();
    }

    /**
     * Nullify all your live data objects.
     */
    public abstract void clearData();
}
