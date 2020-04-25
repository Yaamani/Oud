package com.example.oud.user.player;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.media.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.OudApi;
import com.example.oud.api.StartOrResumePlayback;
import com.example.oud.api.StatusMessageResponse;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.user.UserActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//todo save the state of player when destroy the program to get it again
public class PlayerHelper extends MediaControllerCompat.Callback  implements ExoPlayer.EventListener  {

    private final String TAG = "UserActivity";

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;

    private Context userContext;

    private PlaybackStateCompat.Builder mSaBuilder;
    private NotificationManager mNotificationManager;

    private String mTrackId;
    private boolean resetPlay;

    private Bundle bundle;
    private Intent intentState;

    private Track mTrack;
    private Album mAlbum;
    private Activity mUserActivity;
    private boolean mGetPlayerBack;
    private com.example.oud.user.player.CacheDataSourceFactory cacheDataSourceFactory;
    private MediaMetadataCompat mediaMetadataCompat;

    public PlayerHelper(Context context, NotificationManager nM ) {

        userContext = context;
        mNotificationManager = nM;

        /*mUserActivity = activity;*/
        initializeMediaSession();

    }

    public PlayerHelper(Context context){

        userContext = context;
        initPlayer();

        bundle = new Bundle();
        intentState = new Intent();

    }

    public void getPlayerback(Uri trackUri){

        mExoPlayer = new SimpleExoPlayer.Builder(userContext).build();

        com.example.oud.user.player.CacheDataSourceFactory cacheDataSourceFactory =
                new com.example.oud.user.player.CacheDataSourceFactory(userContext,
                        100 * 1024 * 1024,
                        5 * 1024 * 1024);
        MediaSource mediaSource = new ExtractorMediaSource(/*trackUri*/Uri.parse("asset:///toccata_fugue.mp3"),cacheDataSourceFactory
                , new DefaultExtractorsFactory(),
                null/*new Handler()*/,null /*Throwable::printStackTrace*/);

        mExoPlayer.prepare(mediaSource,false,false);
        mExoPlayer.addListener(this);
        handleAudioFocus();
    }

    public Album getAlbum() {
        return mAlbum;
    }

    public void setAlbum(Album mAlbum) {
        this.mAlbum = mAlbum;
    }

