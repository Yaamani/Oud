package com.example.oud.connectionaware;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionAwareRepository {

    private static final String TAG = ConnectionAwareRepository.class.getSimpleName();

    private String baseUrl;
    protected ConnectionStatusListener connectionStatusListener;

    public ConnectionAwareRepository() {
        this.baseUrl = Constants.BASE_URL;
    }


    protected OudApi instantiateRetrofitOudApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OudApi.class);

    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;

        //Log.i(TAG, "setBaseUrl: " + baseUrl);

    }

    public ConnectionStatusListener getConnectionStatusListener() {
        return connectionStatusListener;
    }

    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }
}
