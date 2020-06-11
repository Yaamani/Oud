package com.example.oud.user.fragments.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;


import com.bumptech.glide.Glide;
import com.example.oud.Constants;
import com.example.oud.NotificationShareUtils;
import com.example.oud.OudUtils;
import com.example.oud.R;

import com.example.oud.api.ProfilePreview;
import com.example.oud.authentication.MainActivity;


public class PreferencesSettingsFragment extends PreferenceFragmentCompat {

    SettingsViewModel mViewModel;
    Context context;
    String myId;
    String token;

    Preference logoutPreference;
    Preference clearCachePreference;
    SwitchPreferenceCompat autoPlaySwitchPreference;
    SwitchPreferenceCompat allowNotificationsSwitchPreference;

    public PreferencesSettingsFragment(Context context){
        mViewModel= ViewModelProviders.of((FragmentActivity)context).get(SettingsViewModel.class);
        this.context =context;
    }
    public static PreferencesSettingsFragment newInstance(Context context) {
        PreferencesSettingsFragment fragment = new PreferencesSettingsFragment(context);
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        myId = OudUtils.getUserId(context);
        token = OudUtils.getToken(context);


        setPreferencesFromResource(R.xml.preferences_settings_fragment, rootKey);
        logoutPreference = findPreference("log_out");
        logoutPreference.setSummary("you are logged in as " + OudUtils.getUserId(context));

        clearCachePreference = findPreference("clear_cache");
        autoPlaySwitchPreference = (SwitchPreferenceCompat)findPreference("auto_play");
        allowNotificationsSwitchPreference = (SwitchPreferenceCompat)findPreference("enable_notifications");



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel.getProfile(myId,token).observe(getViewLifecycleOwner(), new Observer<ProfilePreview>() {
            @Override
            public void onChanged(ProfilePreview profilePreview) {
                logoutPreference.setSummary("you are logged in as " + profilePreview.getDisplayName());
            }
        });
        setLogoutOnClickListener();
        setClearCacheOnClickListener();
        setAutoPlayOnClickListener();
        setAllowNotificationsOnClickListener();

        boolean isAutoPlayback = OudUtils.isAutoPlayback(context);
        autoPlaySwitchPreference.setChecked(isAutoPlayback);
        allowNotificationsSwitchPreference.setChecked(OudUtils.isNotificationAllowed(getContext()));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setLogoutOnClickListener(){
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences prefs = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= prefs.edit();
                editor.remove(Constants.SHARED_PREFERENCES_TOKEN_NAME);
                editor.remove(Constants.SHARED_PREFERENCES_USER_ID_NAME);
                editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();

                NotificationShareUtils.unsubscribeFromAllTopicsUponLoggingOut();

                return true;
            }
        });

    }

    private void setClearCacheOnClickListener(){
        clearCachePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                clearCache();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yup!", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


                return true;
            }
        });





    }

    private void clearCache(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
            }
        });


        //todo clear player cache
        Toast.makeText(getContext(),"Cache cleared!",Toast.LENGTH_LONG).show();
    }

    private void setAutoPlayOnClickListener(){
        autoPlaySwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isAutoPlay =(boolean)newValue;
                String text ;
                if(isAutoPlay)
                    text = "true";
                else
                    text = "false";
                OudUtils.setIsAutoPlayback(context,isAutoPlay);

                return true;
            }
        });
    }
    private void setAllowNotificationsOnClickListener(){
        allowNotificationsSwitchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isAutoPlay =(boolean)newValue;
                String text ;
                if(isAutoPlay)
                    text = "true";
                else
                    text = "false";
                OudUtils.setIsNotificationAllowed(context,isAutoPlay);

                return true;
            }
        });
    }
}
