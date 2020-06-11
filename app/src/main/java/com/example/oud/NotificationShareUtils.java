package com.example.oud;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.oud.api.ArtistPreview;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.authentication.MainActivity;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationShareUtils {

    private static final String TAG = NotificationShareUtils.class.getSimpleName();

    private static NotificationShareDestination notificationShareDestination = null;

    /**
     *
     * @param i The {@link Intent} that opens the {@link MainActivity}.
     */
    public static void handleNotificationShareDestinationMainActivity(Intent i) {
        NotificationShareDestination receivedDestination = new NotificationShareDestination();

        Bundle extras = i.getExtras();
        if (extras != null) {

            Log.d(TAG, "handleNotificationDestinationMainActivity: " + extras.toString());


            if (extras.getString(Constants.NOTIFICATION_SHARE_DESTINATION_KEY) != null) {
                receivedDestination.destination = extras.getString(Constants.NOTIFICATION_SHARE_DESTINATION_KEY);
                receivedDestination.id = extras.getString(Constants.ID_KEY);

                NotificationShareUtils.notificationShareDestination = receivedDestination;
            }
        }

        Uri shareData = i.getData();
        if (shareData != null) {

            LinkedList<String> pathSegments = new LinkedList<>(shareData.getPathSegments());

            if (pathSegments.size() > 0) {
                receivedDestination.destination = pathSegments.peekFirst();
                receivedDestination.id = pathSegments.peekLast();

                NotificationShareUtils.notificationShareDestination = receivedDestination;
            }

        }
    }

    /**
     * When following an artist, the device must subscribe to this artist's notification topic to receive a notification upon releasing a new album.
     * @param artistId
     */
    public static void subscribeToFollowedArtistTopic(String artistId) {
        FirebaseMessaging.getInstance().subscribeToTopic(artistId)
                .addOnCompleteListener(task -> Log.d(TAG, "Subscribed to " + artistId));
    }

    /**
     * When unfollowing an artist, the device must unsubscribe from this artist's notification topic to stop receiving notifications related to this artist.
     * @param artistId
     */
    public static void unsubscribeFromFollowedArtistTopic(String artistId) {
        FirebaseMessaging.getInstance().subscribeToTopic(artistId)
                .addOnCompleteListener(task -> Log.d(TAG, "Unsubscribed from " + artistId));
    }

    /**
     * Show a toast containing the notification body when a notification is received while the app is in the foreground.
     * @param remoteMessage
     * @param applicationContext
     */
    public static void foregroundNotification(RemoteMessage remoteMessage, Context applicationContext) {
        if (remoteMessage.getNotification() != null) {
            String text = remoteMessage.getNotification().getBody();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * When logging in, the device must subscribe to all the followed artist's notification topics to receive a notification upon releasing a new album.
     * @param token
     * @param oudApi
     */
    public static void subscribeToAllFollowedArtistsTopicsUponLoggingIn(String token, OudApi oudApi) {

        Call<OudList<ArtistPreview>> followCall = oudApi.getArtistsFollowedByCurrentUser(token, 50, 0);
        followCall.enqueue(new Callback<OudList<ArtistPreview>>() {
            @Override
            public void onResponse(Call<OudList<ArtistPreview>> call, Response<OudList<ArtistPreview>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: ");

                ArrayList<ArtistPreview> followedArtists = response.body().getItems();
                for (ArtistPreview artistPreview : followedArtists) {
                    subscribeToFollowedArtistTopic(artistPreview.get_id());
                }
            }

            @Override
            public void onFailure(Call<OudList<ArtistPreview>> call, Throwable t) {
                Log.e(TAG, "onFailure: ");
            }
        });
    }

    /**
     * When logging out, the device must unsubscribe from all notification topics to stop receiving notifications.
     */
    public static void unsubscribeFromAllTopicsUponLoggingOut() {

        new Thread(() -> {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Once consumed, it becomes null again.
     * @return {@link NotificationShareDestination}.
     */
    public static NotificationShareDestination consumeNotificationShareDestination() {
        NotificationShareDestination temp = notificationShareDestination;

        notificationShareDestination = null;
        return temp;
    }

    public static void consumeNotificationShareDestinationUserActivity(UserActivity userActivity, String userId) {
        NotificationShareDestination dest = consumeNotificationShareDestination();

        if (dest != null) {
            String destination = dest.getDestination();
            String id = dest.getId();

            if (destination.equals(Constants.NOTIFICATION_SHARE_PLAYLIST))
                PlaylistFragment.show(userActivity, R.id.nav_host_fragment, userId, Constants.PlaylistFragmentType.PLAYLIST, id);
            else if (destination.equals(Constants.NOTIFICATION_SHARE_ALBUM))
                PlaylistFragment.show(userActivity, R.id.nav_host_fragment, userId, Constants.PlaylistFragmentType.ALBUM, id);

        }

    }


    public static class NotificationShareDestination {
        private String destination;
        private String id;

        public String getDestination() {
            return destination;
        }

        public String getId() {
            return id;
        }
    }

}
