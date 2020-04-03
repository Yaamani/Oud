package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.FailureSuccessHandledCallback;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserPlaylistsResponse;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileRepository  {
    private ConnectionStatusListener listener;
    private OudApi oudApi;


    private MutableLiveData<ProfilePreview> profile;

    private MutableLiveData<List<PlaylistPreview>> playlists;


    public ProfileRepository(ConnectionStatusListener listener) {
        this.listener = listener;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);
    }

    public MutableLiveData<ProfilePreview> loadProfile(String userId){
        MutableLiveData<ProfilePreview> mutableProfile = new MutableLiveData<>();

        Call<ProfilePreview> call = oudApi.getUserById(userId);

        call.enqueue(new FailureSuccessHandledCallback<ProfilePreview>(listener){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                mutableProfile.setValue((ProfilePreview) response.body());
            }


        });

        return mutableProfile;
    }


    public MutableLiveData<List<PlaylistPreview>> loadUserPlaylists(String userId){
        MutableLiveData<List<PlaylistPreview>> mutablePlaylists = new MutableLiveData<>();

        Call<UserPlaylistsResponse> call = oudApi.getUserPlaylists(userId);

        call.enqueue(new FailureSuccessHandledCallback<UserPlaylistsResponse>(listener){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                PlaylistPreview[] arrayPlaylists = ((UserPlaylistsResponse) response.body()).getPlaylists();
                mutablePlaylists.setValue(Arrays.asList(arrayPlaylists));
            }


        });
        return mutablePlaylists;
    }






}
