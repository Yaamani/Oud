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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.OudApi;
import com.example.oud.api.RecentlyPlayedTrack;
import com.example.oud.api.RecentlyPlayedTracks;
import com.example.oud.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.nestedrecyclerview.adapters.VerticalRecyclerViewAdapter;
import com.example.oud.nestedrecyclerview.decorations.VerticalSpaceDecoration;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
        /*for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            new Section(getContext(), this, homeViewModel.getCategoryLiveData(i));
        }*/

        /*OkHttpClient client = new OkHttpClient();
        String base = Constants.YAMANI_MOCK_BASE_URL;
        Request request = new Request.Builder().url(base + "/me/player/recently-played").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful())
                    Log.i(TAG, "onResponse: " + response.body().string());
                else
                    Log.e(TAG, "onResponse: " + "Fail");

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();

                Log.e(TAG, "onFailure: " + "Fail");
            }
        });*/

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.YAMANI_MOCK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OudApi oudApi = retrofit.create(OudApi.class);
        retrofit2.Call<RecentlyPlayedTracks> recentlyPlayedTracksCall =
                oudApi.recentlyPlayedTracks(Constants.USER_HOME_CATEGORIES_COUNT, null, null);
        recentlyPlayedTracksCall.enqueue(new Callback<RecentlyPlayedTracks>() {
            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + "Fail, code = " + response.code());
                    return;
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();
                Log.i(TAG, "onResponse: " + recentlyPlayedTracks.getItems()[0].getPlayedAt());
            }

            @Override
            public void onFailure(Call<RecentlyPlayedTracks> call, Throwable t) {
                t.printStackTrace();

                Log.e(TAG, "onFailure: " + "Fail");
            }
        });*/

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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

                liveData.getInnerItems()[i].getImage().observe(lifecycleOwner, s -> {
                    if (mInnerImages == null)
                        mInnerImages = new ArrayList<>(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT);

                    boolean success = false;
                    while (!success) {
                        try {
                            mInnerImages.set(final_i, s);
                            success = true;
                        } catch (IndexOutOfBoundsException e) {
                            mInnerImages.add(null);
                            //mInnerImages.set(final_i, s);
                        }
                    }

                    handleHorizontalRecyclerViewData();

                    handleVerticalRecyclerViewData();
                });

                liveData.getInnerItems()[i].getTitle().observe(lifecycleOwner, s -> {
                    if (mInnerTitles == null)
                        mInnerTitles = new ArrayList<>(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT);

                    boolean success = false;
                    while (!success) {
                        try {
                            mInnerTitles.set(final_i, s);
                            success = true;
                        } catch (IndexOutOfBoundsException e) {
                            mInnerTitles.add(null);
                            //mInnerImages.set(final_i, s);
                        }
                    }
                    //mInnerTitles.set(final_i, s);

                    handleHorizontalRecyclerViewData();

                    handleVerticalRecyclerViewData();
                });

                liveData.getInnerItems()[i].getSubTitle().observe(lifecycleOwner, s -> {
                    if (mInnerSubTitles == null)
                        mInnerSubTitles = new ArrayList<>(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT);

                    boolean success = false;
                    while (!success) {
                        try {
                            mInnerSubTitles.set(final_i, s);
                            success = true;
                        } catch (IndexOutOfBoundsException e) {
                            mInnerSubTitles.add(null);
                            //mInnerImages.set(final_i, s);
                        }
                    }
                    //mInnerSubTitles.set(final_i, s);

                    handleHorizontalRecyclerViewData();

                    handleVerticalRecyclerViewData();
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
