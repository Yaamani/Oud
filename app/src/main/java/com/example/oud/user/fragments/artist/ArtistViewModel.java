package com.example.oud.user.fragments.artist;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.OudList;
import com.example.oud.api.RelatedArtists;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class ArtistViewModel extends ConnectionAwareViewModel<ArtistRepository> {


    private MutableLiveData<Artist> artistMutableLiveData;

    private MutableLiveData<OudList<Album>> lastSetOfLoadedAlbums;
    private ArrayList<MutableLiveData<Album>> loadedAlbums = new ArrayList<>();

    private MutableLiveData<RelatedArtists> similarArtistsMutableLiveData;



    public ArtistViewModel() {
        super(ArtistRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Artist> getArtistMutableLiveData(String token, String artistId) {
        if (artistMutableLiveData == null) {
            artistMutableLiveData = mRepo.fetchArtist(token, artistId);
        } else {
            Artist current = artistMutableLiveData.getValue();
            if (current != null) {
                if (current.get_id().equals(artistId)) {
                    artistMutableLiveData = mRepo.fetchArtist(token, artistId);
                }
            }
        }
        return artistMutableLiveData;
    }


    public MutableLiveData<OudList<Album>> getLastSetOfLoadedAlbums(String token, String artistId) {
        if (lastSetOfLoadedAlbums == null) {
            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        }

        return lastSetOfLoadedAlbums;
    }

    public void loadMoreAlbums(String token, String artistId) {
        lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(token, artistId, lastSetOfLoadedAlbums.getValue().getLimit(), Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
    }

    public ArrayList<MutableLiveData<Album>> getLoadedAlbums() {
        return loadedAlbums;
    }

    public MutableLiveData<RelatedArtists> getSimilarArtistsMutableLiveData(String token, String artistId) {
        if (similarArtistsMutableLiveData == null) {
            similarArtistsMutableLiveData = mRepo.fetchSimilarArtists(token, artistId);
        }
        return similarArtistsMutableLiveData;
    }

    @Override
    public void clearData() {
        artistMutableLiveData = null;
    }
}
