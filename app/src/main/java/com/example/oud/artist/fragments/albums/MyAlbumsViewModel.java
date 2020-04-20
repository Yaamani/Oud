package com.example.oud.artist.fragments.albums;

import androidx.lifecycle.ViewModel;

import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

public class MyAlbumsViewModel extends ConnectionAwareViewModel<MyAlbumsRepository> {

    public  MyAlbumsViewModel(){
        super(new MyAlbumsRepository(), Constants.YAMANI_MOCK_BASE_URL);
    }

    @Override
    public void clearData() {

    }
}
