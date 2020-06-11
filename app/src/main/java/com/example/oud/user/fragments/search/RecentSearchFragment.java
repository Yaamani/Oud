package com.example.oud.user.fragments.search;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Categories;
import com.example.oud.api.Category2;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.RecentItem;
import com.example.oud.api.SearchedItems;
import com.example.oud.connectionaware.ConnectionAwareFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentSearchFragment extends Fragment/*ConnectionAwareFragment<RecentSearchViewModel>*/ {

    private Context mContext;

    private String mToken;
    private ArrayList<SearchedItems> mSearchedItems;

    private final String TAG = RecentSearchFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SearchedItemAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;

    private OudList<RecentItem> mOudList;
    private TextView mTextView;
    private RelativeLayout relativeLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recent_search_fragment, container,false);
        mToken = OudUtils.getToken(mContext);
        mSearchedItems = new ArrayList<>();
        relativeLayout = v.findViewById(R.id.group_for_no_recent_data);
        mRecyclerView = v.findViewById(R.id.searchedRecycleView);
        mTextView = v.findViewById(R.id.txt_recent_searches);
        /*relativeLayout.setVisibility(View.VISIBLE);*/
        getRecentItems(v);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;

    }

    private void handleRecycleView(View v) {


        /*mRecyclerView.setHasFixedSize(true);*/
        mLayoutManager = new LinearLayoutManager(mContext); //what important of this?
        mAdapter = new SearchedItemAdapter(mContext, mSearchedItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SearchedItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {

                /*FragmentTransaction fragmentTransaction = getActivity().
                        getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.nav_host_fragment, new CategoryFragment())
                        .addToBackStack(null)
                        .commit();*/

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });

    }

    private void getRecentItems(View v){

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<OudList<RecentItem>> call = oudApi.getUserRecentlySearched(mToken);

        call.enqueue(new Callback<OudList<RecentItem>>() {

            RecentItem recentItem = new RecentItem();
            @Override
            public void onResponse(Call<OudList<RecentItem>> call, Response<OudList<RecentItem>> response) {

                if (!response.isSuccessful()) {

                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.e(TAG, "OnResponse" + response.code());
                    return;
                }
                mOudList = response.body();

                assert mOudList != null;
                if(mOudList.getTotal() == 0 || mOudList == null){
                    Log.d(TAG,"CLEAR DATA" );
                    relativeLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mTextView.setVisibility(View.GONE);
                    return;

                }

/*
                mSearchedItems.add(new SearchedItems("Recent searches",true));
*/
                for (int i=0 ;i<mOudList.getTotal();i++){
                    recentItem =  mOudList.getItems().get(i);

                    if(recentItem != null){


                        mSearchedItems.add(new SearchedItems(/*recentItem.getImages().get(0)*/"image",
                                recentItem.getDisplayName(), recentItem.getType(), false));


                    }

                }
                if(mSearchedItems.size()==0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    mTextView.setVisibility(View.GONE);
                    return;
                }

                handleRecycleView(v);
                relativeLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<OudList<RecentItem>> call, Throwable t) {

                Log.e(TAG, t.getMessage());

            }
        });

    }
    private void putRecentItems(){}
}
