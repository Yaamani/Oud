package com.example.oud;



import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.facebook.FacebookSdk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import okhttp3.mockwebserver.MockWebServer;

@Config(sdk = 25,manifest=Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MainLoginFragmentTest {
    MainActivity mainActivity;
    MockWebServer server;


    @Before
    public void initialize(){
        mainActivity= Robolectric.buildActivity(MainActivity.class).create().resume().get();
        server = new MockWebServer();


    }

    @Test
    public void checkLoginButton(){
        Button toLoginFragmentBtn = mainActivity.findViewById(R.id.Btn_to_login_fragment);
        toLoginFragmentBtn.performClick();

        Button loginBtn = mainActivity.findViewById(R.id.Btn_login);
        Assert.assertNotNull(loginBtn);

    }

    @Test
    public void checkSignupButton(){
        Button toSignupFragmentBtn = mainActivity.findViewById(R.id.Btn_to_signup_fragment);
        toSignupFragmentBtn.performClick();

        Button signupBtn = mainActivity.findViewById(R.id.btn_signup);
        Assert.assertNotNull(signupBtn);
    }

    @Test
    public void checkLoginRequestWhenSuccessful(){


    }





}
