package com.example.oud.user.player;


import android.content.Context;

import android.graphics.BitmapFactory;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.oud.R;

import com.example.oud.api.Album;
import com.example.oud.api.Track;
import com.example.oud.user.player.smallplayer.SmallPlayerFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.RepeatModeUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment /*implements ExoPlayer.EventListener*/ {

   /* private SpotifyApi spotifyApi;*/
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerHelper mPlayerHelper;


    private final String TAG = "PlayerFragment";


    private View v;
    private PlayerInterface mPlayerInterface;
    private Track mTrack;
    private Album mAlbum;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_player, container, false);

        mPlayerHelper = mPlayerInterface.getPlayerHelper();
        mTrack = mPlayerHelper.getTrack();
        mAlbum = mPlayerHelper.getAlbum();

        initializeViews();
        initializePlayerView();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mPlayerInterface = (PlayerInterface) context;

        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SendMediaSession");
        }
    }

    private void initializeViews() {
        /*mSong = new Song();*/
        mPlayerView = v.findViewById(R.id.player);
    }

    private void initializePlayerView() {

        TextView playerName = v.findViewById(R.id.text_player_name);
        TextView artistName = v.findViewById(R.id.text_artist_name);
        TextView albumName = v.findViewById(R.id.text_album_name);
        ImageView imageView = v.findViewById(R.id.exo_artwork);

        playerName.setText(mTrack.getName());
        albumName.setText(mAlbum.getName());
        artistName.setText(mPlayerHelper.getArtistsNames());

        Glide.with(v.getContext())
                .load(mAlbum.getImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_oud_loading)
                .into(imageView);


        /*mPlayerView.setDefaultArtwork(getResources().getDrawable(R.drawable.bach1));*/
        mPlayerView.setRepeatToggleModes(RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE | RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL);
        mPlayerView.setControllerAutoShow(false);
        mPlayerView.showController();
        mPlayerView.setControllerHideOnTouch(false);
        mExoPlayer = mPlayerInterface.getSimpleExoPlayer();
        mPlayerView.setPlayer(mExoPlayer);


    }

    /*private void handleArtistsName(){

        *//*String*//*

    }*/

    public void onDestroy() {
        super.onDestroy();
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
