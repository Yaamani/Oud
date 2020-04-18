package com.example.oud.user.fragments.library;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LibraryFragment extends Fragment implements ReconnectingListener {

    private static final String TAG = LibraryFragment.class.getSimpleName();

    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;

    private LibraryFragmentAdapter libraryFragmentAdapter;

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
        //if (libraryFragmentAdapter == null)
            libraryFragmentAdapter = new LibraryFragmentAdapter(this);
        mViewPager2.setAdapter(libraryFragmentAdapter);

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //libraryFragmentAdapter = new LibraryFragmentAdapter(this);
    }



    @Override
    public void onTryingToReconnect() {
        /*int currentTab = mTabLayout.getSelectedTabPosition();
        switch (currentTab) {
            case 0:
                libraryFragmentAdapter.getLibraryLikedTracksFragment().onTryingToReconnect();
                break;
            case 1:
                libraryFragmentAdapter.getLibraryPlaylistsFragment().onTryingToReconnect();
                break;
            case 2:  libraryFragmentAdapter.getLibraryArtistsFragment().onTryingToReconnect();
                break;
            case 3:  libraryFragmentAdapter.getLibrarySavedAlbumsFragment().onTryingToReconnect();
                break;
        }*/

        libraryFragmentAdapter.getLibraryLikedTracksFragment().onTryingToReconnect();
        libraryFragmentAdapter.getLibraryPlaylistsFragment().onTryingToReconnect();
        libraryFragmentAdapter.getLibraryArtistsFragment().onTryingToReconnect();
        libraryFragmentAdapter.getLibrarySavedAlbumsFragment().onTryingToReconnect();
    }
}
