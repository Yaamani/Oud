package com.example.oud.user.fragments.library.likedtracks;

import androidx.lifecycle.MutableLiveData;

import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.LikedTrack;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.user.TrackListRecyclerViewAdapter;
import com.example.oud.user.fragments.library.LibrarySubFragment;

import java.util.ArrayList;


public class LibraryLikedTracksFragment extends LibrarySubFragment<LikedTrack, LibraryLikedTracksRepository, LibraryLikedTracksViewModel> {
    

    public LibraryLikedTracksFragment() {
        // Required empty public constructor
        super(LibraryLikedTracksViewModel.class,
                R.layout.fragment_library_liked_tracks,
                R.id.progress_library_liked_tracks,
                R.id.view_block_ui_input_library_liked_tracks,
                null, 
                R.id.recycler_view_library_liked_tracks, 
                R.id.txt_no_liked_tracks);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mItemsAdapter != null)
            ((TrackListRecyclerViewAdapter) mItemsAdapter.getAdapter()).disableDownloadListener();
    }

    @Override
    protected void observerLoadedItems() {

        ArrayList<String> trackIds = new ArrayList<>();
        ArrayList<String> trackImages = new ArrayList<>();
        ArrayList<String> trackNames = new ArrayList<>();
        ArrayList<Boolean> likedTracks = new ArrayList<>();


        int i = 0;
        for (MutableLiveData<LikedTrack> trackLiveData : mViewModel.getLoadedItems()) {
            int _i = i;
            trackLiveData.observe(getViewLifecycleOwner(), likedTrack -> {

                if (mItemsAdapter != null) {
                    TrackListRecyclerViewAdapter trackAdapter = (TrackListRecyclerViewAdapter) mItemsAdapter.getAdapter();

                    if (mItemsAdapter.getItemCount()-1 >= _i) { // Tracks already loaded
                        /*if (mAlbumsAdapter.getRelatedInfo().get(_i).get(Constants.ID_KEY).equals(likedTrack.get_id())) {*/

                        trackAdapter.setTrack(_i, likedTrack.getTrack().get_id(),
                                likedTrack.getTrack().getAlbum().getImage(),
                                likedTrack.getTrack().getName(),
                                true);

                        mItemsAdapter.notifyItemChanged(_i);

                    } else {

                        trackAdapter.addTrack(likedTrack.getTrack().get_id(),
                                likedTrack.getTrack().getAlbum().getImage(),
                                likedTrack.getTrack().getName(),
                                true);

                        mItemsAdapter.notifyItemInserted(_i);
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

        if (mItemsAdapter == null) {

            TrackListRecyclerViewAdapter trackAdapter = new TrackListRecyclerViewAdapter(
                    getContext(),
                    mRecyclerViewItems,
                    mViewModel.getRepoBaseUrl(),
                    loggedInUserId,
                    trackClickListener, availableOfflineClickListener, heartClickListener, false);


            for (int j = 0; j < trackIds.size(); j++) {
                trackAdapter.addTrack(trackIds.get(j),
                        trackImages.get(j),
                        trackNames.get(j),
                        likedTracks.get(j));
            }


            mItemsAdapter = new LoadMoreAdapter(mRecyclerViewItems,
                    trackAdapter,
                    TrackListRecyclerViewAdapter.TrackItemViewHolder.class,
                    trackAdapter.getmIds(),
                    R.layout.progressbar_item_vertical);

            //mRecyclerViewAlbums.setAdapter(mAlbumsAdapter);
            mItemsAdapter.setOnLoadMoreListener(() -> {
                //images.add(null);
                loadMoreItems();
            });
        }




        if (mRecyclerViewItems.getAdapter() == null) {
            mItemsAdapter.setRecyclerView(mRecyclerViewItems);
            mItemsAdapter.notifyDataSetChanged();
        }
    }

    private TrackListRecyclerViewAdapter.OnTrackClickListener trackClickListener = (position, view) -> {
        TrackListRecyclerViewAdapter trackAdapter = (TrackListRecyclerViewAdapter) mItemsAdapter.getAdapter();
        /*talkToPlayer.configurePlayer(trackAdapter.getId(position), true);*/
    };

    private TrackListRecyclerViewAdapter.OnTrackClickListener availableOfflineClickListener = (position, view) -> {
        Toast.makeText(getContext(), "track offline !!", Toast.LENGTH_SHORT).show();
    };

    private TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListener = (position, view) -> {

        // likedTrackToBeRemovedPosition = position;

        if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
            return;

        ConnectionStatusListener undoUiAndUpdateLiveData = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                mViewModel.updateLiveDataUponRemovingItemFromLikedItems();
            }

            @Override
            public void onConnectionFailure() {
                // Undo ui
                TrackListRecyclerViewAdapter adapter = (TrackListRecyclerViewAdapter) mItemsAdapter.getAdapter();

                /*adapter.getmIds().add(position, removedItemId);
                adapter.getTrackNames().add(position, removedItemName);
                adapter.getTrackImages().add(position, removedItemImage);
                adapter.getLikedTracks().add(position, true);*/

                adapter.addTrack(removedItemId,
                        removedItemName,
                        removedItemImage,
                        true);

                mItemsAdapter.notifyItemInserted(position);
            }
        };

        TrackListRecyclerViewAdapter adapter = (TrackListRecyclerViewAdapter) mItemsAdapter.getAdapter();
        String id = adapter.getId(position);
        mViewModel.removeItem(token, id, position, undoUiAndUpdateLiveData);

        removedItemId = adapter.getId(position);
        removedItemName = adapter.getName(position);
        removedItemImage = adapter.getImage(position);
        // adapter.getLikedTracks().remove(position);

        adapter.removeTrack(position);

        mItemsAdapter.notifyItemRemoved(position);

        blockUiAndWait();

    };

}
