package com.example.oud;

import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.fragments.artist.ArtistRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.contrib.RecyclerViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.google.common.truth.Truth.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class ArtistFragmentTest {

    public static final int MILLIS_TO_PAUSE = 500;


    @Test
    // @LooperMode(LooperMode.Mode.PAUSED)
    public void whenConnectionFailsUndoUiLikeTrack() {
        ActivityScenario<UserActivity> activityScenario = ActivityScenario.launch(UserActivity.class);

        activityScenario.onActivity(activity -> {
            //TestUtils.sleep(2, MILLIS_TO_PAUSE);

            try {
                Thread.sleep(MILLIS_TO_PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArtistFragment.show(activity, R.id.nav_host_fragment, "artist10", "user0");
            //TestUtils.sleep(2, MILLIS_TO_PAUSE*4);

            try {
                Thread.sleep(MILLIS_TO_PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // ImageButton followArtist = ((ImageButton) activity.findViewById(R.id.btn_artist_follow));

            FragmentManager manager = activity.getSupportFragmentManager();
            ArtistFragment artistFragment = (ArtistFragment) manager.findFragmentByTag(Constants.ARTIST_FRAGMENT_TAG);


            boolean before = artistFragment.getTrackListRecyclerViewAdapter().getLikedTracks().get(0);

            /*WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);*/
            ArtistRepository.getInstance().setBaseUrl("http:anything");

            /*onView(withId(R.id.recycler_view_artist_popular_songs))
                    .perform(actionOnItemAtPosition(0, click()));*/
            String heartTagPrefix =
                    InstrumentationRegistry.getInstrumentation().getContext().getResources().getString(R.string.tag_track_list_adapter_heart);
            /*onView(withTagValue(is(heartTagPrefix+0)))
                    .perform(click());*/

            onView(withId(R.id.recycler_view_artist_popular_songs))
                    .perform(actionOnItemAtPosition(0, ViewActions.scrollTo()));

            onView(allOf(isDescendantOfA(withId(R.id.recycler_view_artist_popular_songs)),
                    withTagValue(is(heartTagPrefix+0))))
                    .perform(click());

            try {
                Thread.sleep(MILLIS_TO_PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //TestUtils.sleep(2, MILLIS_TO_PAUSE);
            boolean after = artistFragment.getTrackListRecyclerViewAdapter().getLikedTracks().get(0);


            assertThat(before).isEqualTo(after);

        });
    }
}
