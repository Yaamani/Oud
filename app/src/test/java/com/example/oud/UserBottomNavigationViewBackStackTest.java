package com.example.oud;


import com.example.oud.user.UserActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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
    public void subFragmentOpenedTest() {
        ActivityScenario<UserActivity> userActivityActivityScenario = ActivityScenario.launch(UserActivity.class);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        shadowOf(getMainLooper()).idle();

        userActivityActivityScenario.onActivity(activity -> {

            // given
            String tagPrefix = activity.getResources().getString(R.string.tag_home_inner_item_image);
            onView(withTagValue(is(tagPrefix + "0"))).perform(click());

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
