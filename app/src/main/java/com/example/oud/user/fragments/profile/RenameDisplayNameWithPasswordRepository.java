package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.LoggedInUser;
import com.example.oud.api.UpdateProfileData;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RenameDisplayNameWithPasswordRepository extends ConnectionAwareRepository {

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

    public void updateProfile(String token, UpdateProfileData data,MutableLiveData<String> errorMessage){
        Call<ResponseBody> call  =oudApi.updateProfile(token,data);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if(response.isSuccessful())
                    errorMessage.setValue("success");
                else
                    errorMessage.setValue(response.message());
            }
        });
    }

}
