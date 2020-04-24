package com.example.oud.user.fragments.premium;



import com.example.oud.user.fragments.premium.offlinetracks.PremiumOfflineTracksFragment;
import com.example.oud.user.fragments.premium.redeemsubscribe.PremiumRedeemSubscribeFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PremiumFragmentAdapter extends FragmentStateAdapter {

    private PremiumRedeemSubscribeFragment redeemSubscribeFragment;
    private PremiumOfflineTracksFragment offlineTracksFragment;

    public PremiumFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);

        redeemSubscribeFragment = new PremiumRedeemSubscribeFragment();
        offlineTracksFragment = new PremiumOfflineTracksFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0: return redeemSubscribeFragment;
            case 1: return offlineTracksFragment;
        }



        return null;
    }

    public PremiumRedeemSubscribeFragment getRedeemSubscribeFragment() {
        return redeemSubscribeFragment;
    }

    public PremiumOfflineTracksFragment getOfflineTracksFragment() {
        return offlineTracksFragment;
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
