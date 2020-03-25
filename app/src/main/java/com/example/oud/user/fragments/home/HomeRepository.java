package com.example.oud.user.fragments.home;

import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.OudApi;
import com.example.oud.api.RecentlyPlayedTrack;
import com.example.oud.api.RecentlyPlayedTracks;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeRepository implements NestedRecyclerViewOuterItemSupplier {

    private static final String TAG = HomeRepository.class.getSimpleName();

    public static HomeRepository instance = new HomeRepository();

    private String baseUrl;

    private HomeRepository() {
        this.baseUrl = Constants.BASE_URL;
    }

    public static HomeRepository getInstance() {
        return instance;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HomeViewModel.OuterItemLiveData loadRecentlyPlayed() {

        MutableLiveData<String> icon = new MutableLiveData<>(
                "https://iconmonstr.com/wp-content/g/gd/makefg.php?i=../assets/preview/2017/png/iconmonstr-time-17.png&r=255&g=255&b=255");
        MutableLiveData<String> title = new MutableLiveData<>("Recently played");
        HomeViewModel.InnerItemLiveData[] innerItems = new HomeViewModel.InnerItemLiveData[Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT];

        for (int i = 0; i < innerItems.length; i++) {
            innerItems[i] = new HomeViewModel.InnerItemLiveData();
            /*innerItems[i].getTitle().setValue("");
            innerItems[i].getSubTitle().setValue("");
            innerItems[i].getImage().setValue("");*/
        }

        OudApi oudApi = instantiateRetrofitOddAdi();

        fetchRecentlyPlayedTracks(oudApi, Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT, innerItems);

        return new HomeViewModel.OuterItemLiveData(icon, title, innerItems);
    }

    @Override
    public HomeViewModel.OuterItemLiveData loadCategory(int position) {
        return null;
    }

    private OudApi instantiateRetrofitOddAdi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OudApi.class);
    }

    private void fetchRecentlyPlayedTracks(OudApi oudApi, int itemsCount, HomeViewModel.InnerItemLiveData[] innerItems) {
        Call<RecentlyPlayedTracks> recentlyPlayedTracksCall =
                oudApi.recentlyPlayedTracks(itemsCount, null, null);
        recentlyPlayedTracksCall.enqueue(new Callback<RecentlyPlayedTracks>() {

            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                }

                RecentlyPlayedTracks recentlyPlayedTracks = response.body();

                ArrayList<RecentlyPlayedTrack> list = recentlyPlayedTracks.getItems();

                for (int i = 0; i < innerItems.length; i++) {

                    HomeViewModel.InnerItemLiveData current = innerItems[i];

                    //innerItems[i].getPosition().setValue(i);
                    innerItems[i].getTitle().setValue(list.get(i).getTrack().getName());

                    fetchAlbumData(oudApi, list.get(i).getTrack().getAlbumId(), current);
                }
            }

            @Override
            public void onFailure(Call<RecentlyPlayedTracks> call, Throwable t) {
                t.printStackTrace();
            }
        });



    }

    private void fetchAlbumData(OudApi oudApi, String albumId, HomeViewModel.InnerItemLiveData innerItem) {
        Call<Album> albumCall = oudApi.album(albumId);

        albumCall.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                }

                Album album = response.body();

                Log.i(TAG, "onResponse: albumId = " + album.get_id() + ", trackId = " + innerItem.getTitle().getValue());

                innerItem.getImage().setValue(album.getImage());
                innerItem.getSubTitle().setValue(album.getName());
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
