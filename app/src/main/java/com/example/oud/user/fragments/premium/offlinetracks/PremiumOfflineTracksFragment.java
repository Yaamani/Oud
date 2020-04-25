package com.example.oud.user.fragments.premium.offlinetracks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.TrackListRecyclerViewAdapter;
import com.example.oud.user.fragments.premium.database.DownloadedTrack;
import com.example.oud.user.fragments.premium.database.DownloadedTracksDatabase;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class PremiumOfflineTracksFragment extends ConnectionAwareFragment<PremiumOfflineTracksViewModel> {

    private static final String TAG = PremiumOfflineTracksFragment.class.getSimpleName();

    private String loggedInUserId;
    private String token;

    private TextView mTextViewYouAreNotSubscribed;
    private TextView mTextViewYouHaveNoOfflineTracks;

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
        mTextViewYouHaveNoOfflineTracks = view.findViewById(R.id.txt_you_have_no_offline_tracks);
        mRecyclerViewOfflineTracks = view.findViewById(R.id.recycler_view_premium_offline_tracks);
        mRecyclerViewOfflineTracks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
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
            mTrackListRecyclerViewAdapter.preventLeaksBecauseOfDownloadService();
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
        /*DownloadedTracksDatabase downloadedTracksDatabase =
                Room.databaseBuilder(getContext(), DownloadedTracksDatabase.class, Constants.DOWNLOADED_TRACKS_DATABASE_NAME).build();*/

        mTrackListRecyclerViewAdapter = new TrackListRecyclerViewAdapter(getContext(),
                mRecyclerViewOfflineTracks,
                mViewModel.getRepoBaseUrl(),
                loggedInUserId,
                onTrackClickListener,
                null,
                null,
                true);

        new GetAllDownloadedTracks(getContext(), mTrackListRecyclerViewAdapter, mTextViewYouHaveNoOfflineTracks).execute();

        mRecyclerViewOfflineTracks.setAdapter(mTrackListRecyclerViewAdapter);
        // mTrackListRecyclerViewAdapter.notifyDataSetChanged();
    }

    private TrackListRecyclerViewAdapter.OnTrackClickListener onTrackClickListener = (position, view) -> {

    };

    private static class GetAllDownloadedTracks extends AsyncTask<Void, Void, List<DownloadedTrack>> {

        private WeakReference<Context> mContext;
        private WeakReference<TrackListRecyclerViewAdapter> mTrackListRecyclerViewAdapter;
        private WeakReference<TextView> mTextViewYouHaveNoOfflineTracks;

        public GetAllDownloadedTracks(Context context, TrackListRecyclerViewAdapter trackListRecyclerViewAdapter, TextView youHaveNoOfflineTracks) {
            this.mContext = new WeakReference<>(context);
            this.mTrackListRecyclerViewAdapter = new WeakReference<>(trackListRecyclerViewAdapter);
            this.mTextViewYouHaveNoOfflineTracks = new WeakReference<>(youHaveNoOfflineTracks);
        }

        @Override
        protected List<DownloadedTrack> doInBackground(Void... voids) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());
            return downloadedTracksDatabase.downloadedTrackDao().getAll();
        }

        @Override
        protected void onPostExecute(List<DownloadedTrack> downloadedTracks) {
            super.onPostExecute(downloadedTracks);

            LinkedList<DownloadedTrack> currentlyBeingDownloadedTracks = TrackListRecyclerViewAdapter.getCurrentlyBeingDownloadedTracks();
            /*File file;
            File parent;
            String[] files;


            if (!downloadedTracks.isEmpty()) {

                List<String> ids = new LinkedList<>();
                for (DownloadedTrack downloadedTrack : downloadedTracks) {
                    ids.add(downloadedTrack.id);
                }

                file = new File(downloadedTracks.get(0).fileName);
                parent = file.getParentFile();
                files = parent.list();
                Log.d(TAG, "onPostExecute: ids = " + Arrays.toString(ids.toArray()));
                //Log.d(TAG, "onPostExecute: parent = " + parent.getAbsolutePath());
                Log.d(TAG, "onPostExecute: files = " + Arrays.toString(files));
            }*/




            if (downloadedTracks.isEmpty() & currentlyBeingDownloadedTracks.isEmpty()) {
                mTextViewYouHaveNoOfflineTracks.get().setVisibility(View.VISIBLE);
                return;
            }

            for (DownloadedTrack downloadedTrack : downloadedTracks) {

                mTrackListRecyclerViewAdapter.get().addTrack(downloadedTrack.id,
                        downloadedTrack.image,
                        downloadedTrack.name,
                        false);

            }

            for (DownloadedTrack downloadedTrack : currentlyBeingDownloadedTracks) {
                mTrackListRecyclerViewAdapter.get().addTrack(downloadedTrack.id,
                        downloadedTrack.image,
                        downloadedTrack.name,
                        false);
            }


            mTrackListRecyclerViewAdapter.get().notifyDataSetChanged();
        }
    }
}
