package com.example.oud.user.fragments.search;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.Category2;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import retrofit2.Call;
import retrofit2.Response;

public class SearchRepository extends ConnectionAwareRepository {

    private final String TAG = SearchRepository.class.getSimpleName();
    public static SearchRepository ourInstance = new SearchRepository();

    private MutableLiveData<OudList<Category2>> mCategories;

    private SearchRepository(){}

    public MutableLiveData<OudList<Category2>> fetchCategories(String token){

        mCategories = new MutableLiveData<>();

        Call<OudList<Category2>> call = oudApi.getCategories(token);
        call.enqueue(new FailureSuccessHandledCallback<OudList<Category2>>(this){

            OudList<Category2> categories;
            @Override
            public void onResponse(Call<OudList<Category2>> call, Response<OudList<Category2>> response) {


                if(!response.isSuccessful()){


                    Log.e(TAG ,"OnResponse" + response.code());
                    return;
                }
                categories = response.body();

            }

        });

        return mCategories;

    }


}
