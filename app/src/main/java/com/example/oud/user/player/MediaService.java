package com.example.oud.user.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import java.util.ArrayList;
import java.util.List;

public class MediaService extends MediaBrowserServiceCompat {

    private List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>(); // should contain playlist of songs
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mSaBuilder;
    private PlayerHelper playback;


    @Override
    public void onCreate() {
        super.onCreate();

        initializeMediaSession();
        /*playback = new PlayerHelper();*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        mMediaSession.setActive(false);
        playback.releasePlayer();
        stopSelf();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {


        return new BrowserRoot("none",null);

    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

            result.sendResult(null);

    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(this, "com.example.android.audioapp.MediaService");
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        /*mMediaSession.setMediaButtonReceiver(null);*/

        mSaBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mSaBuilder.build());
        mMediaSession.setCallback(new mySessionCallback());

        setSessionToken(mMediaSession.getSessionToken());
        //MediaMetadataCompat mediaMetadataCompat = new MediaMetadataCompat();
        /*mMediaSession.setActive(true);*/
    }

    public class mySessionCallback extends MediaSessionCompat.Callback {




       /* @Override
        public void onPlay() {

            mMediaSession.setActive(true);
            playback.play();

        }

        @Override
        public void onPause() {

            playback.pause();

        }*/

        @Override
        public void onSkipToPrevious() {



        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();

        }

        @Override
        public void onStop() {
            super.onStop();
            Log.e("MediaService","Stop in mediaSession callBack");

        }
    }
}
