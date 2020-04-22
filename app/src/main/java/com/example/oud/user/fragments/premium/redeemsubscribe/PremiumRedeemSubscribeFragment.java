package com.example.oud.user.fragments.premium.redeemsubscribe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremiumRedeemSubscribeFragment extends ConnectionAwareFragment<PremiumRedeemSubscribeViewModel> {

    private String token;

    private ImageView mImageViewProfilePic;
    private TextView mTextViewUserName;
    private TextView mTextViewCredit;

    private EditText mEditTextCoupon;
    private Button mButtonRedeem;

    private TextView mTextViewPlan;
    private Button mButtonSubscribe;


    public PremiumRedeemSubscribeFragment() {
        // Required empty public constructor
        super(PremiumRedeemSubscribeViewModel.class,
                R.layout.fragment_premium_redeem_subscribe,
                R.id.progress_premium_redeem_subscribe,
                R.id.view_block_ui_input,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleToken();

        initializeUiStuff(view);

        handleData();

    }

    private void handleToken() {
        token = OudUtils.getToken(getContext());
    }

    private void initializeUiStuff(View view) {
        mImageViewProfilePic = view.findViewById(R.id.img_profile_pic);
        mTextViewUserName = view.findViewById(R.id.txt_user_name);
        mTextViewCredit = view.findViewById(R.id.txt_credit);

        mEditTextCoupon = view.findViewById(R.id.edit_txt_coupon);
        mButtonRedeem = view.findViewById(R.id.btn_redeem);

        mTextViewPlan = view.findViewById(R.id.txt_plan);
        mButtonSubscribe = view.findViewById(R.id.btn_subscribe_extend);
    }

    private void handleData() {
        mViewModel.getProfileLiveData(token).observe(getViewLifecycleOwner(), profile -> {




        });
    }
}
