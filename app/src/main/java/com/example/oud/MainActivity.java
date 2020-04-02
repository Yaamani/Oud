package com.example.oud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.oud.api.AccessToken;
import com.example.oud.api.LoginResponse;
import com.example.oud.api.OudApi;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    MyViewModel myViewModel;
    OudApi oudApi;

    boolean signupWithFacebook = false;
    public boolean isSignupWithFacebook() {
        return signupWithFacebook;
    }

    public void setSignupWithFacebook(boolean signupWithFacebook) {
        this.signupWithFacebook = signupWithFacebook;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        callbackManager = CallbackManager.Factory.create();//for facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String facebookToken =   loginResult.getAccessToken().getToken();
                initializeApi();
                makeFacebookAuthenticationRequest(facebookToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("facebook", exception.getMessage());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void initializeApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        oudApi = retrofit.create(OudApi.class);
    }

    private void makeFacebookAuthenticationRequest(String accessTokenString){
        // after i have the access token from facebook i send it and check if the user is signed up


        AccessToken accessToken = new AccessToken(accessTokenString);
        Call<JsonObject> call = oudApi.authenticateWithFacebook(accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){


                    if(response.body().toString().contains("\"_id\":")){
                        getTokenFromFacebookAccessToken(accessTokenString);

                    }
                    else if (response.body().toString().contains("facebook_id")){
                        //the user is not signed up yet
                        myViewModel.setSignupWithFacebook(true);
                        giveDataToViewModelFromFacebookAuthenticationResponse(response.body());
                        //close the opened dialog
                        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().findFragmentByTag("tag");
                        dialogFragment.dismiss();
                        //go to sign up fragment
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.nav_host_fragment,new SignupFragment());
                        ft.addToBackStack(null).commit();

                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }
    private void giveDataToViewModelFromFacebookAuthenticationResponse(JsonObject jsonResponse){

        if(jsonResponse.toString().contains("email")){
            myViewModel.setEmail(jsonResponse.get("email").toString());
        }
        if(jsonResponse.toString().contains("gender")){
            myViewModel.setGender(jsonResponse.get("gender").toString());
        }
        if(jsonResponse.toString().contains("displayName")){
            myViewModel.setDisplayName(jsonResponse.get("displayName").toString());
        }
        if(jsonResponse.toString().contains("birthDate")){
            myViewModel.setBirthDate(jsonResponse.get("birthDate").toString());
        }

    }



    private void getTokenFromFacebookAccessToken(String accessTokenString){

        AccessToken accessToken = new AccessToken(accessTokenString);
        Call<LoginResponse> call = oudApi.getLoginResponseFromFacebookAccessToken(accessToken);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    //go to home page
                    String token = response.body().getToken();
                    saveToken(token);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }
    private void saveToken(String token){

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token",token);
        prefsEditor.apply();    //token saved in shared preferences

    }

}





