package com.example.oud;

import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.oud.api.*;
import com.example.oud.user.UserActivity;
import com.google.gson.Gson;

import android.telephony.TelephonyManager;


import android.content.Context;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class ActualLoginFragment extends Fragment {
    private Button loginBtn;
    private Button toForgetPasswordBtn;
    private EditText usernameEditText;
    private EditText passwordEditText;
    OudApi oudApi;
    TextView errorTextView;


    public ActualLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_actual_login, container, false);

        initializeViews(v);
        setButtonsOnClickListeners();

        //TODO: add a toolbar to this fragment
        //setToolbar();

        return v;
    }

    private void setToolbar() {
        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initializeViews(View v) {
        toForgetPasswordBtn = v.findViewById(R.id.btn_to_forget_password);
        loginBtn = v.findViewById(R.id.btn_login);
        usernameEditText = v.findViewById(R.id.text_login_username);
        passwordEditText = v.findViewById(R.id.text_login_password);
        errorTextView = v.findViewById(R.id.text_view_login_error_message);
    }

    private void setButtonsOnClickListeners() {
        toForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_actualLoginFragment_to_forgetPasswordFragment);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeLoginRequest(view);
            }
        });


    }

    private void makeLoginRequest(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        LoginUserInfo loginUserInfo =  new LoginUserInfo(username, password);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);
        Call<LoginResponse> call = oudApi.login(loginUserInfo);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                //errorTextView.setText("test");

                if (response.isSuccessful()) {
                    //errorTextView.setText(response.body().getUser().getEmail());//remove and change the testing class after you add the correct response
                    String token = response.body().getToken();
                    String userId = response.body().getUser().get_id();
                    OudUtils.saveUserData(view,token,userId);
                    Log.e("ActualLoginFragment",token);
                    Intent i = new Intent(getActivity(), UserActivity.class);
                    i.putExtra(Constants.USER_ID_KEY, response.body().getUser().get_id());
                    startActivity(i);

                } else if (response.errorBody() != null) {
                    Gson gson = new Gson();
                    try{
                    StatusMessageResponse errorMessage = gson.fromJson(response.errorBody().charStream(),StatusMessageResponse.class);
                    errorTextView.setText(errorMessage.getMessage());
                    }catch (Exception e){
                      }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //TODO: make an internet connection error toast or something

                errorTextView.setText(t.getMessage());
            }
        });


    }


}
