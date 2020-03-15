package com.example.oud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/*
 * A simple {@link Fragment} subclass.
 * Use the {@link MainLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainLoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    private Button toLoginBtn;
    private Button toSignupBtn;
    private Button ConnectWithFacebookBtn;

    // TODO: Rename and change types of parameters



    public MainLoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO: add a toolbar to this fragment
        //setToolbar();
        View v = inflater.inflate(R.layout.fragment_main_login, container, false);
        toLoginBtn = v.findViewById(R.id.Btn_to_login_fragment);
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController =  Navigation.findNavController(view);
                navController.navigate(R.id.action_mainLoginFragment_to_actualLoginFragment);
            }
        });




        return v;
    }

    private void setToolbar(){
        ((MainActivity) getActivity()).getSupportActionBar().hide();

    }
}
