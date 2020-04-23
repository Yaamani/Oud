package com.example.oud;

import android.content.SharedPreferences;

import androidx.fragment.app.testing.FragmentScenario;


import androidx.lifecycle.Lifecycle;

import com.example.oud.api.OudApi;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.settings.SettingsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import okhttp3.mockwebserver.MockWebServer;

@RunWith(RobolectricTestRunner.class)
public class SettingsFragmentTest {

    private MockWebServer mockWebServer;
    private OudApi oudApi;


    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();

    }

    @Test
    public void checkDataLoadedSuccessfullyFromSharedPreferences(){
        FragmentScenario<SettingsFragment> fragmentScenario = FragmentScenario.launchInContainer(SettingsFragment.class);
        fragmentScenario.moveToState(Lifecycle.State.CREATED);


    }


}
