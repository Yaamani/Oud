package com.example.oud.user.fragments.premium;



import com.example.oud.user.fragments.premium.downloading.PremiumDownloadingFragment;
import com.example.oud.user.fragments.premium.offlinetracks.PremiumOfflineTracksFragment;
import com.example.oud.user.fragments.premium.redeemsubscribe.PremiumRedeemSubscribeFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PremiumFragmentAdapter extends FragmentStateAdapter {

    private PremiumRedeemSubscribeFragment redeemSubscribeFragment;
    private PremiumOfflineTracksFragment offlineTracksFragment;
    private PremiumDownloadingFragment downloadingFragment;

    public PremiumFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);

        redeemSubscribeFragment = new PremiumRedeemSubscribeFragment();
        offlineTracksFragment = new PremiumOfflineTracksFragment();
        downloadingFragment = new PremiumDownloadingFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0: return redeemSubscribeFragment;
            case 1: return offlineTracksFragment;
            case 2: return downloadingFragment;
        }



        return null;
    }

    public PremiumRedeemSubscribeFragment getRedeemSubscribeFragment() {
        return redeemSubscribeFragment;
    }

    public PremiumOfflineTracksFragment getOfflineTracksFragment() {
        return offlineTracksFragment;
    }

    public PremiumDownloadingFragment getDownloadingFragment() {
        return downloadingFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
