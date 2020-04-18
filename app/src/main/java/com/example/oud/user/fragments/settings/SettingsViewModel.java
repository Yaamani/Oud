package com.example.oud.user.fragments.settings;

import androidx.lifecycle.ViewModel;

import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class SettingsViewModel extends ConnectionAwareViewModel<SettingsRepository> {

    public SettingsViewModel(){
        super(new SettingsRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }

    @Override
    public void clearData() {

    }
}
