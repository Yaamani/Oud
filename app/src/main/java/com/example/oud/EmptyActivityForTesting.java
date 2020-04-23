package com.example.oud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyActivityForTesting extends AppCompatActivity implements ConnectionStatusListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_for_testing);
    }

    @Override
    public void onConnectionSuccess() {

    }

    @Override
    public void onConnectionFailure() {

    }
}
