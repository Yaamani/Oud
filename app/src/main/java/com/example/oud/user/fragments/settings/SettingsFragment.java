package com.example.oud.user.fragments.settings;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.profile.ProfileFragment;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        Button viewMyProfileTest =v.findViewById(R.id.btn_view_my_profile_test);

        viewMyProfileTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ProfileFragment profileFragment = ProfileFragment.newInstance(((UserActivity)getActivity()).getUserId(),getActivity());
                ProfileFragment.show(getActivity(),R.id.nav_host_fragment, OudUtils.getUserId(getContext()));


            }
        });
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }

}
