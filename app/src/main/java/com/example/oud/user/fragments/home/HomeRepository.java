package com.example.oud.user.fragments.home;

import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.api.OudApi;
import com.example.oud.api.RecentlyPlayedTrack;
import com.example.oud.api.RecentlyPlayedTracks;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeRepository implements NestedRecyclerViewOuterItemSupplier{


    private static final String TAG = HomeRepository.class.getSimpleName();


    public HomeViewModel.OuterItemLiveData loadRecentlyPlayed() {
        MutableLiveData<String> icon = new MutableLiveData<>(
                "https://iconmonstr.com/wp-content/g/gd/makefg.php?i=../assets/preview/2017/png/iconmonstr-time-17.png&r=255&g=255&b=255");
        MutableLiveData<String> title = new MutableLiveData<>("Recently played");
        HomeViewModel.InnerItemLiveData[] innerItems = new HomeViewModel.InnerItemLiveData[Constants.USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT];


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OudApi oudApi = retrofit.create(OudApi.class);

        Call<RecentlyPlayedTracks> call = oudApi.recentlyPlayedTracks(Constants.USER_HOME_CATEGORIES_COUNT, null, null);
        call.enqueue(new Callback<RecentlyPlayedTracks>() {

            @Override
            public void onResponse(Call<RecentlyPlayedTracks> call, Response<RecentlyPlayedTracks> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                }

                RecentlyPlayedTracks recentlyPlayedTrack = response.body();

                RecentlyPlayedTrack[] list = recentlyPlayedTrack.getItems();

                for (int i = 0; i < innerItems.length; i++) {
                    innerItems[i] = new HomeViewModel.InnerItemLiveData();

                    //innerItems[i].getImage().setValue(list[i].getTrack().get);
                    //innerItems[i].mImage = list[i].getTrack().
                    //innerItems[i].mTitle = list[i].getTrack().
                }
            }

            @Override
            public void onFailure(Call<RecentlyPlayedTracks> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return new HomeViewModel.OuterItemLiveData(icon, title, innerItems);
    }

    @Override
    public HomeViewModel.OuterItemLiveData loadCategory(int position) {
        return null;
    }


}
