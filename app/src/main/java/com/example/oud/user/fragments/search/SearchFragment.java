package com.example.oud.user.fragments.search;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Categories;
import com.example.oud.api.Category2;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.SearchActivity;
import com.example.oud.user.fragments.home.HomeFragment2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends ConnectionAwareFragment<SearchViewModel> {

    private Context mContext;
    private String mToken;
    private ArrayList<Categories> mCategories;
    private CardView cardView;

    private String mUserId;

    private final String TAG = SearchFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;

    private OudList<Category2> mOudList;

    public SearchFragment() {
        super(SearchViewModel.class, R.layout.fragment_search, R.id.loading_track, null);
    }

    public static SearchFragment newInstance(String userId) {

        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID_KEY, userId);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){

            mUserId = getArguments().getString(Constants.USER_ID_KEY);
        }
        else {
            throw new RuntimeException("Instead of calling new " + SearchFragment.class.getSimpleName() + "()" +
                    ", call " + SearchFragment.class.getSimpleName() + ".newInstance()" +
                    " to pass the arguments to the fragment. Or you can use " + SearchFragment.class.getSimpleName() + ".setArguments().");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToken = OudUtils.getToken(mContext);
        mCategories = new ArrayList<>();
        cardView = view.findViewById(R.id.cardView);
        handleCategories(view);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), SearchActivity.class);

                startActivity(intent);

            }
        });


    }

    private void handleCategories(View v) {

       /* mViewModel.getCategories(mToken).observe(getViewLifecycleOwner(),  (OudList<Category2> category2OudList) -> {

            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
            ArrayList<Category2> items = category2OudList.getItems();
            int index = 0;
            for(int i=0 ;i < category2OudList.getTotal();i=i+2){

                Category2 currentCategory = items.get(i);
                Category2 currentCategory2 = items.get(i+1);

                mCategories.add(index,new Categories(currentCategory.getIcon(),
                        currentCategory2.getIcon(),
                        currentCategory.getName(),
                        currentCategory2.getName()) );
                index++;

            }
            handleRecycleView(v);

        } );*/

        fetchCategories(v);


    }

    private void handleRecycleView(View v) {

        mRecyclerView = v.findViewById(R.id.recycleView);
        /*mRecyclerView.setHasFixedSize(true);*/
        mLayoutManager = new LinearLayoutManager(mContext); //what important of this?
        mAdapter = new SearchAdapter(mContext, mCategories);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {

            // todo send containerId and CategoryId
            // boolean isRightItem for specific category for sending wright categoryId
            // if RightItem so position+1 if left position we will work on OudList because i don't have id in Categories
            @Override
            public void onItemClick(int position) {

               /* FragmentTransaction fragmentTransaction = getActivity().
                        getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.nav_host_fragment, new CategoryFragment())
                        .addToBackStack(null)
                        .commit();*/
               CategoryFragment.show(getActivity(), R.id.nav_host_fragment,
                       mOudList.getItems().get(position).get_id(),mUserId);


            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;
    }

    private void fetchCategories(View v) {

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<OudList<Category2>> call = oudApi.getCategories(mToken);
        call.enqueue(new Callback<OudList<Category2>>() {

            @Override
            public void onResponse(Call<OudList<Category2>> call, Response<OudList<Category2>> response) {
                if (!response.isSuccessful()) {


                    Log.e(TAG, "OnResponse" + response.code());
                    return;
                }
                mOudList = response.body();


                assert mOudList != null;
                ArrayList<Category2> items = mOudList.getItems();

                for (int i = 0; i < mOudList.getTotal(); i = i + 2) {

                    Category2 currentCategory = items.get(i);
                    Category2 currentCategory2 = null;
                    if(i+1 <mOudList.getTotal()) {
                        currentCategory2 = items.get(i + 1);
                    }
                    if(currentCategory2 == null) {
                        mCategories.add(new Categories(currentCategory.getIcon(),
                                null,
                                currentCategory.getName(),
                                null));
                    }
                    else{

                        mCategories.add(new Categories(currentCategory.getIcon(),
                                currentCategory2.getIcon(),
                                currentCategory.getName(),
                                currentCategory2.getName()));
                    }


                }
                handleRecycleView(v);

            }

            @Override
            public void onFailure(Call<OudList<Category2>> call, Throwable t) {

                Log.e(TAG, t.getMessage());

            }
        });


    }
}
