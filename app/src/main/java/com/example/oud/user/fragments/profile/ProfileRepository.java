package com.example.oud.user.fragments.profile;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.LoggedInUser;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.ProfilePreview;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.api.UserPlaylistsResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

    public MutableLiveData<ProfilePreview> loadProfile(String userId,String token){
        MutableLiveData<ProfilePreview> mutableProfile = new MutableLiveData<>();

        Log.e("ProfileRepository",token);
        Call<ProfilePreview> call = oudApi.getUserById("Bearer "+token,userId);

        call.enqueue(new FailureSuccessHandledCallback<ProfilePreview>(listener){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if (response.isSuccessful())
                    mutableProfile.setValue((ProfilePreview) response.body());
                else
                    Log.e("tag",response.toString());
            }


        });

        return mutableProfile;
    }





    public void setProfileImage(String token , Uri newImage,Bitmap bitmap,Context context){

        Log.e("profile Repository","image  repo started");


        File sd = context.getCacheDir();
        File folder = new File(sd, "/myfolder/");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                folder.mkdirs();
            }
        }

        File fileName = new File(folder,"mypic.jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.close();
            Log.e("profile Repository","image output stream");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("Profile Repository",e.getMessage());
            Log.e("Profile Repository","first catch");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Profile Repository",e.getMessage());
            Log.e("Profile Repository","second catch");
        }


        Log.e("profile Repository", ("file size :"+Integer.parseInt(String.valueOf(fileName.length()/1024))));



        RequestBody requestFile = RequestBody.create(fileName,MediaType.parse("multipart/form-data"));

        //MultipartBody.Part body = MultipartBody.Part.createFormData("profileImage", fileName.getName(), requestFile);
        MultipartBody.Part body = MultipartBody.Part.create(requestFile);



        Call<LoggedInUser> call = oudApi.updateUserPicture("Bearer "+token,body);
        call.enqueue(new FailureSuccessHandledCallback<LoggedInUser>(listener) {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                if(response.isSuccessful()){
                    Log.e("profile Repository","image uploaded");
                    Log.e("profile Repository","number of images "+response.body().getImages().length);
                }
                else{
                    Log.e("profile Repository","image not successful");
                    Log.e("profile Repository",response.message());
                }

            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                Log.e("profile Repository","image not uploaded");
                Log.e("profile Repository",t.getMessage());



            }
        });



    }







}
