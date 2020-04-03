package com.example.oud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


import com.example.oud.api.AccessToken;
import com.example.oud.api.LoginResponse;
import com.example.oud.api.OudApi;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ConnectWithOtherServicesDialogFragment extends DialogFragment {
    final int RC_SIGN_IN= 1;
    CallbackManager callbackManager;
    OudApi oudApi;
    MyViewModel myViewModel;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();//for facebook

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);

        myViewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_dialog_connect_with_other_services, null);

        SignInButton signInButton = v.findViewById(R.id.sign_in_button);
        signInButton.setStyle(SignInButton.SIZE_WIDE,SignInButton.COLOR_AUTO);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        });

        LoginButton loginButton = v.findViewById(R.id.btn_facebook_login);
        loginButton.setPermissions(Arrays.asList("email"));



        builder.setView(v);

        return builder.create();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
       if (requestCode == RC_SIGN_IN) {
           // The Task returned from this call is always completed, no need to attach
           // a listener.
           Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
           try {
               GoogleSignInAccount googleAccount= task.getResult(ApiException.class);
               googleAccount.getIdToken();
           }
           catch (ApiException e){
               Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());
               if(e.getStatusCode()==10)
                   Log.e("TAG", "if you have this problem visit this page https://developers.google.com/android/guides/client-auth");
           }
         //Toast.makeText(getContext(),account,Toast.LENGTH_LONG);
       }


    }


    private void makeGoogleAuthenticationRequest(String accessTokenString){
        // after i have the access token from facebook i send it and check if the user is signed up


        AccessToken accessToken = new AccessToken(accessTokenString);
        Call<JsonObject> call = oudApi.authenticateWithFacebook(accessToken);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){


                    if(response.body().toString().contains("\"_id\":")){
                        getTokenFromGoogleAccessToken(accessTokenString);

                    }
                    else if (response.body().toString().contains("google_id")){
                        //the user is not signed up yet
                        myViewModel.setSignupWithGoogle(true);
                        giveDataToViewModelFromGoogleAuthenticationResponse(response.body());
                        //close the opened dialog

                        //go to sign up fragment
                        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.add(R.id.nav_host_fragment,new SignupFragment());
                        ft.addToBackStack(null).commit();

                        dismiss();

                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    }

    private void giveDataToViewModelFromGoogleAuthenticationResponse(JsonObject jsonResponse){

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

    private void getTokenFromGoogleAccessToken(String accessTokenString){

        AccessToken accessToken = new AccessToken(accessTokenString);
        Call<LoginResponse> call = oudApi.getLoginResponseFromGoogleAccessToken(accessToken);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){

                    String token = response.body().getToken();
                    saveToken(token);

                    //go to home page

                    dismiss();
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
