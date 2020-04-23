package com.example.oud.user.player;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.CurrentPlayback;
import com.example.oud.user.UserActivity;

import java.util.ArrayList;
import java.util.List;

public class MediaService extends MediaBrowserServiceCompat {

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mSaBuilder;

    private final String TAG = MediaService.class.getSimpleName();

    private StartOrResumePlaybackReceiver startOrResumePlaybackReceiver;
    /*private MediaReceiver mediaButtonReceiver;*/
    private BecomingNoisyReceiver becomingNoisyReceiver;
    private PlayerInfoReceiver playerInfoReceiver;


    private NotificationManager mNotificationManager;
    private MediaMetadataCompat mediaMetadata;

    private Bundle bundle;
    private Intent intentState;

    private PlayerServiceHelper playerServiceHelper;

    private MutableLiveData<CurrentPlayback> currentlyPlaybackMutableLiveData;


    @Override
    public void onCreate() {
        super.onCreate();

        initializeMediaSession();
        Log.d(TAG,"On Create OF Service");

        bundle = new Bundle();
        intentState = new Intent();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        playerServiceHelper = new PlayerServiceHelper();

        currentlyPlaybackMutableLiveData = new MutableLiveData<>();

        initStartOrResumeBroadCast();
        /*initMediaButtonReceiver();*/
        initNoisyReceiver();
        initPlayerInfoReceiver();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Log.d(TAG,"On Start Command of Service");

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        if(startOrResumePlaybackReceiver != null){

            unregisterReceiver(startOrResumePlaybackReceiver);
        }

        /*if(mediaButtonReceiver != null){

            unregisterReceiver(mediaButtonReceiver);
        }*/

        if(becomingNoisyReceiver != null){

            unregisterReceiver(becomingNoisyReceiver);
        }

        if(playerInfoReceiver != null){

            unregisterReceiver(playerInfoReceiver);
        }


        Log.d(TAG,"On Task Removed");

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bundle.putInt(Constants.CURRENT_PLAYBACK_STATE, PlaybackStateCompat.STATE_STOPPED);

        intentState.putExtras(bundle);
        sendBroadcast(intentState);
        mMediaSession.setActive(false);
        mNotificationManager.cancelAll();
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

    private void initStartOrResumeBroadCast(){

        IntentFilter intentFilter = new IntentFilter(String.valueOf(Constants.IntentAction.START_OR_RESUME_BROADCAST));
        startOrResumePlaybackReceiver = new StartOrResumePlaybackReceiver();
        registerReceiver(startOrResumePlaybackReceiver,intentFilter);

    }

    private void initNoisyReceiver() {

        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        becomingNoisyReceiver = new BecomingNoisyReceiver();
        registerReceiver(becomingNoisyReceiver,intentFilter);
    }

   /* private void initMediaButtonReceiver() {

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonReceiver = new MediaReceiver();
        registerReceiver(mediaButtonReceiver,intentFilter);
    }*/

    private void initPlayerInfoReceiver() {

        IntentFilter intentFilter = new
                IntentFilter(String.valueOf(Constants.IntentAction.STATE_OF_PLAYBACK_COMING_FROM_EXOPLAYER));
        playerInfoReceiver = new PlayerInfoReceiver();
        registerReceiver(playerInfoReceiver,intentFilter);

    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mSaBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mSaBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());

        setSessionToken(mMediaSession.getSessionToken());
        //MediaMetadataCompat mediaMetadataCompat = new MediaMetadataCompat();
        /*mMediaSession.setActive(true);*/

    }

    public class MySessionCallback extends MediaSessionCompat.Callback {

        private Intent intent = new Intent(String.valueOf(Constants.IntentAction.STATE_OF_PLAYBACK));
        private long positionOfPlayback = 0;
        private String mToken;

        @Override
        public void onPrepare() {

            //todo fetch currentlyPlayback

            playerServiceHelper.getTrackMutableLiveData(mToken).observe((LifecycleOwner) this, currentPlayback -> {

                String songName = currentPlayback.getTrack().getName();
                ArtistPreview[] artistName = currentPlayback.getTrack().getArtists();
                String artistsNames = getArtistsNames(artistName);
                String audioUrl = currentPlayback.getTrack().getAudioUrl();
                String albumImage = currentPlayback.getTrack().getAlbum().getImage();
                String albumName = currentPlayback.getTrack().getAlbum().getName();

                mediaMetadata = new MediaMetadataCompat.Builder().putString(MediaMetadataCompat.METADATA_KEY_TITLE, songName)
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, audioUrl)
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, albumName)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artistsNames)
                        .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, albumImage)
                        .build();

