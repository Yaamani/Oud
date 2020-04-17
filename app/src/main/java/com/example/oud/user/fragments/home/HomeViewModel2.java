package com.example.oud.user.fragments.home;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.Category2;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTracks2;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public class HomeViewModel2 extends ConnectionAwareViewModel<HomeRepository2> {

    private MutableLiveData<RecentlyPlayedTracks2> recentlyPlayedLiveData;
    private ArrayList<MutableLiveData> recentlyPlayedAlbumsPlaylistsArtists = new ArrayList<>();

    private MutableLiveData<OudList<Category2>> categoryListLiveData;
    private ArrayList<MutableLiveData<OudList<Playlist>>> playlistsOfEachCategory = new ArrayList<>();

    public HomeViewModel2() {
        super(HomeRepository2.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<RecentlyPlayedTracks2> getRecentlyPlayedLiveData(String token) {
        if (recentlyPlayedLiveData == null) {
            recentlyPlayedLiveData = mRepo.fetchRecentlyPlayedTracks(token);
        }
        return recentlyPlayedLiveData;
    }

    public ArrayList<MutableLiveData> getRecentlyPlayedAlbumsPlaylistsArtists() {
        return recentlyPlayedAlbumsPlaylistsArtists;
    }

    public void addRecentlyPlayedAlbum(String token, String albumId, int position) {
        /*try {
            recentlyPlayedAlbumsPlaylistsArtists.add(position, mRepo.fetchAlbum(albumId));
        } catch (IndexOutOfBoundsException e) {
            recentlyPlayedAlbumsPlaylistsArtists.add(mRepo.fetchAlbum(albumId));
        }*/

        MutableLiveData<Album> data = mRepo.fetchAlbum(token, albumId);

        boolean done = false;
        while(!done) {
            try {
                recentlyPlayedAlbumsPlaylistsArtists.set(position, data);
                done = true;
            } catch (IndexOutOfBoundsException e) {
                recentlyPlayedAlbumsPlaylistsArtists.add(null);
            }

        }
    }

    public void addRecentlyPlayedArtist(String token, String artistId, int position) {
        /*try {
            recentlyPlayedAlbumsPlaylistsArtists.add(position, mRepo.fetchArtist(artistId));
        } catch (IndexOutOfBoundsException e) {
            recentlyPlayedAlbumsPlaylistsArtists.add(mRepo.fetchArtist(artistId));
        }*/

        MutableLiveData<Artist> data = mRepo.fetchArtist(token, artistId);

        boolean done = false;
        while (!done) {
            try {
                recentlyPlayedAlbumsPlaylistsArtists.set(position, data);
                done = true;
            } catch (IndexOutOfBoundsException e) {
                recentlyPlayedAlbumsPlaylistsArtists.add(null);

            }
        }
    }

    public void addRecentlyPlayedPlaylist(String token, String playlistId, int position) {
        /*try {
            recentlyPlayedAlbumsPlaylistsArtists.add(position, mRepo.fetchPlaylist(playlistId));
        } catch (IndexOutOfBoundsException e) {
            recentlyPlayedAlbumsPlaylistsArtists.add(mRepo.fetchPlaylist(playlistId));
        }*/

        MutableLiveData<Playlist> data = mRepo.fetchPlaylist(token, playlistId);

        boolean done = false;
        while (!done) {
            try {
                recentlyPlayedAlbumsPlaylistsArtists.set(position, data);
                done = true;
            } catch (IndexOutOfBoundsException e) {
                recentlyPlayedAlbumsPlaylistsArtists.add(null);

            }
        }
    }

    public MutableLiveData<OudList<Category2>> getCategoryListLiveData() {
        if (categoryListLiveData == null) {
            categoryListLiveData = mRepo.fetchCategoryList();
        }
        return categoryListLiveData;
    }

    public ArrayList<MutableLiveData<OudList<Playlist>>> getPlaylistsOfEachCategory() {
        return playlistsOfEachCategory;
    }

    public void addCategoryPlaylists(String token, String categoryId, int position) {

        MutableLiveData<OudList<Playlist>> data = mRepo.fetchCategoryPlaylists(token, categoryId);

        boolean done = false;
        while (!done) {
            try {
                playlistsOfEachCategory.set(position, data);
                done = true;
            } catch (IndexOutOfBoundsException e) {
                playlistsOfEachCategory.add(null);

            }
        }
    }


    @Override
    public void clearData() {
        recentlyPlayedLiveData = null;
        //recentlyPlayedAlbumsPlaylistsArtists = new ArrayList<>();
        categoryListLiveData = null;
    }
}
