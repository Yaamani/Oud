package com.example.oud.user.library.savedalbums;

import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.SavedAlbum;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.library.savedalbums.LibrarySavedAlbumsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LibrarySavedAlbumsViewModelTest {
    
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
        LibrarySavedAlbumsViewModel viewModel = new LibrarySavedAlbumsViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<SavedAlbum> oudList = oudApi.getSavedAlbumsByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();

        MutableLiveData<OudList<SavedAlbum>> liveData = viewModel.loadMoreItems("");

        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < oudList.getItems().size(); i++) {
            assertThat(oudList.getItems().get(i).getAlbum().get_id())
                    .isEqualTo(liveData.getValue().getItems().get(i).getAlbum().get_id());
        }
    }
}
