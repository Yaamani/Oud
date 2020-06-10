package com.example.oud.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.NotificationUtils;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.AccessToken;
import com.example.oud.api.LoginResponse;
import com.example.oud.api.OudApi;
import com.example.oud.user.UserActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    CallbackManager callbackManager;
    MyViewModel myViewModel;
    OudApi oudApi;

    Boolean isFacebookSignup =false;
    Boolean isGoogleSignup= false;


    public Boolean getFacebookSignup() {
        return isFacebookSignup;
    }

    public void setFacebookSignup(Boolean facebookSignup) {
        isFacebookSignup = facebookSignup;
    }

    public Boolean getGoogleSignup() {
        return isGoogleSignup;
    }

    public void setGoogleSignup(Boolean googleSignup) {
        isGoogleSignup = googleSignup;
    }



    String email;
    String displayname;
    String gender;
    String birthDate;

    boolean signupWithFacebook = false;

    public boolean isSignupWithFacebook() {
        return signupWithFacebook;
    }

    public void setSignupWithFacebook(boolean signupWithFacebook) {
        this.signupWithFacebook = signupWithFacebook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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


        /*FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {

                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    // String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, "Current Firebase Token: " + token);

                });*/

        NotificationUtils.handleNotificationDestinationMainActivity(getIntent());
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
        Call<ResponseBody> call = oudApi.authenticateWithFacebook(accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){


                    if(response.body().toString().contains("token")){
                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(response.body().toString(),LoginResponse.class);
                        OudUtils.saveUserData(getApplicationContext(),loginResponse.getToken(),loginResponse.getUser().get_id());

                        Intent i = new Intent(getParent(), UserActivity.class);
                        i.putExtra(Constants.USER_ID_KEY, loginResponse.getUser().get_id());
                        startActivity(i);

                    }
                    else {
                        //the user is not signed up yet
                        isFacebookSignup=true;
                        putDatafromFacebook(JsonParser.parseString(response.body().toString()).getAsJsonObject());
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



    }
    private void putDatafromFacebook(JsonObject jsonResponse){

        if(jsonResponse.toString().contains("email")){
            email = jsonResponse.get("email").toString();
        }
        if(jsonResponse.toString().contains("gender")){
            gender = jsonResponse.get("gender").toString();
        }
        if(jsonResponse.toString().contains("displayName")){
            displayname = jsonResponse.get("displayName").toString();
        }
        if(jsonResponse.toString().contains("birthDate")){
            birthDate = jsonResponse.get("birthDate").toString();
        }

    }





}





