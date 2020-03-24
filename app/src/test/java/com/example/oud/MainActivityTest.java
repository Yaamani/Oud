package com.example.oud;



import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@Config(sdk = 25,manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    MainActivity mainActivity;
    MockWebServer server;
    String jsonResponse;


    @Before
    public void initialize(){
        mainActivity= Robolectric.buildActivity(MainActivity.class).create().resume().get();
        server = new MockWebServer();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Constants.BASE_URL = server.url("/").toString();



    }

    @Test
    public void checkLoginButton(){
        Button toLoginFragmentBtn = mainActivity.findViewById(R.id.btn_to_login_fragment);
        toLoginFragmentBtn.performClick();

        Button loginBtn = mainActivity.findViewById(R.id.btn_login);
        Assert.assertNotNull(loginBtn);

    }

    @Test
    public void checkSignupButton(){
        Button toSignupFragmentBtn = mainActivity.findViewById(R.id.btn_to_signup_fragment);
        toSignupFragmentBtn.performClick();

        Button signupBtn = mainActivity.findViewById(R.id.btn_signup);
        Assert.assertNotNull(signupBtn);
    }

    @Test
    public void checkLoginRequestSentCorrectly() throws JSONException {
        //go to actualLoginFragment
        Button toLoginFragmentBtn = mainActivity.findViewById(R.id.btn_to_login_fragment);
        toLoginFragmentBtn.performClick();

        EditText username = mainActivity.findViewById(R.id.text_login_username);
        EditText password = mainActivity.findViewById(R.id.text_login_password);
        //set username & password
        username.setText("youssefMahmoud");
        password.setText("123456");

        //make the login request
        Button loginBtn = mainActivity.findViewById(R.id.btn_login);
        loginBtn.performClick();



        RecordedRequest request1 = null;
        try {
            request1 = server.takeRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("/users/login", request1.getPath());

        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(request1.getBody().readUtf8());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String actualPassword = json.getString("password");
        String sentUsername = json.getString("email");
        assertEquals("123456",actualPassword);
        assertEquals("youssefMahmoud",sentUsername);

    }

    @Test
    public void checkSuccessfulLoginResponseReceivedCorrectly(){
        //setup server response
        setSuccessfulLoginResponse();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(jsonResponse));

        goToLoginFragmentAndPressLogin();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();
        TextView messageResponse = mainActivity.findViewById(R.id.text_view_login_error_message);
        String message = messageResponse.getText().toString();
        Assert.assertEquals("example@example.com",message);//change after the home page is added


    }


    @Test
    public void checkUnSuccessfulLoginResponseReceivedCorrectly(){
        //setup server response
        setUnsuccessfulLoginResponse();
        server.enqueue(new MockResponse().setResponseCode(400).setBody(jsonResponse));

        goToLoginFragmentAndPressLogin();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();

        TextView messageResponse = mainActivity.findViewById(R.id.text_view_login_error_message);
        String message = messageResponse.getText().toString();
        Assert.assertEquals("username or password are invalid",message);


    }

    @Test
    public void checkTokenSavedSuccessfully(){
        setSuccessfulLoginResponse();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(jsonResponse));

        goToLoginFragmentAndPressLogin();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();

        SharedPreferences prefs = mainActivity.getApplicationContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        String savedToken = prefs.getString("token","");
        assertEquals("123456token",savedToken);
    }

    @Test
    public void checkSignupRequestSentCorrectly() throws JSONException {
        //go to signupFragment
        Button toSignupFragment = mainActivity.findViewById(R.id.btn_to_signup_fragment);
        toSignupFragment.performClick();

        fillSignupData();
        //make the signup request
        Button signupButton = mainActivity.findViewById(R.id.btn_signup);
        signupButton.performClick();



        RecordedRequest request1 = null;
        try {
            request1 = server.takeRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("/users/signUp", request1.getPath());

        String jsonString =request1.getBody().readUtf8();

        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String sentPassword = json.getString("password");
        String sentUsername = json.getString("username");
        String sentEmail = json.getString("email");
        String gender = json.getString("gender");
        assertEquals("123456789",sentPassword);
        assertEquals("youssefmahmoud",sentUsername);
        assertEquals("test@gmail.com",sentEmail);
        assertEquals("m",gender);


    }

    @Test
    public void checkSuccessfulSignupResponseReceivedCorrectly(){
        //setup server response

        setSuccessfulLoginResponse();
        server.enqueue(new MockResponse().setResponseCode(200).setBody(jsonResponse));

        Button toSignupFragment = mainActivity.findViewById(R.id.btn_to_signup_fragment);
        toSignupFragment.performClick();

        fillSignupData();

        Button signupButton = mainActivity.findViewById(R.id.btn_signup);
        signupButton.performClick();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();
        TextView messageResponse = mainActivity.findViewById(R.id.text_view_signup_error_message);
        String message = messageResponse.getText().toString();
        Assert.assertEquals("example@example.com",message);//change after the home page is added


    }



    @Test
    public void checkUnSuccessfulSignupResponseReceivedCorrectly(){
        //setup server response
        setUnsuccessfulSignupResponse();
        server.enqueue(new MockResponse().setResponseCode(400).setBody(jsonResponse));

        //go to signupFragment
        Button toSignupFragment = mainActivity.findViewById(R.id.btn_to_signup_fragment);
        toSignupFragment.performClick();

        fillSignupData();

        Button signupButton = mainActivity.findViewById(R.id.btn_signup);
        signupButton.performClick();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();

        TextView messageResponse = mainActivity.findViewById(R.id.text_view_signup_error_message);
        String message = messageResponse.getText().toString();
        Assert.assertEquals("username already taken",message);


    }


    private void goToLoginFragmentAndPressLogin(){
        //go to actualLoginFragment
        Button toLoginFragmentBtn = mainActivity.findViewById(R.id.btn_to_login_fragment);
        toLoginFragmentBtn.performClick();

        //make the login request
        Button loginBtn = mainActivity.findViewById(R.id.btn_login);
        loginBtn.performClick();
    }

    private void fillSignupData(){
        //country default is egypt
        EditText usernameEditText =mainActivity.findViewById(R.id.text_signup_username);
        EditText displayNameEditText =mainActivity.findViewById(R.id.text_signup_display_name);
        EditText emailEditText=mainActivity.findViewById(R.id.text_signup_email);
        EditText passwordEditText=mainActivity.findViewById(R.id.text_signup_password);

        RadioGroup genderRadioGroup =mainActivity.findViewById(R.id.radio_group_signup_gender);


        usernameEditText.setText("youssefmahmoud");
        displayNameEditText.setText("yousef dispalyname");
        emailEditText.setText("test@gmail.com");
        passwordEditText.setText("123456789");
        genderRadioGroup.check(1);


    }

    private void setSuccessfulLoginResponse(){
        jsonResponse= "{\n" +
                "  \"token\": \"123456token\",\n" +
                "  \"user\": {\n" +
                "    \"_id\": \"5a2539b41c574006c46f1a07\",\n" +
                "    \"username\": \"string\",\n" +
                "    \"birthDate\": \"2020-03-23\",\n" +
                "    \"gender\": \"F\",\n" +
                "    \"email\": \"example@example.com\",\n" +
                "    \"displayName\": \"string\",\n" +
                "    \"role\": \"free\",\n" +
                "    \"country\": \"EG\",\n" +
                "    \"credit\": 0,\n" +
                "    \"plan\": null,\n" +
                "    \"images\": [\n" +
                "      \"string\"\n" +
                "    ],\n" +
                "    \"followersCount\": 0,\n" +
                "    \"verified\": false,\n" +
                "    \"Artist\": {\n" +
                "      \"_id\": \"string\",\n" +
                "      \"followersCount\": 0,\n" +
                "      \"genres\": [\n" +
                "        \"string\"\n" +
                "      ],\n" +
                "      \"images\": [\n" +
                "        \"string\"\n" +
                "      ],\n" +
                "      \"name\": \"string\",\n" +
                "      \"bio\": \"string\",\n" +
                "      \"popularSongs\": [\n" +
                "        {\n" +
                "          \"_id\": \"string\",\n" +
                "          \"name\": \"string\",\n" +
                "          \"artists\": [\n" +
                "            {\n" +
                "              \"_id\": \"string\",\n" +
                "              \"name\": \"string\",\n" +
                "              \"type\": \"string\",\n" +
                "              \"image\": \"string\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"albumId\": \"string\",\n" +
                "          \"type\": \"string\",\n" +
                "          \"audioUrl\": \"string\",\n" +
                "          \"duartion\": 0,\n" +
                "          \"views\": 0\n" +
                "        }\n" +
                "      ],\n" +
                "      \"type\": \"string\"\n" +
                "    },\n" +
                "    \"facebook_id\": \"string\",\n" +
                "    \"google_id\": \"string\",\n" +
                "    \"type\": \"string\"\n" +
                "  }\n" +
                "}";


    }


    private void setUnsuccessfulLoginResponse(){
        jsonResponse = "{\n" +
                "  \"status\": \"string\",\n" +
                "  \"message\": \"username or password are invalid\"\n" +
                "}";
    }


    private void setUnsuccessfulSignupResponse(){
        jsonResponse = "{\n" +
                "  \"status\": \"400\",\n" +
                "  \"message\": \"username already taken\"\n" +
                "}";
    }

    @After
    public void cleanup(){
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
