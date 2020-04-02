package com.example.oud.user.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.media.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;


import com.example.oud.R;
import com.example.oud.user.UserActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


//todo save the state of player when destroy the program to get it again
public class PlayerHelper implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private final String TAG = "PlayerFragment";
    private static MediaSessionCompat mMediaSession;
    private Context userContext;
    /*private Song mSong;*/ //here will get id of song
    private PlaybackStateCompat.Builder mSaBuilder;
    private NotificationManager mNotificationManager;


    public PlayerHelper(Context context, NotificationManager nM) {

        userContext = context;
        mNotificationManager = nM;

        initializePlayer();
        initializeMediaSession();
        handleAudioFocus();

    }

    public PlaybackStateCompat.Builder getSaBuilder() {

        return mSaBuilder;
    }

    public static MediaSessionCompat getMediaSession() {
        return mMediaSession;
    }

    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public void initializePlayer() {

        if (mExoPlayer == null) {

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(userContext, trackSelector, loadControl);
            /* mExoPlayer = ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector(trackSelectionFactory));*/

            String userAgent = Util.getUserAgent(userContext, "AudioApp");

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(/*mSong.getUri()*/"asset:///toccata_fugue.mp3"),
                    new DefaultDataSourceFactory(userContext, userAgent), new DefaultExtractorsFactory(),
                    null/*new Handler()*/, null);

            mExoPlayer.prepare(mediaSource);
            /*mExoPlayer.setPlayWhenReady(true);*/
            mExoPlayer.addListener(this);

        }

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

        mExoPlayer.stop();
        mNotificationManager.cancelAll();
        mExoPlayer.release();
        mExoPlayer = null;
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

        }


        androidx.core.app.NotificationCompat.Builder builder = new
                androidx.core.app.NotificationCompat.Builder(userContext, NotificationChannel.DEFAULT_CHANNEL_ID);


        int playPauseIcon;
        int nextIcon = R.drawable.my_icon_next;
        int prevIcon = R.drawable.my_icon_prev;
        int likeIcon = R.drawable.ic_heart_thin;

        String audioName = "978";
        String artistName = "mohamed";
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

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (userContext, 0, new Intent(userContext, UserActivity.class), 0);

        MediaSessionCompat.Token token = mMediaSession.getSessionToken();

        NotificationCompat.MediaStyle nM = new NotificationCompat.MediaStyle();

        nM.setMediaSession(token);
        nM.setShowActionsInCompactView(0, 1, 2, 3);

        builder.setContentTitle(audioName)
                .setContentText(artistName)
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
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

            Log.i("PlayerFragment", "player is Playing");
            mSaBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            /*Toast.makeText(getContext(), "player is playing ", Toast.LENGTH_SHORT).show();*/
        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_IDLE) {
            Log.i("PlayerFragment", "player in " + "problem");
        } else if (mExoPlayer.getPlaybackState() == mExoPlayer.STATE_ENDED) {
            /*mSaBuilder.setState(PlaybackStateCompat.STATE_STOPPED, mExoPlayer.getCurrentPosition(), 1f);*/
            /*Toast.makeText(getContext(), "player is ended", Toast.LENGTH_SHORT).show();*/
        } else {

            mSaBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
            Log.i("PlayerFragment", "player is " + "pausing");
        }

        mMediaSession.setPlaybackState(mSaBuilder.build());
        //showNotification(mSaBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

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