    public static MediaSessionCompat getMediaSession() {

        return mMediaSession;
    }

    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public void initializePlayer(Uri trackUri) {

        if (mExoPlayer == null) {


            /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(userContext, trackSelector, loadControl);
             mExoPlayer = ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector(trackSelectionFactory));*/
            mExoPlayer = new SimpleExoPlayer.Builder(userContext).build();

            String userAgent = Util.getUserAgent(userContext, "Oud");

            com.example.oud.user.player.CacheDataSourceFactory cacheDataSourceFactory =
                    new com.example.oud.user.player.CacheDataSourceFactory(userContext,
                    100 * 1024 * 1024,
                    5 * 1024 * 1024);


            MediaSource mediaSource = new ExtractorMediaSource(trackUri/*Uri.parse("asset:///toccata_fugue.mp3")*/,cacheDataSourceFactory
                    , new DefaultExtractorsFactory(),
                    null/*new Handler()*/,null /*Throwable::printStackTrace*/);

          /*MediaSource mediaSourceHelper = new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(userContext, userAgent),new DefaultExtractorsFactory());*/
           /* MediaSource secondSource= new ProgressiveMediaSource.Factory(new FileDataSourceFactory()).createMediaSource(Uri.parse(uri.get(1)));
            MediaSource mediaSource;
            mediaSource.addMediaSources(firstSource);*/

            /*MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(*//*mSong.getUri()*//**//*"asset:///toccata_fugue.mp3"*//*userContext.getExternalCacheDir().getAbsolutePath() + "/5e9b40b80f27b613b44fba32-5e907db0a0645f558160760d-1587347295787.mp3"),
                    new DefaultDataSourceFactory(userContext, userAgent), new DefaultExtractorsFactory(),
                    null*//*new Handler()*//*, null);*/


            mExoPlayer.prepare(mediaSource);
            /*mExoPlayer.setPlayWhenReady(true);*/
            mExoPlayer.addListener(this);
            handleAudioFocus();

        }

    }

    public void setTrackId(String trackID){
        mTrackId = trackID;
    }

    public String getTrackId(){

        return mTrackId;
    }

   public void setTrack(Track track){

        mTrack = track;
   }

   public Track getTrack(){

        return mTrack;
   }


    public void setResetPlay(boolean resetPlay) {
        this.resetPlay = resetPlay;
    }

    public boolean isResetPlay() {
        return resetPlay;
    }

    public void setPLayerBackBoolean(boolean getPlayerBack) {

        mGetPlayerBack=getPlayerBack;
    }

    public boolean ismGetPlayerBack() {
        return mGetPlayerBack;
    }


    public class mySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {

            /*v.getContext().registerReceiver(mediaReceiver,intentFilterButton);*/
            /* getContext().registerReceiver(mediaReceiver,intentFilterButton);*/
            /*getContext().registerReceiver(myNoisyAudioStreamReceiver, intentFilter);*/
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {

            /*v.getContext().registerReceiver(mediaReceiver,intentFilterButton);*/
            /*getContext().unregisterReceiver(mediaReceiver);*/
            /*getContext().unregisterReceiver(myNoisyAudioStreamReceiver);*/
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {

            mExoPlayer.seekTo(0);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            Log.i(TAG, "Skip To NEXT");
        }

    }

    private void handleAudioFocus() {

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        mExoPlayer.setAudioAttributes(audioAttributes, true);
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(userContext, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mSaBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mSaBuilder.build());
        mMediaSession.setCallback(new mySessionCallback());
        mMediaSession.setActive(true);
    }

    public void releasePlayer() {

        if(mExoPlayer != null) {
            mExoPlayer.stop();
            /*mNotificationManager.cancelAll();*/
            /*mExoPlayer.release();*/
            mExoPlayer = null;
        }
    }

    /*public String getArtistsNames(){

        StringBuilder stringBuilder = new StringBuilder();
        ArtistPreview[] artistPreviews = mTrack.getArtists();

        for (int index = 0 ; index < artistPreviews.length ; index++){

            stringBuilder.append(artistPreviews[index].getName());
        }
        return stringBuilder.toString();
    }*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    /*private void showNotification(PlaybackStateCompat state) {

        Log.v(TAG, "Open Show Notification");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel name";
            *//*String description = userContext.getString(R.string.channel_description);*//*
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
            *//*channel.setDescription(description);*//*
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
            mExoPlayer.stop();

        }


        androidx.core.app.NotificationCompat.Builder builder = new
                androidx.core.app.NotificationCompat.Builder(userContext, NotificationChannel.DEFAULT_CHANNEL_ID);


        int playPauseIcon;
        int nextIcon = R.drawable.my_icon_next;
        int prevIcon = R.drawable.my_icon_prev;
        int likeIcon = R.drawable.ic_heart_thin;

        String audioName = mTrack.getName();
        String artistName = getArtistsNames();
        String like = "like";
        String next = "next";
        String play_pause;

        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            playPauseIcon = R.drawable.exo_controls_pause;
            play_pause = userContext.getString(R.string.pause);
        } else {
            playPauseIcon = R.drawable.exo_controls_play;
            play_pause = userContext.getString(R.string.play);
        }

        androidx.core.app.NotificationCompat.Action playPauseAction;


        playPauseAction = new androidx.core.app.NotificationCompat.Action(
                playPauseIcon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(userContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));


        androidx.core.app.NotificationCompat.Action restartAction = new androidx.core.app.NotificationCompat.Action
                (prevIcon, userContext.getString(R.string.restart),
                        MediaButtonReceiver.buildMediaButtonPendingIntent
                                (userContext, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        androidx.core.app.NotificationCompat.Action nextAction = new androidx.core.app.NotificationCompat.Action(nextIcon, next,
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (userContext, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));
        androidx.core.app.NotificationCompat.Action likeAction = new androidx.core.app.NotificationCompat.Action
                (likeIcon, like, null);

        Intent notificationIntent = new Intent(userContext, UserActivity.class);
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle1.putBoolean("openPlayerFragment",true);
        bundle2.putString(Constants.USER_ID_KEY,"user0");
        notificationIntent.putExtras(bundle1);
        notificationIntent.putExtras(bundle2);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (userContext, 0, notificationIntent, 0);

        MediaSessionCompat.Token token = mMediaSession.getSessionToken();

        NotificationCompat.MediaStyle nM = new NotificationCompat.MediaStyle();

        nM.setMediaSession(token);
        nM.setShowActionsInCompactView(0, 1, 2, 3);

        final Bitmap[] largeIcon = new Bitmap[1];

        Glide.with(userContext)
                .asBitmap()
                .load(mAlbum.getImage())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,  Transition<? super Bitmap> transition) {
                        largeIcon[0] = resource;
                        builder.setLargeIcon(largeIcon[0]);

                    }

                    @Override
                    public void onLoadCleared( Drawable placeholder) {
                    }
                });


        builder.setContentTitle(audioName)
                .setContentText(artistName)
                .setSmallIcon(R.drawable.ic_oud)
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(contentPendingIntent)
                .addAction(likeAction)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .addAction(nextAction)
                .setShowWhen(false)
                .setStyle(nM);

        mNotificationManager.notify(0, builder.build());
    }*/

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)*/
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playWhenReady && playbackState == mExoPlayer.STATE_READY) {

            Log.d(TAG, "player is Playing");

            resumePlayback(mExoPlayer.getCurrentPosition());

            bundle.putInt(Constants.CURRENT_PLAYBACK_STATE_SENDING_FROM_EXOPLAYER, PlaybackStateCompat.STATE_PLAYING);
            bundle.putLong(Constants.POSITION_OF_PLAYBACK_ON_SEEK, mExoPlayer.getCurrentPosition());

            intentState.putExtras(bundle);
            intentState.setAction(String.valueOf(Constants.IntentAction.STATE_OF_PLAYBACK_COMING_FROM_EXOPLAYER));

            userContext.sendBroadcast(intentState);


        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_IDLE) {

            Log.d("UserActivity", "player in " + "problem");

        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_ENDED) {

            MediaControllerCompat.getMediaController((UserActivity) userContext).getTransportControls().seekTo(mExoPlayer.getCurrentPosition());
            MediaControllerCompat.getMediaController((UserActivity) userContext).getTransportControls().skipToNext();
            /*mSaBuilder.setState(PlaybackStateCompat.STATE_STOPPED, mExoPlayer.getCurrentPosition(), 1f);*/
            Log.d(TAG, "playback is " + "Ended");

        } else {

            /*mSaBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);*/

            /*MediaControllerCompat.getMediaController((UserActivity) userContext).getTransportControls().seekTo(mExoPlayer.getCurrentPosition());
            MediaControllerCompat.getMediaController((UserActivity) userContext).getTransportControls().pause();*/
            putPause();
            bundle.putInt(Constants.CURRENT_PLAYBACK_STATE_SENDING_FROM_EXOPLAYER,PlaybackStateCompat.STATE_PAUSED);
            bundle.putLong(Constants.POSITION_OF_PLAYBACK_ON_SEEK,mExoPlayer.getCurrentPosition());

            intentState.putExtras(bundle);
            intentState.setAction(String.valueOf(Constants.IntentAction.STATE_OF_PLAYBACK_COMING_FROM_EXOPLAYER));

            userContext.sendBroadcast(intentState);
            Log.d(TAG, "player is " + "pausing");
        }


        /*mMediaSession.setPlaybackState(mSaBuilder.build());*/
       /* showNotification(mSaBuilder.build());*/
    }

    @Override
    public void onSeekProcessed() {

        if(MediaControllerCompat.getMediaController((UserActivity) userContext) != null) {

            MediaControllerCompat.getMediaController((UserActivity) userContext).getTransportControls().seekTo(mExoPlayer.getCurrentPosition());

            Log.d(TAG,"Sending SeekRequest to mediaSession");
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

        if(MediaControllerCompat.getMediaController((UserActivity) userContext) != null){

            MediaControllerCompat.getMediaController((UserActivity) userContext).
                    getTransportControls().setRepeatMode(repeatMode);

            Log.d(TAG,"Sending RepeatRequest to mediaSession");

            return;
        }
        Log.d(TAG,"mediaController is null");

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        if(MediaControllerCompat.getMediaController((UserActivity) userContext) == null){

            Log.d(TAG,"mediaController is null");
            return;
        }

        if(shuffleModeEnabled){

                MediaControllerCompat.getMediaController((UserActivity) userContext).
                        getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);

                Log.d(TAG,"Sending ShuffleRequest to mediaSession");
            }
        else{

            MediaControllerCompat.getMediaController((UserActivity) userContext).
                    getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);

            Log.d(TAG,"Sending ShuffleRequest to mediaSession");
        }

    }

    public void playCurrentPlayback(){

        if(mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
        Log.d(TAG,"ExoPlayer is null");
    }

    public void pauseCurrentPlayback(){

        if(mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
        Log.d(TAG,"ExoPlayer is null");

    }

    public Boolean isInTheBeginning(){

        if(mExoPlayer != null) {

            return mExoPlayer.getCurrentPosition() == 0;
        }
        Log.d(TAG,"ExoPlayer is null");
        return null;
    }

    public void resetPlayback(){

        if( mExoPlayer != null ) {
            mExoPlayer.seekTo(0);
        }
        Log.d(TAG,"ExoPlayer is null");

    }

    public void preparePlayback(MediaMetadataCompat currentPlayback){

        mediaMetadataCompat = currentPlayback;

        if(mExoPlayer == null){

            initPlayer();

        }
       /* String userAgent = Util.getUserAgent(userContext, "Oud");*/

        try {
            playCurrentPlayback();
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentPlayback.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)),cacheDataSourceFactory
                    , new DefaultExtractorsFactory(),
                    new Handler(),null);

            playCurrentPlayback();
            mExoPlayer.prepare(mediaSource);
            Log.d(TAG, "onPlayerStateChanged: PREPARE");

        } catch (Exception e) {

            throw new RuntimeException("Failed to play media uri: "
                    + currentPlayback.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI), e);
        }

    }

    private void initPlayer() {

        mExoPlayer = new SimpleExoPlayer.Builder(userContext).build();
        cacheDataSourceFactory =
                new com.example.oud.user.player.CacheDataSourceFactory(userContext,
                        100 * 1024 * 1024,
                        5 * 1024 * 1024);
        mExoPlayer.addListener(this);
        handleAudioFocus();

    }

    public MediaMetadataCompat getCurrentPlayback(){

        if(mediaMetadataCompat != null) {

            return mediaMetadataCompat;
        }
        Log.d(TAG, "Metadata is null" );
        return null;
    }

    private void putPause(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://oud-zerobase.me/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OudApi oudApi = retrofit.create(OudApi.class);

        Call<StatusMessageResponse> call = oudApi.pausePlayback(OudUtils.getToken(userContext));
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {

                StatusMessageResponse statusMessageResponse;

                if(!response.isSuccessful()){

                    Log.e(TAG, "Response code: "+response.code());

                }

                statusMessageResponse = response.body();

                Log.d(TAG, statusMessageResponse.getStatus());

                Log.d(TAG, statusMessageResponse.getMessage());

            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {

                Log.e(TAG, t.getMessage());
            }
        });

    }

    private void resumePlayback(Long positionMs){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://oud-zerobase.me/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OudApi oudApi = retrofit.create(OudApi.class);
        StartOrResumePlayback startOrResumePlayback = new StartOrResumePlayback(positionMs);

        Call<StatusMessageResponse> call = oudApi.startOrResumeTrack(OudUtils.getToken(userContext), startOrResumePlayback);
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {

                StatusMessageResponse statusMessageResponse;

                if(!response.isSuccessful()){

                    Log.e(TAG, "Response code: "+response.code());

                }

                statusMessageResponse = response.body();

                Log.d(TAG, statusMessageResponse.getStatus());

                Log.d(TAG, statusMessageResponse.getMessage());

            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {

                Log.e(TAG, t.getMessage());
            }
        });
    }

}
