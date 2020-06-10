package com.example.oud.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.R;
import com.example.oud.user.player.MediaBrowserHelper;
import com.example.oud.user.player.PlayerHelper;
import com.example.oud.user.player.PlayerInterface;
import com.example.oud.user.ui.main.TestFragment;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements PlayerInterface, ConnectionStatusListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TestFragment.newInstance())
                    .commitNow();*/
        }
    }

    @Override
    public void configurePlayer(String contextId, String contextType, Integer offset, String token) {

    }

    @Override
    public void configurePlayer(ArrayList<String> ids, String token) {

    }

    @Override
    public PlayerHelper getPlayerHelper() {
        return null;
    }

    @Override
    public MediaBrowserHelper getMediaBrowserHelper() {
        return null;
    }

    @Override
    public void onConnectionSuccess() {

    }

    @Override
    public void onConnectionFailure() {

    }
}