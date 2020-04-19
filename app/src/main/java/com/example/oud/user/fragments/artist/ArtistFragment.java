package com.example.oud.user.fragments.artist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.decorations.HorizontalSpaceDecoration;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.TrackListRecyclerViewAdapter;
import com.example.oud.user.player.PlayerInterface;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.Fragment;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ArtistFragment extends ConnectionAwareFragment<ArtistViewModel> {

    private static final String TAG = ArtistFragment.class.getSimpleName();

    private String token;
    private String userId;
    private String artistId;

    private MotionLayout mMotionLayout;

    private TextView mTextViewArtistName;
    private ImageButton mImageButtonFollowArtist;
    public int currentFollowColor;
    private ImageView mImageViewArtist;
    private ImageView mImageViewArtistBlurred;
    private View mViewImageGradientTint;

    private RecyclerView mRecyclerViewPopularSongs;
    private RecyclerView mRecyclerViewAlbums;
    private RecyclerView mRecyclerViewSimilarArtists;

    private TrackListRecyclerViewAdapter trackListRecyclerViewAdapter;
    private LoadMoreAdapter mAlbumsAdapter;
    private HorizontalRecyclerViewAdapter mSimilarArtistsAdapter;

    private TextView mTextViewNoSongsToShow;
    private TextView mTextViewNoAlbumsToShow;
    private TextView mTextViewNoArtistsToShow;

    private TextView mTextViewBio;


    private PlayerInterface talkToPlayer;


    private int trackLikePosition;


    public ArtistFragment() {
        super(ArtistViewModel.class,
                R.layout.fragment_artist,
                R.id.progress_artist,
                R.id.view_block_ui_input,
                null);
    }

    public static ArtistFragment newInstance(String artistId) {
        ArtistFragment artistFragment = new ArtistFragment();
        artistFragment.setArguments(myArgs(artistId));
        return artistFragment;
    }

    /**
     *  @param activity Most likely you'll call {@link #getActivity()} if you're in other {@link Fragment}.
     *                 Or <code>this</code> if you're inside the activity itself.
     * @param containerId The {@link FragmentContainerView} that you want the layout of this fragment to be inflated in.
     * @param artistId
     */
    public static void show(FragmentActivity activity, @IdRes int containerId, String artistId) {
        FragmentManager manager = activity.getSupportFragmentManager();
        ArtistFragment artistFragment/* = (ArtistFragment) manager.findFragmentByTag(Constants.ARTIST_FRAGMENT_TAG)*/;
        //if (artistFragment == null)
            artistFragment = ArtistFragment.newInstance(artistId);
        //else {
            //artistFragment.setArguments(ArtistFragment.myArgs(artistId));
        //}


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, artistFragment, Constants.ARTIST_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


    /**
     * When creating a new instance, this method returns the {@link Bundle} object that {@link ArtistFragment} needs.
     * @param artistId
     * @return
     */
    public static Bundle myArgs(String artistId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARTIST_ID_KEY, artistId);
        return bundle;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: ");

        handleArgs();
        //handleToken();
        token = OudUtils.getToken(getContext());
        userId = OudUtils.getUserId(getContext());

        mViewModel.clearTheDataThatHasThePotentialToBeChangedOutside();

        mTextViewArtistName = view.findViewById(R.id.txt_artist_name);
        mImageButtonFollowArtist = view.findViewById(R.id.btn_artist_follow);
        mImageViewArtist = view.findViewById(R.id.img_artist);
        mImageViewArtistBlurred = view.findViewById(R.id.img_artist_blurred);
        mViewImageGradientTint = view.findViewById(R.id.view_artist_img_tint);

        mRecyclerViewPopularSongs = view.findViewById(R.id.recycler_view_artist_popular_songs);
        mRecyclerViewPopularSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewAlbums = view.findViewById(R.id.recycler_view_artist_albums);
        mRecyclerViewAlbums.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL,
                false));
        mRecyclerViewSimilarArtists = view.findViewById(R.id.recycler_view_artist_similar_artists);
        mRecyclerViewSimilarArtists.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL,
                false));

        mTextViewNoSongsToShow = view.findViewById(R.id.txt_artist_no_songs_to_show);
        mTextViewNoAlbumsToShow = view.findViewById(R.id.txt_artist_no_albums_to_show);
        mTextViewNoArtistsToShow = view.findViewById(R.id.txt_artist_no_artists_to_show);

        mTextViewBio = view.findViewById(R.id.txt_artist_about_bio);

        mMotionLayout = view.findViewById(R.id.motion_layout_artist);
        //mMotionLayout.getTransition(R.id.transition_artist).setEnable(false);


        /*motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
                Log.i(TAG, "onTransitionStarted: ");
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                Log.i(TAG, "onTransitionChange: ");
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                Log.i(TAG, "onTransitionCompleted: ");
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
                Log.i(TAG, "onTransitionTrigger: ");
            }
        });*/

        handleData();

        // mMotionLayout.transitionToEnd();

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

    /*private ViewGroup container;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.container = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }*/

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: ");

        // Motion layout bug fix.
        // if (paused) {

            //((ViewGroup)(getView())).removeAllViews();
            //onCreateView(LayoutInflater.from(getContext()), container, null);

            /*getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, newInstance(artistId), Constants.ARTIST_FRAGMENT_TAG).commit();*/
        // }


        // paused = false;
    }

    // private boolean paused = false;

    @Override
    public void onPause() {
        super.onPause();

        // Motion layout bug fix.
        /*((UserActivity) getActivity()).setArtistFragPaused(true);
        ((UserActivity) getActivity()).setArtistFragPausedArtistId(artistId);*/

        // paused = true;

        /*getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(this)
                .commit();*/

    }


    /**
     * Assigns the values coming with {@link #getArguments()} to their corresponding variables.
     */
    private void handleArgs() {
        Bundle args = getArguments();
        if (args != null) {
            artistId = args.getString(Constants.ARTIST_ID_KEY);
            userId = args.getString(Constants.USER_ID_KEY);
        } else {
            String fragName = ArtistFragment.class.getSimpleName();
            throw new RuntimeException("Instead of calling new " + fragName + "()" +
                    ", call " + fragName + ".newInstance(id)" +
                    " to pass the arguments to the fragment. Or you can use " + fragName + ".setArguments(" + fragName + ".myArgs()).");
        }
    }

    /*private void handleToken() {
        SharedPreferences prefs = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        token = prefs.getString("token","000000");
    }*/

    /**
     * Observes all the {@link MutableLiveData} objects inside {@link ArtistViewModel}.
     */
    private void handleData() {
        mViewModel.getArtistMutableLiveData(token, artistId).observe(getViewLifecycleOwner(), artist -> {

            //mMotionLayout.getTransition(R.id.transition_artist).setEnable(true);

            mTextViewArtistName.setText(artist.getName());
            Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .apply(RequestOptions.fitCenterTransform())
                    //.placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtist);
            //new RequestOptions();
            Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 2)))
                    //.placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtistBlurred);



            handlePopularSongs(artist);


            mTextViewBio.setText(artist.getBio());

        });

        handleFollowArtist();

        handleAlbums();

        handleSimilarArtists();
    }

    /**
     * Handles the data and the logic behind following an unfollowing the artist.
     */
    private void handleFollowArtist() {
        mViewModel.getDoesUserFollowThisArtist(token, artistId).observe(getViewLifecycleOwner(), booleanIdsResponse -> {

            boolean doesCurrentUserFollowArtist = booleanIdsResponse.getIds().get(0);
            if (doesCurrentUserFollowArtist) {
                mImageButtonFollowArtist.setVisibility(View.VISIBLE);
                uiFollow();
            } else {
                mImageButtonFollowArtist.setVisibility(View.VISIBLE);
                uiUnFollow();
            }

            mImageButtonFollowArtist.setOnClickListener(v -> {
                if (mViewModel.getConnectionStatus().getValue() == Constants.ConnectionStatus.FAILED)
                    return;

                if (mViewModel.getDoesUserFollowThisArtist(token, artistId).getValue().getIds().get(0)) {
                    mViewModel.unfollowThisArtist(token, artistId);
                    uiUnFollow();
                } else {
                    mViewModel.followThisArtist(token, artistId);
                    uiFollow();
                }

                blockUiAndWait();
            });

        });
    }

    /**
     * Handles the data and the logic behind this artist's popular songs.
     */
    private void handlePopularSongs(Artist artist) {
        ArrayList<Track> tracks = artist.getPopularSongs();

        ArrayList<String> ids = new ArrayList<>();
        for (Track track : tracks) {
            ids.add(track.get_id());
        }

        mViewModel.getAreTracksLikedLiveData(token, ids).observe(getViewLifecycleOwner(), userAreTracksLiked -> {

            if (!tracks.isEmpty()) {
                ArrayList<String> trackIds = new ArrayList<>();
                //TrackListRecyclerViewAdapter.OnTrackClickListener trackClickListeners = new ArrayList<>();
                ArrayList<String> trackImages = new ArrayList<>();
                ArrayList<String> trackNames = new ArrayList<>();
                //TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListeners = new ArrayList<>();
                int i = 0;
                for (Track track : tracks) {
                    if (i >= Constants.USER_ARTIST_POPULAR_SONGS_COUNT) break;
                    trackIds.add(track.get_id());
                    //trackClickListeners.add(v -> talkToPlayer.configurePlayer(track.get_id(), true));
                    trackImages.add(track.getAlbum().getImage());
                    trackNames.add(track.getName());
                    //heartClickListeners.add(v -> Toast.makeText(getContext(), "track clicked !!", Toast.LENGTH_SHORT).show());
                    i++;
                }

                TrackListRecyclerViewAdapter.OnTrackClickListener trackClickListener = (position, view) -> {
                    talkToPlayer.configurePlayer(trackListRecyclerViewAdapter.getIds().get(position), true);
                };


                TrackListRecyclerViewAdapter.OnTrackClickListener availableOfflineClickListener = (position, view) -> {
                    Toast.makeText(getContext(), "track offline !!", Toast.LENGTH_SHORT).show();
                };

                trackListRecyclerViewAdapter = new TrackListRecyclerViewAdapter(getContext(),
                        trackIds,
                        trackClickListener,
                        trackImages,
                        trackNames,
                        userAreTracksLiked.getIsFound(),
                        availableOfflineClickListener,
                        heartClickListener);

                mRecyclerViewPopularSongs.setAdapter(trackListRecyclerViewAdapter);
            } else {
                mRecyclerViewPopularSongs.setVisibility(View.GONE);
                mTextViewNoSongsToShow.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * The {@link OnClickListener} when the user likes or dislikes a popular song.
     */
    private TrackListRecyclerViewAdapter.OnTrackClickListener heartClickListener = (position, view) -> {
        Toast.makeText(getContext(), "track liked !!", Toast.LENGTH_SHORT).show();

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

    /**
     * Handles the data and the logic behind the albums of this artist.
     */
    private void handleAlbums() {

        if (mViewModel.getLoadedAlbums().size() < Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT)
            loadMoreAlbums();
        else
            observeLoadedAlbums();


    }

    /**
     * Observes lastSetOfLoadedAlbums in {@link ArtistViewModel} and adds the newly loaded albums to the loaded ones.
     */
    private void loadMoreAlbums() {
        mViewModel.loadMoreAlbums(token, artistId).observe(getViewLifecycleOwner(), albumOudList -> {

            if (albumOudList.getTotal() == 0 & mViewModel.getLoadedAlbums().size() == 0) {
                mRecyclerViewAlbums.setVisibility(View.GONE);
                mTextViewNoAlbumsToShow.setVisibility(View.VISIBLE);
            }

            if (mAlbumsAdapter != null)
                mAlbumsAdapter.setLoaded();

            for (Album album : albumOudList.getItems()) {
                mViewModel.getLoadedAlbums().add(new MutableLiveData<>(album));
            }

            observeLoadedAlbums();

        });
    }

    /**
     * Observes loadedAlbums in {@link ArtistViewModel}. and populates {@link #mAlbumsAdapter}.
     */
    private void observeLoadedAlbums() {

        ArrayList<View.OnClickListener> clickListeners = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        ArrayList<Boolean> circularImages = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> subtitles = new ArrayList<>();
        ArrayList<HashMap<String, Object>> relatedInfo = new ArrayList<>();


        int i = 0;
        for (MutableLiveData<Album> albumMutableLiveData : mViewModel.getLoadedAlbums()) {
            int _i = i;
            albumMutableLiveData.observe(getViewLifecycleOwner(), album -> {

                if (mAlbumsAdapter != null) {
                    if (mAlbumsAdapter.getItemCount()-1 >= _i) { // Tracks already loaded
                        /*if (mAlbumsAdapter.getRelatedInfo().get(_i).get(Constants.ID_KEY).equals(album.get_id())) {*/
                            return;
                        } else {

                        HorizontalRecyclerViewAdapter hAdapter = (HorizontalRecyclerViewAdapter) mAlbumsAdapter.getAdapter();

                        hAdapter.getClickListeners().add(v -> PlaylistFragment.show(getActivity(),
                                    R.id.nav_host_fragment,
                                    userId,
                                    Constants.PlaylistFragmentType.ALBUM,
                                    album.get_id()));
                            hAdapter.getImages().add(album.getImage());
                            hAdapter.getCircularImages().add(false);
                            hAdapter.getTitles().add(album.getName());
                            hAdapter.getSubtitles().add("");

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put(Constants.ID_KEY, album.get_id());
                            hAdapter.getRelatedInfo().add(hashMap);
                            
                            hAdapter.notifyItemInserted(_i);
                        }

                        
                } else {

                    clickListeners.add(v -> PlaylistFragment.show(getActivity(),
                            R.id.nav_host_fragment,
                            userId,
                            Constants.PlaylistFragmentType.ALBUM,
                            album.get_id()));
                    images.add(album.getImage());
                    circularImages.add(false);
                    titles.add(album.getName());
                    subtitles.add("");

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(Constants.ID_KEY, album.get_id());
                    relatedInfo.add(hashMap);
                }
            });

            i++;
        }

        if (mAlbumsAdapter == null) {

            HorizontalRecyclerViewAdapter hAdapter = new HorizontalRecyclerViewAdapter(
                    getContext(),
                    R.layout.item_inner,
                    clickListeners,
                    images,
                    circularImages,
                    titles,
                    subtitles,
                    relatedInfo);

            mAlbumsAdapter = new LoadMoreAdapter(mRecyclerViewAlbums,
                    hAdapter,
                    HorizontalRecyclerViewAdapter.InnerItemViewHolder.class, hAdapter.getImages(), R.layout.progressbar_item);
            mRecyclerViewAlbums.addItemDecoration(new HorizontalSpaceDecoration(getResources(), R.dimen.item_margin));
            //mRecyclerViewAlbums.setAdapter(mAlbumsAdapter);
            mAlbumsAdapter.setOnLoadMoreListener(() -> {
                //images.add(null);
                loadMoreAlbums();
            });
        }


        if (mRecyclerViewAlbums.getAdapter() == null) {
            mRecyclerViewAlbums.addItemDecoration(new HorizontalSpaceDecoration(getResources(), R.dimen.item_margin));
            mAlbumsAdapter.setRecyclerView(mRecyclerViewAlbums);
        }
    }

    /**
     * Observes similarArtistsMutableLiveData in {@link ArtistViewModel}. and populates {@link #mSimilarArtistsAdapter}.
     */
    private void handleSimilarArtists() {
        mViewModel.getSimilarArtistsMutableLiveData(token, artistId).observe(getViewLifecycleOwner(), artists -> {

            if (!artists.getArtists().isEmpty()) {

                ArrayList<View.OnClickListener> clickListeners = new ArrayList<>();
                ArrayList<String> images = new ArrayList<>();
                ArrayList<Boolean> circularImages = new ArrayList<>();
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<String> subtitles = new ArrayList<>();
                ArrayList<HashMap<String, Object>> relatedInfo = new ArrayList<>();


                for (Artist artist : artists.getArtists()) {
                    clickListeners.add(v -> ArtistFragment.show(getActivity(),
                            R.id.nav_host_fragment,
                            artist.get_id()));
                    images.add(artist.getImages().get(0));
                    circularImages.add(true);
                    titles.add(artist.getName());
                    subtitles.add("");

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(Constants.ID_KEY, artist.get_id());
                    relatedInfo.add(hashMap);
                }

                mSimilarArtistsAdapter = new HorizontalRecyclerViewAdapter(getContext(), R.layout.item_inner, clickListeners, images, circularImages, titles, subtitles, relatedInfo);
                mRecyclerViewSimilarArtists.addItemDecoration(new HorizontalSpaceDecoration(getResources(), R.dimen.item_margin));

                mRecyclerViewSimilarArtists.setAdapter(mSimilarArtistsAdapter);
            } else {
                mRecyclerViewSimilarArtists.setVisibility(View.GONE);
                mTextViewNoArtistsToShow.setVisibility(View.VISIBLE);
            }

        });
    }

    /**
     * Undo the ui change when liking a track.
     */
    private void undoLikingTrack() {
        boolean bool = trackListRecyclerViewAdapter.getLikedTracks().get(trackLikePosition);
        trackListRecyclerViewAdapter.getLikedTracks().set(trackLikePosition, !bool);
        trackListRecyclerViewAdapter.notifyItemChanged(trackLikePosition);
    }

    /**
     * Indicate that this artist is being followed by the logged in user.
     */
    private void uiFollow() {
        currentFollowColor = getResources().getColor(R.color.colorPrimary);
        mImageButtonFollowArtist.setColorFilter(currentFollowColor);
    }

    /**
     * Indicate that this artist isn't followed by the logged in user.
     */
    private void uiUnFollow() {
        currentFollowColor = Color.WHITE;
        mImageButtonFollowArtist.setColorFilter(currentFollowColor);
    }

    /**
     * When the currentOperations fails, undo the change happened in the ui.
     */
    @Override
    public void onConnectionFailure() {
        super.onConnectionFailure();

        // Undo the current operation

        if (mViewModel.getCurrentOperation() != null)
            switch (mViewModel.getCurrentOperation()) {

                case REMOVE_TRACK_FROM_LIKED_TRACKS:
                case ADD_TRACK_TO_LIKED_TRACKS:
                    undoLikingTrack();
                    break;
                case FOLLOW_ARTIST:
                    uiUnFollow();
                    break;
                case UNFOLLOW_ARTIST:
                    uiFollow();
                    break;

            }

        mViewModel.setCurrentOperation(null);
    }

    /**
     * For testing only.
     * @return
     */
    @Deprecated
    public TrackListRecyclerViewAdapter getTrackListRecyclerViewAdapter() {
        return trackListRecyclerViewAdapter;
    }

    /**
     * For testing only.
     * @return
     */
    @Deprecated
    public MotionLayout getmMotionLayout() {
        return mMotionLayout;
    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        handleData();

    }
}
