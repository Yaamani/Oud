package com.example.oud;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oud.api.LoginResponse;
import com.example.oud.api.LoginUserInfo;
import com.example.oud.api.OudApi;
import com.example.oud.api.SignupUser;
import com.example.oud.api.StatusMessageResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class SignupFragment extends Fragment {
    private Spinner countrySpinner;
    private EditText usernameEditText;
    private EditText displayNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private DatePicker dateOfBirthDatePicker;
    private RadioGroup genderRadioGroup;
    private Button signupButton;

    private TextView errorTextView;

    private SignupUser signupUser;
    private OudApi oudApi;





    public SignupFragment() {
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
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        initialize(v);
        setCountriesSpinner(v);
        setButtonsOnClickListener();



        return v;
    }

    private void initialize(View v){
        countrySpinner = v.findViewById(R.id.spinner_signup_country);
        usernameEditText =v.findViewById(R.id.text_signup_username);
        displayNameEditText =v.findViewById(R.id.text_signup_display_name);
        emailEditText=v.findViewById(R.id.text_signup_email);
        passwordEditText=v.findViewById(R.id.text_signup_password);
        dateOfBirthDatePicker =v.findViewById(R.id.date_signup);
        genderRadioGroup =v.findViewById(R.id.radio_group_signup_gender);
        signupButton = v.findViewById(R.id.btn_signup);
        errorTextView = v.findViewById(R.id.text_view_signup_error_message);
    }
    private void setButtonsOnClickListener(){
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataIsAccurate()){
                    makeSignupRequest(view);
                }
            }
        });

    }
    private void setCountriesSpinner(View v){


        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            //String country = locale.getDisplayCountry();
            String countryName = locale.getDisplayCountry()+"("+locale.getCountry()+")";

            if (countryName.trim().length() > 2 && !countries.contains(countryName)) {
                countries.add(countryName);
            }
        }
        Collections.sort(countries);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(v.getContext(),  android.R.layout.simple_spinner_item, countries);
        // set the view for the Drop down list

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set the ArrayAdapter to the spinner
        countrySpinner.setAdapter(dataAdapter);
        countrySpinner.setSelection(countries.indexOf("Egypt(EG)"));

    }

    private boolean dataIsAccurate(){
        if(usernameEditText.getText().toString()==""){
            errorTextView.setText("please enter your username");
            return false;
        }
        else if(displayNameEditText.getText().toString()==""){
            errorTextView.setText("please enter your display name");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()){
            errorTextView.setText("please enter a valid email address");
            return false;
        }
        else if(passwordEditText.getText().toString()==""||passwordEditText.getText().toString().length()<8){
            errorTextView.setText("password must be at least 8 characters long");
            return false;
        }
        else if(genderRadioGroup.getCheckedRadioButtonId()==-1){
            errorTextView.setText("please select a gender");
            return false;
        }

        return true;

    }

    private void makeSignupRequest(View v){

        //LoginUserInfo loginUserInfo =  new LoginUserInfo(username, password);
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String displayName = displayNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String gender;

        if(genderRadioGroup.getCheckedRadioButtonId()==1)
           gender= "m";
        else
           gender="f";

        String dateOfBirth = dateOfBirthDatePicker.getYear()+"-"+dateOfBirthDatePicker.getMonth()+"-"+dateOfBirthDatePicker.getDayOfMonth();
        String country = countrySpinner.getSelectedItem().toString();
        country = country.substring(country.indexOf("(") + 1);
        country = country.substring(0, country.indexOf(")"));

        SignupUser signupUser = new SignupUser(username,displayName,dateOfBirth,email,password,password,"free",country,gender);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        oudApi = retrofit.create(OudApi.class);
        Call<LoginResponse> call = oudApi.signup(signupUser);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    //todo go to choose artists page
                    errorTextView.setText(response.body().getUser().getEmail());//remove after select artists page is done & change test
                    String token = response.body().getToken();
                    saveToken(v,token);
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
                errorTextView.setText("internet connection error");
            }
        });



    }

    private void saveToken(View v , String token){

        SharedPreferences prefs = v.getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("token",token);
        prefsEditor.apply();    //token saved in shared preferences

    }



}
