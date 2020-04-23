package com.example.oud.user.home;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.Category2;
import com.example.oud.api.Context;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTracks2;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.home.HomeFragment2;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import okhttp3.mockwebserver.MockWebServer;

import static org.robolectric.Shadows.shadowOf;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static com.google.common.truth.Truth.*;


@RunWith(RobolectricTestRunner.class)
public class HomeFragmentTest {

    private MockWebServer mockWebServer;

    private OudApi oudApi;

    //private IdlingResource idlingResource;

    @Before
    public void setUp() {
        //mockWebServer = getOudMockServer(Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, 6, 7);
        /*oudApi = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OudApi.class);*/

        oudApi = TestUtils.instantiateOudApi();

    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void homeFragmentDataTest() {

        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);

        //HomeRepository2.getInstance().setBaseUrl(mockWebServer.url("/").toString()); // The test fails because this line doesn't get executed before fetching for the data.

        activityScenario.onActivity(activity -> {

            activity.setUserId("user0");

            //FragmentScenario scenario = FragmentScenario.launchInContainer(HomeFragment.class);
            //scenario.moveToState(Lifecycle.State.CREATED);
            //shadowOf(getMainLooper()).idle();

            TestUtils.sleep(2, 200);

            testRecentlyPlayedTracksContent();

            OudList<Category2> categories = loadCategory2List();

            TestUtils.sleep(2, 200);

            for (int i = 1; i < Constants.USER_HOME_CATEGORIES_COUNT + 1; i++) {
                testCategory(i, categories);
            }

            //onView(withRecyclerView(R.id.recycler_view_home).a)


            /*Espresso.onView(ViewMatchers.withId(R.id.txt_home_test)).check(ViewAssertions.matches(ViewMatchers.withText("Home")));*/
        });

    }

    private OudList<Category2> loadCategory2List() {
        OudList<Category2> categories = null;

        try {
            categories = oudApi.listOfCategories2(null, 7).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private void testRecentlyPlayedTracksContent() {
        System.out.println("Recently played : ");


        onView(ViewMatchers.withId(R.id.recycler_view_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.scrollTo()));

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.txt_item_outer_title))
                .check(matches(withText("Recently played")));*/

        String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_title);


        onView(withTagValue(is(titleTagPrefix + 1)))
                .check(matches(withText(Constants.USER_HOME_RECENTLY_PLAYED)));


