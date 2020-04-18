package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserOrArtistPreview;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.List;

public class ProfileFollowersViewModel extends ConnectionAwareViewModel<ProfileFollowersRepository> {
    private MutableLiveData<List<UserOrArtistPreview>> followers;
    private MutableLiveData<Integer> totalNumberOfFollowers;
    public ProfileFollowersViewModel(){
        super(new ProfileFollowersRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<List<UserOrArtistPreview>> getUserFollowers(String userId){
        MutableUserOrArtistWithTotal result;
        if(followers == null){
            result = mRepo.loadUserFollowers(userId);
            followers = result.getUserOrArtistList();
            totalNumberOfFollowers = result.getTotal();

        }
        return followers;
    }
    public MutableLiveData<Integer> getTotalNumberOfFollowers(){

        return totalNumberOfFollowers;
    }

    public void loadMorePlaylists(String userId){
        if(followers == null)
            return;
        mRepo.loadMoreUserFollowers(userId,followers.getValue().size()-1,followers);
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
        followers=null;
        totalNumberOfFollowers = null;
    }

}
