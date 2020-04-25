package com.example.oud.user.fragments.premium;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.huxq17.download.Pump;
import com.huxq17.download.core.DownloadInfo;
import com.huxq17.download.core.DownloadInfoManager;
import com.huxq17.download.core.DownloadListener;

import java.io.File;

public class PremiumFragment extends Fragment implements ReconnectingListener {

    private static final String TAG = PremiumFragment.class.getSimpleName();

    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    private PremiumFragmentAdapter premiumFragmentAdapter;

    public static PremiumFragment newInstance() {
        return new PremiumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_premium, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: Download list: " + Pump.getAllDownloadList());

        for (DownloadInfo downloadInfo : Pump.getAllDownloadList()) {
            Log.d(TAG, "onViewCreated: Download status for " + downloadInfo.getId() +
                    " is " + downloadInfo.getStatus() +
                    ", Error code = " + downloadInfo.getErrorCode());
            Pump.deleteById(downloadInfo.getId());
        }

        mViewPager2 = view.findViewById(R.id.view_pager2_premium);
        premiumFragmentAdapter = new PremiumFragmentAdapter(this);
        mViewPager2.setAdapter(premiumFragmentAdapter);

        mTabLayout = view.findViewById(R.id.tab_layout_premium);
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Subscribe"); break;
                case 1: tab.setText("Offline tracks"); break;

            }
        }).attach();

    }

    @Override
    public void onTryingToReconnect() {
        //premiumFragmentAdapter.getRedeemSubscribeFragment().onT
        premiumFragmentAdapter.getRedeemSubscribeFragment().onTryingToReconnect();
        //premiumFragmentAdapter.getOfflineTracksFragment()
    }
}
