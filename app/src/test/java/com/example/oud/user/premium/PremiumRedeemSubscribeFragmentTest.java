package com.example.oud.user.premium;


import com.example.oud.Constants;
import com.example.oud.EmptyActivityForTesting;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.OudApi;
import com.example.oud.api.Profile;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.premium.redeemsubscribe.PremiumRedeemSubscribeFragment;
import com.example.oud.user.fragments.premium.redeemsubscribe.PremiumRedeemSubscribeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.text.SimpleDateFormat;

import androidx.fragment.app.FragmentManager;
import androidx.test.core.app.ActivityScenario;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.RootMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.google.common.truth.Truth.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
public class PremiumRedeemSubscribeFragmentTest {
    public static final int MILLIS_TO_PAUSE = 100;

    private String freeProfile = "{\n" +
            "    \"_id\": \"5a2539b41c574006c46f1a07\",\n" +
            "    \"username\": \"user0\",\n" +
            "    \"birthDate\": \"2020-04-21\",\n" +
            "    \"gender\": \"F\",\n" +
            "    \"email\": \"example@example.com\",\n" +
            "    \"displayName\": \"user0\",\n" +
            "    \"role\": \"free\",\n" +
            "    \"country\": \"EG\",\n" +
            "    \"credit\": 0,\n" +
            "    \"plan\": null,\n" +
            "    \"images\": [\n" +
            "      \"https://preview.redd.it/p0gtn3ua8qo41.jpg?width=640&crop=smart&auto=webp&s=6698972aa82b03c9ae708964889d52b581b5d1bd\"\n" +
            "    ],\n" +
            "    \"verified\": false,\n" +
            "    \"lastLogin\": \"2020-04-21\",\n" +
            "    \"facebook_id\": \"string\",\n" +
            "    \"google_id\": \"string\",\n" +
            "    \"type\": \"string\"\n" +
            "  }";

    private String freeProfileWithCredit = "{\n" +
            "    \"_id\": \"5a2539b41c574006c46f1a07\",\n" +
            "    \"username\": \"user0\",\n" +
            "    \"birthDate\": \"2020-04-21\",\n" +
            "    \"gender\": \"F\",\n" +
            "    \"email\": \"example@example.com\",\n" +
            "    \"displayName\": \"user0\",\n" +
            "    \"role\": \"free\",\n" +
            "    \"country\": \"EG\",\n" +
            "    \"credit\": 100,\n" +
            "    \"plan\": null,\n" +
            "    \"images\": [\n" +
            "      \"https://preview.redd.it/p0gtn3ua8qo41.jpg?width=640&crop=smart&auto=webp&s=6698972aa82b03c9ae708964889d52b581b5d1bd\"\n" +
            "    ],\n" +
            "    \"verified\": false,\n" +
            "    \"lastLogin\": \"2020-04-21\",\n" +
            "    \"facebook_id\": \"string\",\n" +
            "    \"google_id\": \"string\",\n" +
            "    \"type\": \"string\"\n" +
            "  }";

    private String premiumProfile = "{\n" +
            "    \"_id\": \"5a2539b41c574006c46f1a07\",\n" +
            "    \"username\": \"user0\",\n" +
            "    \"birthDate\": \"2020-04-21\",\n" +
            "    \"gender\": \"F\",\n" +
            "    \"email\": \"example@example.com\",\n" +
            "    \"displayName\": \"user0\",\n" +
            "    \"role\": \"premium\",\n" +
            "    \"country\": \"EG\",\n" +
            "    \"credit\": 30,\n" +
            "    \"plan\": \"2020-5-21T04:49:50Z\",\n" +
            "    \"images\": [\n" +
            "      \"https://preview.redd.it/p0gtn3ua8qo41.jpg?width=640&crop=smart&auto=webp&s=6698972aa82b03c9ae708964889d52b581b5d1bd\"\n" +
            "    ],\n" +
            "    \"verified\": false,\n" +
            "    \"lastLogin\": \"2020-04-21\",\n" +
            "    \"facebook_id\": \"string\",\n" +
            "    \"google_id\": \"string\",\n" +
            "    \"type\": \"string\"\n" +
            "  }";

