package com.example.oud.user.fragments.artist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.Artist;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;
import com.example.oud.user.fragments.home.nestedrecyclerview.decorations.HorizontalSpaceDecoration;
import com.example.oud.user.fragments.playlist.PlaylistRecyclerViewAdapter;
import com.example.oud.user.player.PlayerInterface;

import java.util.ArrayList;

public class ArtistFragment extends ConnectionAwareFragment<ArtistViewModel> {

    private static final String TAG = ArtistFragment.class.getSimpleName();

    private String artistId;

    private MotionLayout mMotionLayout;

    private TextView mTextViewArtistName;
    private ImageView mImageViewArtist;
    private ImageView mImageViewArtistBlurred;
    private View mViewImageGradientTint;

    private RecyclerView mRecyclerViewPopularSongs;
    private RecyclerView mRecyclerViewAlbums;
    private RecyclerView mRecyclerViewSimilarArtists;

    private PlaylistRecyclerViewAdapter mPopularSongsAdapter;
    HorizontalRecyclerViewAdapter mSimilarArtistsAdapter;

    private TextView mTextViewNoSongsToShow;
    private TextView mTextViewNoAlbumsToShow;
    private TextView mTextViewNoArtistsToShow;

    private TextView mTextViewBio;


    private PlayerInterface talkToPlayer;



    public ArtistFragment() {
        super(ArtistViewModel.class,
                R.layout.fragment_artist,
                R.id.progress_artist,
                null);
    }

    public static ArtistFragment newInstance(String artistId) {
        ArtistFragment artistFragment = new ArtistFragment();
        artistFragment.setArguments(myArgs(artistId));
        return artistFragment;
    }

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

        mTextViewArtistName = view.findViewById(R.id.txt_artist_name);
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
        if (paused) {

            //((ViewGroup)(getView())).removeAllViews();
            //onCreateView(LayoutInflater.from(getContext()), container, null);

            /*getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, newInstance(artistId), Constants.ARTIST_FRAGMENT_TAG).commit();*/
        }


        paused = false;
    }

    private boolean paused = false;

    @Override
    public void onPause() {
        super.onPause();

        // Motion layout bug fix.
        /*((UserActivity) getActivity()).setArtistFragPaused(true);
        ((UserActivity) getActivity()).setArtistFragPausedArtistId(artistId);*/

        paused = true;

        /*getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(this)
                .commit();*/

    }



    private void handleArgs() {
        Bundle args = getArguments();
        if (args != null) {
            artistId = args.getString(Constants.ARTIST_ID_KEY);
        } else {
            String fragName = ArtistFragment.class.getSimpleName();
            throw new RuntimeException("Instead of calling new " + fragName + "()" +
                    ", call " + fragName + ".newInstance(id)" +
                    " to pass the arguments to the fragment. Or you can use " + fragName + ".setArguments(" + fragName + ".myArgs()).");
        }
    }

    private void handleData() {
        mViewModel.getArtistMutableLiveData(artistId).observe(getViewLifecycleOwner(), artist -> {

            //mMotionLayout.getTransition(R.id.transition_artist).setEnable(true);

            mTextViewArtistName.setText(artist.getName());
            Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .apply(RequestOptions.fitCenterTransform())
                    //.placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtist);
            //new RequestOptions();
            /*Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 2)))
                    //.placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtistBlurred);*/



            ArrayList<Track> tracks = artist.getPopularSongs();
            if (!tracks.isEmpty()) {
                ArrayList<View.OnClickListener> clickListeners = new ArrayList<>();
                ArrayList<String> trackImages = new ArrayList<>();
                ArrayList<String> trackNames = new ArrayList<>();
                int i = 0;
                for (Track track : tracks) {
                    if (i >= Constants.USER_ARTIST_POPULAR_SONGS_COUNT) break;
                    clickListeners.add(v -> talkToPlayer.configurePlayer(track.get_id(), true));
                    trackImages.add(track.getAlbum().getImage());
                    trackNames.add(track.getName());
                    i++;
                }

                mPopularSongsAdapter = new PlaylistRecyclerViewAdapter(getContext(), clickListeners, trackImages, trackNames);
                mRecyclerViewPopularSongs.setAdapter(mPopularSongsAdapter);
            } else {
                mRecyclerViewPopularSongs.setVisibility(View.GONE);
                mTextViewNoSongsToShow.setVisibility(View.VISIBLE);
            }




            mTextViewBio.setText(artist.getBio());

        });


        // TODO: Observe the loaded albums first.
        // TODO: Observe last set of loaded albums and add the newly fetched ones to the loaded albums if they weren't already added.


        mViewModel.getSimilarArtistsMutableLiveData(artistId).observe(getViewLifecycleOwner(), artists -> {

            if (!artists.getArtists().isEmpty()) {

                ArrayList<View.OnClickListener> clickListeners = new ArrayList<>();
                ArrayList<String> images = new ArrayList<>();
                ArrayList<String> titles = new ArrayList<>();
                ArrayList<String> subtitles = new ArrayList<>();


                for (Artist artist : artists.getArtists()) {
                    clickListeners.add(v -> ArtistFragment.show(getActivity(),
                            R.id.nav_host_fragment,
                            artist.get_id()));
                    images.add(artist.getImages().get(0));
                    titles.add(artist.getName());
                    subtitles.add("");
                }

                mSimilarArtistsAdapter = new HorizontalRecyclerViewAdapter(getContext(), clickListeners, images, titles, subtitles, true);
                mRecyclerViewSimilarArtists.addItemDecoration(new HorizontalSpaceDecoration(getResources(), R.dimen.item_margin));

                mRecyclerViewSimilarArtists.setAdapter(mSimilarArtistsAdapter);
            } else {
                mRecyclerViewSimilarArtists.setVisibility(View.GONE);
                mTextViewNoArtistsToShow.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        handleData();

    }
}
