package com.example.oud.user.library.playlists;

import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.library.artists.LibraryArtistsRepository;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static com.google.common.truth.Truth.assertThat;


@RunWith(RobolectricTestRunner.class)
public class LibraryPlaylistsViewModelTest {
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

        LibraryPlaylistsViewModel viewModel = new LibraryPlaylistsViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus


        OudList<Playlist> oudList = oudApi.getPlaylistsFollowedByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();

        MutableLiveData<OudList<Playlist>> liveData = viewModel.loadMoreItems("");

        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < oudList.getItems().size(); i++) {
            assertThat(oudList.getItems().get(i).getId())
                    .isEqualTo(liveData.getValue().getItems().get(i).getId());
        }
    }



}
