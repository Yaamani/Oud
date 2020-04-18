package com.example.oud.user.fragments.library.likedtracks;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.LikedTrack;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.LoadMoreAdapter;
import com.example.oud.user.TrackListRecyclerViewAdapter;
import com.example.oud.user.player.PlayerInterface;

import java.util.ArrayList;


public class LibraryLikedTracksFragment extends ConnectionAwareFragment<LibraryLikedTracksViewModel> {

    private static final String TAG = LibraryLikedTracksFragment.class.getSimpleName();

    private String token;

    private RecyclerView mRecyclerViewLikedTracks;
    private LoadMoreAdapter mLikedTracksAdapter;

    private TextView mTextViewNoLikedTracks;

    private PlayerInterface talkToPlayer;

    private int likedTrackToBeMovedPosition;


    public LibraryLikedTracksFragment() {
        // Required empty public constructor
        super(LibraryLikedTracksViewModel.class,
                R.layout.fragment_library_liked_tracks,
                R.id.progress_library_liked_tracks,
                R.id.view_block_ui_input_library_liked_tracks,
                null);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PlayerInterface) {
            talkToPlayer = (PlayerInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement" + PlayerInterface.class.getSimpleName() + ".");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleToken();

        mViewModel.clearTheDataThatHasThePotentialToBeChangedOutside();

        initializeUiStuff(view);

        handleLikedTracks();
    }

    private void handleToken() {
        token = OudUtils.getToken(getContext());
    }

    private void initializeUiStuff(@NonNull View view) {
        mRecyclerViewLikedTracks = view.findViewById(R.id.recycler_view_library_liked_tracks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewLikedTracks.setLayoutManager(layoutManager);

        mTextViewNoLikedTracks = view.findViewById(R.id.txt_no_liked_tracks);
    }

    /**
     * Handles the data and the logic behind liked tracks.
     */
    private void handleLikedTracks() {

        if (mViewModel.getLoadedLikedTracks().size() < Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT)
            loadMoreTracks();
        else
            observerLoadedTracks();
    }

    /**
     * Observes loadedLikedTracks in {@link LibraryLikedTracksViewModel} and adds the newly loaded tracks to the loaded ones.
     */
    private void loadMoreTracks() {
        mViewModel.loadMoreTracks(token).observe(getViewLifecycleOwner(), likedTrackOudList -> {
            if (likedTrackOudList.getTotal() == 0 & mViewModel.getLoadedLikedTracks().size() == 0) {
                mRecyclerViewLikedTracks.setVisibility(View.GONE);
                mTextViewNoLikedTracks.setVisibility(View.VISIBLE);
            }

            if (mLikedTracksAdapter != null)
                mLikedTracksAdapter.setLoaded();

            for (LikedTrack likedTrack : likedTrackOudList.getItems()) {
                mViewModel.getLoadedLikedTracks().add(new MutableLiveData<>(likedTrack));
            }

            observerLoadedTracks();
        });
    }

    /**
     * Observes loadedLikedTracks in {@link LibraryLikedTracksViewModel}. and populates {@link #mLikedTracksAdapter}.
     */
    private void observerLoadedTracks() {

        ArrayList<String> trackIds = new ArrayList<>();
        ArrayList<String> trackImages = new ArrayList<>();
        ArrayList<String> trackNames = new ArrayList<>();
        ArrayList<Boolean> likedTracks = new ArrayList<>();


        int i = 0;
        for (MutableLiveData<LikedTrack> trackLiveData : mViewModel.getLoadedLikedTracks()) {
            int _i = i;
            trackLiveData.observe(getViewLifecycleOwner(), likedTrack -> {

                if (mLikedTracksAdapter != null) {
                    if (mLikedTracksAdapter.getItemCount()-1 >= _i) { // Tracks already loaded
                        /*if (mAlbumsAdapter.getRelatedInfo().get(_i).get(Constants.ID_KEY).equals(likedTrack.get_id())) {*/
                        return;
                    } else {

                        TrackListRecyclerViewAdapter trackAdapter = (TrackListRecyclerViewAdapter) mLikedTracksAdapter.getAdapter();

                        trackAdapter.getIds().add(likedTrack.getTrack().get_id());
                        trackAdapter.getTrackImages().add(likedTrack.getTrack().getAlbum().getImage());
                        trackAdapter.getTrackNames().add(likedTrack.getTrack().getName());
                        trackAdapter.getLikedTracks().add(true);

                        trackAdapter.notifyItemInserted(_i);
                    }


                } else {

                    trackIds.add(likedTrack.getTrack().get_id());
                    trackImages.add(likedTrack.getTrack().getAlbum().getImage());
                    trackNames.add(likedTrack.getTrack().getName());
                    likedTracks.add(true);

                }
            });

            i++;
        }

        if (mLikedTracksAdapter == null) {

            TrackListRecyclerViewAdapter trackAdapter = new TrackListRecyclerViewAdapter(
                    getContext(),
                    trackIds,
                    trackClickListener,
                    trackImages,
                    trackNames, 
                    likedTracks, 
                    availableOfflineClickListener, 
                    heartClickListener);

            mLikedTracksAdapter = new LoadMoreAdapter(mRecyclerViewLikedTracks,
                    trackAdapter,
                    TrackListRecyclerViewAdapter.TrackItemViewHolder.class,
                    trackAdapter.getIds(),
                    R.layout.progressbar_item_vertical);

            //mRecyclerViewAlbums.setAdapter(mAlbumsAdapter);
            mLikedTracksAdapter.setOnLoadMoreListener(() -> {
                //images.add(null);
                loadMoreTracks();
            });
        }


        if (mRecyclerViewLikedTracks.getAdapter() == null) {
            mLikedTracksAdapter.setRecyclerView(mRecyclerViewLikedTracks);
        }
    }

    TrackListRecyclerViewAdapter.OnTrackClickListener trackClickListener = (position, view) -> {
        TrackListRecyclerViewAdapter trackAdapter = (TrackListRecyclerViewAdapter) mLikedTracksAdapter.getAdapter();
        talkToPlayer.configurePlayer(trackAdapter.getIds().get(position), true);
    };

    TrackListRecyclerViewAdapter.OnTrackClickListener availableOfflineClickListener = (position, view) -> {
        Toast.makeText(getContext(), "track offline !!", Toast.LENGTH_SHORT).show();
    };

    private TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListener = (position, view) -> {
        Toast.makeText(getContext(), "track liked !!", Toast.LENGTH_SHORT).show();

        /*likedTrackToBeMovedPosition = position;

        if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
            return;

        TrackListRecyclerViewAdapter adapter = (TrackListRecyclerViewAdapter) mLikedTracksAdapter.getAdapter();
        String id = adapter.getIds().get(position);
        mViewModel.removeTrackFromLikedTracks(token, id, position);
        trackListRecyclerViewAdapter.getLikedTracks().set(position, false);
        trackListRecyclerViewAdapter.notifyItemChanged(position);


        blockUiAndWait();*/

    };
}
