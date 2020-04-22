package com.example.oud.artist.fragments.albums;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.GenericVerticalRecyclerViewAdapter;
import com.example.oud.OptionsFragment;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareFragment;


public class MyAlbumsFragment extends ConnectionAwareFragment<MyAlbumsViewModel> {
    String token;
    String myId;
    RecyclerView recyclerView;
    Button createAlbumButton;
    ProgressBar loadMoreProgressBar;

    boolean isAllLoaded=false;

    GenericVerticalRecyclerViewAdapter adapter;
    public  MyAlbumsFragment(Activity activity){
        super(MyAlbumsViewModel.class
                ,R.layout.fragment_my_albums
                ,(ProgressBar) activity.findViewById(R.id.progress_bar_artist_activity)
                ,(View)activity.findViewById(R.id.block_view_artist_Activity)
                ,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GenericVerticalRecyclerViewAdapter.OnItemClickListener  onItemClickListener= new GenericVerticalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {

            }
        };

        GenericVerticalRecyclerViewAdapter.OnItemClickListener  onImageButtonClick = new GenericVerticalRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, View view) {
                String albumId= adapter.getIds().get(position);
                String albumImage = adapter.getImage(position);
                String albumName=adapter.getTitle(position);
                ConnectionStatusListener undoDeleteAlbum= new ConnectionStatusListener() {
                    @Override
                    public void onConnectionSuccess() {

                    }
                    @Override
                    public void onConnectionFailure() {
                        adapter.addItem(position,albumId,albumImage,false,albumName,false);
                        adapter.notifyItemInserted(position);
                    }
                };

                DialogInterface.OnClickListener dialogClickListenerForDelete = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                adapter.removeItem(position);
                                adapter.notifyItemRemoved(position);
                                mViewModel.deleteAlbum(token,albumId,undoDeleteAlbum);
                                mViewModel.deleteAlbum(position);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };


                View.OnClickListener deleteAlbumClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you want to delete this album?").setPositiveButton("yes", dialogClickListenerForDelete)
                                .setNegativeButton("No", dialogClickListenerForDelete).show();
                    }
                };

                View.OnClickListener editAlbumClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo open edit album
                    }
                };

                OptionsFragment.builder(getActivity())
                        .inContainer(R.id.nav_host_fragment_artist)
                        .addItem(R.drawable.ic_edit_2,"edit album",editAlbumClickListener)
                        .addItem(R.drawable.ic_delete_2,"delete album",deleteAlbumClickListener)
                        .show();
            }
        };


        adapter = new GenericVerticalRecyclerViewAdapter(getContext(),R.drawable.ic_options,onItemClickListener,onImageButtonClick);

        recyclerView = view.findViewById(R.id.recycler_view_artist_activity_albums);

        token = OudUtils.getToken(getContext());
        myId = OudUtils.getUserId(getContext());

        handleAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager =(LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager.getChildCount()==0)
                    return;
                int lastVisibleViewPosition =layoutManager.findLastCompletelyVisibleItemPosition();
                int lastViewPosition = layoutManager.getChildCount()-1;
                if(lastVisibleViewPosition == lastViewPosition) {
                    if (!isAllLoaded) {
                        mViewModel.getMoreAlbums(token, myId, layoutManager.getChildCount()+1);
                        isAllLoaded = true; //to avoid multible requests
                    }
                }
            }
        });






    }

    public static MyAlbumsFragment newInstance(Activity activity) {
        return new MyAlbumsFragment(activity);
    }

    private void handleAdapter(){
        mViewModel.getMyAlbums(token,myId).observe(getViewLifecycleOwner(), new Observer<OudList<Album>>() {
            @Override
            public void onChanged(OudList<Album> albumOudList) {
                if(albumOudList.getTotal()<=albumOudList.getItems().size())
                    isAllLoaded = true;
                else
                    isAllLoaded = false;

                for(int i =adapter.getItemCount();i<albumOudList.getItems().size();i++){
                    Album album = albumOudList.getItems().get(i);

                    String imageUrl = OudUtils.convertImageToFullUrl(album.getImage());
                    adapter.addItem(album.get_id(),imageUrl,false,album.getName(),true);
                    adapter.notifyItemInserted(adapter.getIds().size()-1);
                }
            }
        });
    }



}
