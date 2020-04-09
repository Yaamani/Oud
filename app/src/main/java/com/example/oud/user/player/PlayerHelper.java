package com.example.oud.user.player;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.Track;
import com.example.oud.user.UserActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;


//todo save the state of player when destroy the program to get it again
public class PlayerHelper implements ExoPlayer.EventListener {

    private final String TAG = "UserActivity";

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;

    private Context userContext;

    private PlaybackStateCompat.Builder mSaBuilder;
    private NotificationManager mNotificationManager;

    private String mTrackId;
    private boolean resetPlay;

    private Track mTrack;
    private Album mAlbum;
    private Activity mUserActivity;
    private boolean mGetPlayerBack;

    public PlayerHelper(Context context, NotificationManager nM ) {

        userContext = context;
        mNotificationManager = nM;

        /*mUserActivity = activity;*/
        initializeMediaSession();

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

            String userAgent = Util.getUserAgent(userContext, "AudioApp");

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
            mNotificationManager.cancelAll();
            /*mExoPlayer.release();*/
            mExoPlayer = null;
        }
    }

    public String getArtistsNames(){

        StringBuilder stringBuilder = new StringBuilder();
        ArtistPreview[] artistPreviews = mTrack.getArtists();

        for (int index = 0 ; index < artistPreviews.length ; index++){

            stringBuilder.append(artistPreviews[index].getName());
        }
        return stringBuilder.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNotification(PlaybackStateCompat state) {

        Log.v(TAG, "Open Show Notification");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel name";
            /*String description = userContext.getString(R.string.channel_description);*/
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
            /*channel.setDescription(description);*/
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
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        /* todo mSaBuilder in another states*/
        if (playWhenReady && playbackState == mExoPlayer.STATE_READY) {

            Log.i(TAG, "player is Playing");
            mSaBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            /*Toast.makeText(getContext(), "player is playing ", Toast.LENGTH_SHORT).show();*/
        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_IDLE) {
            Log.e("UserActivity", "player in " + "problem");
        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_ENDED) {
            /*mSaBuilder.setState(PlaybackStateCompat.STATE_STOPPED, mExoPlayer.getCurrentPosition(), 1f);*/
            /*Toast.makeText(getContext(), "player is ended", Toast.LENGTH_SHORT).show();*/
        } else {

            mSaBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            Log.i(TAG, "player is " + "pausing");
        }

        mMediaSession.setPlaybackState(mSaBuilder.build());
       /* showNotification(mSaBuilder.build());*/
    }

    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {

        if(mExoPlayer.TIMELINE_CHANGE_REASON_PREPARED == reason){

            Log.i(TAG,"TIMELINE_CHANGE_REASON_PREPARED");
        }
        else if(mExoPlayer.TIMELINE_CHANGE_REASON_RESET == reason){

            Log.i(TAG,"TIMELINE_CHANGE_REASON_RESET");
        }
        else if(mExoPlayer.TIMELINE_CHANGE_REASON_DYNAMIC == reason){

            Log.i(TAG,"TIMELINE_CHANGE_REASON_DYNAMIC");

        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

        if(isLoading) {
            Toast.makeText(userContext, "isLoading", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(userContext,"noLoading",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSeekProcessed() {

        Toast.makeText(userContext,"onSeekProcessed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

        if(mExoPlayer.DISCONTINUITY_REASON_SEEK == reason){

            Log.i(TAG,"DISCONTINUITY_REASON_SEEK");
        }
        else if(mExoPlayer.DISCONTINUITY_REASON_SEEK_ADJUSTMENT==reason){

            Log.i(TAG,"DISCONTINUITY_REASON_SEEK_ADJUSTMENT");
        }
        else if(mExoPlayer.DISCONTINUITY_REASON_INTERNAL == reason){

            Log.i(TAG,"DISCONTINUITY_REASON_INTERNAL");
        }
        else if(mExoPlayer.DISCONTINUITY_REASON_AD_INSERTION == reason){

            Log.i(TAG,"DISCONTINUITY_REASON_AD_INSERTION");

        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {


        /*Toast.makeText(TAG , error.getMessage(),Toast.LENGTH_LONG).show();*/
        Log.e(TAG,error.getMessage());
        //todo here will handle connection
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        if (repeatMode == Player.REPEAT_MODE_ALL) {
            /*Toast.makeText(getContext(), "player will repeat times", Toast.LENGTH_SHORT).show();*/
        } else if (repeatMode == Player.REPEAT_MODE_ONE) {
            /*Toast.makeText(getContext(), "player will repeat one time", Toast.LENGTH_SHORT).show();*/
        } else {
            /*Toast.makeText(getContext(), "player will not repeat", Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        if (shuffleModeEnabled) {
            /*Toast.makeText(getContext(), "shuffle is happening", Toast.LENGTH_SHORT).show();*/
        } else {
            /*Toast.makeText(getContext(), "shuffle is not happening", Toast.LENGTH_SHORT).show();*/
        }

    }
}
