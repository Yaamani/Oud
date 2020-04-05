package com.example.oud.dummy;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.OudApi;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DummyRepository {

    private ConnectionStatusListener listener;

    public DummyRepository(ConnectionStatusListener listener) {
        this.listener = listener;
    }

    public MutableLiveData<String> loadDummyString() {
        // Create new instance
        MutableLiveData<String> dummy = new MutableLiveData<>();

        // Fetch data using retrofit
        OudApi api = instantiateRetrofitOudApi();
        Call<Album> trackCall = api.album("id");
        trackCall.enqueue(new FailureSuccessHandledCallback<Album>(listener) {
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);

                dummy.setValue("Response");
            }
        });


        // Return the instance
        return dummy;
    }

    private OudApi instantiateRetrofitOudApi(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://baseUrl.oud")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OudApi.class);

    }

}