                mMediaSession.setMetadata(mediaMetadata);

                bundle.putInt(Constants.CURRENT_PLAYBACK_STATE, Constants.STATE_PREPARING);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
            );


            Log.d(TAG,"On Prepare is Called");
        }

        @Override
        public void onPlay() {

            bundle.putInt(Constants.CURRENT_PLAYBACK_STATE, PlaybackStateCompat.STATE_PLAYING);

            intent.putExtras(bundle);

            sendBroadcast(intent);

            Log.d(TAG,"On Play Callback of MediaSession is Called");

        }

        @Override
        public void onPause() {


            bundle.putInt(Constants.CURRENT_PLAYBACK_STATE, PlaybackStateCompat.STATE_PAUSED);

            intent.putExtras(bundle);

            sendBroadcast(intent);

            Log.d(TAG,"On Pause Callback of MediaSession Called");
        }

        @Override
        public void onSkipToPrevious() {

            if(positionOfPlayback != 0){

                bundle.putInt(Constants.CURRENT_PLAYBACK_STATE, PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS);

                intent.putExtras(bundle);

                sendBroadcast(intent);
                onSeekTo(0);

            }
            else{

                playerServiceHelper.postSkipToPrev(mToken);
                //todo postSkipToPrevious
                onPrepare();

            }
            Log.d(TAG,"On Previous Callback of MediaSession Called");

        }

        @Override
        public void onSkipToNext() {

            playerServiceHelper.postSkipToNext(mToken);
            onPrepare();
            Log.d(TAG,"On Next Callback of MediaSession Called");
        }

        @Override
        public void onStop() {

            Log.d(TAG,"On Stop Callback of MediaSession Called");

        }

        @Override
        public void onSeekTo(long pos) {

            playerServiceHelper.putSeekTo(mToken, pos);
            positionOfPlayback = pos;
            Log.d(TAG,"On seek Callback of MediaSession Called");
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {

            Log.d(TAG,"Repeat Mode: "+repeatMode);
            if(repeatMode == 0){

                playerServiceHelper.putRepeatMode(mToken, "off");
            }
            else if(repeatMode == 1){playerServiceHelper.putRepeatMode(mToken, "track");}
            else if(repeatMode == 2){playerServiceHelper.putRepeatMode(mToken, "context");}
            Log.d(TAG,"On serRepeatMode Callback of MediaSession Called");
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {

            if(shuffleMode == 0) {
                playerServiceHelper.putShuffleEnable(mToken, false);
            }else{
                playerServiceHelper.putShuffleEnable(mToken, true);
            }
            Log.d(TAG,"On shuffle Callback of MediaSession Called");
        }

        public void setToken(String token){

            mToken = token;

        }

        private String getArtistsNames(ArtistPreview[] artistPreviews){

            StringBuilder stringBuilder = new StringBuilder();

            for (int index = 0 ; index < artistPreviews.length ; index++){

                stringBuilder.append(artistPreviews[index].getName());
            }
            return stringBuilder.toString();
        }

    }

    public class PlayerInfoReceiver extends  BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                if (intent.getExtras() != null) {

                    int state = intent.getExtras().getInt(Constants.CURRENT_PLAYBACK_STATE_SENDING_FROM_EXOPLAYER);
                    long position = intent.getExtras().getLong(Constants.POSITION_OF_PLAYBACK_ON_SEEK);

                    switch (state) {

                        case PlaybackStateCompat.STATE_PLAYING:

                            mSaBuilder.setState(PlaybackStateCompat.STATE_PLAYING, position,1f);
                            break;
                        case PlaybackStateCompat.STATE_PAUSED:
                            mSaBuilder.setState(PlaybackStateCompat.STATE_PAUSED, position,1f);
                            break;

                    }

                    Log.d(TAG,"PlayerInfoReceiver is Called");

                    mMediaSession.setPlaybackState(mSaBuilder.build());
                    showNotification(mSaBuilder.build());

                }
                else{

                    Log.d(TAG, "bundle of currentPlaybackState which is Coming from ExoPlayer is null");
                }
            } else {

                Log.d(TAG, "intent of currentPlaybackState from ExoPlayer is null");
            }
        }
    }

    public class StartOrResumePlaybackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG,"StartOrResumePlaybackReceiver is Called");

            if(intent != null){

                if(intent.getExtras() != null){

                    String contextUri = intent.getExtras().getString(Constants.CONTEXT_URI);
                    Integer offset = intent.getExtras().getInt(Constants.OFFSET);
                    String token = intent.getExtras().getString(Constants.SHARED_PREFERENCES_TOKEN_NAME);
                    ArrayList<String> uris = intent.getExtras().getStringArrayList(Constants.LIST_OF_TRACKS_URIS);

                    Log.d(TAG,"ContextUri is:" + contextUri);
                    Log.d(TAG,"Offset is:" + offset);
                    Log.d(TAG,"Token is:" + token);

                    if(uris == null && offset == -1) {

                        playerServiceHelper.putStartTrack(contextUri, offset, token);

                    }
                    else{

                        playerServiceHelper.putStartTrack(uris, token);

                    }

                    MySessionCallback mySessionCallback = new MySessionCallback();
                    mySessionCallback.setToken(token);
                    mySessionCallback.onPrepare();

                }else {

                    Log.d(TAG, "Can't send StartOrResume request bec bundle is null");
                }
            }else {

                Log.d(TAG, "Can't send StartOrResume request bec intent is null");
            }
        }
    }

    public class BecomingNoisyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {

                bundle.putInt(Constants.CURRENT_PLAYBACK_STATE,PlaybackStateCompat.STATE_PAUSED);

                intentState.putExtras(bundle);
                sendBroadcast(intentState);

            }
        }
    }

    // for handle button click in notification
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            MediaButtonReceiver.handleIntent(mMediaSession, intent);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(PlaybackStateCompat state) {

        Log.d(TAG, "Open Show Notification");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);

        }

        androidx.core.app.NotificationCompat.Builder builder = new
                androidx.core.app.NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID);

        int playPauseIcon;
        int nextIcon = R.drawable.my_icon_next;
        int prevIcon = R.drawable.my_icon_prev;
        int likeIcon = R.drawable.ic_heart_thin;

        String audioName = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE);
        String artistName = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST);
        String like = "like";
        String next = "next";
        String play_pause;

        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            playPauseIcon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            playPauseIcon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }

        androidx.core.app.NotificationCompat.Action playPauseAction;


        playPauseAction = new androidx.core.app.NotificationCompat.Action(
                playPauseIcon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));


        androidx.core.app.NotificationCompat.Action restartAction = new androidx.core.app.NotificationCompat.Action
                (prevIcon, this.getString(R.string.restart),
                        MediaButtonReceiver.buildMediaButtonPendingIntent
                                (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        androidx.core.app.NotificationCompat.Action nextAction = new androidx.core.app.NotificationCompat.Action(nextIcon, next,
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));
        androidx.core.app.NotificationCompat.Action likeAction = new androidx.core.app.NotificationCompat.Action
                (likeIcon, like, null);

        Intent notificationIntent = new Intent(this, UserActivity.class);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.OPEN_BIG_PLAYER,true);
        notificationIntent.putExtras(bundle);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        MediaSessionCompat.Token token = mMediaSession.getSessionToken();

        NotificationCompat.MediaStyle nM = new NotificationCompat.MediaStyle();

        nM.setMediaSession(token);
        nM.setShowActionsInCompactView(0, 1, 2, 3);

        /*final Bitmap[] largeIcon = new Bitmap[1];

        Glide.with(this)
                .asBitmap()
                .load(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                        largeIcon[0] = resource;
                        builder.setContentTitle(audioName)
                                .setContentText(artistName)
                                .setSmallIcon(R.drawable.ic_oud)
                                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                                .setContentIntent(contentPendingIntent)
                                .addAction(likeAction)
                                .addAction(restartAction)
                                .addAction(playPauseAction)
                                .addAction(nextAction)
                                .setOngoing(true)
                                .setShowWhen(false)
                                .setLargeIcon(largeIcon[0])
                                .setStyle(nM);

                    }

                    @Override
                    public void onLoadCleared( Drawable placeholder) {

                        builder.setContentTitle(audioName)
                                .setContentText(artistName)
                                .setSmallIcon(R.drawable.ic_oud)
                                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                                .setContentIntent(contentPendingIntent)
                                .addAction(likeAction)
                                .addAction(restartAction)
                                .addAction(playPauseAction)
                                .addAction(nextAction)
                                .setOngoing(true)
                                .setShowWhen(false)
                                .setStyle(nM);
                    }
                });*/
        builder.setContentTitle(audioName)
                .setContentText(artistName)
                .setSmallIcon(R.drawable.ic_oud)
                .setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(contentPendingIntent)
                .addAction(likeAction)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .addAction(nextAction)
                .setOngoing(true)
                .setShowWhen(false)
                .setStyle(nM);

        mNotificationManager.notify(0, builder.build());
    }

}
