package com.example.oud;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class OudUtils {

    public static <T> String commaSeparatedListQueryParameter(ArrayList<T> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : list) {
            stringBuilder.append(item.toString());
            stringBuilder.append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static <T> String commaSeparatedListQueryParameter(T[] list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : list) {
            stringBuilder.append(item.toString());
            stringBuilder.append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    public static void saveUserData(View v , String token,String userId){

        SharedPreferences prefs = v.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

        token = "Bearer "+token;
        
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME,token);
        prefsEditor.putString(Constants.SHARED_PREFERENCES_USER_ID_NAME,userId);

        prefsEditor.apply();    //token saved in shared preferences

    }

    public static void saveUserData(Context context, String token,String userId){

        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);

        token = "Bearer "+token;

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME,token);
        prefsEditor.putString(Constants.SHARED_PREFERENCES_USER_ID_NAME,userId);
        prefsEditor.apply();    //token saved in shared preferences

    }
    public static String getToken(Context context){
    SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
    return  prefs.getString(Constants.SHARED_PREFERENCES_TOKEN_NAME,"");

    }

    public static String getUserId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
        return  prefs.getString(Constants.SHARED_PREFERENCES_USER_ID_NAME,"");

    }




}
