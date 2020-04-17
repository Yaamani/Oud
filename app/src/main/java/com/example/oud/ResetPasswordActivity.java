package com.example.oud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oud.api.LoginResponse;
import com.example.oud.api.LoginUserInfo;
import com.example.oud.api.OudApi;
import com.example.oud.api.ResetPasswordBody;
import com.example.oud.api.StatusMessageResponse;
import com.example.oud.user.UserActivity;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button resetPasswordButton;
    TextView errorMessageText;


    OudApi oudApi;

    String resetPasswordToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // ATTENTION: This was auto-generated to handle app links.
        getSupportActionBar().hide(); //hide the title bar
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        List<String> pathSegmants = appLinkData.getPathSegments();
        if(pathSegmants.get(pathSegmants.size()-2).equals("verify"))
            resetPasswordToken = pathSegmants.get(pathSegmants.size()-1);
        else
            resetPasswordToken = "";

        //Toast.makeText(getApplicationContext(),resetPasswordToken,Toast.LENGTH_LONG).show();

        initializeViews();
        setButtonsOnClickListeners();


    }
    private void initializeViews(){
        passwordEditText = findViewById(R.id.text_reset_password);
        confirmPasswordEditText = findViewById(R.id.text_reset_confirm_password);
        errorMessageText = findViewById(R.id.text_view_reset_password_error_message);
        resetPasswordButton = findViewById(R.id.btn_reset_password);
    }
    private void setButtonsOnClickListeners(){
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessageText.setText("");
                if(dataIsCorrect()){
                    makeLoginRequest();
                }
            }
        });

    }
    private boolean dataIsCorrect(){
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if(password.length()<8){
            errorMessageText.setText("Password must be at least 8 characters long");
            return false;
        }
        else if(! password.equals(confirmPassword)){
            errorMessageText.setText("The entered password and the confirmation doesn't match");
            return false;
        }

        return true;
    }

    private void makeLoginRequest() {
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();


        ResetPasswordBody resetPasswordBody = new ResetPasswordBody(password,confirmPassword);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);
        Call<LoginResponse> call = oudApi.resetPassword(resetPasswordToken,resetPasswordBody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                errorMessageText.setText("test");

                if (response.isSuccessful()) {
                    //errorTextView.setText(response.body().getUser().getEmail());//remove and change the testing class after you add the correct response
                    String token = response.body().getToken();
                    String userId = response.body().getUser().get_id();

                    OudUtils.saveUserData(getApplicationContext(),token,userId);
                    Intent i = new Intent(getParent(), UserActivity.class);
                    i.putExtra(Constants.USER_ID_KEY, response.body().getUser().get_id());
                    startActivity(i);

                } else if (response.errorBody() != null) {
                    Gson gson = new Gson();
                    try{
                        StatusMessageResponse errorMessage = gson.fromJson(response.errorBody().charStream(),StatusMessageResponse.class);
                        errorMessageText.setText(errorMessage.getMessage());
                    }catch (Exception e){
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                errorMessageText.setText("internet connection error");
            }
        });


    }


}
