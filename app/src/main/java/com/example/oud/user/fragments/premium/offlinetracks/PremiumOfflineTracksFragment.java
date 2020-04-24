package com.example.oud.user.fragments.premium.offlinetracks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.View;
import android.widget.TextView;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.TrackListRecyclerViewAdapter;
import com.example.oud.user.fragments.premium.database.DownloadedTrack;
import com.example.oud.user.fragments.premium.database.DownloadedTracksDatabase;
import com.huxq17.download.Pump;
import com.huxq17.download.core.DownloadInfo;

import java.util.List;

public class PremiumOfflineTracksFragment extends ConnectionAwareFragment<PremiumOfflineTracksViewModel> {

    private String loggedInUserId;
    private String token;

    private TextView mTextViewYouAreNotSubscribed;

    private RecyclerView mRecyclerViewOfflineTracks;
    private TrackListRecyclerViewAdapter mTrackListRecyclerViewAdapter;

    public PremiumOfflineTracksFragment() {
        // Required empty public constructor
        super(PremiumOfflineTracksViewModel.class,
                R.layout.fragment_premium_offline_tracks,
                R.id.progress,
                R.id.view_block_ui_input,
                null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loggedInUserId = OudUtils.getUserId(getContext());
        token = OudUtils.getToken(getContext());

        initializeUiStuff(view);
    }

    private void initializeUiStuff(View view) {
        mTextViewYouAreNotSubscribed = view.findViewById(R.id.txt_you_are_not_subscribed);
        mRecyclerViewOfflineTracks = view.findViewById(R.id.recycler_view_premium_offline_tracks);
    }

    @Override
    public void onResume() {
        super.onResume();

        mViewModel.clearTheDataThatHasThePotentialToBeChangedOutside();

        handleData();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mTrackListRecyclerViewAdapter != null)
            mTrackListRecyclerViewAdapter.disableDownloadListener();
    }

    private void handleData() {

        String userType = OudUtils.getUserTypeForPremiumFeature(getContext(), "");

        if (userType.equals(""))
            mViewModel.getProfileLiveData(token).observe(getViewLifecycleOwner(),
                    profile -> handlePremiumFree(profile.getRole()));
        else {
            unBlockUi();
            handlePremiumFree(userType);
        }

    }

    private void handlePremiumFree(String userType) {
        if (userType.equals(Constants.API_PREMIUM))
            populateRecyclerView();
        else {
            mRecyclerViewOfflineTracks.setVisibility(View.GONE);
            mTextViewYouAreNotSubscribed.setVisibility(View.VISIBLE);
        }
    }

    private void populateRecyclerView() {
        DownloadedTracksDatabase downloadedTracksDatabase =
                Room.databaseBuilder(getContext(), DownloadedTracksDatabase.class, Constants.DOWNLOADED_TRACKS_DATABASE_NAME).build();

        mTrackListRecyclerViewAdapter = new TrackListRecyclerViewAdapter(getContext(),
                mRecyclerViewOfflineTracks,
                mViewModel.getRepoBaseUrl(),
                loggedInUserId,
                onTrackClickListener,
                null,
                null,
                true);

        List<DownloadInfo> downloadInfoList = Pump.getDownloadListByTag(loggedInUserId);

        for (DownloadInfo downloadInfo : downloadInfoList) {
            DownloadInfo.Status status = downloadInfo.getStatus();
            if (status.shouldStop() | status == DownloadInfo.Status.FINISHED) {
                String id = downloadInfo.getId();
                DownloadedTrack downloadedTrack = downloadedTracksDatabase.downloadedTrackDao().getDownloadedTrack(id);

                mTrackListRecyclerViewAdapter.addTrack(id,
                        downloadedTrack.image,
                        downloadedTrack.name,
                        false);
            }
        }

        mRecyclerViewOfflineTracks.setAdapter(mTrackListRecyclerViewAdapter);
        mTrackListRecyclerViewAdapter.notifyDataSetChanged();
    }

    private TrackListRecyclerViewAdapter.OnTrackClickListener onTrackClickListener = (position, view) -> {

    };
}
