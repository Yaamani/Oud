package com.example.oud;

import android.app.Activity;
import android.os.Looper;

import com.example.oud.user.fragments.home.HomeRepository;
import com.example.oud.user.fragments.home.HomeViewModel;
import com.example.tryingstuff.OudApiJsonGenerator;
import com.google.common.truth.Truth;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.os.Looper.getMainLooper;
import static org.robolectric.Shadows.shadowOf;
//import static com.google.common.truth.Truth.*;

@RunWith(RobolectricTestRunner.class)
public class HomeRepositoryTest {



    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        mockWebServer = TestUtils.getOudMockServer();
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadRecentlyPlayedTest() {
        HomeRepository.getInstance().setBaseUrl(mockWebServer.url("/").toString());

        HomeViewModel.OuterItemLiveData outerItemLiveData = HomeRepository.getInstance().loadRecentlyPlayed();

        Assert.assertEquals("Recently played", outerItemLiveData.getTitle().getValue());

        //ActivityController<AppCompatActivity> controller = Robolectric.buildActivity(AppCompatActivity.class);

        HomeViewModel.InnerItemLiveData[] innerItems = outerItemLiveData.getInnerItems();
        for (int i = 0; i < innerItems.length; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            //ShadowLooper.idleMainLooper(1000, TimeUnit.MILLISECONDS);

            shadowOf(getMainLooper()).idle();
            /*shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();
            shadowOf(getMainLooper()).idle();*/

            //Assert.assertEquals("track name as the title, index = " + i, "track"+i, innerItems[i].getTitle().getValue());
            Assert.assertEquals("image url, index = " + i, OudApiJsonGenerator.VECTOR_ART[i], innerItems[i].getImage().getValue());
            Assert.assertEquals("album name as the subtitle,  index = " + i, "album"+i, innerItems[i].getSubTitle().getValue());

            Truth.assertThat(innerItems[i].getTitle().getValue()).isEqualTo("track"+i);

            /*int finalI = i;
            outerItemLiveData.getIcon().observe(controller.get(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Assert.assertEquals("image url, index = " + finalI, OudApiJsonGenerator.PORTRAITS[finalI], *//*innerItems[finalI].getImage().getValue()*//*"HI");
                }
            });*/


            //Assert.fail();
        }
    }

    /*@Test
    public void loadCategoryTest() {
        HomeViewModel.OuterItemLiveData outerItemLiveData = new HomeRepository(mockWebServer.url("/").toString()).loadCategory(0);


    }*/

    @After
    public void cleanup() {
        try {
            mockWebServer.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
