package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.List;

public class ProfileFollowingViewModel extends ConnectionAwareViewModel<ProfileFollowingRepository> {
    private MutableLiveData<List<UserOrArtistPreview>> followedArtists;
    private MutableLiveData<Integer> totalNumberOfFollowedArtists;

    private MutableLiveData<List<UserOrArtistPreview>> followedUsers;
    private MutableLiveData<Integer> totalNumberOfFollowedUsers;

    public ProfileFollowingViewModel(){
        super(new ProfileFollowingRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<List<UserOrArtistPreview>> getUserFollowedArtists(String userId){
        MutableUserOrArtistWithTotal result;
        if(followedArtists == null){
            result = mRepo.loadUserFollowedArtists(userId);
            followedArtists = result.getUserOrArtistList();
            totalNumberOfFollowedArtists = result.getTotal();

        }
        return followedArtists;
    }

    public MutableLiveData<Integer> getTotalNumberOfFollowedArtists(){
        return totalNumberOfFollowedArtists;
    }

    public void loadMoreFollowedArtists(String userId){
        if(followedArtists == null)
            return;
        mRepo.loadMoreUserFollowedArtists(userId,followedArtists.getValue().size()-1,followedArtists);
    }


    public MutableLiveData<List<UserOrArtistPreview>> getUserFollowedUsers(String userId){
        MutableUserOrArtistWithTotal result;
        if(followedUsers == null){
            result = mRepo.loadUserFollowedUsers(userId);
            followedUsers = result.getUserOrArtistList();
            totalNumberOfFollowedUsers = result.getTotal();

        }
        return followedUsers;
    }

    public MutableLiveData<Integer> getTotalNumberOfFollowedUsers(){
        return totalNumberOfFollowedUsers;
    }

    public void loadMoreFollowedUsers(String userId){
        if(followedUsers == null)
            return;
        mRepo.loadMoreUserFollowedUsers(userId,followedUsers.getValue().size()-1,followedUsers);
    }

    public void followUser(String token, String userId,ConnectionStatusListener connectionStatusListener){
        mRepo.followUser(token,userId,connectionStatusListener);
    }

    public void unFollowUser(String token, String userId,ConnectionStatusListener connectionStatusListener){
        mRepo.unFollowUser(token,userId,connectionStatusListener);
    }


    public void followArtist(String token, String userId,ConnectionStatusListener connectionStatusListener){
        mRepo.followArtist(token,userId,connectionStatusListener);
    }

    public void unFollowArtist(String token, String userId,ConnectionStatusListener connectionStatusListener){
        mRepo.unFollowArtist(token,userId,connectionStatusListener);
    }

    public MutableLiveData<Boolean> checkIfIFollowThisUser(String token,String userId){
        MutableLiveData<Boolean> iFollowThisUser= new MutableLiveData<>();
        mRepo.checkIfIFollowThisUser(token,userId,iFollowThisUser);

        return iFollowThisUser;
    }

    public MutableLiveData<Boolean> checkIfIFollowThisArtist(String token,String userId){
        MutableLiveData<Boolean> iFollowThisUser= new MutableLiveData<>();
        mRepo.checkIfIFollowThisArtist(token,userId,iFollowThisUser);

        return iFollowThisUser;
    }





    @Override
    public void clearData() {
        followedArtists=null;
        followedUsers = null;
        totalNumberOfFollowedUsers  = null;
        totalNumberOfFollowedArtists= null;
    }

}
