package com.example.oud.artist.fragments.albums;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.AlbumForUpdate;
import com.example.oud.api.Genre;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

public class ChooseGenresFragment extends ConnectionAwareFragment<MyAlbumsViewModel> {
    Bundle albumData;
    Button finishButton;
    RecyclerView recyclerView;
    GenresAdapter adapter;
    ConnectionStatusListener connectionStatusListener;

    public ChooseGenresFragment(Activity activity, Bundle albumData, ConnectionStatusListener connectionStatusListener) {
        super(MyAlbumsViewModel.class
                ,R.layout.fragment_choose_genres
                ,(ProgressBar) activity.findViewById(R.id.progress_bar_artist_activity)
                ,(View)activity.findViewById(R.id.block_view_artist_Activity)
                ,null);
        this.albumData= albumData;
        this.connectionStatusListener = connectionStatusListener;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new GenresAdapter(getContext());
        finishButton = view.findViewById(R.id.btn_choose_genres);
        recyclerView = view.findViewById(R.id.recycler_view_choose_genres);

        setFinishButtonClickListener();
        handleAdapter();
        recyclerView.setLayoutManager(new FlexboxLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void setFinishButtonClickListener(){
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ArrayList<String>chosenGenres = adapter.getChosenGenres();
               if(chosenGenres.size()==0){
                   Toast.makeText(getContext(),"Choose at least one genre",Toast.LENGTH_LONG).show();
                   return;
               }
               albumData.putStringArrayList(Constants.BUNDLE_CREATE_ALBUM_GENRES_ID,chosenGenres);
               sendAlbumData();

            }
        });
    }


    public void handleAdapter(){
        mViewModel.getGenres().observe(getViewLifecycleOwner(), new Observer<OudList<Genre>>() {
            @Override
            public void onChanged(OudList<Genre> genreOudList) {
                ArrayList<Genre> myGenres = genreOudList.getItems();
                for(int i = adapter.getItemCount();i<genreOudList.getTotal();i++){

                    Genre genre = myGenres.get(i);
                    adapter.addItem(genre.getName(),genre.getId());
                    adapter.notifyItemChanged(adapter.getItemCount()-1);
                }
            }
        });

    }

    public void sendAlbumData(){
        ConnectionStatusListener myConnectionStatusListener= new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                connectionStatusListener.onConnectionSuccess();
                for(int i =0 ;i<4 ; i++)
                    getParentFragmentManager().popBackStack();

            }

            @Override
            public void onConnectionFailure() {
                connectionStatusListener.onConnectionSuccess();
                for(int i =0 ;i<4 ; i++)
                    getParentFragmentManager().popBackStack();
                Toast.makeText(getContext(),"Changes where not applied",Toast.LENGTH_LONG);
            }
        };

        if(albumData.getBoolean(Constants.BUNDLE_CREATE_ALBUM_IS_NEW_ALBUM_ID)){
            String albumName = albumData.getString(Constants.BUNDLE_CREATE_ALBUM_ALBUM_NAME);
            ArrayList<String> albumGenres= albumData.getStringArrayList(Constants.BUNDLE_CREATE_ALBUM_GENRES_ID);
            String albumType= albumData.getString(Constants.BUNDLE_CREATE_ALBUM_TYPE_ID);
            String releaseDate = albumData.getString(Constants.BUNDLE_CREATE_ALBUM_RELEASE_DATE_ID);
            ArrayList<String> artistId =albumData.getStringArrayList(Constants.BUNDLE_CREATE_ALBUM_ARTIST_ID) ;
            AlbumForUpdate album = new AlbumForUpdate(albumType,artistId,albumGenres,albumName,releaseDate,null);
            mViewModel.createAlbum(OudUtils.getToken(getContext()),album,myConnectionStatusListener);
        }
        else {
            String albumName = albumData.getString(Constants.BUNDLE_CREATE_ALBUM_ALBUM_NAME);
            ArrayList<String> albumGenres= albumData.getStringArrayList(Constants.BUNDLE_CREATE_ALBUM_GENRES_ID);
            String albumType= albumData.getString(Constants.BUNDLE_CREATE_ALBUM_TYPE_ID);
            String releaseDate = albumData.getString(Constants.BUNDLE_CREATE_ALBUM_RELEASE_DATE_ID);
            ArrayList<String> artistId =albumData.getStringArrayList(Constants.BUNDLE_CREATE_ALBUM_ARTIST_ID) ;
            String albumId = albumData.getString(Constants.BUNDLE_CREATE_ALBUM_ALBUM_ID_ID);
            AlbumForUpdate album = new AlbumForUpdate(albumType,artistId,albumGenres,albumName,releaseDate,null);

            mViewModel.updateAlbum(OudUtils.getToken(getContext()),albumId,album,myConnectionStatusListener);

        }


    }


}
