package com.example.oud.user.fragments.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.AlbumPreview;
import com.example.oud.api.Artist;
import com.example.oud.api.OudAlbum;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudArtist;
import com.example.oud.api.OudPlaylist;
import com.example.oud.api.OudTrack;
import com.example.oud.api.Playlist;
import com.example.oud.api.SearchedItems;
import com.example.oud.api.SearchedResults2;
import com.example.oud.api.Track;
import com.example.oud.api.UpdateRecentItem;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchedResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchedResultsFragment extends Fragment {

    private String mQuery;
    private String mToken;
    private static final String TAG = SearchedResultsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SearchedItemAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SearchedItems> searchedItems;

    private Context mContext;

    public SearchedResultsFragment() {
        // Required empty public constructor
    }

    public static SearchedResultsFragment newInstance(String query) {
        SearchedResultsFragment searchedResultsFragment = new SearchedResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("QUERY_KEY",query);
        searchedResultsFragment.setArguments(bundle);

        return searchedResultsFragment;
    }

    public static void show(FragmentActivity activity,
                            @IdRes int containerId,
                            String query) {

        FragmentManager manager = activity.getSupportFragmentManager();
        SearchedResultsFragment searchedResultsFragment = (SearchedResultsFragment) manager.findFragmentByTag("SEARCHED_RESULTS_FRAGMENT");

        searchedResultsFragment = SearchedResultsFragment.newInstance(query);


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, searchedResultsFragment, "SEARCHED_RESULTS_FRAGMENT")
/*
                .addToBackStack(null)
*/
/*
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
*/
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuery = getArguments().getString("QUERY_KEY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_searched_results, container, false);
        mToken = OudUtils.getToken(mContext);
        mRecyclerView = v.findViewById(R.id.searchedResultsRecycleView);

        searchedItems = new ArrayList<>();
        fetchSearchedItems(mQuery);

        return v;
    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);

        mContext = context;
    }

    private void UpdateRecentFragment(UpdateRecentItem updateRecentItem){

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<ResponseBody> call = oudApi.updateUserRecentlySearched(mToken,updateRecentItem);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (!response.isSuccessful()){

                    Log.e(TAG, "OnResponse" + response.code());
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.e(TAG,t.getMessage());
            }
        });
    }

    private void handleRecycleView() {


        /*mRecyclerView.setHasFixedSize(true);*/
        mLayoutManager = new LinearLayoutManager(mContext); //what important of this?
        mAdapter = new SearchedItemAdapter(mContext, searchedItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SearchedItemAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position /*,String Type,String id*/) {

                SearchedItems currentItem = searchedItems.get(position);
                if(!currentItem.isStringOnly()) {
                    UpdateRecentItem updateRecentItem = new UpdateRecentItem(currentItem.getId(), currentItem.getItemType());

                    UpdateRecentFragment(updateRecentItem);
                }

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);


    }

    private void fetchSearchedItems(String query){


        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<SearchedResults2> call = oudApi.getItem(mToken,query);

        call.enqueue(new Callback<SearchedResults2>() {

            SearchedResults2 searchedResults = new SearchedResults2();
            @Override
            public void onResponse(Call<SearchedResults2> call, Response<SearchedResults2> response) {

                if( query.equals(" ")){

                    searchedItems.clear();
                    return;
                }

                if (!response.isSuccessful()) {


                    /*mRecyclerView.setVisibility(View.GONE);*/
                    /*backStack();*/


                    Log.e(TAG, "OnResponse" + response.code());
                    /*Log.e(TAG, "OnResponse" + response.message());*/
                    /*Log.e(TAG, "OnResponse" + response.);*/

                    return;
                }

                searchedResults = response.body();

                if(searchedItems != null && searchedItems.size()>0){

                    searchedItems.clear();
                }
                if(searchedResults == null){

                    Log.d(TAG,"SearchedResults are Empty");
                    return;

                }
                /*ArrayList<Track> tracks = searchedResults.getTracks();*/
                OudTrack tracks = searchedResults.getTracks();
                Track currentTrack;

                /*ArrayList<AlbumPreview> albums = searchedResults.getAlbums();*/
                OudAlbum albums = searchedResults.getAlbums();
                AlbumPreview currentAlbum;

                /*ArrayList<Artist> artists = searchedResults.getArtists();*/
                OudArtist artists = searchedResults.getArtists();
                Artist currentArtist;

                /*ArrayList<Playlist> playlists = searchedResults.getPlaylists();*/
                OudPlaylist playlists = searchedResults.getPlaylists();
                Playlist currentPlaylist;


                for (int i=0 ; i< tracks.getTotal() ;i++){

                    currentTrack = tracks.getTracks().get(i);
                    searchedItems.add(new SearchedItems(currentTrack.getAlbum().getImage(), currentTrack.getName(),
                            "track", currentTrack.get_id(),false));

                    if(i == 1){

                        break;
                    }

                }

                for(int i=0 ; i< albums.getTotal() ;i++){

                    currentAlbum = albums.getAlbums().get(i);
                    searchedItems.add(new SearchedItems(currentAlbum.getImage(),currentAlbum.getName(),
                            "Album",currentAlbum.getId(),false));

                    if(i == 1){

                        break;
                    }

                }

                for(int i=0 ; i< artists.getTotal() ;i++){
                    currentArtist = artists.getArtists().get(i);
                    searchedItems.add(new SearchedItems(currentArtist.getImages().get(0),currentArtist.getDisplayName(),
                            "Artist",currentArtist.get_id(),false));
                    if(i == 1){

                        break;
                    }

                }

                for(int i=0 ; i< playlists.getTotal() ;i++){
                    currentPlaylist = playlists.getPlaylists().get(i);
                    searchedItems.add(new SearchedItems(currentPlaylist.getImage(),currentPlaylist.getName(),
                            "Playlist",currentPlaylist.getId(),false));
                    if(i == 1){

                        break;
                    }
                }

                searchedItems.add(new SearchedItems("See all artists",true));
                searchedItems.add(new SearchedItems("See all songs",true));
                searchedItems.add(new SearchedItems("See all playlists",true));
                searchedItems.add(new SearchedItems("See all albums",true));

                handleRecycleView();


            }

            @Override
            public void onFailure(Call<SearchedResults2> call, Throwable t) {

                Log.e(TAG, t.getMessage());
            }
        });


    }



}
