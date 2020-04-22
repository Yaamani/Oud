package com.example.oud.user.fragments.premium.redeemsubscribe;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private Button mButtonSubscribeExtend;


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
        mButtonSubscribeExtend = view.findViewById(R.id.btn_subscribe_extend);
    }

    private void handleData() {
        mViewModel.getProfileLiveData(token).observe(getViewLifecycleOwner(), profile -> {

            Resources resources = getContext().getResources();

            OudUtils.glideBuilder(getContext(), profile.getImages().get(0))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_oud_loading_circular)
                    //.error(R.drawable.ic_warning)
                    .apply(RequestOptions.circleCropTransform())
                    .into(mImageViewProfilePic);

            mTextViewUserName.setText(profile.getDisplayName());

            String credit = resources.getString(R.string.credit)
                    .concat("" + profile.getCredit());
            mTextViewCredit.setText(credit);


            if (profile.getRole().equals(Constants.API_PREMIUM)) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String plan = resources.getString(R.string.end_date);
                if (profile.getPlan() != null)
                    plan.concat(dateFormat.format(profile.getPlan()));
                mTextViewPlan.setText(plan);
                mButtonSubscribeExtend.setText(resources.getString(R.string.extend));
            } else
                mTextViewPlan.setText(resources.getString(R.string.free_plan));




        });
    }
}
