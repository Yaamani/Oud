package com.example.oud.artist.fragments.albums;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class MyAlbumsFragment extends ConnectionAwareFragment<MyAlbumsViewModel> {

    public  MyAlbumsFragment(Activity activity){
        super(MyAlbumsViewModel.class
                ,R.layout.fragment_my_albums
                ,(ProgressBar) activity.findViewById(R.id.progress_bar_artist_activity)
                ,(View)activity.findViewById(R.id.block_view_artist_Activity)
                ,null);
    }


    public static MyAlbumsFragment newInstance(Activity activity) {
        return new MyAlbumsFragment(activity);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
