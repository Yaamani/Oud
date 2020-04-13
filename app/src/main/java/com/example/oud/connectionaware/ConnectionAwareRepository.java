package com.example.oud.connectionaware;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudApi;

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

    public static final int SENDING = 1;
    public static final int RECEIVING = 1 << 1;
    public static final int JSON_RESPONSE = 1 << 2;

    private String baseUrl;
    protected ConnectionStatusListener connectionStatusListener;
    LinkedList<Call> calls;

    protected OudApi oudApi;

    public ConnectionAwareRepository() {
        //this.baseUrl = Constants.BASE_URL;
        setBaseUrl(Constants.BASE_URL);

    }


    protected OudApi instantiateRetrofitOudApi(){

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(OudApi.class);

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
        oudApi = instantiateRetrofitOudApi();

    }

    public ConnectionStatusListener getConnectionStatusListener() {
        return connectionStatusListener;
    }

    public void setConnectionStatusListener(ConnectionStatusListener connectionStatusListener) {
        this.connectionStatusListener = connectionStatusListener;
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {

            long t1 = System.nanoTime();
            Request request = chain.request();

            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & SENDING) == SENDING) {
                Log.i(TAG, String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));
            }

            long t2 = System.nanoTime();
            Response response = chain.proceed(request);

            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & RECEIVING) == RECEIVING) {
                Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            }


            final String responseString = new String(response.body().bytes());
            if ((Constants.SERVER_CONNECTION_AWARE_LOG_SETTINGS & JSON_RESPONSE) == JSON_RESPONSE) {
                Log.i(TAG, "Response for " + response.request().url()+ ": " + responseString);
            }

            return  response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }
}
