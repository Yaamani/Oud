package com.example.oud.user.fragments.search;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oud.Constants;
import com.example.oud.api.Category2;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.home.HomeRepository2;

public class SearchViewModel extends ConnectionAwareViewModel<SearchRepository> {

    MutableLiveData<OudList<Category2>> mCategories;
    public SearchViewModel(){

        super(SearchRepository.ourInstance, Constants.YAMANI_MOCK_BASE_URL);

    }

    public MutableLiveData<OudList<Category2>> getCategories(String token){

        if(mCategories == null){

            mCategories = mRepo.fetchCategories(token);
        }
        return mCategories;
    }

    @Override
    public void clearData() {

        mCategories = null;

    }
}
