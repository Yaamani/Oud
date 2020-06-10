package com.example.oud.user.library.notifications;

import com.example.oud.Constants;
import com.example.oud.api.ArtistPreview;
import com.example.oud.api.Notification;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.library.notifications.LibraryNotificationsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LibraryNotificationsViewModelTest {
    public static final int MILLIS_TO_PAUSE = 250;

    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadMoreItemsTest() throws IOException {
        LibraryNotificationsViewModel viewModel = new LibraryNotificationsViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<Notification> oudList = oudApi.getNotificationHistory("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();

        MutableLiveData<OudList<Notification>> liveData = viewModel.loadMoreItems("");

        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < oudList.getItems().size(); i++) {
            //System.out.println(i);
            assertThat(oudList.getItems().get(i).get_id())
                    .isEqualTo(liveData.getValue().getItems().get(i).get_id());
        }
    }    
}
