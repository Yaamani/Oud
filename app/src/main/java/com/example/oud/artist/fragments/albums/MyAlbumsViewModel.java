package com.example.oud.artist.fragments.albums;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.Album;
import com.example.oud.api.AlbumForUpdate;
import com.example.oud.api.Genre;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class MyAlbumsViewModel extends ConnectionAwareViewModel<MyAlbumsRepository> {
    MutableLiveData<OudList<Album>> myAlbums;
    MutableLiveData<OudList<Genre>> genres;

    public  MyAlbumsViewModel(){
        super(new MyAlbumsRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }


    public MutableLiveData<OudList<Album>> getMyAlbums(String token, String myId){
        if(myAlbums == null)
            myAlbums = mRepo.getMyAlbums(token,myId);

        return myAlbums;
    }

    public void deleteAlbum(int position){
        OudList<Album>  newAlbums= myAlbums.getValue();
        newAlbums.deleteItem(position);
        newAlbums.setTotal(newAlbums.getTotal()-1);
        myAlbums.setValue(newAlbums);
    }
    public void createAlbum(String token, AlbumForUpdate album, ConnectionStatusListener connectionStatusListener){
        mRepo.createAlbum(token,album,connectionStatusListener);
    }

    public void updateAlbum(String token ,String albumId,AlbumForUpdate album,ConnectionStatusListener connectionStatusListener){
        mRepo.updateAlbum(token, albumId, album, connectionStatusListener);
    }


    public void deleteAlbum(String token, String albumId, ConnectionStatusListener connectionStatusListener){
        mRepo.deleteAnAlbum(token,albumId,connectionStatusListener);
    }

    public void getMoreAlbums(String token,String myId,int offset){
        mRepo.getMoreAlbums(token,myId,offset,myAlbums);
    }

    public MutableLiveData<OudList<Genre>> getGenres(){
        if(genres == null)
           genres= mRepo.getGenres(0);

        return genres;
    }
    public void getMoreGenres(int offset){
        mRepo.getMoreGenres(offset,genres);
    }

    @Override
    public void clearData() {
        myAlbums=null;
    }
}
