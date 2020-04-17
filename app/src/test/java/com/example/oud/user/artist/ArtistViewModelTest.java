package com.example.oud.user.artist;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.BooleanIdsResponse;
import com.example.oud.api.IsFoundResponse;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.testutils.TestUtils;
import com.example.oud.user.fragments.artist.ArtistViewModel;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ArtistViewModelTest {
    
    public static final int MILLIS_TO_PAUSE = 250;

    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY
    // WORKS WITH THE MOCK SERVER ONLY

    private OudApi oudApi;

    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void getArtistMutableLiveDataTest() throws IOException {

        Artist artist0 = oudApi.artist("", "artist0").execute().body();


        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        MutableLiveData<Artist> artistMutableLiveData = viewModel.getArtistMutableLiveData("", "artist0");

        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        assertThat(artistMutableLiveData.getValue().get_id())
                .isEqualTo(artist0.get_id());
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void getArtistMutableLiveData_differentArtist_Test() throws IOException {

        Artist artist = oudApi.artist("", "artist1").execute().body();


        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        MutableLiveData<Artist> artistMutableLiveData = viewModel.getArtistMutableLiveData("", "artist0");

        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<Artist> artistMutableLiveData1 = viewModel.getArtistMutableLiveData("", "artist1");


        TestUtils.sleep(1, MILLIS_TO_PAUSE);


        assertThat(artistMutableLiveData1.getValue().get_id())
                .isEqualTo(artist.get_id());
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void getDoesUserFollowThisArtistTest() throws IOException {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("artist10");

        BooleanIdsResponse doesUserFollow = oudApi.doesCurrentUserFollowsArtistsOrUsers("", Constants.API_ARTIST, OudUtils.commaSeparatedListQueryParameter(ids)).execute().body();

        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        MutableLiveData<BooleanIdsResponse> doesUserFollowArtistsLiveData = viewModel.getDoesUserFollowThisArtist("", "artist10");

        TestUtils.sleep(1, 100);

        assertThat(doesUserFollow.getIds().get(0))
                .isEqualTo(doesUserFollowArtistsLiveData.getValue().getIds().get(0));
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void followThisArtist_liveDataChangedAccordingly_Test() {
        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        viewModel.getDoesUserFollowThisArtist("", "artist10");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        viewModel.followThisArtist("", "artist10");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<BooleanIdsResponse> doesUserFollowArtistsLiveData = viewModel.getDoesUserFollowThisArtist("", "artist10");

        assertThat(doesUserFollowArtistsLiveData.getValue().getIds().get(0))
                .isTrue();

    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void unfollowThisArtist_liveDataChangedAccordingly_Test() {
        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        viewModel.getDoesUserFollowThisArtist("", "artist0");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        viewModel.unfollowThisArtist("", "artist0");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<BooleanIdsResponse> doesUserFollowArtistsLiveData = viewModel.getDoesUserFollowThisArtist("", "artist0");

        assertThat(doesUserFollowArtistsLiveData.getValue().getIds().get(0))
                .isFalse();

    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void areTheseTracksLikedTest() throws IOException {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("track0");
        ids.add("track1");
        ids.add("track2");
        ids.add("track3");

        IsFoundResponse isFoundResponse = oudApi.getAreTheseTracksLiked("", OudUtils.commaSeparatedListQueryParameter(ids)).execute().body();

        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus
        
        MutableLiveData<IsFoundResponse> areTheseTracksLikedLiveData = viewModel.getAreTracksLikedLiveData("", ids);
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < isFoundResponse.getIsFound().size(); i++) {
            assertThat(isFoundResponse.getIsFound().get(i))
                    .isEqualTo(areTheseTracksLikedLiveData.getValue().getIsFound().get(i));
        }
        
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void addTrackToLikedTracks_liveDataChangedAccordingly_Test() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("track0");
        ids.add("track1");
        ids.add("track2");
        ids.add("track3");

        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        viewModel.getAreTracksLikedLiveData("", ids);
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        viewModel.addTrackToLikedTracks("", "track2", 2);
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<IsFoundResponse> areTheseTracksLikedLiveData = viewModel.getAreTracksLikedLiveData("", ids);

        assertThat(areTheseTracksLikedLiveData.getValue().getIsFound().get(2))
                .isTrue();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void removeTrackToLikedTracks_liveDataChangedAccordingly_Test() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("track0");
        ids.add("track1");
        ids.add("track2");
        ids.add("track3");

        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        viewModel.getAreTracksLikedLiveData("", ids);
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        viewModel.removeTrackFromLikedTracks("", "track2", 2);
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<IsFoundResponse> areTheseTracksLikedLiveData = viewModel.getAreTracksLikedLiveData("", ids);

        assertThat(areTheseTracksLikedLiveData.getValue().getIsFound().get(2))
                .isFalse();
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadMoreAlbumsTest() throws IOException {
        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<Album> albumOudList = oudApi.artistAlbums("", "artist10", 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT).execute().body();

        MutableLiveData<OudList<Album>> albumListMutableLiveData = viewModel.loadMoreAlbums("", "artist10");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < albumOudList.getItems().size(); i++) {
            assertThat(albumOudList.getItems().get(i).get_id())
                .isEqualTo(albumListMutableLiveData.getValue().getItems().get(i).get_id());
        }
    }

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void loadMoreAlbums_Again_Test() throws IOException {
        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<Album> albumOudList = oudApi.artistAlbums("", "artist10", Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT).execute().body();

        viewModel.loadMoreAlbums("", "artist10");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        MutableLiveData<OudList<Album>> albumListMutableLiveData = viewModel.loadMoreAlbums("", "artist10");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < albumOudList.getItems().size(); i++) {
            assertThat(albumOudList.getItems().get(i).get_id())
                    .isEqualTo(albumListMutableLiveData.getValue().getItems().get(i).get_id());
        }
    }

    /*@Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void getLoadedAlbumsTest() throws IOException {
        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        OudList<Album> albumOudList = oudApi.artistAlbums("", "artist10", 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT).execute().body();

        viewModel.loadMoreAlbums("", "artist10");
        TestUtils.sleep(1, millisToPause);

        ArrayList<MutableLiveData<Album>> albumListMutableLiveData = viewModel.getLoadedAlbums();
        // TestUtils.sleep(1, millisToPause);

        for (int i = 0; i < albumOudList.getItems().size(); i++) {
            assertThat(albumOudList.getItems().get(i).get_id())
                    .isEqualTo(albumListMutableLiveData.get(i).getValue().get_id());
        }
    }*/

    @Test
    // @Ignore
    @LooperMode(LooperMode.Mode.PAUSED)
    public void getSimilarArtistsMutableLiveDataTest() throws IOException {
        RelatedArtists relatedArtists = oudApi.similarArtists("", "artist0").execute().body();

        ArtistViewModel viewModel = new ArtistViewModel();
        viewModel.getConnectionStatus(); // Initializes connectionStatus

        MutableLiveData<RelatedArtists> relatedArtistsMutableLiveData = viewModel.getSimilarArtistsMutableLiveData("", "artist0");
        TestUtils.sleep(1, MILLIS_TO_PAUSE);

        for (int i = 0; i < relatedArtists.getArtists().size(); i++) {
            assertThat(relatedArtists.getArtists().get(i).get_id())
                    .isEqualTo(relatedArtistsMutableLiveData.getValue().getArtists().get(i).get_id());
        }
    }

}
