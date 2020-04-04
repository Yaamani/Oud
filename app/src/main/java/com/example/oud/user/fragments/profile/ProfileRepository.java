package com.example.oud.user.fragments.profile;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.FailureSuccessHandledCallback;
import com.example.oud.api.LoggedInUser;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserPlaylistsResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
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
        String base = Constants.BASE_URL;
        if(Constants.MOCK)
            base = Constants.YAMANI_MOCK_BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base)
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
                if(response.isSuccessful()){
                PlaylistPreview[] arrayPlaylists = ((UserPlaylistsResponse) response.body()).getPlaylists();
                mutablePlaylists.setValue(Arrays.asList(arrayPlaylists));
                }
            }


        });
        return mutablePlaylists;
    }

    public void setProfileImage(String token , Uri newImage){

        File file = new File(newImage.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<LoggedInUser> call = oudApi.updateUserPicture(token,body);
        call.enqueue(new FailureSuccessHandledCallback<LoggedInUser>(listener) {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                Log.i("profile Repository","image uploaded");
                if(response.isSuccessful())
                    Log.i("profile Repository","image uploaded");
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {

            }
        });

    }







}
