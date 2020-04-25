package com.example.oud;


import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;

import static android.os.Looper.getMainLooper;
import static org.robolectric.Shadows.shadowOf;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.*;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static com.google.common.truth.Truth.*;

@RunWith(RobolectricTestRunner.class)
public class UserBottomNavigationViewBackStackTest {


    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void noSubFragmentOpenedTest() {
        ActivityScenario<UserActivity> userActivityActivityScenario = ActivityScenario.launch(UserActivity.class);

        userActivityActivityScenario.onActivity(activity -> {
            // given
            onView(withId(R.id.navigation_search)).perform(click());

            shadowOf(getMainLooper()).idle();

            // when
            onView(isRoot()).perform(ViewActions.pressBack());

            // then
            BottomNavigationView navigationView = activity.findViewById(R.id.nav_view);
            assertThat(navigationView.getSelectedItemId())
                .isEqualTo(R.id.navigation_home);
        });
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void subFragmentOpenedTest() {
        ActivityScenario<UserActivity> userActivityActivityScenario = ActivityScenario.launch(UserActivity.class);

        TestUtils.sleep(1, 200);

        userActivityActivityScenario.onActivity(activity -> {

            // given
            String outerTagPrefix = activity.getResources().getString(R.string.tag_home_outer_item_recycler_view);
            String innerTagPrefix = activity.getResources().getString(R.string.tag_home_inner_item_image);
            //onView(withTagValue(is(innerTagPrefix + "0"))).perform(click());
            onView(allOf(isDescendantOfA(withTagValue(is(outerTagPrefix + 1))),
                    withTagValue(is(innerTagPrefix + "0")))).perform(click());

            onView(withId(R.id.navigation_search)).perform(click());

            // when
            onView(isRoot()).perform(ViewActions.pressBack());

            // then
            BottomNavigationView navigationView = activity.findViewById(R.id.nav_view);
            assertThat(navigationView.getSelectedItemId())
                    .isEqualTo(R.id.navigation_home);


        });
    }
}
