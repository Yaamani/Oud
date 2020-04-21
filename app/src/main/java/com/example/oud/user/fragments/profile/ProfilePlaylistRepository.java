package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.FollowingPublicityPayload;
import com.example.oud.api.ListOfBoolean;
import com.example.oud.api.ListOfIds;
import com.example.oud.api.OudApi;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserPlaylistsResponse;
import com.example.oud.api.publicPlaylistfollow;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilePlaylistRepository extends ConnectionAwareRepository {

    // private OudApi oudApi;

    public ProfilePlaylistRepository() {
        // oudApi = instantiateRetrofitOudApi();
    }


        public  MutablePlaylistWithTotal loadUserPlaylists(String userId){
            // oudApi = instantiateRetrofitOudApi();
            MutableLiveData<List<PlaylistPreview>> mutablePlaylists = new MutableLiveData<>();
            MutableLiveData<Integer> total= new MutableLiveData<>();

            Call<UserPlaylistsResponse> call = oudApi.getUserPlaylists(userId);

            addCall(call).enqueue(new FailureSuccessHandledCallback<UserPlaylistsResponse>(this){
                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    if(response.isSuccessful()){
                        total.setValue(((UserPlaylistsResponse) response.body()).getTotal());
                        mutablePlaylists.setValue(((UserPlaylistsResponse) response.body()).getPlaylists());
                    }
                }


            });
            return new MutablePlaylistWithTotal(mutablePlaylists,total);
        }



        public void loadMoreUserPlaylists(String userId,int offset,MutableLiveData<List<PlaylistPreview>> mutablePlaylists){
            // oudApi = instantiateRetrofitOudApi();
            Call<UserPlaylistsResponse> call = oudApi.getMoreUserPlaylists(userId,offset);

            addCall(call).enqueue(new FailureSuccessHandledCallback<UserPlaylistsResponse>(this){
                @Override
                public void onResponse(Call call, Response response) {
                    super.onResponse(call, response);
                    if(response.isSuccessful()){
                        UserPlaylistsResponse userPlaylistsResponse = ((UserPlaylistsResponse) response.body());
                        List<PlaylistPreview>  previouslyLoadedList = mutablePlaylists.getValue();
                        List<PlaylistPreview>  newLoadedPlaylists =userPlaylistsResponse.getPlaylists();
                        previouslyLoadedList.addAll(newLoadedPlaylists);
                        mutablePlaylists.setValue(previouslyLoadedList);
                    }
                }


            });
        }

    public void followPlaylist(String token,String playlistId,ConnectionStatusListener connectionStatusListener){


        FollowingPublicityPayload followingPublicityPayload = new FollowingPublicityPayload(true);
        Call<ResponseBody> call = oudApi.followPlaylist(token,playlistId,followingPublicityPayload);

        addCall(call).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this,connectionStatusListener) {});

    }

    public void unFollowPlaylist(String token,String playlistId,ConnectionStatusListener connectionStatusListener){
        Call<ResponseBody> call = oudApi.unfollowPlaylist(token,playlistId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this,connectionStatusListener) {


        });


    }


    public void checkIfIFollowThisPlaylist(String token,String commaSeparatedUsers,String userId,MutableLiveData<Boolean> isFollowed){
        Call<ListOfBoolean> call = oudApi.checkIfIFollowThisPlaylist(token,commaSeparatedUsers,userId);
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

