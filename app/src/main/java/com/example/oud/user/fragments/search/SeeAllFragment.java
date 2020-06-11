package com.example.oud.user.fragments.search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.oud.api.AlbumPreview;
import com.example.oud.api.Artist;
import com.example.oud.api.OudAlbum;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudArtist;
import com.example.oud.api.OudList;
import com.example.oud.api.OudPlaylist;
import com.example.oud.api.OudTrack;
import com.example.oud.api.Playlist;
import com.example.oud.api.SearchedItems;
import com.example.oud.api.SearchedResults2;
import com.example.oud.api.Track;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeeAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeeAllFragment extends Fragment {


    private String mToken;
    private String mQuery;

    private Context mContext;

    private static final String TAG = SeeAllFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SearchedItemAdapter mAdapter; //Adapter reaches the items to recycleView for good performance.
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> mItemType;

    private ArrayList<SearchedItems> searchedItems;

    private OudArtist oudListArtist;
    private OudTrack oudListTrack;
    private OudPlaylist oudListPlaylist;
    private OudAlbum oudListAlbum;

    public SeeAllFragment() {
        // Required empty public constructor
    }


    public static SeeAllFragment newInstance(String type, String query) {

        SeeAllFragment fragment = new SeeAllFragment();

        Bundle args = new Bundle();
        args.putString("TYPE", type);
        args.putString("QUERY", query);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemType = new ArrayList<>();

        if (getArguments() != null) {

            mItemType.add(getArguments().getString("TYPE")) ;
            mQuery = getArguments().getString("QUERY");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_see_all, container, false);
        mToken = OudUtils.getToken(mContext);

        searchedItems = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.searchedResultsRecycleView);
        getAllItems();

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mContext = context;

    }

    private void getAllItems(){

        OudApi oudApi = OudUtils.instantiateRetrofitOudApi(Constants.BASE_URL);

        ArrayList<String> type = new ArrayList<>();

        switch (mItemType.get(0)){

            case "See all artists":

                type.add("Artist");
                Call<OudArtist> call1 =oudApi.getArtists(mToken,mQuery,OudUtils.commaSeparatedListQueryParameter(type));
                call1.enqueue(new Callback<OudArtist>() {
                    @Override
                    public void onResponse(Call<OudArtist> call, Response<OudArtist> response) {

                        if(!response.isSuccessful()){

                            Log.e(TAG,"Response:"+ response.code());

                        }
                        oudListArtist = response.body();

                        assert oudListArtist != null;
                        ArrayList<Artist> oudArtist = oudListArtist.getArtists();

                        if(oudArtist == null){
                            Log.e(TAG,"Null DATA");
                            return;
                        }
                        for (int i = 0; i < oudArtist.size(); i++) {

                            Artist currentArtist = oudArtist.get(i);

                            searchedItems.add(new SearchedItems(currentArtist.getImages().get(0),
                                    currentArtist.getDisplayName(), currentArtist.getType(),
                                    currentArtist.get_id(), false));

                        }

                        handleRecycleView();


                    }

                    @Override
                    public void onFailure(Call<OudArtist> call, Throwable t) {

                    }
                });

                break;

            case "See all songs":

                type.add("track");
                Call<OudTrack> call2 =oudApi.getTracks(mToken,mQuery,OudUtils.commaSeparatedListQueryParameter(type));

                call2.enqueue(new Callback<OudTrack>() {
                    @Override
                    public void onResponse(Call<OudTrack> call, Response<OudTrack> response) {

                        if(!response.isSuccessful()){

                            Log.e(TAG,"Response:"+ response.code());

                        }
                        oudListTrack = response.body();

                        assert oudListTrack != null;
                        ArrayList<Track> oudTrack = oudListTrack.getTracks();

                        for (int i = 0; i < oudTrack.size(); i++) {

                            Track currentTrack = oudTrack.get(i);

                            searchedItems.add(new SearchedItems(currentTrack.getAlbum().getImage(),
                                    currentTrack.getName(), currentTrack.getType(),
                                    currentTrack.get_id(), false));
                        }
                        handleRecycleView();

                    }

                    @Override
                    public void onFailure(Call<OudTrack> call, Throwable t) {

                    }
                });

                break;

            case "See all Playlists":

                type.add("Playlist");
                Call<OudPlaylist> call3 =oudApi.getPlaylists(mToken,mQuery,OudUtils.commaSeparatedListQueryParameter(type));

                call3.enqueue(new Callback<OudPlaylist>() {
                    @Override
                    public void onResponse(Call<OudPlaylist> call, Response<OudPlaylist> response) {

                        if(!response.isSuccessful()){

                            Log.e(TAG,"Response:"+ response.code());

                        }
                        oudListPlaylist = response.body();

                        assert oudListPlaylist != null;
                        ArrayList<Playlist> playlists = oudListPlaylist.getPlaylists();

                        for (int i = 0; i < playlists.size(); i++) {

                            Playlist currentPlaylist= playlists.get(i);

                            searchedItems.add(new SearchedItems(currentPlaylist.getImage(),
                                    currentPlaylist.getName(), currentPlaylist.getType(),
                                    currentPlaylist.getId(), false));

                        }
                        handleRecycleView();

                    }

                    @Override
                    public void onFailure(Call<OudPlaylist> call, Throwable t) {

                    }
                });


                break;
            case "See all albums":

                type.add("album");
                Call<OudAlbum> call4 =oudApi.getAlbums(mToken,mQuery,OudUtils.commaSeparatedListQueryParameter(type));

                call4.enqueue(new Callback<OudAlbum>() {
                    @Override
                    public void onResponse(Call<OudAlbum> call, Response<OudAlbum> response) {

                        if(!response.isSuccessful()){

                            Log.e(TAG,"Response:"+ response.code());

                        }
                        oudListAlbum = response.body();

                        assert oudListAlbum != null;

                        ArrayList<AlbumPreview> albums = oudListAlbum.getAlbums();
                        for (int i = 0; i < albums.size(); i++) {

                            AlbumPreview currentAlbum= albums.get(i);

                            searchedItems.add(new SearchedItems(currentAlbum.getImage(),
                                    currentAlbum.getName(), currentAlbum.getType(),
                                    currentAlbum.getId(), false));

                        }

                        handleRecycleView();

                    }

                    @Override
                    public void onFailure(Call<OudAlbum> call, Throwable t) {

                    }
                });


        }


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
               /* if(!currentItem.isStringOnly()) {
                    UpdateRecentItem updateRecentItem = new UpdateRecentItem(currentItem.getId(), currentItem.getItemType());

                    UpdateRecentFragment(updateRecentItem);
                }*/

            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        mAdapter.notifyDataSetChanged();
        /*mRecyclerView.setVisibility(View.VISIBLE);*/


    }
}
