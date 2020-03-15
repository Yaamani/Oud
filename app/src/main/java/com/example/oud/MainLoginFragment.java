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
    private Button loginBtn;
    private Button signupBtn;
    private Button ConnectWithFacebookBtn;

    // TODO: Rename and change types of parameters

    //private String mParam1;
    //private String mParam2;

    public MainLoginFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    //public static MainLoginFragment newInstance(String param1, String param2) {
    //   MainLoginFragment fragment = new MainLoginFragment();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

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
        View v = inflater.inflate(R.layout.fragment_main_login, container, false);
        loginBtn = v.findViewById(R.id.Btn_to_login_fragment);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController =  Navigation.findNavController(view);
                navController.navigate(R.id.action_mainLoginFragment_to_actualLoginFragment);
            }
        });


        return v;
    }
}
