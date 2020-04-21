package com.example.oud.connectionaware;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.OudApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionAwareRepository {

    private static final String TAG = ConnectionAwareRepository.class.getSimpleName();


    private String baseUrl;
    protected ConnectionStatusListener connectionStatusListener;
    LinkedList<Call> calls;

    protected OudApi oudApi;

    public ConnectionAwareRepository() {
        //this.baseUrl = Constants.BASE_URL;
        setBaseUrl(Constants.BASE_URL);

    }


    protected OudApi instantiateRetrofitOudApi(String baseUrl){

        return OudUtils.instantiateRetrofitOudApi(baseUrl);

    }

    protected <T> Call<T> addCall(Call<T> call) {
        if (calls == null)
            calls = new LinkedList<>();

        calls.add(call);

        return call;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;

        //Log.i(TAG, "setBaseUrl: " + baseUrl);
        oudApi = instantiateRetrofitOudApi(baseUrl);

    }

    public ConnectionStatusListener getConnectionStatusListener() {
        return connectionStatusListener;
    }

    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }


}
