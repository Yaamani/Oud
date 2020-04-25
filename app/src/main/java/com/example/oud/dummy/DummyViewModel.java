package com.example.oud.dummy;

import com.example.oud.ConnectionStatusListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DummyViewModel extends ViewModel implements ConnectionStatusListener {
    // TODO: Implement the ViewModel

    private DummyRepository repository;

    private MutableLiveData<Boolean> connectionSuccessful;

    private MutableLiveData<String> dummyString;

    public DummyViewModel() {
        repository = new DummyRepository(this);
    }

    public LiveData<String> getDummyString() {
        if (dummyString == null)
            dummyString = repository.loadDummyString();

        return dummyString;
    }

    public LiveData<Boolean> getConnectionSuccessful() {
        return connectionSuccessful;
    }

    @Override
    public void onConnectionSuccess() {
        connectionSuccessful.setValue(true);
    }

    @Override
    public void onConnectionFailure() {
        connectionSuccessful.setValue(false);
    }
}
