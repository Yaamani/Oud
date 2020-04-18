package com.example.oud.user.fragments.library;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LibraryFragment extends Fragment {

    private static final String TAG = LibraryFragment.class.getSimpleName();

    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager2 = view.findViewById(R.id.view_pager2_library);
        mViewPager2.setAdapter(new LibraryFragmentAdapter(this));

        mTabLayout = view.findViewById(R.id.tab_layout_library);
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Liked Tracks"); break;
                case 1: tab.setText("Playlists"); break;
                case 2: tab.setText("Followed Artists"); break;
                case 3: tab.setText("Saved Albums"); break;
            }
        }).attach();
    }



}
