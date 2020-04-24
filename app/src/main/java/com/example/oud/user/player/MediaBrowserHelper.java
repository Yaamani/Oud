package com.example.oud.user.player;

import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.media.MediaBrowserServiceCompat;

import com.example.oud.user.UserActivity;

public class MediaBrowserHelper {


    private static final String TAG = MediaBrowserHelper.class.getSimpleName();
    private Context context;

    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat mediaControllerCompat;
    private MediaBrowserConnectionCallback connectionCallback;
    private MediaControllerCallback mediaControllerCallback;
    private MediaBrowserHelperCallback mediaBrowserHelperCallback;
    private Class<? extends MediaBrowserServiceCompat> mediaBrowserServiceCompat;
    private MediaMetadataCompat mediaMetadata;


    public MediaBrowserHelper(Context context, Class<? extends MediaBrowserServiceCompat> mediaBrowserServiceCompatClass){

        this.context = context;

        mediaBrowserServiceCompat = mediaBrowserServiceCompatClass;


        connectionCallback = new MediaBrowserConnectionCallback();
        mediaControllerCallback = new MediaControllerCallback();


    }

    public void setMediaBrowserHelperCallback(MediaBrowserHelperCallback callback){

        mediaBrowserHelperCallback = callback;

    }

    public void startTheService(){

        if(mediaBrowserCompat == null) {

            mediaBrowserCompat = new MediaBrowserCompat(context,
                    new ComponentName(context, mediaBrowserServiceCompat),
                    connectionCallback, null);
        }
        mediaBrowserCompat.connect();
        Log.d(TAG, "onStart: CALLED : creating media browser and connecting");
    }

    public void stopTheService(){

        if(mediaBrowserCompat != null && mediaBrowserCompat.isConnected()){

            mediaBrowserCompat.disconnect();
            mediaBrowserCompat = null;

        }
        if(mediaControllerCompat != null){

            mediaControllerCompat.unregisterCallback(mediaControllerCallback);
            mediaControllerCompat = null;
        }
        Log.d(TAG, "onStop: CALLED: Releasing MediaController, Disconnecting from MediaBrowser");
    }

    public boolean isConnected(){

        if(mediaBrowserCompat != null) {
            return mediaBrowserCompat.isConnected();
        }
        return false;
    }

    public void setPlaylistInfo(Bundle bundle){}

    private class MediaBrowserConnectionCallback extends MediaBrowserCompat.ConnectionCallback{

        @Override
        public void onConnected() {
            super.onConnected();

            MediaSessionCompat.Token token = mediaBrowserCompat.getSessionToken();
            try {
                mediaControllerCompat = new MediaControllerCompat(context, token);
                MediaControllerCompat.setMediaController((UserActivity)context, mediaControllerCompat);

                mediaControllerCompat.registerCallback(mediaControllerCallback);

            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }

        }

        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
        }
    }

    private class MediaControllerCallback extends MediaControllerCompat.Callback{

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {

            mediaMetadata = metadata;
           /* mediaBrowserHelperCallback.mediaMetaDataChanged(metadata);*/
            Log.d(TAG,"onMetadataChanged is Called");

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            super.onRepeatModeChanged(repeatMode);
        }

        @Override
        public void onShuffleModeChanged(int shuffleMode) {
            super.onShuffleModeChanged(shuffleMode);
        }

    }

    public MediaControllerCompat.TransportControls getTransportControls(){

        if(mediaControllerCompat == null){
            Log.d(TAG, "getTransportControls: mediaController is null!");
            throw new IllegalStateException("mediaController is null!");
        }
        return mediaControllerCompat.getTransportControls();
    }

    public MediaMetadataCompat getMediaMetadata(){

        return mediaControllerCompat.getMetadata();

    }


}
