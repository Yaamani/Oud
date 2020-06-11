package com.example.oud.user.library.notifications;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.Notification;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.TestActivity;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.library.notifications.LibraryNotificationsFragment;
import com.example.oud.user.fragments.library.notifications.LibraryNotificationsRepository;

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

import static android.os.Looper.getMainLooper;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class LibraryNotificationsFragmentTest {

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

        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody("{\"items\": [\n" +
                "],\n" +
                "\"limit\": 0,\n" +
                "\"offset\": 0,\n" +
                "\"total\": 0}"));



        scenario.onActivity(activity -> {

            LibraryNotificationsFragment fragment = new LibraryNotificationsFragment();

            LibraryNotificationsRepository.getInstance().setBaseUrl(server.url("/").toString());

            //System.out.println(server.url("/").toString());

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container, fragment, Constants.LIBRARY_NOTIFICATIONS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_no_notifications))
                    .check(matches(isDisplayed()));
        });


        server.shutdown();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadFirstSetOfItemsAndLoadMoreTest() throws IOException {
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);


        LibraryNotificationsRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {
            OudList<Notification> itemOudListFirstSet = null;
            try {
                itemOudListFirstSet = oudApi.getNotificationHistory("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LibraryNotificationsFragment fragment = new LibraryNotificationsFragment();

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container, fragment, Constants.LIBRARY_NOTIFICATIONS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            ArrayList<Notification> items = itemOudListFirstSet.getItems();
            for (int i = 0; i < items.size(); i++) {
                onView(withId(R.id.recycler_view_library_notifications))
                        .perform(RecyclerViewActions.scrollToPosition(i));


                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_notifications)),
                        withTagValue(is(titleTagPrefix+i))))
                        .check(matches(withText(items.get(i).getTitle())));
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

            OudList<Notification> itemOudListMore = null;

            try {
                itemOudListMore = oudApi.getNotificationHistory("",
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT,
                        Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT)
                        .execute()
                        .body();

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Notification> itemsMore = itemOudListMore.getItems();
            for (int i = 0; i < itemsMore.size(); i++) {
                int recyclerViewIndex = i + Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

                onView(withId(R.id.recycler_view_library_notifications))
                        .perform(RecyclerViewActions.scrollToPosition(recyclerViewIndex));




                String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);

                onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_notifications)),
                        withTagValue(is(titleTagPrefix+recyclerViewIndex))))
                        .check(matches(withText(itemsMore.get(i).getTitle())));
            }

        });
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void removeNotification_AndConnectionFails_test() {
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);

        //shadowOf(getMainLooper()).idle();

        LibraryNotificationsRepository.getInstance().setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);

        scenario.onActivity(activity -> {

            OudList<Notification> itemOudListFirstSet = null;
            try {
                itemOudListFirstSet = oudApi.getNotificationHistory("", Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }


            LibraryNotificationsFragment fragment = new LibraryNotificationsFragment();
            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container, fragment, Constants.LIBRARY_NOTIFICATIONS_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            // Simulate connection failure
            LibraryNotificationsRepository.getInstance().setBaseUrl("http:anything");

            onView(withId(R.id.recycler_view_library_notifications))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String btnTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_btn);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_notifications)),
                    withTagValue(is(btnTagPrefix+0))))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.recycler_view_library_notifications))
                    .perform(RecyclerViewActions.scrollToPosition(0));

            String firstItemName = itemOudListFirstSet.getItems().get(0).getTitle();;

            String titleTagPrefix = InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_generic_vertical_item_title);
            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_library_notifications)),
                    withTagValue(is(titleTagPrefix+0))))
                    .check(matches(withText(firstItemName)));
        });
    }
}
