package com.example.oud.testutils;

import android.view.View;

import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.tryingstuff.OudApiJsonGenerator;

import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Looper.getMainLooper;
import static org.robolectric.Shadows.shadowOf;

public class TestUtils {

    public static MockWebServer getOudMockServer(int recentlyPlayedTrackCount,
                                                 int categoryPlaylistCount,
                                                 int playlistTrackCount) {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(getOudMockServerDispatcher(recentlyPlayedTrackCount,
                                                                categoryPlaylistCount,
                                                                playlistTrackCount));
        return mockWebServer;
    }

    public static MockWebServer getOudMockServerTimeoutFailure() {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(getOudMockServerTimeoutFailureDispatcher());
        return mockWebServer;
    }

    private static Dispatcher getOudMockServerTimeoutFailureDispatcher() {
        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                return new MockResponse()
                        .setResponseCode(200)
                        .throttleBody(1, 1, TimeUnit.MINUTES)
                        .setBody(OudApiJsonGenerator.getJsonTrack(0));
            }
        };

        return dispatcher;
    }

    public static OudApi instantiateOudApi() {
        String baseUrl;

        if (Constants.MOCK)
            baseUrl = Constants.YAMANI_MOCK_BASE_URL;
        else
            baseUrl = Constants.BASE_URL;

        OudApi oudApi = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OudApi.class);

        return oudApi;
    }

    public static void sleep(int iterationCount, int millisForEachIteration) {
        for (int i = 0; i < iterationCount; i++) {
            try {
                Thread.sleep(millisForEachIteration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            shadowOf(getMainLooper()).idle();
        }
    }

    private static Dispatcher getOudMockServerDispatcher(int recentlyPlayedTrackCount, 
                                                         int categoryPlaylistCount, 
                                                         int playlistTrackCount) {
        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {

                String path = recordedRequest.getRequestUrl().encodedPath();

                // paths with character at the end
                switch (path) {
                    case "/me/player/recently-played":
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(OudApiJsonGenerator.getJsonRecentlyPlayed(recentlyPlayedTrackCount));

                    case "/browse/categories":
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(OudApiJsonGenerator.getJsonListOfCategories(Constants.USER_HOME_CATEGORIES_COUNT));
                }

                // paths with integer at the end
                //int i = Integer.parseInt(path.substring(path.length() - 1));
                switch (path/*.substring(0, path.length()-1)*/) {

                    case "/albums/album0":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(0));
                    case "/albums/album1":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(1));
                    case "/albums/album2":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(2));
                    case "/albums/album3":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(3));
                    case "/albums/album4":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(4));
                    case "/albums/album5":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(5));


                    /*case "/albums/album":
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(OudApiJsonGenerator.getJsonAlbum(i));*/
                        
                    case "/browse/categories/category0":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(0));
                    case "/browse/categories/category1":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(1));
                    case "/browse/categories/category2":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(2));
                    case "/browse/categories/category3":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(3));
                    case "/browse/categories/category4":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(4));
                    case "/browse/categories/category5":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(5));
                    case "/browse/categories/category6":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonCategory(6));

                    /*case "/browse/categories/category":
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(OudApiJsonGenerator.getJsonCategory(i));*/
                        
                    case "/playlists/playlist0":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(0, playlistTrackCount));
                    case "/playlists/playlist1":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(1, playlistTrackCount));
                    case "/playlists/playlist2":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(2, playlistTrackCount));
                    case "/playlists/playlist3":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(3, playlistTrackCount));
                    case "/playlists/playlist4":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(4, playlistTrackCount));
                    case "/playlists/playlist5":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(5, playlistTrackCount));
                    case "/playlists/playlist6":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(6, playlistTrackCount));
                    case "/playlists/playlist7":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(7, playlistTrackCount));
                    case "/playlists/playlist8":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(8, playlistTrackCount));
                    case "/playlists/playlist9":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(9, playlistTrackCount));
                    case "/playlists/playlist10":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(10, playlistTrackCount));
                    case "/playlists/playlist11":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(11, playlistTrackCount));
                    case "/playlists/playlist12":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(12, playlistTrackCount));
                    case "/playlists/playlist13":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(13, playlistTrackCount));
                    case "/playlists/playlist14":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(14, playlistTrackCount));
                    case "/playlists/playlist15":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(15, playlistTrackCount));
                    case "/playlists/playlist16":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(16, playlistTrackCount));
                    case "/playlists/playlist17":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(17, playlistTrackCount));
                    case "/playlists/playlist18":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(18, playlistTrackCount));
                    case "/playlists/playlist19":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(19, playlistTrackCount));
                    case "/playlists/playlist20":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(20, playlistTrackCount));
                    case "/playlists/playlist21":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(21, playlistTrackCount));
                    case "/playlists/playlist22":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(22, playlistTrackCount));
                    case "/playlists/playlist23":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(23, playlistTrackCount));
                    case "/playlists/playlist24":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(24, playlistTrackCount));
                    case "/playlists/playlist25":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(25, playlistTrackCount));
                    case "/playlists/playlist26":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(26, playlistTrackCount));
                    case "/playlists/playlist27":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(27, playlistTrackCount));
                    case "/playlists/playlist28":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(28, playlistTrackCount));
                    case "/playlists/playlist29":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(29, playlistTrackCount));
                    case "/playlists/playlist30":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(30, playlistTrackCount));
                    case "/playlists/playlist31":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(31, playlistTrackCount));
                    case "/playlists/playlist32":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(32, playlistTrackCount));
                    case "/playlists/playlist33":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(33, playlistTrackCount));
                    case "/playlists/playlist34":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(34, playlistTrackCount));
                    case "/playlists/playlist35":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(35, playlistTrackCount));
                    case "/playlists/playlist36":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(36, playlistTrackCount));
                    case "/playlists/playlist37":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(37, playlistTrackCount));
                    case "/playlists/playlist38":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(38, playlistTrackCount));
                    case "/playlists/playlist39":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(39, playlistTrackCount));
                    case "/playlists/playlist40":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(40, playlistTrackCount));
                    case "/playlists/playlist41":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(41, playlistTrackCount));
                    case "/playlists/playlist42":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(42, playlistTrackCount));
                    case "/playlists/playlist43":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(43, playlistTrackCount));
                    case "/playlists/playlist44":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(44, playlistTrackCount));
                    case "/playlists/playlist45":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(45, playlistTrackCount));
                    case "/playlists/playlist46":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(46, playlistTrackCount));
                    case "/playlists/playlist47":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(47, playlistTrackCount));
                    case "/playlists/playlist48":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(48, playlistTrackCount));
                    case "/playlists/playlist49":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonPlaylist(49, playlistTrackCount));

                    /*case "/playlists/playlist":
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(OudApiJsonGenerator.getJsonPlaylist(i, playlistTrackCount));*/

                }
                return new MockResponse().setResponseCode(404);
            }
        };

        return dispatcher;
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

}
