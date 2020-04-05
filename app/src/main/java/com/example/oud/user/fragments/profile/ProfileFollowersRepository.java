package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.FollowingOrFollowersResponse;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.api.UserPlaylistsResponse;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileFollowersRepository extends ConnectionAwareRepository {
    public MutableUserOrArtistWithTotal loadUserFollowers(String userId){
        OudApi oudApi = instantiateRetrofitOudApi();

        MutableLiveData<List<UserOrArtistPreview>> mutableFollowers = new MutableLiveData<>();
        MutableLiveData<Integer> mutableTotal= new MutableLiveData<>();

        Call<FollowingOrFollowersResponse> call = oudApi.getFollowers(userId,"user",0);
        addCall(call).enqueue(new FailureSuccessHandledCallback<FollowingOrFollowersResponse>(this){
            @Override
            public void onResponse(Call<FollowingOrFollowersResponse> call, Response<FollowingOrFollowersResponse> response) {
                super.onResponse(call, response);
                if(response.isSuccessful()){
                    mutableTotal.setValue(response.body().getTotal());
                    mutableFollowers.setValue(response.body().getItems());
                }
            }
        });
        return new MutableUserOrArtistWithTotal(mutableFollowers,mutableTotal);
    }

    public void loadMoreUserFollowers(String userId,int offset,MutableLiveData<List<UserOrArtistPreview>> mutableUserOrArtistList){
        OudApi oudApi = instantiateRetrofitOudApi();
        Call<FollowingOrFollowersResponse> call = oudApi.getFollowers(userId,"user",offset);

        addCall(call).enqueue(new FailureSuccessHandledCallback<FollowingOrFollowersResponse>(this){
            @Override
            public void onResponse(Call call, Response response) {
                super.onResponse(call, response);
                if(response.isSuccessful()){
                    FollowingOrFollowersResponse followingOrFollowersResponse = ((FollowingOrFollowersResponse) response.body());
                    List<UserOrArtistPreview>  previouslyLoadedList = mutableUserOrArtistList.getValue();
                    List<UserOrArtistPreview>  newLoadedPlaylists = followingOrFollowersResponse.getItems();
                    previouslyLoadedList.addAll(newLoadedPlaylists);
                    mutableUserOrArtistList.setValue(previouslyLoadedList);
                }
            }


        });
    }

}
