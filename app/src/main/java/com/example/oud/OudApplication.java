package com.example.oud;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.oud.user.fragments.premium.AuthorizationHeaderConnection;
import com.huxq17.download.config.DownloadConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OudApplication extends Application {

    private static final String TAG = OudApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        pumpConfig();

    }

    /**
     * Configure the download manager (Pump).
     */
    private void pumpConfig() {
        String token = OudUtils.getToken(this);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    Log.d(TAG, "pumpConfig: " + response.code());
                    if (response.code() == 403)
                        Toast.makeText(this, "يا فقير.", Toast.LENGTH_SHORT).show();
                    return response;
                })
                .addInterceptor(new OudUtils.LoggingInterceptor())
                .build();

        DownloadConfig.newBuilder()
                .setDownloadConnectionFactory(new AuthorizationHeaderConnection.Factory(client, token))
                .build();
    }
}
