package com.example.oud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.oud.api.OudApi;


/*

 * create an instance of this fragment.
 */
public class ForgetPasswordFragment extends Fragment {

    private Button getLinkBtn;
    private final String BASE_URL = "http://example.com";
    OudApi oudApi;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forget_password, container, false);

        return v;
    }
    private void initializeViews(View v){
        getLinkBtn = v.findViewById(R.id.btn_forget_password_get_link);
    }
    private void setButtonsOnClickListeners(){

        getLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
