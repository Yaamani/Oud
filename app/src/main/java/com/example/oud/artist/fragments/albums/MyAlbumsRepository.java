package com.example.oud.artist.fragments.albums;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.Album;
import com.example.oud.api.AlbumForUpdate;
import com.example.oud.api.Genre;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MyAlbumsRepository extends ConnectionAwareRepository {


    public MutableLiveData<OudList<Album>> getMyAlbums(String token,String myId){
        Call<OudList<Album>>call = oudApi.artistAlbums(token,myId,0, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);
        MutableLiveData<OudList<Album>> myAlbums= new MutableLiveData<>();
        addCall(call).enqueue(new FailureSuccessHandledCallback<OudList<Album>>(this){
            @Override
            public void onResponse(Call<OudList<Album>> call, Response<OudList<Album>> response) {
                super.onResponse(call, response);
                if(response.isSuccessful()){
                    myAlbums.setValue(response.body());
                }
            }
        });
        return myAlbums;

    }


    public MutableLiveData<OudList<Genre>> getGenres(int offset){
        MutableLiveData<OudList<Genre>> genres= new MutableLiveData<>();
        Call<OudList<Genre>> call = oudApi.getGenres(offset);
        addCall(call).enqueue(new FailureSuccessHandledCallback<OudList<Genre>>(this){
            @Override
            public void onResponse(Call<OudList<Genre>> call, Response<OudList<Genre>> response) {
                super.onResponse(call, response);
                if(response.isSuccessful())
                    genres.setValue(response.body());

            }
        });
        return genres;
    }

    public void getMoreGenres(int offset,MutableLiveData<OudList<Genre>> genres){
        Call<OudList<Genre>> call = oudApi.getGenres(offset);
        addCall(call).enqueue(new FailureSuccessHandledCallback<OudList<Genre>>(this){
            @Override
            public void onResponse(Call<OudList<Genre>> call, Response<OudList<Genre>> response) {
                super.onResponse(call, response);
                if(response.isSuccessful()){
                    OudList<Genre> oldData = genres.getValue();
                    oldData.addItems(response.body().getItems());
                    oldData.setOffset(response.body().getOffset());
                    genres.setValue(oldData);
                }
            }
        });


    }





    public void getMoreAlbums(String token, String myId, int offset, MutableLiveData<OudList<Album>> albumList){
        Call<OudList<Album>>call = oudApi.artistAlbums(token,myId,offset, Constants.USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT);

        addCall(call).enqueue(new FailureSuccessHandledCallback<OudList<Album>>(this){
            @Override
            public void onResponse(Call<OudList<Album>> call, Response<OudList<Album>> response) {
                super.onResponse(call, response);
                if(response.isSuccessful()){
                    OudList<Album> albums =  albumList.getValue();
                    albums.addItems(response.body().getItems());
                    albumList.setValue(albums);
                }
            }
        });
    }


    public void deleteAnAlbum(String token, String albumId, ConnectionStatusListener connectionStatusListener){
        Call<ResponseBody>call = oudApi.deleteAlbum(token,albumId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this,connectionStatusListener){});


    }

    public void createAlbum(String token, AlbumForUpdate album, ConnectionStatusListener undo){
        Call<Album> call = oudApi.createNewAlbum(token,album);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Album>(this,undo){});
    }

    public void updateAlbum(String token,String albumId,AlbumForUpdate album,ConnectionStatusListener undo){
        Call<Album> call = oudApi.updateAlbum(token,album,albumId);
        addCall(call).enqueue(new FailureSuccessHandledCallback<Album>(this,undo){});
    }
}
