package com.example.oud.user.library.likedtracks;

import android.app.Activity;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.LikedTrack;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.testutils.RecyclerViewMatcher;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksFragment;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksRepository;
import com.example.oud.user.fragments.library.likedtracks.LibraryLikedTracksViewModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.google.common.truth.Truth.*;
import static org.hamcrest.Matchers.*;
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
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadFirstSetOfTracksAndLoadMoreTest() throws IOException {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);


        LibraryLikedTracksRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {
            OudList<LikedTrack> likedTrackOudListFirstSet = null;
            try {
                likedTrackOudListFirstSet = oudApi.getLikedTrackByCurrentUser("", Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT, 0).execute().body();
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

                onView(TestUtils.withRecyclerView(R.id.recycler_view_library_liked_tracks).atPositionOnView(i, R.id.txt_track_playlist))
                        .check(matches(withText(items.get(i).getTrack().getName())));
            }

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            OudList<LikedTrack> likedTrackOudListMore = null;

            try {
                likedTrackOudListMore = oudApi.getLikedTrackByCurrentUser("",
                        Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT,
                        Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT)
                        .execute()
                        .body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<LikedTrack> itemsMore = likedTrackOudListMore.getItems();
            for (int i = 0; i < itemsMore.size(); i++) {
                int recyclerViewIndex = i + Constants.USER_LIBRARY_LIKED_TRACKS_SINGLE_FETCH_LIMIT;

                onView(withId(R.id.recycler_view_library_liked_tracks))
                        .perform(RecyclerViewActions.scrollToPosition(recyclerViewIndex));


                onView(TestUtils.withRecyclerView(R.id.recycler_view_library_liked_tracks).atPositionOnView(recyclerViewIndex, R.id.txt_track_playlist))
                        .check(matches(withText(itemsMore.get(i).getTrack().getName())));
            }

        });
    }
}
