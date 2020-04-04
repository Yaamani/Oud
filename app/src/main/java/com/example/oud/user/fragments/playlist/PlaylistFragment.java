package com.example.oud.user.fragments.playlist;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.Constants;
import com.example.oud.OptionsFragment;
import com.example.oud.R;
import com.example.oud.api.OudList;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.RenameFragment;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class PlaylistFragment extends ConnectionAwareFragment<PlaylistViewModel> {

    private static final String TAG = PlaylistFragment.class.getSimpleName();

    private String userId;
    private Constants.PlaylistFragmentType type;
    private String playlistOrAlbumId;

    private MotionLayout mMotionLayout;

    private RecyclerView mRecyclerViewTracks;
    private PlaylistRecyclerViewAdapter adapter;
    private ItemTouchHelper touchHelper;

    private ImageView mImageViewPlaylist;
    private TextView mTextViewPlaylistName;
    private ImageButton mImageButtonRename;
    private ImageButton mImageButtonOptions;






    private int positionBeforeDeletion;
    private String trackImageBeforeDeletion;
    private String trackNameBeforeDeletion;

    private String playlistNameBeforeRenaming;

    private int reorderingFromPosition;
    private int reorderingToPosition;




    public PlaylistFragment() {
        super(PlaylistViewModel.class,
                R.layout.fragment_playlist,
                R.id.progress_playlist,
                null);
    }

    public static Bundle myArgs(String userId, Constants.PlaylistFragmentType type, String playlistOrAlbumId) {
        Bundle args = new Bundle();
        args.putString(Constants.USER_ID_KEY, userId);
        args.putSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY, type);
        args.putString(Constants.ID_KEY, playlistOrAlbumId);
        return args;
    }

    public static PlaylistFragment newInstance(String userId, Constants.PlaylistFragmentType type, String playlistOrAlbumId) {
        PlaylistFragment playlistFragment = new PlaylistFragment();

        playlistFragment.setArguments(myArgs(userId, type, playlistOrAlbumId));

        return playlistFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        handleArgs();



        Log.i(TAG, "onViewCreated: " + view.findViewById(R.id.progress_playlist).toString());

        mMotionLayout = view.findViewById(R.id.motion_layout_playlist);

        mImageButtonOptions = view.findViewById(R.id.btn_playlist_options);
        mImageButtonOptions.setOnClickListener(v -> {
            /*OptionsFragment.builder(getActivity())
                    .addItem(R.drawable.ic_category, "Hi", v1 -> Log.i(TAG, "Hi Clicked"))
                    .addItem(null, "Hello", v1 -> Log.i(TAG, "Hello Clicked"))
                    .addItem(null, "Cheers", null)
                    .addItem(null, "Chao", null)
                    .addItem(null, "5moa", null)
                    .show();*/
        });

        mRecyclerViewTracks = view.findViewById(R.id.recycler_view_playlist_tracks);
        mRecyclerViewTracks.setLayoutManager(new LinearLayoutManager(getContext()));
        touchHelper = new ItemTouchHelper(recyclerViewTouchCallback);
        touchHelper.attachToRecyclerView(mRecyclerViewTracks);


        mImageViewPlaylist = view.findViewById(R.id.img_playlist);

        mTextViewPlaylistName = view.findViewById(R.id.txt_playlist_name);
        mTextViewPlaylistName.setSelected(true);
        mTextViewPlaylistName.addTextChangedListener(renameTextWatcher);


        mImageButtonRename = view.findViewById(R.id.btn_rename_playlist);
        mImageButtonRename.setOnClickListener(v -> {
            mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.RENAME);
            playlistNameBeforeRenaming = mTextViewPlaylistName.getText().toString();

            RenameFragment.showRenameFragment(getActivity(), R.id.nav_host_fragment, playlistNameBeforeRenaming, mTextViewPlaylistName);
        });





        if (type == Constants.PlaylistFragmentType.ALBUM) {
            disableEditing(view);
            handleAlbumData(mViewModel, view);
        } else {
            handlePlaylistData(mViewModel, view);
        }





    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void disableEditing(View view) {
        view.findViewById(R.id.btn_rename_playlist).setVisibility(View.GONE);
        view.findViewById(R.id.btn_upload_playlist_image).setVisibility(View.GONE);
        view.findViewById(R.id.btn_floating_add_track_to_playlist).setVisibility(View.GONE);
        touchHelper.attachToRecyclerView(null);
    }

    private void handleArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString(Constants.USER_ID_KEY);
            type = (Constants.PlaylistFragmentType) args.getSerializable(Constants.PLAYLIST_FRAGMENT_TYPE_KEY);
            playlistOrAlbumId = args.getString(Constants.ID_KEY);
        } else
            throw new RuntimeException("Instead of calling new " + PlaylistFragment.class.getSimpleName() + "()" +
                    ", call " + PlaylistFragment.class.getSimpleName() + ".newInstance(type, id)" +
                    " to pass the arguments to the fragment. Or you can use playlistFragment.setArguments(" + PlaylistFragment.class.getSimpleName() + ".myArgs()).");
    }

    private void handlePlaylistData(PlaylistViewModel mViewModel, View view) {
        mViewModel.getPlaylistLiveData(playlistOrAlbumId).observe(getViewLifecycleOwner(), playlist -> {



            if (!playlist.isCollaborative() & !playlist.getOwner().equals(userId))
                disableEditing(view);



            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

            Glide.with(getContext())
                    .load(playlist.getImage())
                    .placeholder(R.drawable.ic_oud_loading)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(mImageViewPlaylist);

            mTextViewPlaylistName.setText(playlist.getName());

            ArrayList<Track> tracks = playlist.getTracks();
            ArrayList<String> trackImages = new ArrayList<>();
            ArrayList<String> trackNames = new ArrayList<>();

            adapter = new PlaylistRecyclerViewAdapter(getContext(), trackImages, trackNames);


            for (int i = 0; i < tracks.size(); i++) {

                Track current = tracks.get(i);

                trackImages.add("");

                int _i = i;
                mViewModel.getTrackAlbumLiveData(i, current.getAlbumId()).observe(getViewLifecycleOwner(),
                        album -> {
                            trackImages.set(_i, album.getImage());
                            adapter.notifyItemChanged(_i);
                        });

                trackNames.add(current.getName());
            }

            mRecyclerViewTracks.setAdapter(adapter);
        });
    }

    private void handleAlbumData(PlaylistViewModel mViewMode, View view) {
        mViewModel.getAlbumLiveData(playlistOrAlbumId).observe(getViewLifecycleOwner(), album -> {
            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

            Glide.with(getContext())
                    .load(album.getImage())
                    .placeholder(R.drawable.ic_loading)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(mImageViewPlaylist);

            mTextViewPlaylistName.setText(album.getName());

            OudList<Track> tracksOudList = album.getTracks();
            ArrayList<Track> tracks = tracksOudList.getItems();
            ArrayList<String> trackImages = new ArrayList<>();
            ArrayList<String> trackNames = new ArrayList<>();

            adapter = new PlaylistRecyclerViewAdapter(getContext(), trackImages, trackNames);


            for (int i = 0; i < tracks.size(); i++) {

                Track current = tracks.get(i);

                trackImages.add(album.getImage());

                trackNames.add(current.getName());
            }

            mRecyclerViewTracks.setAdapter(adapter);
        });
    }

    private ItemTouchHelper.SimpleCallback recyclerViewTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        boolean moved = false;
        int posBeforeMoving;

        View toBeReorderedView;

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();

            if (!moved) {
                reorderingFromPosition = fromPosition;
            }

            moved = true;

            int toPosition = target.getAdapterPosition();

            //viewHolder.itemView.setAlpha(0.5f);
            //toBeReorderedView = viewHolder.itemView;

            /*adapter.hideAllReorderingSeparators();
            if (toPosition > fromPosition)
                target.itemView.findViewById(R.id.track_reorder_separator_below).setVisibility(View.VISIBLE);
            else if (toPosition < fromPosition)
                target.itemView.findViewById(R.id.track_reorder_separator_above).setVisibility(View.VISIBLE);*/

            Collections.swap(adapter.getTrackImages(), fromPosition, toPosition);
            Collections.swap(adapter.getTrackNames(), fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);

            mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.REORDER);
            reorderingToPosition = toPosition;


            Log.i(TAG, "onMove: " + fromPosition + " -> " + toPosition);




            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            String trackImage = adapter.getTrackImages().remove(position);
            String trackName = adapter.getTrackNames().remove(position);
            adapter.notifyItemRemoved(position);

            mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.DELETE);
            positionBeforeDeletion = position;

            //mMotionLayout.refreshDrawableState();
            //mMotionLayout.jumpDrawablesToCurrentState();
            //if (mMotionLayout.getProgress() != 0 | mMotionLayout.getProgress() != 1)
            //Log.i(TAG, "onSwiped: " + );
                //mMotionLayout.setProgress(mMotionLayout.getProgress() + 0.001f); // Little hack to refresh the recycler view,

            snackbarUndoTrackRemoved(position, trackImage, trackName);

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            Log.i(TAG, "onSelectedChanged: " + actionState);


            if (toBeReorderedView != null)
                toBeReorderedView.setAlpha(1);

            /*if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder != null)
                    viewHolder.itemView.setAlpha(1);
            } else*/ if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                if (viewHolder != null) {
                    viewHolder.itemView.setAlpha(0.5f);
                    toBeReorderedView = viewHolder.itemView;
                }
            }


            if (moved) {
                /*Collections.swap(adapter.getTrackImages(), reorderingFromPosition, reorderingToPosition);
                Collections.swap(adapter.getTrackNames(), reorderingFromPosition, reorderingToPosition);
                adapter.notifyItemMoved(reorderingFromPosition, reorderingToPosition);
                mRecyclerViewTracks.getLayoutManager().scrollToPosition(reorderingToPosition);*/
                //adapter.notifyDataSetChanged();
                //adapter.hideAllReorderingSeparators();



                // Server stuff
                if (reorderingToPosition != reorderingFromPosition)
                    mViewModel.reorderTrack(reorderingFromPosition, reorderingToPosition);





            }/* else {
                if (viewHolder != null)
                    viewHolder.itemView.setAlpha(0.5f);
            }*/

            moved = false;
        }

        @Override
        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
            super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            Log.i(TAG, "onMoved: ");
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, mRecyclerViewTracks, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(Color.RED)
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void snackbarUndoTrackRemoved(int position, String trackImage, String trackName) {
        Snackbar.make(mMotionLayout, "Track removed.", BaseTransientBottomBar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoDeletionRecyclerView(position, trackImage, trackName);
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                // Server stuff
                mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.DELETE);
            }
        }).show();
    }

    private TextWatcher renameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i(TAG, "onTextChanged: ");
            // Server stuff
            // Current operation has been set in onClickListener


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void undoDeletionRecyclerView(int position, String trackImage, String trackName) {
        adapter.getTrackImages().add(position, trackImage);
        adapter.getTrackNames().add(position, trackName);
        adapter.notifyItemInserted(position);
    }

    private void undoReorderingRecyclerView(int fromPosition, int toPosition) {
        Collections.swap(adapter.getTrackImages(), toPosition, fromPosition); // swap fromPosition & toPosition params
        Collections.swap(adapter.getTrackNames(), toPosition, fromPosition); // swap fromPosition & toPosition params
        adapter.notifyItemMoved(toPosition, fromPosition); // swap fromPosition & toPosition params
    }

    private void undoRenaming(String previousName) {
        mTextViewPlaylistName.setText(previousName);
    }

    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();
        //mViewModel.setCurrentOperation(null);
    }

    @Override
    public void onConnectionFailure() {
        super.onConnectionFailure();

        // Undo the current operation

        switch (mViewModel.getCurrentOperation()) {
            case DELETE: undoDeletionRecyclerView(positionBeforeDeletion, trackImageBeforeDeletion, trackNameBeforeDeletion);
                break;
            case RENAME: undoRenaming(playlistNameBeforeRenaming);
                break;
            case REORDER: undoReorderingRecyclerView(reorderingFromPosition, reorderingToPosition);
                break;
            case UPLOAD_IMAGE:
                break;

        }


        mViewModel.setCurrentOperation(null);
    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();
    }
}
