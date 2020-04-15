package com.example.oud.user.fragments.playlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.Constants;
import com.example.oud.OptionsFragment;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.Track;
import com.example.oud.api.TrackPreview;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.RenameFragment;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.player.PlayerInterface;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class PlaylistFragment extends ConnectionAwareFragment<PlaylistViewModel> implements RenameFragment.OnRenamingListener {

    private static final String TAG = PlaylistFragment.class.getSimpleName();

    private String token;
    private String userId;
    private Constants.PlaylistFragmentType type;
    private String playlistOrAlbumId;

    private MotionLayout mMotionLayout;

    private RecyclerView mRecyclerViewTracks;
    private TrackListRecyclerViewAdapter trackListRecyclerViewAdapter;
    private ItemTouchHelper touchHelper;

    private ImageView mImageViewPlaylist;
    private TextView mTextViewPlaylistName;
    private ImageButton mImageButtonRename;
    private ImageButton mImageButtonOptions;




    private PlayerInterface talkToPlayer;




    private int deletionPosition;
    //private View.OnClickListener trackClickListenerBeforeDeletion;
    private String trackImageBeforeDeletion;
    private String trackNameBeforeDeletion;
    private Boolean trackIsLikedBeforeDeletion;
    //private View.OnClickListener heartClickListenerBeforeDeletion;
    private boolean undoDeletionClicked;

    private String playlistNameBeforeRenaming;

    private int reorderingFromPosition;
    private int reorderingToPosition;

    //private boolean renamePressed;

    private int trackLikePosition;




    public PlaylistFragment() {
        super(PlaylistViewModel.class,
                R.layout.fragment_playlist,
                R.id.progress_playlist,
                R.id.view_block_ui_input,
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

    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            String userId,
                            Constants.PlaylistFragmentType type,
                            String playlistOrAlbumId) {

        FragmentManager manager = activity.getSupportFragmentManager();
        PlaylistFragment playlistFragment = (PlaylistFragment) manager.findFragmentByTag(Constants.PLAYLIST_FRAGMENT_TAG);
        if (playlistFragment == null)
            playlistFragment = PlaylistFragment.newInstance(userId, type, playlistOrAlbumId);
        else {
            playlistFragment.setArguments(PlaylistFragment.myArgs(userId, type, playlistOrAlbumId));
        }


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, playlistFragment, Constants.PLAYLIST_FRAGMENT_TAG)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        handleArgs();
        handleToken();



        Log.i(TAG, "onViewCreated: " + view.findViewById(R.id.progress_playlist).toString());

        initializeUiStuff(view);



        loadData(view);

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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void loadData(View view) {
        if (type == Constants.PlaylistFragmentType.ALBUM) {
            disableEditing(view);
            handleAlbumData();
        } else {
            handlePlaylistData(view);
        }

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

    private void handleToken() {
        SharedPreferences prefs = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        token = prefs.getString("token","000000");
    }

    private void initializeUiStuff(View view) {
        mMotionLayout = view.findViewById(R.id.motion_layout_playlist);

        mImageButtonOptions = view.findViewById(R.id.btn_playlist_options);

        mRecyclerViewTracks = view.findViewById(R.id.recycler_view_playlist_tracks);
        mRecyclerViewTracks.setLayoutManager(new LinearLayoutManager(getContext()));
        touchHelper = new ItemTouchHelper(recyclerViewTouchCallback);
        touchHelper.attachToRecyclerView(mRecyclerViewTracks);


        mImageViewPlaylist = view.findViewById(R.id.img_playlist);

        mTextViewPlaylistName = view.findViewById(R.id.txt_playlist_name);
        mTextViewPlaylistName.setSelected(true);
        //mTextViewPlaylistName.addTextChangedListener(renameTextWatcher);


        mImageButtonRename = view.findViewById(R.id.btn_rename_playlist);
        mImageButtonRename.setOnClickListener(v -> {
            // mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.RENAME);
            playlistNameBeforeRenaming = mTextViewPlaylistName.getText().toString();

            RenameFragment.showRenameFragment(getActivity(), R.id.nav_host_fragment, playlistNameBeforeRenaming, this);
        });

    }



    private void handlePlaylistOptions() {

        mViewModel.getDoesUserFollowThisPlaylist(token, playlistOrAlbumId, userId).observe(getViewLifecycleOwner(), doesUserFollowThisPlaylist -> {
            mImageButtonOptions.setVisibility(View.VISIBLE);
        });


        mImageButtonOptions.setOnClickListener(v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            Playlist playlist = mViewModel.getPlaylistLiveData(token, playlistOrAlbumId).getValue();

            OptionsFragment.Builder optionsBuilder = OptionsFragment.builder(getActivity());

            if (!playlist.getOwner().equals(userId)) {

                boolean isFollowed = mViewModel.getDoesUserFollowThisPlaylist(token, playlistOrAlbumId, userId).getValue().get(0);
                View.OnClickListener followClickListener = instantiateFollowClickListener(isFollowed);
                optionsBuilder.addItem(R.drawable.ic_follow_playlist, "Follow", isFollowed, followClickListener);
            }

            if (playlist.getOwner().equals(userId)) {
                boolean isPublic = playlist.isPublicPlaylist();
                View.OnClickListener makePublicClickListener = instantiateMakePublicClickListener(isPublic);
                optionsBuilder.addItem(R.drawable.ic_public, "Make Public", isPublic, makePublicClickListener);

                boolean isCollaborative = playlist.isCollaborative();
                View.OnClickListener makeCollaborativeClickListener = instantiateMakeCollaborativeClickListener(isCollaborative);
                optionsBuilder.addItem(R.drawable.ic_collaborative, "Make Collaborative", isCollaborative, makeCollaborativeClickListener);
            }

            optionsBuilder.show();
        });


    }

    private void handleAlbumOptions() {
        mViewModel.getIsThisAlbumSavedByUser(token).observe(getViewLifecycleOwner(), isFoundResponse -> {
            mImageButtonOptions.setVisibility(View.VISIBLE);
        });

        mImageButtonOptions.setOnClickListener(v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            Album album = mViewModel.getAlbumLiveData(token, playlistOrAlbumId).getValue();

            View.OnClickListener goToArtistClickListener = instantiateGoToArtistClickListener(album.getArtists());

            boolean isSaved = mViewModel.getIsThisAlbumSavedByUser(token).getValue().getIsFound().get(0);
            View.OnClickListener saveAlbumClickListener = instantiateSaveAlbumClickListener(isSaved);


            OptionsFragment.builder(getActivity())
                    .addItem(R.drawable.ic_user, "Go To Artist", false, goToArtistClickListener)
                    .addItem(R.drawable.ic_star, "Save Album", isSaved, saveAlbumClickListener)
                    .show();
        });
    }

    private View.OnClickListener instantiateFollowClickListener(boolean isFollowed) {
        if (isFollowed)
            return v -> {
                if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                    return;

                mViewModel.unfollowThisPlaylist(token);
                blockUiAndWait();
            };

        return v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            mViewModel.followThisPlaylist(token);
            blockUiAndWait();
        };
    }

    private View.OnClickListener instantiateMakePublicClickListener(boolean isPublic) {
        if (isPublic)
            return v -> {
                if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                    return;

                mViewModel.makePlaylistPrivate(token);
                blockUiAndWait();
            };

        return v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            mViewModel.makePlaylistPublic(token);
            blockUiAndWait();
        };
    }

    private View.OnClickListener instantiateMakeCollaborativeClickListener(boolean isCollaborative) {
        if (isCollaborative)
            return v -> {
                if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                    return;

                mViewModel.makePlaylistNonCollaborative(token);
                blockUiAndWait();
            };

        return v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            mViewModel.makePlaylistCollaborative(token);
            blockUiAndWait();
        };
    }

    private View.OnClickListener instantiateGoToArtistClickListener(ArtistPreview[] artists) {
        if (artists.length == 1)
            return openArtistFragmentClickListener(artists[0].get_id());
        else
            return v -> {
                OptionsFragment.Builder optionsBuilder = OptionsFragment.builder(getActivity());

                for (ArtistPreview artistPreview : artists) {
                    optionsBuilder.addItem(R.drawable.ic_user, artistPreview.getName(), false, openArtistFragmentClickListener(artistPreview.get_id()));
                }

                optionsBuilder.show();
            };
    }

    private View.OnClickListener openArtistFragmentClickListener(String artistId) {
        return v -> ArtistFragment.show(getActivity(),
                R.id.nav_host_fragment,
                artistId, userId);
    }

    private View.OnClickListener instantiateSaveAlbumClickListener(boolean isSaved) {
        if (isSaved)
            return v -> {
                if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                    return;

                mViewModel.unsaveAlbum(token);
                blockUiAndWait();
            };

        return v -> {
            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                return;

            mViewModel.saveAlbum(token);
            blockUiAndWait();
        };
    }

    private void handlePlaylistData(View view) {
        mViewModel.getPlaylistLiveData(token, playlistOrAlbumId).observe(getViewLifecycleOwner(), playlist -> {



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

            handlePlaylistTracks(playlist);
            handlePlaylistOptions();
        });
    }

    private void handleAlbumData() {
        mViewModel.getAlbumLiveData(token, playlistOrAlbumId).observe(getViewLifecycleOwner(), album -> {
            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

            Glide.with(getContext())
                    .load(album.getImage())
                    .placeholder(R.drawable.ic_oud_loading)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(mImageViewPlaylist);

            mTextViewPlaylistName.setText(album.getName());

            handleAlbumTracks(album);

            handleAlbumOptions();
        });
    }

    private void handlePlaylistTracks(Playlist playlist) {

        ArrayList<String> ids = new ArrayList<>();
        for (Track track : playlist.getTracks()) {
            ids.add(track.get_id());
        }

        mViewModel.getAreTracksLikedLiveData(token, ids).observe(getViewLifecycleOwner(), userAreTracksLiked -> {
            //TrackListRecyclerViewAdapter.OnTrackClickListener clickListeners = new ArrayList<>();
            ArrayList<Track> tracks = playlist.getTracks();
            ArrayList<String> trackImages = new ArrayList<>();
            ArrayList<String> trackNames = new ArrayList<>();
            //TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListeners = new ArrayList<>();




            trackListRecyclerViewAdapter = new TrackListRecyclerViewAdapter(getContext(),
                    ids,
                    trackClickListener,
                    trackImages,
                    trackNames,
                    userAreTracksLiked.getIsFound(),
                    availableOfflineClickListener,
                    heartClickListener);


            for (int i = 0; i < tracks.size(); i++) {

                Track current = tracks.get(i);

                //clickListeners.add(v -> talkToPlayer.configurePlayer(current.get_id(), true));

                trackImages.add(current.getAlbum().getImage());

                //trackImages.add("");
                //int _i = i;
                /*mViewModel.getTrackAlbumLiveData(i, current.getAlbumId()).observe(getViewLifecycleOwner(),
                        album -> {
                            trackImages.set(_i, album.getImage());
                            adapter.notifyItemChanged(_i);
                        });*/
                playlistNameBeforeRenaming = current.getName();
                trackNames.add(current.getName());

            }

            mRecyclerViewTracks.setAdapter(trackListRecyclerViewAdapter);
        });

    }

    private void handleAlbumTracks(Album album) {
        ArrayList<String> ids = new ArrayList<>();
        for (TrackPreview track : album.getTracks().getItems()) {
            ids.add(track.get_id());
        }

        mViewModel.getAreTracksLikedLiveData(token, ids).observe(getViewLifecycleOwner(), userAreTracksLiked -> {
            OudList<TrackPreview> tracksOudList = album.getTracks();
            ArrayList<TrackPreview> tracks = tracksOudList.getItems();
            //TrackListRecyclerViewAdapter.OnTrackClickListener clickListeners = new ArrayList<>();
            ArrayList<String> trackImages = new ArrayList<>();
            ArrayList<String> trackNames = new ArrayList<>();
            //TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListeners = new ArrayList<>();


            trackListRecyclerViewAdapter = new TrackListRecyclerViewAdapter(getContext(),
                    ids,
                    trackClickListener,
                    trackImages,
                    trackNames,
                    userAreTracksLiked.getIsFound(),
                    availableOfflineClickListener,
                    heartClickListener);


            for (int i = 0; i < tracks.size(); i++) {

                TrackPreview current = tracks.get(i);

                //clickListeners.add(v -> talkToPlayer.configurePlayer(current.get_id(), true));

                trackImages.add(album.getImage());

                trackNames.add(current.getName());

                //heartClickListeners.add(v -> Toast.makeText(getContext(), "Like !!", Toast.LENGTH_SHORT).show());

            }

            mRecyclerViewTracks.setAdapter(trackListRecyclerViewAdapter);
        });
    }

    private TrackListRecyclerViewAdapter.OnTrackClickListener trackClickListener = (position, view) -> {
        talkToPlayer.configurePlayer(trackListRecyclerViewAdapter.getIds().get(position), true);
    };

    private TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListener = (position, view) -> {
        //Toast.makeText(getContext(), "track liked !!", Toast.LENGTH_SHORT).show();

        trackLikePosition = position;

        if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
            return;

        String id = trackListRecyclerViewAdapter.getIds().get(position);
        if (trackListRecyclerViewAdapter.getLikedTracks().get(position)) {
            mViewModel.removeTrackFromLikedTracks(token, id, position);
            trackListRecyclerViewAdapter.getLikedTracks().set(position, false);
            trackListRecyclerViewAdapter.notifyItemChanged(position);
        } else {
            mViewModel.addTrackToLikedTracks(token, id, position);
            trackListRecyclerViewAdapter.getLikedTracks().set(position, true);
            trackListRecyclerViewAdapter.notifyItemChanged(position);
        }

        blockUiAndWait();

    };

    private TrackListRecyclerViewAdapter.OnTrackClickListener availableOfflineClickListener = (position, view) -> {
        Toast.makeText(getContext(), "track offline !!", Toast.LENGTH_SHORT).show();
    };

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

            trackListRecyclerViewAdapter.swapItems(fromPosition, toPosition);
            trackListRecyclerViewAdapter.notifyItemMoved(fromPosition, toPosition);

            reorderingToPosition = toPosition;


            Log.i(TAG, "onMove: " + fromPosition + " -> " + toPosition);




            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            //View.OnClickListener trackClickListener = trackListRecyclerViewAdapter.getTrackClickListeners().get(position);
            String trackImage = trackListRecyclerViewAdapter.getTrackImages().get(position);
            String trackName = trackListRecyclerViewAdapter.getTrackNames().get(position);
            Boolean isLiked = trackListRecyclerViewAdapter.getLikedTracks().get(position);
            //View.OnClickListener heartClickListener = trackListRecyclerViewAdapter.getHeartClickListeners().get(position);
            trackListRecyclerViewAdapter.removeItem(position);
            trackListRecyclerViewAdapter.notifyItemRemoved(position);

            //mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.DELETE);
            deletionPosition = position;
            //trackClickListenerBeforeDeletion = trackClickListener;
            trackImageBeforeDeletion = trackImage;
            trackNameBeforeDeletion = trackName;
            trackIsLikedBeforeDeletion = isLiked;
            //heartClickListenerBeforeDeletion = heartClickListener;

            //mMotionLayout.refreshDrawableState();
            //mMotionLayout.jumpDrawablesToCurrentState();
            //if (mMotionLayout.getProgress() != 0 | mMotionLayout.getProgress() != 1)
            //Log.i(TAG, "onSwiped: " + );
                //mMotionLayout.setProgress(mMotionLayout.getProgress() + 0.001f); // Little hack to refresh the recycler view,

            snackbarUndoTrackRemoved();

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
                if (reorderingToPosition != reorderingFromPosition) {
                    //mViewModel.setCurrentOperation(PlaylistViewModel.PlaylistOperation.REORDER);

                    if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED) {
                        undoReorderingRecyclerView(reorderingFromPosition, reorderingToPosition);
                    } else {
                        blockUiAndWait();
                        mViewModel.reorderTrack(token, reorderingFromPosition, reorderingToPosition);
                    }

                }





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

    private void snackbarUndoTrackRemoved() {
        Snackbar.make(mMotionLayout, "Track removed.", BaseTransientBottomBar.LENGTH_LONG)

                .setAction("Undo", v -> {
                    undoDeletionRecyclerView();
                    undoDeletionClicked = true;
                })

                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (!undoDeletionClicked) {
                            if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED) {
                                undoDeletionRecyclerView();
                            } else {
                                mViewModel.deleteTrack(token, deletionPosition);
                                blockUiAndWait();
                            }
                        }
                        undoDeletionClicked = false;
                    }
                })
                .show();
    }

    private void undoDeletionRecyclerView() {
        trackListRecyclerViewAdapter.addItem(deletionPosition,
                trackImageBeforeDeletion,
                trackNameBeforeDeletion,
                trackIsLikedBeforeDeletion);

        trackListRecyclerViewAdapter.notifyItemInserted(deletionPosition);
    }

    private void undoReorderingRecyclerView(int fromPosition, int toPosition) {
        trackListRecyclerViewAdapter.swapItems(toPosition, fromPosition); // swap fromPosition & toPosition params
        trackListRecyclerViewAdapter.notifyItemMoved(toPosition, fromPosition); // swap fromPosition & toPosition params
    }

    private void undoRenaming(String previousName) {
        mTextViewPlaylistName.setText(previousName);
    }

    private void undoLikingTrack() {
        boolean bool = trackListRecyclerViewAdapter.getLikedTracks().get(trackLikePosition);
        trackListRecyclerViewAdapter.getLikedTracks().set(trackLikePosition, !bool);
        trackListRecyclerViewAdapter.notifyItemChanged(trackLikePosition);
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

        if (mViewModel.getCurrentOperation() != null)
            switch (mViewModel.getCurrentOperation()) {
                case DELETE: undoDeletionRecyclerView();
                    break;
                case RENAME: undoRenaming(playlistNameBeforeRenaming);
                    break;
                case REORDER: undoReorderingRecyclerView(reorderingFromPosition, reorderingToPosition);
                    break;
                case UPLOAD_IMAGE:
                    break;
                case REMOVE_TRACK_FROM_LIKED_TRACKS:
                case ADD_TRACK_TO_LIKED_TRACKS:
                    undoLikingTrack();
                    break;

            }

        mViewModel.setCurrentOperation(null);
    }

    @Override
    public void onRenamingListener(String s) {
        if (type == Constants.PlaylistFragmentType.ALBUM) return;

        String newName = s;

        //if (s.equals(playlistNameBeforeRenaming)) return;

        if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED) {
            //if (playlistNameBeforeRenaming.equals(s))
            undoRenaming(playlistNameBeforeRenaming);
            playlistNameBeforeRenaming = newName;
        } else {
            mTextViewPlaylistName.setText(newName);
            mViewModel.renamePlaylist(token, newName);
            blockUiAndWait();
        }
    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        loadData(getView());
    }
}
