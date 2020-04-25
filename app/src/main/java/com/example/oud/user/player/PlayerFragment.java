package com.example.oud.user.player;


import android.content.Context;

import android.graphics.BitmapFactory;

import android.os.Bundle;


import android.support.v4.media.MediaMetadataCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;

import com.example.oud.api.Album;
import com.example.oud.api.Track;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.player.smallplayer.SmallPlayerFragment;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.RepeatModeUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    /* private SpotifyApi spotifyApi;*/
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerHelper mPlayerHelper;
    private MediaBrowserHelper mediaBrowserHelper;
    private MediaMetadataCompat mediaMetadata;

    private final String TAG = "PlayerFragment";


    private View v;
    private PlayerInterface mPlayerInterface;
    private Track mTrack;
    private Album mAlbum;


    public PlayerFragment() {
        // Required empty public constructor
    }

    public static PlayerFragment newInstance(PlayerHelper playerHelper,
                                             MediaBrowserHelper mediaBrowserHelper,
                                             MediaMetadataCompat mediaMetadata) {

        PlayerFragment instance = new PlayerFragment();

        instance.mediaBrowserHelper = mediaBrowserHelper;
        instance.mPlayerHelper = playerHelper;
        instance.mediaMetadata = mediaMetadata;

        return instance;
    }

    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            PlayerHelper playerHelper,
                            MediaBrowserHelper mediaBrowserHelper,
                            MediaMetadataCompat mediaMetadata) {

        FragmentManager manager = activity.getSupportFragmentManager();
        PlayerFragment playerFragment = (PlayerFragment) manager.findFragmentByTag(Constants.BIG_PLAYER_FRAGMENT_TAG);

        playerFragment = PlayerFragment.newInstance(playerHelper, mediaBrowserHelper, mediaMetadata);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, playerFragment, Constants.BIG_PLAYER_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_player, container, false);

        initializeViews();
        initializePlayerView();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mPlayerInterface = (PlayerInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SendMediaSession");
        }
    }

    private void initializeViews() {

        mPlayerView = v.findViewById(R.id.player);
    }

    private void initializePlayerView() {

        TextView playerName = v.findViewById(R.id.text_player_name);
        TextView artistName = v.findViewById(R.id.text_artist_name);
        TextView albumName = v.findViewById(R.id.text_album_name);

        ImageView imageView = v.findViewById(R.id.exo_artwork);
        ImageView nextButton = v.findViewById(R.id.next_button);
        ImageView prevButton = v.findViewById(R.id.prev_button);

        // todo change the textView in bigPlayer PLAYING FROM ALBUM to variable
        if(mediaMetadata != null) {
            /*playerName.setText(mTrack.getName());
            albumName.setText(mAlbum.getName());
            artistName.setText(mPlayerHelper.getArtistsNames());*/

            playerName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            artistName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            albumName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM));
            /*imageView.setImageBitmap(mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART))*/;
            OudUtils.glideBuilder(getContext(),MediaMetadataCompat.METADATA_KEY_ART_URI).placeholder(R.drawable.ic_oud_loading)
                    .into(imageView);
            /*Glide.with(v.getContext())
                    .load(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_oud_loading)
                    .into(imageView);*/
        }
        else {

            Log.d(TAG, "mediaMetadata of currentPlayback is null");
        }
        Log.d(TAG,"PositionOfPlayback:" + mPlayerView.getControllerShowTimeoutMs());

        /*mPlayerView.setDefaultArtwork(getResources().getDrawable(R.drawable.bach1));*/
        /*if (mediaMetadata != null) {

            playerName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            artistName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            albumName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM));
            imageView.setImageBitmap(mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ART));
        } else {

            Log.d(TAG, "mediaMetadata of currentPlayback is null");
        }*/

        mPlayerView.setRepeatToggleModes(RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE | RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL);
        mPlayerView.setControllerAutoShow(false);
        mPlayerView.showController();
        mPlayerView.setControllerHideOnTouch(false);

        if (mPlayerHelper != null) {
            mExoPlayer = mPlayerHelper.getExoPlayer();
        } else {
            Log.d(TAG, "mPlayerHelper is null");
        }

        if (mExoPlayer != null) {
            mPlayerView.setPlayer(mExoPlayer);
        } else {
            Log.d(TAG, "mExoPlayer is null");
        }

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);

    }

    /*private void handleArtistsName(){

     *//*String*//*

    }*/

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.next_button:
                mediaBrowserHelper.getTransportControls().skipToNext();
                break;
            case R.id.prev_button:
                mediaBrowserHelper.getTransportControls().skipToPrevious();
                break;
            default:
        }

    }

    /*private void retrofitRequest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        spotifyApi = retrofit.create(SpotifyApi.class);

        Call<Song> call = spotifyApi.getTrack("5v1VYGNDKCim1OiVVJRY0t");

        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {

                if(!response.isSuccessful()){
                    Log.e("MainActivity","Code:" + response.code());
                    return;
                }
                mSong= response.body();
                if(mSong != null){

                    initializeMediaSession();
                    initializePlayer();
                    return;
                }
                Log.e("MainActivity","mSong is null");
                mTextView.setText("track is null");
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {

                Log.e("MainActivity",t.getMessage());
                mTextView.setText(t.getMessage());
            }
        });

    }*/

}
