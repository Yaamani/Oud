package com.example.oud.dummy;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.R;

public class DummyFragment extends Fragment {

    private DummyViewModel mViewModel;

    public static DummyFragment newInstance() {
        return new DummyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dummy_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(DummyViewModel.class);
        // TODO: Use the ViewModel

        mViewModel.getDummyString().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mViewModel = ViewModelProviders.of(this).get(DummyViewModel.class);

        mViewModel.getConnectionSuccessful().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (!aBoolean) {
                    // Show you are offline
                    // getActivity().showYouAreOffline();
                    if (context instanceof ConnectionStatusListener)
                        ((ConnectionStatusListener) context).onConnectionFailure();
                    else
                        throw new RuntimeException(context.toString() +
                                " must implement " + ConnectionStatusListener.class.getSimpleName());

                } else {
                    // Remove you are offline fragment


                }
            }
        });
    }
}
