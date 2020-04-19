package com.example.oud.user.artist;

import com.example.oud.ConnectionStatusListener;
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
import com.example.oud.user.fragments.artist.ArtistRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

import java.io.IOException;
import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

import static com.google.common.truth.Truth.*;

@RunWith(RobolectricTestRunner.class)
public class ArtistRepositoryTest {

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
    private ArtistRepository repo;
    private ConnectionStatusListener listener = new ConnectionStatusListener() {
        @Override
        public void onConnectionSuccess() {

        }

        @Override
        public void onConnectionFailure() {

        }
    };


    @Before
    public void setUp() {
        oudApi = TestUtils.instantiateOudApi();
        repo = ArtistRepository.getInstance();
        repo.setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);
        repo.setConnectionStatusListener(listener);
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void fetchArtistTest() throws IOException {

        Artist artist0 = oudApi.artist("", "artist0").execute().body();

        MutableLiveData<Artist> artistMutableLiveData = repo.fetchArtist("", "artist0");

        TestUtils.sleep(1, 100);

        assertThat(artistMutableLiveData.getValue().get_id())
                .isEqualTo(artist0.get_id());

    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void doesUserFollowTheseArtistsOrUsersTest() throws IOException {
        ArrayList<String> ids = new ArrayList<>();
        ids.add("artist10");

        BooleanIdsResponse doesUserFollow = oudApi.doesCurrentUserFollowsArtistsOrUsers("", Constants.API_ARTIST, OudUtils.commaSeparatedListQueryParameter(ids)).execute().body();



        MutableLiveData<BooleanIdsResponse> doesUserFollowArtistsLiveData = repo.doesUserFollowTheseArtistsOrUsers("", Constants.API_ARTIST, ids);

        TestUtils.sleep(1, 100);

        assertThat(doesUserFollow.getIds().get(0))
            .isEqualTo(doesUserFollowArtistsLiveData.getValue().getIds().get(0));

    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void fetchSomeAlbumsTest() throws IOException {

        OudList<Album> albumOudList = oudApi.artistAlbums("", "artist0", 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT).execute().body();

        MutableLiveData<OudList<Album>> albumListMutableLiveData = repo.fetchSomeAlbums("", "artist0", 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);

        TestUtils.sleep(1, 100);

        for (int i = 0; i < albumOudList.getItems().size(); i++) {
            assertThat(albumOudList.getItems().get(i).get_id())
                    .isEqualTo(albumListMutableLiveData.getValue().getItems().get(i).get_id());
        }
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void fetchSimilarArtistsTest() throws IOException {

        RelatedArtists relatedArtists = oudApi.similarArtists("", "artist0").execute().body();

        MutableLiveData<RelatedArtists> relatedArtistsMutableLiveData = repo.fetchSimilarArtists("", "artist0");

        TestUtils.sleep(1, 100);

        for (int i = 0; i < relatedArtists.getArtists().size(); i++) {
            assertThat(relatedArtists.getArtists().get(i).get_id())
                    .isEqualTo(relatedArtistsMutableLiveData.getValue().getArtists().get(i).get_id());
        }
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    public void areTracksLikedTest() throws IOException {

        ArrayList<String> ids = new ArrayList<>();
        ids.add("track0");
        ids.add("track1");
        ids.add("track2");
        ids.add("track3");

        IsFoundResponse isFoundResponse = oudApi.getAreTheseTracksLiked("", OudUtils.commaSeparatedListQueryParameter(ids)).execute().body();

        MutableLiveData<IsFoundResponse> savedTracksMutableLiveData = repo.areTracksLiked("", ids);

        TestUtils.sleep(1, 100);

        for (int i = 0; i < isFoundResponse.getIsFound().size(); i++) {
            assertThat(isFoundResponse.getIsFound().get(i))
                    .isEqualTo(savedTracksMutableLiveData.getValue().getIsFound().get(i));
        }
    }
}