    private String premiumProfileWithMoreCredit = "{\n" +
            "    \"_id\": \"5a2539b41c574006c46f1a07\",\n" +
            "    \"username\": \"user0\",\n" +
            "    \"birthDate\": \"2020-04-21\",\n" +
            "    \"gender\": \"F\",\n" +
            "    \"email\": \"example@example.com\",\n" +
            "    \"displayName\": \"user0\",\n" +
            "    \"role\": \"premium\",\n" +
            "    \"country\": \"EG\",\n" +
            "    \"credit\": 130,\n" +
            "    \"plan\": \"2020-5-21T04:49:50Z\",\n" +
            "    \"images\": [\n" +
            "      \"https://preview.redd.it/p0gtn3ua8qo41.jpg?width=640&crop=smart&auto=webp&s=6698972aa82b03c9ae708964889d52b581b5d1bd\"\n" +
            "    ],\n" +
            "    \"verified\": false,\n" +
            "    \"lastLogin\": \"2020-04-21\",\n" +
            "    \"facebook_id\": \"string\",\n" +
            "    \"google_id\": \"string\",\n" +
            "    \"type\": \"string\"\n" +
            "  }";

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadingData() throws IOException {

        ActivityScenario<EmptyActivityForTesting> scenario = ActivityScenario.launch(EmptyActivityForTesting.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody(freeProfile).setResponseCode(200));
        server.enqueue(new MockResponse().setBody(freeProfile).setResponseCode(200));

        String serverUrl = server.url("/").toString();

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(serverUrl);
        Profile profile = oudApi.getProfileOfCurrentUser("").execute().body();


        scenario.onActivity(activity -> {

            PremiumRedeemSubscribeFragment premiumRedeemSubscribeFragment = new PremiumRedeemSubscribeFragment();

            PremiumRedeemSubscribeRepository.getInstance().setBaseUrl(serverUrl);

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainerView, premiumRedeemSubscribeFragment, Constants.PREMIUM_REDEEM_SUBSCRIBE_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_user_name))
                    .check(matches(withText(profile.getDisplayName())));

            onView(withId(R.id.txt_credit))
                    .check(matches(withText(containsString(String.valueOf(profile.getCredit())))));

            onView(withId(R.id.txt_plan))
                    .check(matches(withText(R.string.free_plan)));
        });

        server.shutdown();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenRedeemingCouponSuccessfully_updateViewWithTheNewlyReturnedProfile() throws IOException {
        ActivityScenario<EmptyActivityForTesting> scenario = ActivityScenario.launch(EmptyActivityForTesting.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody(freeProfileWithCredit).setResponseCode(200));
        server.enqueue(new MockResponse().setBody(freeProfile).setResponseCode(200));
        server.enqueue(new MockResponse().setBody(freeProfileWithCredit).setResponseCode(200));

        String serverUrl = server.url("/").toString();

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(serverUrl);
        Profile profile = oudApi.getProfileOfCurrentUser("").execute().body();


        scenario.onActivity(activity -> {

            PremiumRedeemSubscribeFragment premiumRedeemSubscribeFragment = new PremiumRedeemSubscribeFragment();

            PremiumRedeemSubscribeRepository.getInstance().setBaseUrl(serverUrl);

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainerView, premiumRedeemSubscribeFragment, Constants.PREMIUM_REDEEM_SUBSCRIBE_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.edit_txt_coupon))
                    .perform(typeText("مجدى حبيبى يا مغلبنى"));

            onView(withId(R.id.btn_redeem))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_user_name))
                    .check(matches(withText(profile.getDisplayName())));

            onView(withId(R.id.txt_credit))
                    .check(matches(withText(containsString(String.valueOf(profile.getCredit())))));

            onView(withId(R.id.txt_plan))
                    .check(matches(withText(R.string.free_plan)));


        });

        server.shutdown();
    }


    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenSubscribingSuccessfully_updateViewWithTheNewlyReturnedProfile() throws IOException {
        ActivityScenario<EmptyActivityForTesting> scenario = ActivityScenario.launch(EmptyActivityForTesting.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody(premiumProfile).setResponseCode(200));
        server.enqueue(new MockResponse().setBody(freeProfile).setResponseCode(200));
        server.enqueue(new MockResponse().setBody(premiumProfile).setResponseCode(200));

        String serverUrl = server.url("/").toString();

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(serverUrl);
        Profile profile = oudApi.getProfileOfCurrentUser("").execute().body();


        scenario.onActivity(activity -> {

            PremiumRedeemSubscribeFragment premiumRedeemSubscribeFragment = new PremiumRedeemSubscribeFragment();

            PremiumRedeemSubscribeRepository.getInstance().setBaseUrl(serverUrl);

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainerView, premiumRedeemSubscribeFragment, Constants.PREMIUM_REDEEM_SUBSCRIBE_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);


            onView(withId(R.id.btn_subscribe_extend))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.txt_user_name))
                    .check(matches(withText(profile.getDisplayName())));

            onView(withId(R.id.txt_credit))
                    .check(matches(withText(containsString(String.valueOf(profile.getCredit())))));

            String plan = new SimpleDateFormat("yyyy-mm-dd").format(profile.getPlan());

            onView(withId(R.id.txt_plan))
                    .check(matches(withText(containsString(plan))));

            onView(withId(R.id.btn_subscribe_extend))
                    .check(matches(withText(R.string.extend)));


        });

        server.shutdown();
    }

    /*@Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void whenInvalidCoupon_showInvalidCouponToast() throws IOException {
        ActivityScenario<EmptyActivityForTesting> scenario = ActivityScenario.launch(EmptyActivityForTesting.class);

        MockWebServer server = TestUtils.getOkHttpMockWebServer();
        server.enqueue(new MockResponse().setBody(freeProfile).setResponseCode(200));
        server.enqueue(new MockResponse().setResponseCode(PremiumRedeemSubscribeFragment.INVALID_COUPON_CODE));

        String serverUrl = server.url("/").toString();

        scenario.onActivity(activity -> {

            PremiumRedeemSubscribeFragment premiumRedeemSubscribeFragment = new PremiumRedeemSubscribeFragment();

            PremiumRedeemSubscribeRepository.getInstance().setBaseUrl(serverUrl);

            FragmentManager manager = activity.getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragmentContainerView, premiumRedeemSubscribeFragment, Constants.PREMIUM_REDEEM_SUBSCRIBE_FRAGMENT_TAG)
                    .commit();

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            onView(withId(R.id.edit_txt_coupon))
                    .perform(typeText("مجدى حبيبى يا مغلبنى"));

            onView(withId(R.id.btn_redeem))
                    .perform(click());

            TestUtils.sleep(1, MILLIS_TO_PAUSE);

            *//*onView(withText(R.string.please_enter_a_valid_coupon))
                    .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                    .check(matches(isDisplayed()));*//*

            onView(withText(R.string.please_enter_a_valid_coupon))
                    .inRoot(TestUtils.isToast())
                    .check(matches(isDisplayed()));


        });

        server.shutdown();
    }*/

}
