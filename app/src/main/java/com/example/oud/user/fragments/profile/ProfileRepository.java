package com.example.oud.user.fragments.profile;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;

import com.example.oud.api.ListOfBoolean;
import com.example.oud.api.ListOfIds;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.LoggedInUser;

import com.example.oud.api.ProfilePreview;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.Response;


public class ProfileRepository extends ConnectionAwareRepository {




    public MutableLiveData<ProfilePreview> loadProfile(String userId,String token){
        MutableLiveData<ProfilePreview> mutableProfile = new MutableLiveData<>();

        Log.e("ProfileRepository",token);
        Call<ProfilePreview> call = oudApi.getUserById(token,userId);

        addCall(call).enqueue(new FailureSuccessHandledCallback<ProfilePreview>(this){
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



        Call<LoggedInUser> call = oudApi.updateUserPicture(token,body);
        addCall(call).enqueue(new FailureSuccessHandledCallback<LoggedInUser>(this) {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                super.onResponse(call,response);
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
                super.onFailure(call,t);
                Log.e("profile Repository","image not uploaded");
                Log.e("profile Repository",t.getMessage());



            }
        });



    }
    public void followUser(String token,String userId,ConnectionStatusListener connectionStatusListener){
        ArrayList<String>id = new ArrayList<>();
        id.add(userId);
        ListOfIds listOfIds = new ListOfIds(id);
        Call<Void> call = oudApi.followUsersOrArtists(token,"user",listOfIds);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }

    public void unFollowUser(String token,String userId,ConnectionStatusListener connectionStatusListener){

        Call<Void> call = oudApi.unFollowUsersOrArtists(token,"user",userId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }


    public void checkIfIFollowThisUser(String token,String commaSeparatedUsers,MutableLiveData<Boolean> isFollowed){
        Call<ListOfBoolean> call = oudApi.checkIfIFollowTheseUsersOrArtists(token,"user",commaSeparatedUsers);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ListOfBoolean>(this) {
            @Override
            public void onResponse(Call<ListOfBoolean> call, Response<ListOfBoolean> response) {
                super.onResponse(call,response);
                if(response.isSuccessful()){
                    isFollowed.setValue(response.body().getIds().get(0));
                }

            }

        });



    }

    public MutableLiveData<LoggedInUser> getCurrentUser(String token){
        MutableLiveData<LoggedInUser> loggedInUserMutableLiveData = new MutableLiveData<>();
        Call<LoggedInUser> call = oudApi.getUserProfile(token);
        addCall(call).enqueue(new FailureSuccessHandledCallback<LoggedInUser>(this) {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                super.onResponse(call,response);
                if(response.isSuccessful()){
                    loggedInUserMutableLiveData.setValue(response.body());
                }
            }

        });


        return loggedInUserMutableLiveData;
    }









}
