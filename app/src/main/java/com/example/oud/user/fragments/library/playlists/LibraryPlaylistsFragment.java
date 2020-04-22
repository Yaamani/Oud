package com.example.oud.user.fragments.library.playlists;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.GenericVerticalRecyclerViewAdapter;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Playlist;
import com.example.oud.user.fragments.library.LibrarySubFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;


public class LibraryPlaylistsFragment extends LibrarySubFragment<Playlist, LibraryPlaylistsRepository, LibraryPlaylistsViewModel> {

    private static final String TAG = LibraryPlaylistsFragment.class.getSimpleName();


    private Button mButtonCreatePlaylist;

    public LibraryPlaylistsFragment() {
        // Required empty public constructor
        super(LibraryPlaylistsViewModel.class,
                R.layout.fragment_library_playlists,
                R.id.progress_library_playlists,
                R.id.view_block_ui_input_library_playlists,
                null,
                R.id.recycler_view_library_playlists,
                R.id.txt_no_playlists);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mButtonCreatePlaylist = view.findViewById(R.id.btn_create_playlist);
        mButtonCreatePlaylist.setOnClickListener(v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;
            mViewModel.createPlaylist(token, loggedInUserId, playlistCreationListener);
        });
    }

    private LibraryPlaylistsRepository.PlaylistCreationListener playlistCreationListener = new LibraryPlaylistsRepository.PlaylistCreationListener() {
        @Override
        public void onSuccessfulCreation(Playlist playlist) {
            PlaylistFragment.show(getActivity(),
                    R.id.nav_host_fragment,
                    loggedInUserId,
                    Constants.PlaylistFragmentType.PLAYLIST,
                    playlist.getId());
        }

        @Override
        public void onCreationFailure(PlaylistCreationFailureState playlistCreationFailureState) {
            Log.e(TAG, "onCreationFailure: " + playlistCreationFailureState);
            Toast.makeText(getContext(), "Error!!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void observerLoadedItems() {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        int i = 0;
        for (MutableLiveData<Playlist> itemLiveData : mViewModel.getLoadedItems()) {
            int _i = i;
            itemLiveData.observe(getViewLifecycleOwner(), playlist -> {

                if (mItemsAdapter != null) {
                    GenericVerticalRecyclerViewAdapter adapter = (GenericVerticalRecyclerViewAdapter) mItemsAdapter.getAdapter();

                    if (mItemsAdapter.getItemCount()-1 >= _i) { // Items already loaded
                        /*if (mAlbumsAdapter.getRelatedInfo().get(_i).get(Constants.ID_KEY).equals(likedTrack.get_id())) {*/
                        adapter.setItem(_i,
                                playlist.getId(),
                                playlist.getImage(),
                                false, playlist.getName(),
                                true);

                        mItemsAdapter.notifyItemChanged(_i);

                    } else {


                        adapter.addItem(playlist.getId(), 
                                playlist.getImage(),
                                false, playlist.getName(),
                                true);

                        mItemsAdapter.notifyItemInserted(_i);
                    }


                } else {

                    ids.add(playlist.getId());
                    images.add(playlist.getImage());
                    names.add(playlist.getName());

                }
            });

            i++;
        }

        if (mItemsAdapter == null) {

            GenericVerticalRecyclerViewAdapter itemAdapter = new GenericVerticalRecyclerViewAdapter(
                    getContext(),
                    R.drawable.ic_follow_playlist,
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
        // talkToPlayer.configurePlayer(itemAdapter.getId(position), true);
        PlaylistFragment.show(getActivity(),
                R.id.nav_host_fragment,
                loggedInUserId,
                Constants.PlaylistFragmentType.PLAYLIST,
                itemAdapter.getId(position));
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
