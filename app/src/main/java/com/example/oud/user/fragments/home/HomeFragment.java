package com.example.oud.user.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.nestedrecyclerview.adapters.VerticalRecyclerViewAdapter;
import com.example.oud.nestedrecyclerview.decorations.VerticalSpaceDecoration;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private ArrayList<String> mIcons;
    private ArrayList<String> mTitles;
    private ArrayList<HorizontalRecyclerViewAdapter> mInnerItemAdapters;

    private VerticalRecyclerViewAdapter mVerticalAdapter;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        new Section(getContext(), this, homeViewModel.getRecentlyPlayedLiveData());
        for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            new Section(getContext(), this, homeViewModel.getCategoryLiveData(i));
        }

        //handleVerticalRecyclerViewData();
        //initializeVerticalRecyclerView();


        Log.i(TAG, "onCreateView: ");        

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: ");

        handleVerticalRecyclerViewData();
        //initializeVerticalRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
    }

    private void handleVerticalRecyclerViewData() {
        if (mIcons != null & mTitles != null & mInnerItemAdapters != null) {

            if ((mIcons.size() == mTitles.size())
                    & (mTitles.size() == mInnerItemAdapters.size())) {

                if (mVerticalAdapter == null) {
                    initializeVerticalRecyclerView();
                } else updateVerticalRecyclerView();
            }
        }
    }


    private void initializeVerticalRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.addItemDecoration(new VerticalSpaceDecoration(getResources(), R.dimen.item_margin));


        mVerticalAdapter = new VerticalRecyclerViewAdapter(
                this.getContext(), mIcons, mTitles, mInnerItemAdapters, Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT);
        recyclerView.setAdapter(mVerticalAdapter);
    }

    private void updateVerticalRecyclerView() {
        RecyclerView recyclerView  = getView().findViewById(R.id.recycler_view_home);
        if (recyclerView.getAdapter() == null)
            initializeVerticalRecyclerView();

        mVerticalAdapter.notifyDataSetChanged();
    }






    public class Section {
        private Context mContext;

        private String mIcon;
        private String mTitle;
        private ArrayList<String> mInnerImages;
        private ArrayList<String> mInnerTitles;
        private ArrayList<String> mInnerSubTitles;

        private HorizontalRecyclerViewAdapter mAdapter;

        public Section(Context mContext,
                    LifecycleOwner lifecycleOwner,
                    final HomeViewModel.OuterItemLiveData liveData) {

            this.mContext = mContext;

            liveData.getIcon().observe(lifecycleOwner, s -> {

                mIcon = liveData.getIcon().getValue();
                if (mIcons == null)
                    mIcons = new ArrayList<>();
                //mLogos.contains()
                mIcons.add(mIcon);

                handleVerticalRecyclerViewData();
            });

            liveData.getTitle().observe(lifecycleOwner, new Observer<String>() {
                @Override
                public void onChanged(String integer) {
                    mTitle = liveData.getTitle().getValue();

                    if (mTitles == null)
                        mTitles = new ArrayList<>();
                    mTitles.add(mTitle);

                    handleVerticalRecyclerViewData();
                }
            });

            for (int i = 0; i < liveData.getInnerItems().length; i++) {
                final int final_i = i;

                liveData.getInnerItems()[i].getImage().observe(lifecycleOwner, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (mInnerImages == null)
                            mInnerImages = new ArrayList<>();

                        mInnerImages.add(liveData.getInnerItems()[final_i].getImage().getValue());
                        //printChanges("Bitmaps");
                        handleHorizontalRecyclerViewData();

                        handleVerticalRecyclerViewData();
                    }
                });

                liveData.getInnerItems()[i].getTitle().observe(lifecycleOwner, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (mInnerTitles == null)
                            mInnerTitles = new ArrayList<>();

                        mInnerTitles.add(liveData.getInnerItems()[final_i].getTitle().getValue());
                        //printChanges("Bitmaps");
                        handleHorizontalRecyclerViewData();

                        handleVerticalRecyclerViewData();
                    }
                });

                liveData.getInnerItems()[i].getSubTitle().observe(lifecycleOwner, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (mInnerSubTitles == null)
                            mInnerSubTitles = new ArrayList<>();

                        mInnerSubTitles.add(liveData.getInnerItems()[final_i].getSubTitle().getValue());
                        //printChanges("Bitmaps");
                        handleHorizontalRecyclerViewData();

                        handleVerticalRecyclerViewData();
                    }
                });
            }
        }

        private void handleHorizontalRecyclerViewData() {

            if (mInnerImages != null & mInnerTitles != null & mInnerSubTitles != null) {

                if ((mInnerImages.size() == mInnerTitles.size())
                        & (mInnerTitles.size() == mInnerSubTitles.size())) {

                    if (mAdapter == null) {
                        /*initializeRecyclerView();*/
                        mAdapter = new HorizontalRecyclerViewAdapter(mContext, mInnerImages, mInnerTitles, mInnerSubTitles);

                        if (mInnerItemAdapters == null)
                            mInnerItemAdapters = new ArrayList<>();
                        mInnerItemAdapters.add(mAdapter);
                    } else updateHorizontalRecyclerView();
                }
            }
        }

        private void updateHorizontalRecyclerView() {
            mAdapter.notifyDataSetChanged();
        }
    }
}
