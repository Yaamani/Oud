package com.example.oud;

import android.content.Intent;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.oud.api.*;
import com.example.oud.user.UserActivity;

import android.telephony.TelephonyManager;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActualLoginFragment extends Fragment {
    private Button loginBtn;
    private Button toForgetPasswordBtn;
    private EditText usernameEditText;
    private EditText passwordEditText;
    TelephonyManager telephonyManager;
    OudApi oudApi;



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

        //telephonyManager = (TelephonyManager) v.getContext().getSystemService(Context.TELEPHONY_SERVICE);

        toForgetPasswordBtn = v.findViewById(R.id.btn_to_forget_password);
        loginBtn = v .findViewById(R.id.btn_login);
        toForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_actualLoginFragment_to_forgetPasswordFragment);
            }
        });
        usernameEditText = v.findViewById(R.id.text_login_username);
        passwordEditText = v.findViewById(R.id.text_login_password);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String deviceId = telephonyManager.getDeviceId();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Device device  = new Device("id_test",
                        true,
                        true ,
                        "device name test",
                        "device type test",
                        10);

                LoginBody loginBody = new LoginBody(device,new LoginUserInfo(username,password));
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                oudApi = retrofit.create(OudApi.class);
                Call<LoginResponse> call = oudApi.login(loginBody);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.isSuccessful()) {
                            //TODO: go to homepage and forward the user data & token
                        }
                        else if (response.errorBody()!=null){
                            /*Gson gson= new Gson();
                            StatusMessageResponse errorMessage = gson.fromJson(response.errorBody().charStream(),StatusMessageResponse.class);*/
                            //TODO: show the error message
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        //TODO: make an internet connection error toast or something
                    }
                });



            }
        });





        loginBtn = v.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //TODO: add a toolbar to this fragment
        //setToolbar();

        return v;
    }
    private void setToolbar(){
        ((MainActivity) getActivity()).getSupportActionBar().show();
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
