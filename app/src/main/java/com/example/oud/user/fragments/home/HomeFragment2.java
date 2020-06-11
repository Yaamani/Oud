package com.example.oud.user.fragments.home;

import android.os.Bundle;
import android.view.View;

import com.example.oud.Constants;
import com.example.oud.NotificationShareUtils;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.Category2;
import com.example.oud.api.OudApi;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTrack2;
import com.example.oud.api.RecentlyPlayedTracks2;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.fragments.home.nestedrecyclerview.NestedRecyclerViewHelper;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.player.PlayerInterface;

import java.util.ArrayList;

import static com.example.oud.Constants.API_UNKNOWN;
import static com.example.oud.Constants.API_ALBUM;
import static com.example.oud.Constants.API_ARTIST;
import static com.example.oud.Constants.API_PLAYLIST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment2 extends ConnectionAwareFragment<HomeViewModel2> {

    //private PlaylistFragmentOpeningListener playlistFragmentOpeningListener;

    private String userId;
    private String token;

    private NestedRecyclerViewHelper mRecyclerViewHelper;

    private PlayerInterface talkToPlayer;

    private RecyclerView mRecyclerView;

    public HomeFragment2() {
        super(HomeViewModel2.class,
                R.layout.fragment_home,
                R.id.progress_home,
                R.id.swipe_refresh_home);
    }

    public static HomeFragment2 newInstance(String userId) {
        HomeFragment2 homeFragment2 = new HomeFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID_KEY, userId);
        homeFragment2.setArguments(bundle);
        return homeFragment2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerViewHelper = new NestedRecyclerViewHelper(this.getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleArgs();
        //handleToken();
        token = OudUtils.getToken(getContext());

        userId = getArguments().getString(Constants.USER_ID_KEY);

        mRecyclerView = view.findViewById(R.id.recycler_view_home);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewHelper.setRecyclerView(mRecyclerView);


        handleRecentlyPlayed();
        handleCategories();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        OudApi oudApi = retrofit.create(OudApi.class);
        NotificationShareUtils.subscribeToAllFollowedArtistsTopicsUponLoggingIn(token, oudApi);


        //mViewModel.getCategoryListLiveData();

    }

    private void handleArgs() {
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString(Constants.USER_ID_KEY);
        } else {
            throw new RuntimeException("Instead of calling new " + HomeFragment2.class.getSimpleName() + "()" +
                    ", call " + HomeFragment2.class.getSimpleName() + ".newInstance()" +
                    " to pass the arguments to the fragment. Or you can use " + HomeFragment2.class.getSimpleName() + ".setArguments().");
        }
    }

    /*private void handleToken() {
        SharedPreferences prefs = getContext().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        token = prefs.getString("token","000000");
    }*/

    private void handleRecentlyPlayed() {
        mViewModel.getRecentlyPlayedLiveData(token).observe(getViewLifecycleOwner(), recentlyPlayedTracks2 -> {

            NestedRecyclerViewHelper.Section section = handleRecentlyPlayedSection(recentlyPlayedTracks2);
            if (section == null) return;

            int i = 0;
            for (RecentlyPlayedTrack2 recentlyPlayedTrack : recentlyPlayedTracks2.getItems()) {
                com.example.oud.api.Context context = recentlyPlayedTrack.getContext();

                if (context.getType().equals(API_UNKNOWN)) {
                    continue;
                }

                if (!hasAlreadyBeenFetchedRecentlyPlayed(recentlyPlayedTrack)) {
                    // fetch
                    switch (context.getType()) {
                        case API_ALBUM:
                            mViewModel.addRecentlyPlayedAlbum(token, context.getId(), i);
                            break;
                        case API_ARTIST:
                            mViewModel.addRecentlyPlayedArtist(token, context.getId(), i);
                            break;
                        case API_PLAYLIST:
                            mViewModel.addRecentlyPlayedPlaylist(token, context.getId(), i);
                            break;
                    }

                }

                NestedRecyclerViewHelper.Item item = null;

                for (int j = 0; j < section.getItemCount(); j++) {
                    NestedRecyclerViewHelper.Item current = section.getItem(j);

                    //if (current.getTitle() != null) {
                    if (current.getRelatedInfo().get(Constants.ID_KEY).equals(context.getId())) {
                        item = current;
                        break;
                    }
                        /*} else {
                            section.removeItem(j);
                            j--;
                        }*/
                }

                if (item == null) {
                    item = new NestedRecyclerViewHelper.Item();
                    item.getRelatedInfo().put(Constants.ID_KEY, context.getId());

                    section.addItem(i, item);
                }

                MutableLiveData liveData = mViewModel.getRecentlyPlayedAlbumsPlaylistsArtists().get(i);

                NestedRecyclerViewHelper.Item _item = item;
                liveData.observe(getViewLifecycleOwner(), o -> {
                    boolean circularImage = false;

                    String title = "", image = "";
                    if (o instanceof Album) {
                        title = ((Album) o).getName();
                        image = ((Album) o).getImage();
                        _item.setClickListener(v ->
                                PlaylistFragment.show(getActivity(),
                                        R.id.nav_host_fragment,
                                        userId,
                                        Constants.PlaylistFragmentType.ALBUM,
                                        ((Album) o).get_id()));
                    }
                    else if (o instanceof Playlist) {
                        title = ((Playlist) o).getName();
                        image = ((Playlist) o).getImage();
                        _item.setClickListener(v ->
                                PlaylistFragment.show(getActivity(),
                                        R.id.nav_host_fragment,
                                        userId,
                                        Constants.PlaylistFragmentType.PLAYLIST,
                                        ((Playlist) o).getId()));
                    }
                    else if (o instanceof Artist) {
                        circularImage = true;
                        title = ((Artist) o).getDisplayName();
                        image = ((Artist) o).getImages().get(0);
                        _item.setClickListener(v ->
                                ArtistFragment.show(getActivity(),
                                        R.id.nav_host_fragment,
                                        ((Artist) o).get_id()));
                    }

                    _item.setTitle(title);
                    _item.setImage(image, circularImage);

                });

                i++;
            }

        });
    }

    private NestedRecyclerViewHelper.Section handleRecentlyPlayedSection(RecentlyPlayedTracks2 recentlyPlayedTracks2) {
        if (recentlyPlayedTracks2 == null) // response code 204 (No recently played)
            return null;

        // Check if all contexts are unknown. If so, don't add recently played section
        for (int i = 0; i < recentlyPlayedTracks2.getItems().size(); i++) {
            com.example.oud.api.Context context = recentlyPlayedTracks2.getItems().get(i).getContext();

            if (!context.getType().equals(API_UNKNOWN))
                break;

            if (i == recentlyPlayedTracks2.getItems().size() - 1) {
                return null;
            }
        }



        NestedRecyclerViewHelper.Section section = null;

        if (mRecyclerViewHelper.getSectionCount() > 1) {
            NestedRecyclerViewHelper.Section sec1 = mRecyclerViewHelper.getSection(1);
            if (sec1.getTitle() != null) {
                if (sec1.getTitle().equals(Constants.USER_HOME_RECENTLY_PLAYED)) {  // already exists
                    section = sec1;
                }
            }

        }

        if (section == null) {
            section = new NestedRecyclerViewHelper.Section();
            section.setIcon(Constants.USER_HOME_RECENTLY_PLAYED_ICON);
            section.setTitle(Constants.USER_HOME_RECENTLY_PLAYED);
            mRecyclerViewHelper.addSection(1, section);
        }

        return section;
    }


    private boolean hasAlreadyBeenFetchedRecentlyPlayed(RecentlyPlayedTrack2 recentlyPlayedTrack) {
        boolean hasAlreadyBeenFetched = false;

        for (MutableLiveData liveData : mViewModel.getRecentlyPlayedAlbumsPlaylistsArtists()) {
            String id = "";
            if (liveData.getValue() instanceof Album)
                id = ((Album) liveData.getValue()).get_id();
            else if (liveData.getValue() instanceof Playlist)
                id = ((Playlist) liveData.getValue()).getId();
            else if (liveData.getValue() instanceof Artist)
                id = ((Artist) liveData.getValue()).get_id();



            if (id.equals(recentlyPlayedTrack.getContext().getId())) {
                // don't fetch
                hasAlreadyBeenFetched = true;
                break;
            }

        }


        return hasAlreadyBeenFetched;
    }

    private void handleCategories() {
        mViewModel.getCategoryListLiveData().observe(getViewLifecycleOwner(), category2OudList -> {

            int i = 0;
            for (Category2 category : category2OudList.getItems()) {

                NestedRecyclerViewHelper.Section section = handleCategorySection(category);

                handleSingleCategoryItems(category, section, i);

                i++;
            }

        });
    }

    private NestedRecyclerViewHelper.Section handleCategorySection(Category2 category) {
        NestedRecyclerViewHelper.Section section = null;

        if (mRecyclerViewHelper.getSectionCount() > 1) {
            for (int i = 1; i < mRecyclerViewHelper.getSectionCount(); i++) {
                NestedRecyclerViewHelper.Section sec = mRecyclerViewHelper.getSection(i);
                if (sec.getTitle() != null)
                    if (sec.getTitle().equals(category.getName())) {  // already exists
                        section = sec;
                        break;
                    }
            }

        }

        if (section == null) {
            section = new NestedRecyclerViewHelper.Section();
            section.setIcon(Constants.USER_HOME_RECENTLY_CATEGORY_ICON);
            section.setTitle(category.getName());
            mRecyclerViewHelper.addSection(section);
        }

        return section;
    }

    private void handleSingleCategoryItems(Category2 category, NestedRecyclerViewHelper.Section section, int position) {

        //if (!hasAlreadyBeenFetchedCategoryPlaylists(position))
            mViewModel.addCategoryPlaylists(token, category.get_id(), position);

        mViewModel.getPlaylistsOfEachCategory().get(position).observe(getViewLifecycleOwner(), playlistOudList -> {

            //mRecyclerViewHelper.addSection(section);

            int i = 0;
            for (Playlist playlist : playlistOudList.getItems()) {



                NestedRecyclerViewHelper.Item item = null;

                for (int j = 0; j < section.getItemCount(); j++) {
                    NestedRecyclerViewHelper.Item current = section.getItem(j);

                    if (current.getRelatedInfo().get(Constants.ID_KEY).equals(playlist.getId())) {
                        item = current;
                        break;
                    }
                }

                if (item == null) {
                    item = new NestedRecyclerViewHelper.Item();
                    item.getRelatedInfo().put(Constants.ID_KEY, playlist.getId());

                    section.addItem(i, item);

                }


                item.setImage(playlist.getImage(), false);
                item.setTitle(playlist.getName());
                item.setClickListener(v ->
                        PlaylistFragment.show(getActivity(),
                                R.id.nav_host_fragment,
                                userId,
                                Constants.PlaylistFragmentType.PLAYLIST,
                                playlist.getId()));

                i++;
            }

        });

    }

    private boolean hasAlreadyBeenFetchedCategoryPlaylists(int position) {
        ArrayList<MutableLiveData<OudList<Playlist>>> data = mViewModel.getPlaylistsOfEachCategory();
        if (position < data.size())
            return data.get(position) != null;
        else return false;
    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PlaylistFragmentOpeningListener) {
            playlistFragmentOpeningListener = ((PlaylistFragmentOpeningListener) context);
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement " + PlaylistFragmentOpeningListener.class.getSimpleName());
        }
    }*/

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        handleRecentlyPlayed();
        handleCategories();

    }

    /**
     * For tests only.
     * @param userId
     */
    @Deprecated
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
