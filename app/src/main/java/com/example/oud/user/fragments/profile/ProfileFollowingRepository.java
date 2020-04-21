package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.FollowingOrFollowersResponse;
import com.example.oud.api.ListOfBoolean;
import com.example.oud.api.ListOfIds;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.api.UserPlaylistsResponse;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileFollowingRepository extends ConnectionAwareRepository {

    public MutableUserOrArtistWithTotal loadUserFollowedArtists(String userId){
        // OudApi oudApi = instantiateRetrofitOudApi();

        MutableLiveData<List<UserOrArtistPreview>> mutableFollowers = new MutableLiveData<>();
        MutableLiveData<Integer> mutableTotal= new MutableLiveData<>();

        Call<FollowingOrFollowersResponse> call = oudApi.getFollowing(userId,"artist",0);
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

    public void loadMoreUserFollowedArtists(String userId,int offset,MutableLiveData<List<UserOrArtistPreview>> mutableUserOrArtistList){
        // OudApi oudApi = instantiateRetrofitOudApi();
        Call<FollowingOrFollowersResponse> call = oudApi.getFollowing(userId,"artist",offset);

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


    public MutableUserOrArtistWithTotal loadUserFollowedUsers(String userId){
        // OudApi oudApi = instantiateRetrofitOudApi();

        MutableLiveData<List<UserOrArtistPreview>> mutableFollowers = new MutableLiveData<>();
        MutableLiveData<Integer> mutableTotal= new MutableLiveData<>();

        Call<FollowingOrFollowersResponse> call = oudApi.getFollowing(userId,"user",0);
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

    public void loadMoreUserFollowedUsers(String userId,int offset,MutableLiveData<List<UserOrArtistPreview>> mutableUserOrArtistList){
        // OudApi oudApi = instantiateRetrofitOudApi();
        Call<FollowingOrFollowersResponse> call = oudApi.getFollowing(userId,"user",offset);

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

    public void followUser(String token, String userId, ConnectionStatusListener connectionStatusListener){
        ArrayList<String> id = new ArrayList<>();
        id.add(userId);
        ListOfIds listOfIds = new ListOfIds(id);
        Call<Void> call = oudApi.followUsersOrArtists(token,"user",listOfIds);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }

    public void unFollowUser(String token,String userId,ConnectionStatusListener connectionStatusListener){

        Call<Void> call = oudApi.unFollowUsersOrArtists(token,"user",userId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }

    public void followArtist(String token,String userId,ConnectionStatusListener connectionStatusListener){
        ArrayList<String>id = new ArrayList<>();
        id.add(userId);
        ListOfIds listOfIds = new ListOfIds(id);
        Call<Void> call = oudApi.followUsersOrArtists(token,"artist",listOfIds);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }

    public void unFollowArtist(String token,String userId,ConnectionStatusListener connectionStatusListener){

        Call<Void> call = oudApi.unFollowUsersOrArtists(token,"artist",userId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Void>(this,connectionStatusListener) {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call,response);

            }

        });


    }


    public void checkIfIFollowThisUser(String token,String commaSeparatedUsers,MutableLiveData<Boolean> isFollowed){
        Call<ListOfBoolean> call = oudApi.checkIfIFollowTheseUsersOrArtists(token,"user",commaSeparatedUsers);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ListOfBoolean>(this) {
            @Override
            public void onResponse(Call<ListOfBoolean> call, Response<ListOfBoolean> response) {
                super.onResponse(call,response);
                if(response.isSuccessful()){
                    isFollowed.setValue(response.body().getIds().get(0));
                }
            }

        });



    }


    public void checkIfIFollowThisArtist(String token,String commaSeparatedUsers,MutableLiveData<Boolean> isFollowed){
        Call<ListOfBoolean> call = oudApi.checkIfIFollowTheseUsersOrArtists(token,"artist",commaSeparatedUsers);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ListOfBoolean>(this) {
            @Override
            public void onResponse(Call<ListOfBoolean> call, Response<ListOfBoolean> response) {
                super.onResponse(call,response);
                if(response.isSuccessful()){
                    isFollowed.setValue(response.body().getIds().get(0));
                }
            }

        });



    }








}
