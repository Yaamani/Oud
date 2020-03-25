package com.example.oud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oud.api.ForgetPasswordRequestBody;
import com.example.oud.api.LoginResponse;
import com.example.oud.api.LoginUserInfo;
import com.example.oud.api.OudApi;
import com.example.oud.api.StatusMessageResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*

 * create an instance of this fragment.
 */
public class ForgetPasswordFragment extends Fragment {

    private Button getLinkButton;
    private EditText emailEditText;
    private TextView errorTextView;
    OudApi oudApi;

    public ForgetPasswordFragment() {
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
        View v = inflater.inflate(R.layout.fragment_forget_password, container, false);
        initializeViews(v);
        setButtonsOnClickListeners();

        return v;
    }
    private void initializeViews(View v){
        getLinkButton = v.findViewById(R.id.btn_forget_password_get_link);
        emailEditText = v.findViewById(R.id.text_forget_password_email);
        errorTextView = v.findViewById(R.id.text_view_forget_password_error_message);
    }

    private void setButtonsOnClickListeners(){
        getLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void makeForgetPasswordRequest(View view) {
        String email = emailEditText.getText().toString();




        ForgetPasswordRequestBody forgetPasswordRequestBody = new ForgetPasswordRequestBody(email);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);
        Call<StatusMessageResponse> call = oudApi.forgetPasswordRequest(forgetPasswordRequestBody);
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {

                errorTextView.setText("test");

                if (response.isSuccessful()) {
                    //TODO: go to homepage and forward the user data & token
                    errorTextView.setText(response.body().getMessage());

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
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                //TODO: make an internet connection error toast or something
                errorTextView.setText("internet connection error");
            }
        });


    }
}
