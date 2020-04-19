package com.example.oud.user.library.likedtracks;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksFragment;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.google.common.truth.Truth.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class LibraryLikedTracksFragmentTest {

    public static final int MILLIS_TO_PAUSE = 250;

    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenThereAreNoLikedTracks_ShowYouHaveNoLikedTracks() throws IOException {

        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody("{\"items\": [\n" +
                "],\n" +
                "\"limit\": 0,\n" +
                "\"offset\": 0,\n" +
                "\"total\": 0}"));



        scenario.onActivity(activity -> {

            LibraryLikedTracksFragment libraryLikedTracksFragment = new LibraryLikedTracksFragment();


            LibraryLikedTracksRepository.getInstance().setBaseUrl(server.url("/").toString());

            //System.out.println(server.url("/").toString());


            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, libraryLikedTracksFragment, Constants.LIBRARY_LIKED_TRACKS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_no_liked_tracks))
                    .check(matches(isDisplayed()));
        });


        server.shutdown();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadFirstSetOfTracksAndLoadMoreTest() throws IOException {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryLikedTracksRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {
            OudList<LikedTrack> likedTrackOudListFirstSet = null;
            try {
                likedTrackOudListFirstSet = oudApi.getLikedTrackByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LibraryLikedTracksFragment libraryLikedTracksFragment = new LibraryLikedTracksFragment();

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, libraryLikedTracksFragment, Constants.LIBRARY_LIKED_TRACKS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            ArrayList<LikedTrack> items = likedTrackOudListFirstSet.getItems();
            for (int i = 0; i < items.size(); i++) {
                onView(withId(R.id.recycler_view_library_liked_tracks))
                        .perform(RecyclerViewActions.scrollToPosition(i));

                /*onView(TestUtils.withRecyclerView(R.id.recycler_view_library_liked_tracks).atPositionOnView(i, R.id.txt_track_playlist))
                        .check(matches(withText(items.get(i).getTrack().getName())));*/
                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                        withTagValue(is(titleTagPrefix+i))))
                        .check(matches(withText(items.get(i).getTrack().getName())));
            }


            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////
            // Load More
            // Load More
            // Load More
            // Load More

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            OudList<LikedTrack> likedTrackOudListMore = null;

            try {
                likedTrackOudListMore = oudApi.getLikedTrackByCurrentUser("",
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT,
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT)
                        .execute()
                        .body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<LikedTrack> itemsMore = likedTrackOudListMore.getItems();
            for (int i = 0; i < itemsMore.size(); i++) {
                int recyclerViewIndex = i + Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

                onView(withId(R.id.recycler_view_library_liked_tracks))
                        .perform(RecyclerViewActions.scrollToPosition(recyclerViewIndex));


                /*onView(TestUtils.withRecyclerView(R.id.recycler_view_library_liked_tracks).atPositionOnView(recyclerViewIndex, R.id.txt_track_playlist))
                        .check(matches(withText(itemsMore.get(i).getTrack().getName())));*/

                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                        withTagValue(is(titleTagPrefix+recyclerViewIndex))))
                        .check(matches(withText(itemsMore.get(i).getTrack().getName())));
            }

        });
    }


    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void removeTrackFromLikedTracks_AndConnectionSucceeds_test() {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryLikedTracksRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {

            OudList<LikedTrack> likedTrackOudListFirstSet = null;
            try {
                likedTrackOudListFirstSet = oudApi.getLikedTrackByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LibraryLikedTracksFragment libraryLikedTracksFragment = new LibraryLikedTracksFragment();
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, libraryLikedTracksFragment, Constants.LIBRARY_LIKED_TRACKS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.recycler_view_library_liked_tracks))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String heartTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_heart);

            // Remove track from liked
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                    withTagValue(is(heartTagPrefix+0))))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.recycler_view_library_liked_tracks))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String secondTrackName = likedTrackOudListFirstSet.getItems().get(1).getTrack().getName();

            String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_title);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                    withTagValue(is(titleTagPrefix+1))))
                    .check(matches(withText(secondTrackName)));

            assertThat(libraryLikedTracksFragment.getmViewModel().getLoadedItems().get(0).getValue().getTrack().getName())
                    .isEqualTo(secondTrackName);


        });
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void removeTrackFromLikedTracks_AndConnectionFails_test() {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryLikedTracksRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {

            OudList<LikedTrack> likedTrackOudListFirstSet = null;
            try {
                likedTrackOudListFirstSet = oudApi.getLikedTrackByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }


            LibraryLikedTracksFragment libraryLikedTracksFragment = new LibraryLikedTracksFragment();
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, libraryLikedTracksFragment, Constants.LIBRARY_LIKED_TRACKS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            // Simulate connection failure
            LibraryLikedTracksRepository.getInstance().setBaseUrl("http:anything");

            onView(withId(R.id.recycler_view_library_liked_tracks))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String heartTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_heart);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                    withTagValue(is(heartTagPrefix+0))))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.recycler_view_library_liked_tracks))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String firstTrackName = likedTrackOudListFirstSet.getItems().get(0).getTrack().getName();;

            String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_title);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_liked_tracks)),
                    withTagValue(is(titleTagPrefix+0))))
                    .check(matches(withText(firstTrackName)));
            /*onView(TestUtils.withRecyclerView(R.id.recycler_view_library_liked_tracks).atPositionOnView(0, R.id.txt_track_playlist))
                    .check(matches(withText(firstTrackName)));*/
        });
    }
}
