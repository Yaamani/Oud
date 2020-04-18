package com.example.oud.user.library.likedtracks;


import com.example.oud.Constants;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LibraryLikedTracksViewModelTest {
    public static final int MILLIS_TO_PAUSE = 250;


    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }


    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadMoreTracksTest() throws IOException {
        LibraryLikedTracksViewModel viewModel = new LibraryLikedTracksViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<LikedTrack> oudList = oudApi.getLikedTrackByCurrentUser("", Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT, 0).execute().body();

        MutableLiveData<OudList<LikedTrack>> liveData = viewModel.loadMoreTracks("");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < oudList.getItems().size(); i++) {
            assertThat(oudList.getItems().get(i).getTrack().get_id())
                    .isEqualTo(liveData.getValue().getItems().get(i).getTrack().get_id());
        }
    }
}
