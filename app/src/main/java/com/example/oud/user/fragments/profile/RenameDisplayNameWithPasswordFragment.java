package com.example.oud.user.fragments.profile;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.LoggedInUser;
import com.example.oud.api.UpdateProfileData;
import com.example.oud.connectionaware.ConnectionAwareFragment;

public class RenameDisplayNameWithPasswordFragment extends ConnectionAwareFragment<RenameDisplayNameWithPasswordViewModel> {
    String userId;
    String displayName;


    LoggedInUser fullProfile;
    EditText displayNameEditText;
    EditText passwordEditText;
    Button renameButton;
    MutableLiveData<String> errorMessage= new MutableLiveData<>();

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RenameDisplayNameWithPasswordFragment(Activity activity){
        super(RenameDisplayNameWithPasswordViewModel.class,
                R.layout.fragment_rename_display_name_with_password,
                activity.findViewById(R.id.progress_bar_user_activity),
                activity.findViewById(R.id.block_view),
                null);
    }

    public static RenameDisplayNameWithPasswordFragment newInstance(Activity activity,String userId,String displayName) {
        RenameDisplayNameWithPasswordFragment fragment= new RenameDisplayNameWithPasswordFragment(activity);
        fragment.setUserId(userId);
        fragment.displayName = displayName;
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayNameEditText = view.findViewById(R.id.edit_text_rename_with_password);
        displayNameEditText.setText(displayName);

        passwordEditText = view.findViewById(R.id.edit_text_password_rename_with_password);
        renameButton = view.findViewById(R.id.btn_rename_with_password);
        renameButton.setEnabled(false);

        mViewModel.getProfile(OudUtils.getToken(getContext())).observe(getViewLifecycleOwner(), new Observer<LoggedInUser>() {
            @Override
            public void onChanged(LoggedInUser loggedInUser) {

                fullProfile = loggedInUser;
                renameButton.setEnabled(true);
            }
        });

        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                String newDisplayname = displayNameEditText.getText().toString();
                UpdateProfileData data = new UpdateProfileData(fullProfile.getEmail(),password,fullProfile.getGender(),fullProfile.getBirthDate(),fullProfile.getCountry(),newDisplayname);
                if(passwordEditText.getText().toString().length()<8)
                    passwordEditText.setError("please enter you password ");
                else
                mViewModel.updateProfile(OudUtils.getToken(getContext()),data,errorMessage);
            }
        });
        errorMessage.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("success")){
                    hideRenameFragment(getActivity(), R.id.nav_host_fragment);
                }else if(s.equals("Bad Request"))
                    passwordEditText.setError("Wrong password");
                    else
                    Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void hideRenameFragment(FragmentActivity activity, @IdRes int fragmentContainerId) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(manager.findFragmentById(fragmentContainerId))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
