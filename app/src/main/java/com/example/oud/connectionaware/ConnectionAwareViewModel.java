package com.example.oud.connectionaware;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public abstract class ConnectionAwareViewModel<ConnectionAwareRepo extends ConnectionAwareRepository> extends ViewModel implements ConnectionStatusListener {

    private static final String TAG = ConnectionAwareViewModel.class.getSimpleName();

    protected ConnectionAwareRepo mRepo;

    private MutableLiveData<Constants.ConnectionStatus> connectionStatus = new MutableLiveData<>();

    public ConnectionAwareViewModel(ConnectionAwareRepo repoInstance, String MOCK_SERVER_URL) {
        mRepo = repoInstance;
        mRepo.setConnectionStatusListener(this);

        if (mRepo.getBaseUrl().equals("http://localhost:"+ Constants.OKHTTP_MOCK_WEB_SERVER_PORT + "/"))
            return;

        if (Constants.MOCK)
            mRepo.setBaseUrl(MOCK_SERVER_URL);
    }

    public MutableLiveData<Constants.ConnectionStatus> getConnectionStatus() {
        if (connectionStatus == null)
            connectionStatus = new MutableLiveData<>();
        return connectionStatus;
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

    /**
     * Nullify all your live data objects.
     */
    public abstract void clearData();
}
