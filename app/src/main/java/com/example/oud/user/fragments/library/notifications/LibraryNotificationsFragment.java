package com.example.oud.user.fragments.library.notifications;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.GenericVerticalRecyclerViewAdapter;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.R;
import com.example.oud.api.Notification;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.fragments.library.LibrarySubFragment;
import com.example.oud.user.fragments.library.artists.LibraryArtistsViewModel;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class LibraryNotificationsFragment extends LibrarySubFragment<Notification, LibraryNotificationsRepository, LibraryNotificationsViewModel> {

    public LibraryNotificationsFragment() {
        super(LibraryNotificationsViewModel.class,
                R.layout.fragment_library_notifications,
                R.id.progress_library_notifications,
                R.id.view_block_ui_input_library_notifications,
                null,
                R.id.recycler_view_library_notifications,
                R.id.txt_no_notifications);
    }


    @Override
    protected void observerLoadedItems() {
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> uris = new ArrayList<>();


        int i = 0;
        for (MutableLiveData<Notification> itemLiveDate : mViewModel.getLoadedItems()) {
            int _i = i;
            itemLiveDate.observe(getViewLifecycleOwner(), notification -> {


                if (mItemsAdapter != null) {

                    GenericVerticalRecyclerViewAdapter adapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();

                    if (mItemsAdapter.getItemCount()-1 >= _i) { // Items already loaded

                        adapter.setItem(_i,
                                notification.get_id(),
                                notification.getImage(),
                                true,
                                notification.getTitle(),
                                false);

                        mItemsAdapter.notifyItemChanged(_i);
                    } else {

                        adapter.addItem(notification.get_id(),
                                notification.getImage(),
                                true,
                                notification.getTitle(),
                                false);

                        mItemsAdapter.notifyItemInserted(_i);
                    }

                } else {

                    uris.add(notification.get_id());
                    images.add(notification.getImage());
                    titles.add(notification.getTitle());
                }
            });

            i++;

        }

        if (mItemsAdapter == null) {

            GenericVerticalRecyclerViewAdapter itemAdapter = new GenericVerticalRecyclerViewAdapter(
                    getContext(),
                    R.drawable.ic_delete,
                    itemClickListener,
                    imageButtonClickListener);


            for (int j = 0; j < uris.size(); j++) {
                itemAdapter.addItem(uris.get(j), images.get(j), true, titles.get(j), false);
            }


            mItemsAdapter = new LoadMoreAdapter(mRecyclerViewItems,
                    itemAdapter,
                    GenericVerticalRecyclerViewAdapter.GenericVerticalAdapterViewHolder.class,
                    itemAdapter.getIds(),
                    R.layout.progressbar_item_vertical);

            //mRecyclerViewAlbums.setAdapter(mAlbumsAdapter);
            mItemsAdapter.setOnLoadMoreListener(() -> {
                //images.add(null);
                loadMoreItems();
            });

        }

        if (mRecyclerViewItems.getAdapter() == null) {
            mItemsAdapter.setRecyclerView(mRecyclerViewItems);
        }
    }


    private GenericVerticalRecyclerViewAdapter.OnItemClickListener itemClickListener = (position, view) -> {
        GenericVerticalRecyclerViewAdapter itemAdapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();
        // talkToPlayer.configurePlayer(itemAdapter.getId(position), true);

        String split[] = itemAdapter.getId(position).split(":");
        String type = split[0];
        String typeId = split[1];

        if (type.equals(Constants.API_ALBUM)) {

            PlaylistFragment.show(getActivity(), R.id.nav_host_fragment, loggedInUserId, Constants.PlaylistFragmentType.ALBUM, typeId);
        }
    };


    private GenericVerticalRecyclerViewAdapter.OnItemClickListener imageButtonClickListener = (position, view) -> {

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
                GenericVerticalRecyclerViewAdapter itemAdapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();

                itemAdapter.addItem(position,
                        removedItemId,
                        removedItemImage,
                        false, removedItemName,
                        true);

                mItemsAdapter.notifyItemInserted(position);
            }
        };

        GenericVerticalRecyclerViewAdapter itemAdapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();
        String id = itemAdapter.getId(position);
        mViewModel.removeItem(token, id, position, undoUiAndUpdateLiveData);

        removedItemId = itemAdapter.getId(position);
        removedItemName = itemAdapter.getTitle(position);
        removedItemImage = itemAdapter.getImage(position);

        itemAdapter.removeItem(position);

        mItemsAdapter.notifyItemRemoved(position);

        blockUiAndWait();
    };


}
