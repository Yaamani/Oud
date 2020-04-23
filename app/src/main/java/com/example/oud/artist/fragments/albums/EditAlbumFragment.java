package com.example.oud.artist.fragments.albums;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;

public class EditAlbumFragment extends ConnectionAwareFragment<MyAlbumsViewModel> {

    String albumId;
    public EditAlbumFragment(Activity activity,String albumId) {
        super(MyAlbumsViewModel.class
                ,R.layout.fragment_edit_album
                ,(ProgressBar) activity.findViewById(R.id.progress_bar_artist_activity)
                ,(View)activity.findViewById(R.id.block_view_artist_Activity)
                ,null);
        this.albumId =albumId;
    }






}
