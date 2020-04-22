package com.example.oud.artist.fragments.albums;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class MyAlbumsViewModel extends ConnectionAwareViewModel<MyAlbumsRepository> {
    MutableLiveData<OudList<Album>> myAlbums;

    public  MyAlbumsViewModel(){
        super(new MyAlbumsRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<OudList<Album>> getMyAlbums(String token, String myId){
        if(myAlbums == null)
            myAlbums = mRepo.getMyAlbums(token,myId);
        return myAlbums;

    }

    public void deleteAlbum(String token, String albumId, ConnectionStatusListener connectionStatusListener){
        mRepo.deleteAnAlbum(token,albumId,connectionStatusListener);
    }

    public void getMoreAlbums(String token,String myId,int offset){
        mRepo.getMoreAlbums(token,myId,offset,myAlbums);
    }

    @Override
    public void clearData() {
        myAlbums=null;
    }
}
