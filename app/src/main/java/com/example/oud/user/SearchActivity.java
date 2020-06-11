package com.example.oud.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.RenameFragment;
import com.example.oud.api.AlbumPreview;
import com.example.oud.api.Artist;
import com.example.oud.api.OudAlbum;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudArtist;
import com.example.oud.api.OudList;
import com.example.oud.api.OudPlaylist;
import com.example.oud.api.OudTrack;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentItem;
import com.example.oud.api.SearchedItems;
import com.example.oud.api.SearchedResults;
import com.example.oud.api.SearchedResults2;
import com.example.oud.api.Track;
import com.example.oud.api.UpdateRecentItem;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.fragments.search.RecentSearchFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.search.SearchedItemAdapter;
import com.example.oud.user.fragments.search.SearchedResultsFragment;
import com.example.oud.user.fragments.search.SeeAllFragment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private final String  TAG = SearchActivity.class.getSimpleName();
    private String mToken;
    private ArrayList<SearchedItems> searchedItems;

    private RecyclerView mRecyclerView;
    private SearchedItemAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentContainerView fragmentContainerView;
    private FragmentContainerView seeAllFragmentContainerView;
    private SearchActivity searchActivity;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToken = OudUtils.getToken(this);
        mRecyclerView = findViewById(R.id.searchedResultsRecycleView);

        searchedItems = new ArrayList<>();
        fragmentContainerView = findViewById(R.id.recent_search_fragment);
        seeAllFragmentContainerView = findViewById(R.id.see_all_fragment);
        mRecyclerView.setVisibility(View.GONE);

        /*searchActivity = this;*/

        backStack();


    }

    private void fetchSearchedItems(String query){


        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);
        Call<SearchedResults2> call = oudApi.getItem(mToken,query);

       call.enqueue(new Callback<SearchedResults2>() {

           SearchedResults2 searchedResults = new SearchedResults2();
           @Override
           public void onResponse(Call<SearchedResults2> call, Response<SearchedResults2> response) {
               if (!response.isSuccessful()) {

                   searchedItems.clear();
                   mRecyclerView.setVisibility(View.GONE);
                   backStack();


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

               handleRecycleView(query);


           }

           @Override
           public void onFailure(Call<SearchedResults2> call, Throwable t) {

               Log.e(TAG, t.getMessage());
           }
       });


    }

    private void handleRecycleView(String query) {


        /*mRecyclerView.setHasFixedSize(true);*/
        mLayoutManager = new LinearLayoutManager(this); //what important of this?
        mAdapter = new SearchedItemAdapter(this, searchedItems);

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

                switch (currentItem.getItemType()){

                    case "track":
                        //todo play song;
                        break;
                    case "Album":
                        //PlaylistFragment.show();
                        break;
                    case "Playlist":
                        //PlaylistFragment.show();
                        break;
                    default:
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        fragmentContainerView.setVisibility(View.GONE);
                        seeAllFragmentContainerView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        /*SeeAllFragment seeAllFragment = (SeeAllFragment) manager.findFragmentByTag(Constants.SEARCH_FRAGMENT_TAG);*/
                        transaction.replace(R.id.see_all_fragment,
                                SeeAllFragment.newInstance(currentItem.getItemType(),query),
                                "SEE_ALL_FRAGMENT")
                        .addToBackStack(null)
                        .commit();


                    /*case "See all artists":
                        // open fragment of sell all artists
                        break;
                    case "See all songs":
                        // open fragment of sell all songs
                        break;
                    case "See all Playlists" :

                        // open fragment of sell all Playlists
                        break;
                    case "See all albums" :

                        // open fragment of sell all Playlists
                        break;*/

                }



            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);
        fragmentContainerView.setVisibility(View.GONE);
        seeAllFragmentContainerView.setVisibility(View.GONE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                fetchSearchedItems(query);
                fragmentContainerView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

/*
                SearchedResultsFragment.show(searchActivity,R.id.searched_results_fragment,query);
*/

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                fragmentContainerView.setVisibility(View.GONE);
                fetchSearchedItems(newText);
/*
                SearchedResultsFragment.show(searchActivity,R.id.searched_results_fragment,newText);
*/

                return false;
            }
        });

        return true;

    }

    private void backStack(){

        fragmentContainerView.setVisibility(View.VISIBLE);
        seeAllFragmentContainerView.setVisibility(View.GONE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recent_search_fragment,new RecentSearchFragment(), Constants.RECENT_SEARCH_FRAGMENT_TAG)
                .commit();

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


}
