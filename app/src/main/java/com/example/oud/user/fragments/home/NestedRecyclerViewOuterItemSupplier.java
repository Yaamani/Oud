package com.example.oud.user.fragments.home;

public interface NestedRecyclerViewOuterItemSupplier {

    HomeViewModel.OuterItemLiveData loadRecentlyPlayed();

    HomeViewModel.OuterItemLiveData loadCategory(int position);

}
