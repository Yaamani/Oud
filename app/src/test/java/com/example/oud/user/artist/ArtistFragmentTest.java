package com.example.oud.user.artist;

import android.os.Bundle;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.OudApi;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.fragments.artist.ArtistRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;

import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.google.common.truth.Truth.*;


@RunWith(RobolectricTestRunner.class)
public class ArtistFragmentTest {

    public static final int MILLIS_TO_PAUSE = 250;

    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void showTest() {
        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);



        activityScenario.onActivity(activity -> {
            TestUtils.sleep(2, MILLIS_TO_PAUSE);

            ArtistFragment.show(activity, R.id.nav_host_fragment, "artist0");
            TestUtils.sleep(2, MILLIS_TO_PAUSE);


            FragmentManager manager = activity.getSupportFragmentManager();

            ArtistFragment artistFragment = (ArtistFragment) manager.findFragmentById(R.id.nav_host_fragment);

            assertThat(artistFragment)
                    .isNotNull();
        });
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenConnectionFailsUndoUiFollowingArtist() {
        // OudApi oudApiConnectionFailure = TestUtils.instantiateOudApiConnectionFailure();

        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);

        activityScenario.onActivity(activity -> {
            TestUtils.sleep(2, MILLIS_TO_PAUSE);

            ArtistFragment.show(activity, R.id.nav_host_fragment, "artist10");
            TestUtils.sleep(2, MILLIS_TO_PAUSE);

            // ImageButton followArtist = ((ImageButton) activity.findViewById(R.id.btn_artist_follow));

            FragmentManager manager = activity.getSupportFragmentManager();
            ArtistFragment artistFragment = (ArtistFragment) manager.findFragmentByTag(Constants.ARTIST_FRAGMENT_TAG);


            int before = artistFragment.currentFollowColor;

            /*WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);*/
            ArtistRepository.getInstance().setBaseUrl("http:anything");

            onView(withId(R.id.btn_artist_follow))
                    .perform(click());

            TestUtils.sleep(2, MILLIS_TO_PAUSE);

            int after = artistFragment.currentFollowColor;

            assertThat(before).isEqualTo(after);

        });
    }

    /*@Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenConnectionFailsUndoUiLikeTrack() {
        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);

        activityScenario.onActivity(activity -> {
            TestUtils.sleep(2, MILLIS_TO_PAUSE);

            ArtistFragment.show(activity, R.id.nav_host_fragment, "artist10", "user0");
            TestUtils.sleep(2, MILLIS_TO_PAUSE*2);

            // ImageButton followArtist = ((ImageButton) activity.findViewById(R.id.btn_artist_follow));

            FragmentManager manager = activity.getSupportFragmentManager();
            ArtistFragment artistFragment = (ArtistFragment) manager.findFragmentByTag(Constants.ARTIST_FRAGMENT_TAG);

            *//*artistFragment.getmMotionLayout().transitionToEnd();
            TestUtils.sleep(1, MILLIS_TO_PAUSE);*//*



            boolean before = artistFragment.getTrackListRecyclerViewAdapter().getLikedTracks().get(0);

            *//*WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);*//*
            ArtistRepository.getInstance().setBaseUrl("http:anything");

            *//*onView(withId(R.id.recycler_view_artist_popular_songs))
                    .perform(actionOnItemAtPosition(0, click()));*//*
            String heartTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_heart);
            *//*onView(withTagValue(is(heartTagPrefix+0)))
                    .perform(click());*//*

            onView(withId(R.id.recycler_view_artist_popular_songs))
                    .perform(actionOnItemAtPosition(0, ViewActions.scrollTo()));

            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_artist_popular_songs)),
                    withTagValue(is(heartTagPrefix+0))))
                    .perform(click());

            TestUtils.sleep(2, MILLIS_TO_PAUSE);
            boolean after = artistFragment.getTrackListRecyclerViewAdapter().getLikedTracks().get(0);


            assertThat(before).isEqualTo(after);

        });
    }*/

    @Test
    public void myArgsTest() {
        Bundle bundle = ArtistFragment.myArgs("artistId", "userId");

        String artistId = bundle.getString(Constants.ARTIST_ID_KEY);
        String userId = bundle.getString(Constants.USER_ID_KEY);

        assertThat(artistId)
                .isEqualTo("artistId");
        assertThat(userId)
                .isEqualTo("userId");

    }


}
