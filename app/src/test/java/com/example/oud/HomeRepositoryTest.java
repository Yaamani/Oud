package com.example.oud;

import com.example.oud.api.Album;
import com.example.oud.api.Category;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTracks;
import com.example.oud.api.Track;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.home.HomeRepository;
import com.example.oud.user.fragments.home.HomeViewModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Looper.getMainLooper;
import static org.robolectric.Shadows.shadowOf;
import static com.google.common.truth.Truth.*;

@Deprecated
@Ignore
@RunWith(RobolectricTestRunner.class)
public class HomeRepositoryTest {

    private MockWebServer mockWebServer;
    private OudApi oudApi;

    @Before
    public void setUp() {
        mockWebServer = TestUtils.getOudMockServer(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, 7, 7);

        oudApi = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OudApi.class);



    }

    /*@Test
    public void test_areThereRecentlyPlayedTracks() {

    }*/

    @Test
    @Ignore
    public void test_connectionFailure() {
        HomeViewModel homeViewModel = new HomeViewModel();
        HomeRepository.getInstance().setBaseUrl(TestUtils.getOudMockServerTimeoutFailure().url("/").toString());

        HomeRepository.getInstance().loadCategory(0);

        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(5500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            shadowOf(getMainLooper()).idle();
        }

        assertThat(homeViewModel.getConnectionStatus().getValue())
                .isEqualTo(false);
    }

    @Test
    @Ignore
    public void test_throwException_whenAreThereRecentlyPlayedTracks_WasNotCalled() {
        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString());

        try {
            HomeRepository.getInstance().loadRecentlyPlayed();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e)
                    .hasMessageThat()
                    .isEqualTo("You should know if there are recently played tracks or not first. call " +
                            "areThereRecentlyPlayedTracks() and observe the changes. When you observe true, you can call loadRecentlyPlayed().");
        }
    }

    @Test
    @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void test_throwException_whenAreThereRecentlyPlayedTracks_WasCalled_AndNoTracksFound() {
        MockWebServer serverWithNoRecentlyPlayedTracks = TestUtils.getOudMockServer(0, 1, 1);

        HomeRepository.getInstance().setBaseUrl(serverWithNoRecentlyPlayedTracks.url("/").toString());

        HomeRepository.getInstance().areThereRecentlyPlayedTracks();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shadowOf(getMainLooper()).idle();

        try {
            HomeRepository.getInstance().loadRecentlyPlayed();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e)
                    .hasMessageThat()
                    .isEqualTo("There are no recently played tracks to load.");
        } finally {
            try {
                serverWithNoRecentlyPlayedTracks.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void test_loadRecentlyPlayed() {
        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString());

        HomeRepository.getInstance().areThereRecentlyPlayedTracks();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shadowOf(getMainLooper()).idle();

        HomeViewModel.OuterItemLiveData outerItemLiveData = HomeRepository.getInstance().loadRecentlyPlayed();

        Assert.assertEquals("Recently played", outerItemLiveData.getTitle().getValue());

        ArrayList<HomeViewModel.InnerItemLiveData> innerItems = outerItemLiveData.getInnerItems().getValue();

        final int sleep = 250;

        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            shadowOf(getMainLooper()).idle();
        }

        RecentlyPlayedTracks recentlyPlayedTracks = null;
        try {
            recentlyPlayedTracks = oudApi.recentlyPlayedTracks("token", 6, null, null).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < innerItems.size(); i++) {

            Track currentTrack = recentlyPlayedTracks.getItems().get(i).getTrack();

            Album currentAlbum = null;

            try {
                currentAlbum = oudApi.album("token", currentTrack.getAlbumId()).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertWithMessage("track name as the title, index = " + i)
                    .that(innerItems.get(i).getTitle().getValue())
                    .isEqualTo(currentTrack.getName());

            assertWithMessage("image url, index = " + i)
                    .that(innerItems.get(i).getImage().getValue())
                    .isEqualTo(currentAlbum.getImage());

            assertWithMessage("album name as the subtitle, index = " + i)
                    .that(innerItems.get(i).getSubTitle().getValue())
                    .isEqualTo(currentAlbum.getName());
        }
    }

    @Test
    @Ignore
    public void test_loadCategory() {
        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString());

        HomeViewModel.OuterItemLiveData[] outerItems = new HomeViewModel.OuterItemLiveData[Constants.USER_HOME_CATEGORIES_COUNT];
        for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            outerItems[i] = HomeRepository.getInstance().loadCategory(i);
        }

        OudList<Category> categories = null;

        try {
            categories = oudApi.listOfCategories(null, 7).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final int sleep = 200;

        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            shadowOf(getMainLooper()).idle();
        }


        for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            singleCategoryTest(i, outerItems[i], categories);
        }
    }

    private void singleCategoryTest(int position,
                                    HomeViewModel.OuterItemLiveData outerItemLiveData,
                                    OudList<Category> categories) {

        Category currentCategory = categories.getItems().get(position);

        Assert.assertEquals(currentCategory.getName(), outerItemLiveData.getTitle().getValue());

        ArrayList<HomeViewModel.InnerItemLiveData> innerItems = outerItemLiveData.getInnerItems().getValue();

        for (int i = 0; i < innerItems.size(); i++) {

            Playlist currentPlaylist = null;

            try {
                currentPlaylist = oudApi.playlist("token", currentCategory.getPlaylists().get(i)).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assertWithMessage("playlist name as the title, outerItem = " + position + ", innerItem = " + i)
                    .that(innerItems.get(i).getTitle().getValue())
                    .isEqualTo(currentPlaylist.getName());

            //int playlistCoverIndex = (i+position*7)%OudApiJsonGenerator.PLAYLIST_COVER.length;
            assertWithMessage("image url, outerItem = " + position + ", innerItem = " + i)
                    .that(innerItems.get(i).getImage().getValue())
                    .isEqualTo(currentPlaylist.getImage());

            assertWithMessage("subtitle should be empty,  outerItem = " + position + ", innerItem = " + i)
                    .that(innerItems.get(i).getSubTitle().getValue())
                    .isEqualTo("");
        }
    }

    @After
    public void cleanup() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
