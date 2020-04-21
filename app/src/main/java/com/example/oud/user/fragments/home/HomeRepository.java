package com.example.oud.user.fragments.home;

import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.EventListener;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;
import com.example.oud.api.Album;
import com.example.oud.api.Category;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTrack;
import com.example.oud.api.RecentlyPlayedTracks;
import com.example.oud.connectionaware.ConnectionAwareRepository;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

// Wow it really needs to be refactored :(

@Deprecated
public class HomeRepository extends ConnectionAwareRepository implements NestedRecyclerViewOuterItemSupplier {

    private static final String TAG = HomeRepository.class.getSimpleName();

    public static HomeRepository instance = new HomeRepository();
    //private OudApi oudApi = instantiateRetrofitOudApi();

    private ArrayList<RecentlyPlayedTrack> fetchedRecentlyPlayedTracks;

    private boolean fetchingForCategoryListStarted = false;
    private OudList<Category> fetchedCategoryList;
    private ArrayList<EventListener> onCategoryListLoadedListeners = new ArrayList<>();


    public static HomeRepository getInstance() {
        return instance;
    }

    public MutableLiveData<Boolean> areThereRecentlyPlayedTracks() {
        MutableLiveData<Boolean> areThereRecentlyPlayedTracks = new MutableLiveData<>();

        OudApi oudApi = instantiateRetrofitOudApi(getBaseUrl());

        fetchRecentlyPlayedTracks(oudApi, areThereRecentlyPlayedTracks);

        return areThereRecentlyPlayedTracks;
    }

    public HomeViewModel.OuterItemLiveData loadRecentlyPlayed() {

        if (fetchedRecentlyPlayedTracks == null)
            throw new IllegalStateException("You should know if there are recently played tracks or not first. call " +
                    "areThereRecentlyPlayedTracks() and observe the changes. When you observe true, you can call loadRecentlyPlayed().");

        if (fetchedRecentlyPlayedTracks.isEmpty())
            throw new IllegalStateException("There are no recently played tracks to load.");

        MutableLiveData<Integer> icon = new MutableLiveData<Integer>(
                Constants.USER_HOME_RECENTLY_PLAYED_ICON);

        MutableLiveData<String> title = new MutableLiveData<>("Recently played");

        MutableLiveData<ArrayList<HomeViewModel.InnerItemLiveData>> innerItemsLiveData =
                new MutableLiveData<>(new ArrayList<>());

        OudApi oudApi = instantiateRetrofitOudApi(getBaseUrl());

        for (int i = 0; i < fetchedRecentlyPlayedTracks.size(); i++) {
            HomeViewModel.InnerItemLiveData current = new HomeViewModel.InnerItemLiveData();
            innerItemsLiveData.getValue().add(current);

            current.getTitle().setValue(fetchedRecentlyPlayedTracks.get(i).getTrack().getName());

            HashMap<String, Object> map = new HashMap<>();
            map.put(Constants.TRACK_ID_KEY, fetchedRecentlyPlayedTracks.get(i).getTrack().get_id());
            current.getRelatedInfo().setValue(map);

            fetchAlbumData(oudApi, fetchedRecentlyPlayedTracks.get(i).getTrack().getAlbumId(), current);
        }


        //fetchRecentlyPlayedTracks(oudApi, Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, innerItemsLiveData);


        return new HomeViewModel.OuterItemLiveData(icon, title, innerItemsLiveData);
    }

    //public

    public HomeViewModel.OuterItemLiveData loadCategory(int position) {

        MutableLiveData<Integer> icon = new MutableLiveData<>(
                Constants.USER_HOME_RECENTLY_CATEGORY_ICON);

        MutableLiveData<String> title = new MutableLiveData<>();
        MutableLiveData<ArrayList<HomeViewModel.InnerItemLiveData>> innerItems = new MutableLiveData<>();



        OudApi oudApi = instantiateRetrofitOudApi(getBaseUrl());

        fetchCategoryList(oudApi);

        if (fetchedCategoryList == null)
            onCategoryListLoadedListeners.add(() -> fetchCategory(oudApi, position, title, innerItems));
        else
            fetchCategory(oudApi, position, title, innerItems);

        return new HomeViewModel.OuterItemLiveData(icon, title, innerItems);
    }

