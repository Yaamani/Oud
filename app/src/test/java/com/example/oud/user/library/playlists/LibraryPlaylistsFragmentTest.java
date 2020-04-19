package com.example.oud.user.library.playlists;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsFragment;
import com.example.oud.user.fragments.library.playlists.LibraryPlaylistsRepository;

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
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class LibraryPlaylistsFragmentTest {
    public static final int MILLIS_TO_PAUSE = 250;
    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenThereAreNoItems_ShowYouHaveNoItems() throws IOException {

        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody("{\"items\": [\n" +
                "],\n" +
                "\"limit\": 0,\n" +
                "\"offset\": 0,\n" +
                "\"total\": 0}"));



        scenario.onActivity(activity -> {

            LibraryPlaylistsFragment fragment = new LibraryPlaylistsFragment();


            LibraryPlaylistsRepository.getInstance().setBaseUrl(server.url("/").toString());

            //System.out.println(server.url("/").toString());


            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, Constants.LIBRARY_PLAYLISTS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_no_playlists))
                    .check(matches(isDisplayed()));
        });


        server.shutdown();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadFirstSetOfItemsAndLoadMoreTest() throws IOException {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryPlaylistsRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {
            OudList<Playlist> itemOudListFirstSet = null;
            try {
                itemOudListFirstSet = oudApi.getPlaylistsFollowedByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LibraryPlaylistsFragment fragment = new LibraryPlaylistsFragment();

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, Constants.LIBRARY_PLAYLISTS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            ArrayList<Playlist> items = itemOudListFirstSet.getItems();
            for (int i = 0; i < items.size(); i++) {
                onView(withId(R.id.recycler_view_library_playlists))
                        .perform(RecyclerViewActions.scrollToPosition(i));


                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_playlists)),
                        withTagValue(is(titleTagPrefix+i))))
                        .check(matches(withText(items.get(i).getName())));
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

            OudList<Playlist> itemOudListMore = null;

            try {
                itemOudListMore = oudApi.getPlaylistsFollowedByCurrentUser("",
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT,
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT)
                        .execute()
                        .body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Playlist> itemsMore = itemOudListMore.getItems();
            for (int i = 0; i < itemsMore.size(); i++) {
                int recyclerViewIndex = i + Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

                onView(withId(R.id.recycler_view_library_playlists))
                        .perform(RecyclerViewActions.scrollToPosition(recyclerViewIndex));




                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_playlists)),
                        withTagValue(is(titleTagPrefix+recyclerViewIndex))))
                        .check(matches(withText(itemsMore.get(i).getName())));
            }

        });
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void removePlaylist_AndConnectionFails_test() {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryPlaylistsRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {

            OudList<Playlist> itemOudListFirstSet = null;
            try {
                itemOudListFirstSet = oudApi.getPlaylistsFollowedByCurrentUser("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }


            LibraryPlaylistsFragment fragment = new LibraryPlaylistsFragment();
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, Constants.LIBRARY_PLAYLISTS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            // Simulate connection failure
            LibraryPlaylistsRepository.getInstance().setBaseUrl("http:anything");

            onView(withId(R.id.recycler_view_library_playlists))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String btnTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_btn);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_playlists)),
                    withTagValue(is(btnTagPrefix+0))))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.recycler_view_library_playlists))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String firstItemName = itemOudListFirstSet.getItems().get(0).getName();;

            String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_playlists)),
                    withTagValue(is(titleTagPrefix+0))))
                    .check(matches(withText(firstItemName)));
        });
    }
}
