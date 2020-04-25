package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.PlaylistPreview;
import com.example.oud.api.UserOrArtistPreview;

import java.util.List;

public class MutableUserOrArtistWithTotal {

    MutableLiveData<List<UserOrArtistPreview>> userOrArtistList;
    MutableLiveData<Integer> total;

    public MutableLiveData<Integer> getTotal() {
        return total;
    }

    public MutableLiveData<List<UserOrArtistPreview>> getUserOrArtistList() {
        return userOrArtistList;
    }

    public MutableUserOrArtistWithTotal(MutableLiveData<List<UserOrArtistPreview>> userOrArtistList, MutableLiveData<Integer> total) {
        this.userOrArtistList = userOrArtistList;
        this.total = total;
    }



}
