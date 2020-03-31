package com.example.oud.connectionaware;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;

import androidx.lifecycle.ViewModel;

public class ConnectionAwareViewModel<ConnectionAwareRepo extends ConnectionAwareRepository> extends ViewModel implements ConnectionStatusListener {

    protected ConnectionAwareRepo connectionAwareRepo;

    public ConnectionAwareViewModel(ConnectionAwareRepo repoInstance) {
        connectionAwareRepo = repoInstance;
        connectionAwareRepo.setConnectionStatusListener(this);
        if (Constants.MOCK)
            connectionAwareRepo.setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);
    }

    @Override
    public void onConnectionSuccess() {

    }

    @Override
    public void onConnectionFailure() {

    }
}
