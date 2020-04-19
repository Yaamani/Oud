package com.example.oud.user.fragments.settings;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.LoggedInUser;
import com.example.oud.api.ProfilePreview;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import retrofit2.Call;
import retrofit2.Response;

public class SettingsRepository extends ConnectionAwareRepository {

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


    public MutableLiveData<ProfilePreview> loadProfile(String userId, String token){
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

}
