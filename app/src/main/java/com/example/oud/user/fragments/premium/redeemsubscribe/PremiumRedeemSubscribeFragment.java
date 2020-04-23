package com.example.oud.user.fragments.premium.redeemsubscribe;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String TAG = PremiumRedeemSubscribeFragment.class.getSimpleName();

    public static final int NOT_ENOUGH_CREDIT_CODE = 400;
    public static final int INVALID_COUPON_CODE = 400;

    private String token;

    private ImageView mImageViewProfilePic;
    private TextView mTextViewUserName;
    private TextView mTextViewCredit;

    private EditText mEditTextCoupon;
    private Button mButtonRedeem;

    private TextView mTextViewPlan;
    private Button mButtonSubscribeExtend;
    
    /*private Toast enterValidCouponToast;
    private Toast youDontHaveEnoughCreditToast;*/


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

    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.clearTheDataThatHasThePotentialToBeChangedOutside();

        handleProfileData();
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
        mButtonRedeem.setOnClickListener(redeemClickListener);

        mTextViewPlan = view.findViewById(R.id.txt_plan);
        mButtonSubscribeExtend = view.findViewById(R.id.btn_subscribe_extend);
        mButtonSubscribeExtend.setOnClickListener(subscribeExtendClickListener);
    }
    
    private OudUtils.ServerFailureResponseListener redeemCouponFailureResponseListener = (code, statusMessageResponse) -> {
        if (code == INVALID_COUPON_CODE) {
            if (statusMessageResponse.getMessage().equals("Coupon is already used."))
                forceToast("Coupon is already used.", Toast.LENGTH_LONG);
            else
                forceToast(R.string.please_enter_a_valid_coupon, Toast.LENGTH_LONG);
            //showEnterValidCouponToast();
        }

        //Log.e(TAG, "error message : " + message);
    };

    private OudUtils.ServerFailureResponseListener subscribeExtendFailureResponseListener = (code, statusMessageResponse) -> {
        if (code == NOT_ENOUGH_CREDIT_CODE)
            forceToast(R.string.you_dont_have_enough_credit, Toast.LENGTH_LONG);
            //showYouDontHaveEnoughCreditToast();
        else
            forceToast(statusMessageResponse.getMessage(), Toast.LENGTH_LONG);
    };

    private View.OnClickListener redeemClickListener = v -> {
        String coupon = mEditTextCoupon.getText().toString();

        if (coupon.isEmpty()) {
            forceToast(R.string.please_enter_a_valid_coupon, Toast.LENGTH_LONG);
            //showEnterValidCouponToast();
            return;
        }

        blockUiAndWait();

        mViewModel.redeemCoupon(token, coupon, redeemCouponFailureResponseListener);
    };

    private View.OnClickListener subscribeExtendClickListener = v -> {
        blockUiAndWait();

        mViewModel.subscribeToPremiumOrExtendCurrentPlan(token, subscribeExtendFailureResponseListener);
    };

    private void handleProfileData() {
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
                    .concat(" " + profile.getCredit());
            mTextViewCredit.setText(credit);


            if (profile.getRole().equals(Constants.API_PREMIUM)) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String plan = resources.getString(R.string.end_date);
                if (profile.getPlan() != null)
                    plan = plan
                            .concat(" ")
                            .concat(dateFormat.format(profile.getPlan()));
                mTextViewPlan.setText(plan);
                mButtonSubscribeExtend.setText(resources.getString(R.string.extend));
            } else
                mTextViewPlan.setText(resources.getString(R.string.free_plan));
        });
    }
    
    /*private void showEnterValidCouponToast() {
        if (enterValidCouponToast == null)
            enterValidCouponToast = Toast.makeText(getContext(), R.string.please_enter_a_valid_coupon, Toast.LENGTH_LONG);

        if (youDontHaveEnoughCreditToast != null)
            youDontHaveEnoughCreditToast.cancel();
        
        enterValidCouponToast.show();
    }

    private void showYouDontHaveEnoughCreditToast() {
        if (youDontHaveEnoughCreditToast == null)
            youDontHaveEnoughCreditToast = Toast.makeText(getContext(), R.string.you_dont_have_enough_credit, Toast.LENGTH_LONG);

        if (enterValidCouponToast != null)
            enterValidCouponToast.cancel();

        youDontHaveEnoughCreditToast.show();
    }*/

}
