package com.example.oud.user.fragments.artist;

import com.example.oud.Constants;
import com.example.oud.api.Artist;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArtistViewModel extends ConnectionAwareViewModel<ArtistRepository> {


    private MutableLiveData<Artist> artistMutableLiveData;



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

    @Override
    public void clearData() {
        artistMutableLiveData = null;
    }
}
