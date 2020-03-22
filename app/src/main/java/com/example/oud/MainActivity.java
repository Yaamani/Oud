package com.example.oud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();//for facebook

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"anything",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"login canceled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"login error",Toast.LENGTH_LONG).show();
//
//
//keytool -exportcert -alias androiddebugkey -keystore "C:\Users\youssef mahmoud\.android\debug.keystore" | "C:\Users\youssef mahmoud\Downloads\openssl-0.9.8k_X64\bin\openssl" sha1 -binary | "C:\Users\youssef mahmoud\Downloads\openssl-0.9.8k_X64\bin\openssl" base64
//keytool -exportcert -list -v -alias androiddebugkey -keystore "C:\Users\youssef mahmoud\.android\debug.keystore"
                Log.e("facebook", exception.getMessage());
            }
        });




        //61720344528-u98v7cubn6hsvv0enoqvrqp2sl4svths.apps.googleusercontent.com
        //ArETLpAewOMGGTYWXHWAYJh3


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(),"in activity",Toast.LENGTH_LONG).show();




    }
}





