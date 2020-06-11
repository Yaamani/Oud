package com.example.oud.user.fragments.search;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Categories;
import com.example.oud.api.Category2;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private CategoryViewModel mViewModel;
    private Context mContext;
    private String mCategoryId;

    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;

    private String mToken;

    private ArrayList<Categories> mPlaylist;
    private OudList<Playlist> mOudList;

    private String mUserId;

    private static final String TAG = SearchedResultsFragment.class.getSimpleName();

    public static CategoryFragment newInstance(String categoryId, String userId) {

        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("CATEGORY_ID",categoryId);
        bundle.putString("USER_ID",userId);
        categoryFragment.setArguments(bundle);

        return categoryFragment;
    }

    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            String categoryId,String userId) {

        FragmentManager manager = activity.getSupportFragmentManager();
        CategoryFragment categoryFragment = (CategoryFragment) manager.findFragmentByTag("CATEGORY_FRAGMENT");

        categoryFragment = CategoryFragment.newInstance(categoryId,userId);


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, categoryFragment, "CATEGORY_FRAGMENT")
                .addToBackStack(null)
/*
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
*/
                .commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCategoryId = getArguments().getString("CATEGORY_ID");
            mUserId = getArguments().getString("USER_ID");
        }


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.category_fragment, container, false);
        mToken = OudUtils.getToken(mContext);
        mRecyclerView = v.findViewById(R.id.recycleView);
        mPlaylist = new ArrayList<>();

        fetchCategoryPlaylists();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;

    }
    private void handleRecycleView() {


        /*mRecyclerView.setHasFixedSize(true);*/
        mLayoutManager = new LinearLayoutManager(mContext); //what important of this?
        mAdapter = new SearchAdapter(mContext, mPlaylist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        // todo send containerId and CategoryId
// boolean isRightItem for specific category for sending wright categoryId
// if RightItem so position+1 if left position we will work on OudList because i don't have id in Categories
        mAdapter.setOnItemClickListener((position) -> {

            /*FragmentTransaction fragmentTransaction = getActivity().
                    getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.nav_host_fragment, new CategoryFragment())
                    .addToBackStack(null)
                    .commit();*/
            PlaylistFragment.show(getActivity(),R.id.nav_host_fragment,mUserId,
                    Constants.PlaylistFragmentType.PLAYLIST,mOudList.getItems().get(position).getId());

        });

    }


    private void fetchCategoryPlaylists(){

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<OudList<Playlist>> call = oudApi.categoryPlaylists(mToken,mCategoryId,null,null);

        call.enqueue(new Callback<OudList<Playlist>>() {
            @Override
            public void onResponse(Call<OudList<Playlist>> call, Response<OudList<Playlist>> response) {

                if (!response.isSuccessful()){

                    Log.e(TAG, "OnResponse" + response.code());
                }
                mOudList = response.body();

                assert mOudList != null;
                ArrayList<Playlist> items = mOudList.getItems();

                for (int i = 0; i < items.size(); i = i + 2) {

                    Playlist currentPlaylist = items.get(i);
                    Playlist currentPlaylist2 = null;
                    if(i+1 <= items.size()-1) {
                        currentPlaylist2 = items.get(i + 1);
                    }
                    if(currentPlaylist2 == null) {
                        mPlaylist.add(new Categories(currentPlaylist.getImage(),
                                null,
                                currentPlaylist.getName(),
                                null));
                    }
                    else{

                        mPlaylist.add(new Categories(currentPlaylist.getImage(),
                                currentPlaylist2.getImage(),
                                currentPlaylist.getName(),
                                currentPlaylist2.getName()));
                    }


                }
                handleRecycleView();


            }

            @Override
            public void onFailure(Call<OudList<Playlist>> call, Throwable t) {

                Log.e(TAG, t.getMessage());

            }
        });

    }
}
