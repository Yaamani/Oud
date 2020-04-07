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

    public MutableLiveData<Artist> getArtistMutableLiveData(String artistId) {
        if (artistMutableLiveData == null) {
            artistMutableLiveData = mRepo.fetchArtist(artistId);
        } else {
            Artist current = artistMutableLiveData.getValue();
            if (current != null) {
                if (current.get_id().equals(artistId)) {
                    artistMutableLiveData = mRepo.fetchArtist(artistId);
                }
            }
        }
        return artistMutableLiveData;
    }


    public MutableLiveData<OudList<Album>> getLastSetOfLoadedAlbums(String artistId) {
        if (lastSetOfLoadedAlbums == null) {
            lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(artistId, 0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        }

        return lastSetOfLoadedAlbums;
    }

    public void loadMoreAlbums(String artistId) {
        lastSetOfLoadedAlbums = mRepo.fetchSomeAlbums(artistId, lastSetOfLoadedAlbums.getValue().getLimit(), Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
    }

    public ArrayList<MutableLiveData<Album>> getLoadedAlbums() {
        return loadedAlbums;
    }

    public MutableLiveData<RelatedArtists> getSimilarArtistsMutableLiveData(String artistId) {
        if (similarArtistsMutableLiveData == null) {
            similarArtistsMutableLiveData = mRepo.fetchSimilarArtists(artistId);
        }
        return similarArtistsMutableLiveData;
    }

    @Override
    public void clearData() {
        artistMutableLiveData = null;
    }
}
