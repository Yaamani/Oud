package com.example.oud;

import com.example.oud.api.Album;
import com.example.oud.api.Category;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTracks;
import com.example.oud.api.Track;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.home.HomeFragment;
import com.example.oud.user.fragments.home.HomeRepository;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Looper.getMainLooper;
import static androidx.test.espresso.action.ViewActions.*;
import static org.robolectric.Shadows.shadowOf;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static com.example.oud.testutils.TestUtils.*;
import static com.google.common.truth.Truth.*;


@RunWith(RobolectricTestRunner.class)
public class HomeFragmentTest {

    private MockWebServer mockWebServer;

    private OudApi oudApi;

    //private IdlingResource idlingResource;

    @Before
    public void setUp() {
        mockWebServer = getOudMockServer(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, 6, 7);
        oudApi = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OudApi.class);
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void homeFragmentDataTest() {

        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);

        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString()); // The test fails because this line doesn't get executed before fetching for the data.

        activityScenario.onActivity(activity -> {



            //FragmentScenario scenario = FragmentScenario.launchInContainer(HomeFragment.class);
            //scenario.moveToState(Lifecycle.State.CREATED);
            //shadowOf(getMainLooper()).idle();

            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                shadowOf(getMainLooper()).idle();
            }

            testRecentlyPlayedTracksContent();

            OudList<Category> categories = null;

            try {
                categories = oudApi.listOfCategories(null, 7).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 1; i < Constants.USER_HOME_CATEGORIES_COUNT + 1; i++) {
                testCategory(i, categories);
            }

            //onView(withRecyclerView(R.id.recycler_view_home).a)


            /*Espresso.onView(ViewMatchers.withId(R.id.txt_home_test)).check(ViewAssertions.matches(ViewMatchers.withText("Home")));*/
        });

    }

    private void testRecentlyPlayedTracksContent() {
        System.out.println("Recently played : ");


        onView(withId(R.id.recycler_view_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.scrollTo()));

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.txt_item_outer_title))
                .check(matches(withText("Recently played")));*/

        String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_title);


        onView(withTagValue(is(titleTagPrefix + 0)))
                .check(matches(withText("Recently played")));


        /*onView(withId(R.id.image_home_test))
                .check(matches(withDrawable(R.drawable.ic_history2)));*/

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.image_item_outer_icon))
                .check(matches(withDrawable(Constants.USER_HOME_RECENTLY_PLAYED_ICON)));*/

        RecentlyPlayedTracks recentlyPlayedTracks = null;
        try {
            recentlyPlayedTracks = oudApi.recentlyPlayedTracks(6, null, null).execute().body();
        } catch (IOException e) {
            e.printStackTrace();


        }
        for (int i = 0; i < /*Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT*/1; i++) {

            Track currentTrack = recentlyPlayedTracks.getItems().get(i).getTrack();

            Album currentAlbum = null;

            try {
                currentAlbum = oudApi.album(currentTrack.getAlbumId()).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*onView(withId(R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));*/

            System.out.println("Recently played : " + i);

            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            onView(withTagValue(is(recyclerViewItemOuterTagPrefix + 0)))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            String innerTitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_title);
            String subtitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_subtitle);

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 0))),
                    withTagValue(is(innerTitleTagPrefix + i))))
                    .check(matches(withText(currentTrack.getName())));

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 0))),
                    withTagValue(is(subtitleTagPrefix + i))))
                    .check(matches(withText(currentAlbum.getName())));

            /*onView(withRecyclerView(R.id.recycler_view_home)
                    .atPositionOnView(0, R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));

            onView(allOf(
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(0)),
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withId(R.id.txt_item_inner_title)))
                    .check(matches(withText(currentTrack.getName())));

            onView(allOf(
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(0)),
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withId(R.id.txt_item_inner_sub_title)))
                    .check(matches(withText(currentAlbum.getName())));*/
        }
    }

    private void testCategory(int position, OudList<Category> categories) {
        System.out.println("Category : position = " + position);

        Category currentCategory = categories.getItems().get(position-1);

        onView(withId(R.id.recycler_view_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, ViewActions.scrollTo()));

        String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_title);


        onView(withTagValue(is(titleTagPrefix + position)))
                .check(matches(withText(currentCategory.getName())));

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(position, R.id.txt_item_outer_title))
                .check(matches(withText("category" + (position - 1))));*/

        /*onView(withId(R.id.image_home_test))
                .check(matches(withDrawable(R.drawable.ic_history2)));*/

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.image_item_outer_icon))
                .check(matches(withDrawable(Constants.USER_HOME_RECENTLY_PLAYED_ICON)));*/

        for (int i = 0; i < Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT; i++) {

            System.out.println("Category : position = " + position + ", i = " + i);

            Playlist currentPlaylist = null;

            try {
                currentPlaylist = oudApi.playlist(currentCategory.getPlaylists().get(i)).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*onView(withRecyclerView(R.id.recycler_view_home)
                    .atPositionOnView(position, R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));*/

            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            onView(withTagValue(is(recyclerViewItemOuterTagPrefix + position)))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            /*onView(withId(R.id.recycler_view_home))
                    .perform(RecyclerViewActions.scrollToPosition(position));*/

            /*Matcher<View> home = isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(position));
            Matcher<View> itemOuter = isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i));
            Matcher<View> itemInner = withId(R.id.relative_layout_item_inner);
            //Matcher<View> innerTitle = withId(R.id.txt_item_inner_title);
            Matcher<View> allOf = allOf(home, itemOuter, itemInner);
            ViewInteraction viewInteraction = onView(allOf);

            Matcher<View> withText = allOf(withText("playlist"+i), isDisplayed());
            ViewAssertion matches = matches(withText);

            viewInteraction.check(matches);*/


            /*onView(allOf(withTagValue(is(innerTitleTagPrefix + i)),
                    withParent(withTagValue(is(recyclerViewItemOuterTagPrefix + position)))))
                    .check(matches(isDisplayed()));*/


            String innerTitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_title);
            String subtitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_subtitle);

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + position))),
                    withTagValue(is(innerTitleTagPrefix + i))))
                    .check(matches(withText(currentPlaylist.getName())));

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + position))),
                    withTagValue(is(subtitleTagPrefix + i))))
                    .check(matches(withText("")));

            /*onView(allOf(withTagValue(is(recyclerViewItemOuterTagPrefix + position))))
                    .check(matches(isDisplayed()));*/

            /*onView(allOf(withId(R.id.recycler_view_item_outer),
                    withParent(withRecyclerView(R.id.recycler_view_home).atPosition(position))))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i,
                            RecyclerViewActions.actionOnItem(withId(R.id.relative_layout_item_inner),
                                    ViewActions.scrollTo())))
                    .check(matches(hasDescendant(withText("USA"))));*/


            /*onView(allOf(withId(R.id.txt_item_inner_title),
                    withParent(withId(R.id.relative_layout_item_inner)),
                    withParent(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withParent(withRecyclerView(R.id.recycler_view_home).atPosition(position))))
                    .check(matches(withText("playlist"+i)));*/

            /*onView(allOf(
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(position)),
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withId(R.id.txt_item_inner_sub_title)))
                    .check(matches(withText("")));*/

        }
    }

    @Test
    public void openPlaylistFragmentTest() {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);

        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString()); // The test fails because this line doesn't get executed before fetching for the data.

        scenario.onActivity(activity -> {

            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                shadowOf(getMainLooper()).idle();
            }


            // when
            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            String innerTitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_image);

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 1))),
                    withTagValue(is(innerTitleTagPrefix + "0"))))
                    .perform(click());

            // then
            FragmentManager manager = activity.getSupportFragmentManager();
            PlaylistFragment playlistFragment = (PlaylistFragment) manager.findFragmentByTag(UserActivity.PLAYLIST_FRAGMENT_TAG);
            HomeFragment homeFragment = (HomeFragment) manager.findFragmentByTag(UserActivity.HOME_FRAGMENT_TAG);

            assertThat(playlistFragment).isNotNull();
            assertThat(homeFragment).isNull();

        });
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
