package com.example.oud.user.fragments.library.savedalbums;

import android.os.Bundle;
import android.view.View;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.GenericVerticalRecyclerViewAdapter;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.R;
import com.example.oud.api.SavedAlbum;
import com.example.oud.api.SavedAlbum;
import com.example.oud.user.fragments.library.LibrarySubFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


public class LibrarySavedAlbumsFragment extends LibrarySubFragment<SavedAlbum, LibrarySavedAlbumsRepository, LibrarySavedAlbumsViewModel> {

    public LibrarySavedAlbumsFragment() {
        // Required empty public constructor
        super(LibrarySavedAlbumsViewModel.class,
                R.layout.fragment_library_saved_albums,
                R.id.progress_library_saved_albums,
                R.id.view_block_ui_input_library_saved_albums,
                null,
                R.id.recycler_view_library_saved_albums,
                R.id.txt_no_albums);
    }


    @Override
    protected void observerLoadedItems() {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        int i = 0;
        for (MutableLiveData<SavedAlbum> itemLiveData : mViewModel.getLoadedItems()) {
            int _i = i;
            itemLiveData.observe(getViewLifecycleOwner(), savedAlbum -> {

                if (mItemsAdapter != null) {
                    if (mItemsAdapter.getItemCount()-1 >= _i) { // Items already loaded
                        /*if (mAlbumsAdapter.getRelatedInfo().get(_i).get(Constants.ID_KEY).equals(likedTrack.get_id())) {*/
                        return;
                    } else {

                        GenericVerticalRecyclerViewAdapter adapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();

                        adapter.addItem(savedAlbum.getAlbum().get_id(),
                                savedAlbum.getAlbum().getImage(),
                                false,
                                savedAlbum.getAlbum().getName(),
                                true);

                        mItemsAdapter.notifyItemInserted(_i);
                    }


                } else {

                    ids.add(savedAlbum.getAlbum().get_id());
                    images.add(savedAlbum.getAlbum().getImage());
                    names.add(savedAlbum.getAlbum().getName());

                }
            });

            i++;
        }

        if (mItemsAdapter == null) {

            GenericVerticalRecyclerViewAdapter itemAdapter = new GenericVerticalRecyclerViewAdapter(
                    getContext(),
                    R.drawable.ic_star,
                    itemClickListener,
                    imageButtonClickListener);

            for (int j = 0; j < ids.size(); j++) {
                itemAdapter.addItem(ids.get(j), images.get(j), false, names.get(j), true);
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
        talkToPlayer.configurePlayer(itemAdapter.getId(position), true);
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
                        false,
                        removedItemName,
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
