package com.example.oud;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.*;


@RunWith(RobolectricTestRunner.class)
public class NotificationUtilsTest {



    @Test
    public void test_handleNotificationDestinationMainActivity_returnDestination() {
        Intent i = new Intent();
        i.putExtra(Constants.NOTIFICATION_DESTINATION_KEY, Constants.PLAYLIST_FRAGMENT_TAG);
        i.putExtra(Constants.ID_KEY, "This is an id.");

        NotificationUtils.handleNotificationDestinationMainActivity(i);

        NotificationUtils.NotificationDestination destination = NotificationUtils.consumeNotificationDestinationEntry();
        assertThat(destination.getDestination()).matches(Constants.PLAYLIST_FRAGMENT_TAG);
        assertThat(destination.getId()).matches("This is an id.");

    }


    @Test
    public void test_consumeNotificationDestinationEntry() {
        Intent i = new Intent();
        i.putExtra(Constants.NOTIFICATION_DESTINATION_KEY, Constants.PLAYLIST_FRAGMENT_TAG);
        i.putExtra(Constants.ID_KEY, "This is an id.");

        NotificationUtils.handleNotificationDestinationMainActivity(i);

        NotificationUtils.NotificationDestination destination = NotificationUtils.consumeNotificationDestinationEntry();
        assertThat(destination.getDestination()).matches(Constants.PLAYLIST_FRAGMENT_TAG);
        assertThat(destination.getId()).matches("This is an id.");

        assertThat(NotificationUtils.consumeNotificationDestinationEntry()).isNull();
    }

}