    private void fetchCategory(OudApi oudApi,
                               int position,
                               MutableLiveData<String> title,
                               MutableLiveData<ArrayList<HomeViewModel.InnerItemLiveData>> innerItemsLiveData) {

        Category category = fetchedCategoryList.getItems().get(position);

        title.setValue(category.getName());

        ArrayList<HomeViewModel.InnerItemLiveData> innerItems = new ArrayList<>(category.getPlaylists().size());

        int loops = category.getPlaylists().size();
        for (int i = 0; i < loops; i++) {

            innerItems.add(new HomeViewModel.InnerItemLiveData());
            innerItems.get(i).getSubTitle().setValue("");

            String playlistId = category.getPlaylists().get(i);
            fetchPlaylist(oudApi, playlistId, innerItems.get(i));
        }

        innerItemsLiveData.setValue(innerItems);
    }

    private void fetchPlaylist(OudApi oudApi, String playlistId, HomeViewModel.InnerItemLiveData innerItem) {

        Call<Playlist> playlistCall = oudApi.playlist("token", playlistId);
        /*playlistCall.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    //connectionFailedListener.onConnectionFailure();
                    return;
                }

                Playlist playlist = response.body();
                innerItem.getImage().setValue(playlist.getImage());
                innerItem.getTitle().setValue(playlist.getName());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                t.printStackTrace();
                connectionFailedListener.onConnectionFailure();
            }
        });*/



        addCall(playlistCall).enqueue(new FailureSuccessHandledCallback<Playlist>(this) {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                super.onResponse(call, response);

                if(!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    //connectionFailedListener.onConnectionFailure();
                    return;
                }

                Playlist playlist = response.body();

                innerItem.getImage().setValue(playlist.getImage());
                innerItem.getTitle().setValue(playlist.getName());

                HashMap<String, Object> map = new HashMap<>();
                map.put(Constants.PLAYLIST_ID_KEY, playlist.getId());
                innerItem.getRelatedInfo().setValue(map);
            }
        });
    }


    private void fetchCategoryList(OudApi oudApi) {
        if (fetchedCategoryList != null | fetchingForCategoryListStarted)
            return;

        fetchingForCategoryListStarted = true;

        Call<OudList<Category>> categoryCall = oudApi.listOfCategories(null, Constants.USER_HOME_CATEGORIES_COUNT);

        /*categoryCall.enqueue(new Callback<OudList<Category>>() {
            @Override
            public void onResponse(Call<OudList<Category>> call, Response<OudList<Category>> response) {
                if (!response.isSuccessful()) {
                    fetchingForCategoryListStarted = false;
                    Log.e(TAG, "onResponse: " + response.code());
                    connectionFailedListener.onConnectionFailure();
                    return;
                }

                fetchedCategoryList = response.body();

                while (onCategoryListLoadedListeners.size() != 0) {
                    onCategoryListLoadedListeners.remove(0).onTriggered();
                }
            }

            @Override
            public void onFailure(Call<OudList<Category>> call, Throwable t) {
                fetchingForCategoryListStarted = false;
                t.printStackTrace();
                connectionFailedListener.onConnectionFailure();
            }
        });*/

        addCall(categoryCall).enqueue(new FailureSuccessHandledCallback<OudList<Category>>(this) {
            @Override
            public void onResponse(Call<OudList<Category>> call, Response<OudList<Category>> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    fetchingForCategoryListStarted = false;
                    Log.e(TAG, "onResponse: " + response.code());
                    //connectionFailedListener.onConnectionFailure();
                    return;
                }

                fetchedCategoryList = response.body();

                while (onCategoryListLoadedListeners.size() != 0) {
                    onCategoryListLoadedListeners.remove(0).onTriggered();
                }
            }

            @Override
            public void onFailure(Call<OudList<Category>> call, Throwable t) {
                super.onFailure(call, t);
                fetchingForCategoryListStarted = false;
            }
        });
    }

    private void fetchRecentlyPlayedTracks(OudApi oudApi, MutableLiveData<Boolean> areThereRecentlyPlayedTracks) {
        Call<RecentlyPlayedTracks> recentlyPlayedTracksCall =
                oudApi.recentlyPlayedTracks("token", Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, null, null);
        /*recentlyPlayedTracksCall.enqueue(new Callback<RecentlyPlayedTracks>() {

            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                if (response.code() == 204) {
                    areThereRecentlyPlayedTracks.setValue(false);
                    return;
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();

                fetchedRecentlyPlayedTracks = recentlyPlayedTracks.getItems();

                if (fetchedRecentlyPlayedTracks.isEmpty()) {
                    areThereRecentlyPlayedTracks.setValue(false);
                    return;
                }

                areThereRecentlyPlayedTracks.setValue(true);
            }

            @Override
            public void onFailure(Call<RecentlyPlayedTracks> call, Throwable t) {
                t.printStackTrace();
                connectionFailedListener.onConnectionFailure();

            }
        });*/

        addCall(recentlyPlayedTracksCall).enqueue(new FailureSuccessHandledCallback<RecentlyPlayedTracks>(this) {
            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                if (response.code() == 204) {
                    areThereRecentlyPlayedTracks.setValue(false);
                    return;
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();

                fetchedRecentlyPlayedTracks = recentlyPlayedTracks.getItems();

                if (fetchedRecentlyPlayedTracks.isEmpty()) {
                    areThereRecentlyPlayedTracks.setValue(false);
                    return;
                }

                areThereRecentlyPlayedTracks.setValue(true);
            }
        });
    }

    @Deprecated
    private void fetchRecentlyPlayedTracks(OudApi oudApi, int itemsCount, ArrayList<HomeViewModel.InnerItemLiveData> innerItems) {
        Call<RecentlyPlayedTracks> recentlyPlayedTracksCall =
                oudApi.recentlyPlayedTracks("token", itemsCount, null, null);
        /*recentlyPlayedTracksCall.enqueue(new Callback<RecentlyPlayedTracks>() {

            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();

                ArrayList<RecentlyPlayedTrack> list = recentlyPlayedTracks.getItems();

                for (int i = 0; i < innerItems.size(); i++) {

                    HomeViewModel.InnerItemLiveData current = innerItems.get(i);

                    //innerItems[i].getPosition().setValue(i);
                    innerItems.get(i).getTitle().setValue(list.get(i).getTrack().getName());

                    fetchAlbumData(oudApi, list.get(i).getTrack().getAlbumId(), current);
                }
            }

            @Override
            public void onFailure(Call<RecentlyPlayedTracks> call, Throwable t) {
                t.printStackTrace();
                connectionFailedListener.onConnectionFailure();
            }
        });*/

        addCall(recentlyPlayedTracksCall).enqueue(new FailureSuccessHandledCallback<RecentlyPlayedTracks>(this) {
            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                super.onResponse(call, response);

                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();

                ArrayList<RecentlyPlayedTrack> list = recentlyPlayedTracks.getItems();

                for (int i = 0; i < innerItems.size(); i++) {

                    HomeViewModel.InnerItemLiveData current = innerItems.get(i);

                    //innerItems[i].getPosition().setValue(i);
                    innerItems.get(i).getTitle().setValue(list.get(i).getTrack().getName());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Constants.TRACK_ID_KEY, list.get(i).getTrack().get_id());
                    innerItems.get(i).getRelatedInfo().setValue(map);

                    fetchAlbumData(oudApi, list.get(i).getTrack().getAlbumId(), current);
                }
            }
        });

    }

    private void fetchAlbumData(OudApi oudApi, String albumId, HomeViewModel.InnerItemLiveData innerItem) {
        Call<Album> albumCall = oudApi.album("token", albumId);

       /* albumCall.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                Album album = response.body();

                Log.i(TAG, "onResponse: albumId = " + album.get_id() + ", trackId = " + innerItem.getTitle().getValue());

                innerItem.getImage().setValue(album.getImage());
                innerItem.getSubTitle().setValue(album.getName());
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                t.printStackTrace();
                connectionFailedListener.onConnectionFailure();
            }
        });*/

       addCall(albumCall).enqueue(new FailureSuccessHandledCallback<Album>(this) {
           @Override
           public void onResponse(Call<Album> call, Response<Album> response) {
               super.onResponse(call, response);

               if (!response.isSuccessful()) {
                   Log.e(TAG, "onResponse: " + response.code());
                   return;
               }

               Album album = response.body();

               Log.i(TAG, "onResponse: albumId = " + album.get_id() + ", trackId = " + innerItem.getTitle().getValue());

               innerItem.getImage().setValue(album.getImage());
               innerItem.getSubTitle().setValue(album.getName());

               /*HashMap<String, Object> map = new HashMap<>();
               map.put(Constants.ALBUM_ID_KEY, album.get_id());
               innerItem.getRelatedInfo().setValue(map);*/
           }
       });
    }


}