        /*onView(withId(R.id.image_home_test))
                .check(matches(withDrawable(R.drawable.ic_history2)));*/

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.image_item_outer_icon))
                .check(matches(withDrawable(Constants.USER_HOME_RECENTLY_PLAYED_ICON)));*/

        RecentlyPlayedTracks2 recentlyPlayedTracks = null;
        try {
            recentlyPlayedTracks = oudApi.recentlyPlayedTracks2("0", 6, null, null).execute().body();
        } catch (IOException e) {
            e.printStackTrace();


        }
        int offset = 0; // if Context.CONTEXT_UNKNOWN
        for (int i = 0; i < Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT/*1*/; i++) {

            Context current = recentlyPlayedTracks.getItems().get(i + offset).getContext();

            String currentName = getCurrentContextName(current);

            TestUtils.sleep(2, 200);

            if (currentName == null) { // Context.CONTEXT_UNKNOWN
                offset++;
                i--;
                continue;
            }


            /*onView(withId(R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));*/

            System.out.println("Recently played : " + i);

            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            onView(withTagValue(is(recyclerViewItemOuterTagPrefix + 1)))
                    .perform(RecyclerViewActions.scrollToPosition(i));

            String innerTitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_title);
            String subtitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_subtitle);

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 1))),
                    withTagValue(is(innerTitleTagPrefix + i))))
                    .check(matches(withText(currentName)));

            /*onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 0))),
                    withTagValue(is(subtitleTagPrefix + i))))
                    .check(matches(withText(currentAlbum.getName())));*/

            /*onView(withRecyclerView(R.id.recycler_view_home)
                    .atPositionOnView(0, R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));

            onView(allOf(
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(0)),
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withId(R.id.txt_item_inner_title)))
                    .check(matches(withText(current.getName())));

            onView(allOf(
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_home).atPosition(0)),
                    isDescendantOfA(withRecyclerView(R.id.recycler_view_item_outer).atPosition(i)),
                    withId(R.id.txt_item_inner_sub_title)))
                    .check(matches(withText(currentAlbum.getName())));*/
        }
    }

    private String getCurrentContextName(Context current) {
        String currentName = null;

        if (current.getType().equals(Constants.API_ALBUM)) {

            try {
                Album currentAlbum = oudApi.album("token", current.getId()).execute().body();
                currentName = currentAlbum.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (current.getType().equals(Constants.API_ARTIST)) {
            try {
                Artist currentArtist = oudApi.artist("token", current.getId()).execute().body();
                currentName = currentArtist.getDisplayName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (current.getType().equals(Constants.API_PLAYLIST)) {
            try {
                Playlist currentPlaylist = oudApi.playlist("token", current.getId()).execute().body();
                currentName = currentPlaylist.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return currentName;
    }

    private void testCategory(int position, OudList<Category2> categories) {
        System.out.println("Category : position = " + position);

        Category2 currentCategory = categories.getItems().get(position-1);

        onView(withId(R.id.recycler_view_home))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position+1, ViewActions.scrollTo()));

        String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_title);


        onView(withTagValue(is(titleTagPrefix + (position+1))))
                .check(matches(withText(currentCategory.getName())));

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(position, R.id.txt_item_outer_title))
                .check(matches(withText("category" + (position - 1))));*/

        /*onView(withId(R.id.image_home_test))
                .check(matches(withDrawable(R.drawable.ic_history2)));*/

        /*onView(withRecyclerView(R.id.recycler_view_home)
                .atPositionOnView(0, R.id.image_item_outer_icon))
                .check(matches(withDrawable(Constants.USER_HOME_RECENTLY_PLAYED_ICON)));*/

        OudList<Playlist> categoryPlaylists = loadCategoryPlaylists(currentCategory.get_id());

        TestUtils.sleep(2, 200);

        for (int i = 0; i < categoryPlaylists.getItems().size(); i++) {

            System.out.println("Category : position = " + position + ", i = " + i);

            Playlist currentPlaylist = categoryPlaylists.getItems().get(i);

            /*try {
                currentPlaylist = oudApi.playlist(currentCategory.getPlaylists().get(i)).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            /*onView(withRecyclerView(R.id.recycler_view_home)
                    .atPositionOnView(position, R.id.recycler_view_item_outer))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, ViewActions.scrollTo()));*/

            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            onView(withTagValue(is(recyclerViewItemOuterTagPrefix + (position+1))))
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

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + (position+1)))),
                    withTagValue(is(innerTitleTagPrefix + i))))
                    .check(matches(withText(currentPlaylist.getName())));

            /*onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + (position+1)))),
                    withTagValue(is(subtitleTagPrefix + i))))
                    .check(matches(withText("")));*/

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

    private OudList<Playlist> loadCategoryPlaylists(String categoryId) {
        OudList<Playlist> playlists = null;

        try {
            playlists = oudApi.categoryPlaylists("token", categoryId, null, Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playlists;
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void openPlaylistFragmentTest() {
        ActivityScenario<UserActivity> scenario = ActivityScenario.launch(UserActivity.class);

        //HomeRepository2.getInstance().setBaseUrl(mockWebServer.url("/").toString()); // The test fails because this line doesn't get executed before fetching for the data.



        scenario.onActivity(activity -> {


            TestUtils.sleep(2, 200);

            activity.setUserId("user0");

            FragmentManager manager = activity.getSupportFragmentManager();
            HomeFragment2 homeFragment = (HomeFragment2) manager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
            homeFragment.setUserId("user0");

            // when
            String recyclerViewItemOuterTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_outer_item_recycler_view);
            String innerTitleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_home_inner_item_image);

            onView(allOf(isDescendantOfA(withTagValue(is(recyclerViewItemOuterTagPrefix + 1))),
                    withTagValue(is(innerTitleTagPrefix + "0"))))
                    .perform(click());

            // then

            PlaylistFragment playlistFragment = (PlaylistFragment) manager.findFragmentByTag(Constants.PLAYLIST_FRAGMENT_TAG);

            assertThat(playlistFragment).isNotNull();

        });
    }



    @After
    public void cleanup() {
        /*try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


}
