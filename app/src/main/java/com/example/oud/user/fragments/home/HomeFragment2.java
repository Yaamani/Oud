package com.example.oud.user.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.Artist;
import com.example.oud.api.Category2;
import com.example.oud.api.OudList;
import com.example.oud.api.Playlist;
import com.example.oud.api.RecentlyPlayedTrack2;
import com.example.oud.api.RecentlyPlayedTracks2;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.home.nestedrecyclerview.NestedRecyclerViewHelper;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.player.PlayerInterface;

import java.util.ArrayList;

import static com.example.oud.api.Context.CONTEXT_UNKNOWN ;
import static com.example.oud.api.Context.CONTEXT_ALBUM   ;
import static com.example.oud.api.Context.CONTEXT_ARTIST  ;
import static com.example.oud.api.Context.CONTEXT_PLAYLIST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment2 extends ConnectionAwareFragment<HomeViewModel2> {

    private PlaylistFragmentOpeningListener playlistFragmentOpeningListener;

    private NestedRecyclerViewHelper mRecyclerViewHelper;

    private PlayerInterface talkToPlayer;

    private RecyclerView mRecyclerView;

    public HomeFragment2() {
        super(HomeViewModel2.class,
                R.layout.fragment_home,
                R.id.progress_home,
                R.id.swipe_refresh_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerViewHelper = new NestedRecyclerViewHelper(this.getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_view_home);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewHelper.setRecyclerView(mRecyclerView);


        handleRecentlyPlayed();
        handleCategories();

    }

    private void handleRecentlyPlayed() {
        mViewModel.getRecentlyPlayedLiveData().observe(getViewLifecycleOwner(), recentlyPlayedTracks2 -> {

            NestedRecyclerViewHelper.Section section = handleRecentlyPlayedSection(recentlyPlayedTracks2);
            if (section == null) return;

            int i = 0;
            for (RecentlyPlayedTrack2 recentlyPlayedTrack : recentlyPlayedTracks2.getItems()) {
                com.example.oud.api.Context context = recentlyPlayedTrack.getContext();

                if (context.getType().equals(CONTEXT_UNKNOWN)) {
                    continue;
                }

                if (!hasAlreadyBeenFetchedRecentlyPlayed(recentlyPlayedTrack)) {
                    // fetch
                    switch (context.getType()) {
                        case CONTEXT_ALBUM:
                            mViewModel.addRecentlyPlayedAlbum(context.getId(), i);
                            break;
                        case CONTEXT_ARTIST:
                            mViewModel.addRecentlyPlayedArtist(context.getId(), i);
                            break;
                        case CONTEXT_PLAYLIST:
                            mViewModel.addRecentlyPlayedPlaylist(context.getId(), i);
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
                    String title = "", image = "";
                    if (o instanceof Album) {
                        title = ((Album) o).getName();
                        image = ((Album) o).getImage();
                    }
                    else if (o instanceof Playlist) {
                        title = ((Playlist) o).getName();
                        image = ((Playlist) o).getImage();
                    }
                    else if (o instanceof Artist) {
                        title = ((Artist) o).getName();
                        image = ((Artist) o).getImages().get(0);
                    }

                    _item.setImageUrl(image);
                    _item.setTitle(title);

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

            if (!context.getType().equals(CONTEXT_UNKNOWN))
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
            mViewModel.addCategoryPlaylists(category.get_id(), position);

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

                item.setImageUrl(playlist.getImage());
                item.setTitle(playlist.getName());

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PlaylistFragmentOpeningListener) {
            playlistFragmentOpeningListener = ((PlaylistFragmentOpeningListener) context);
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement " + PlaylistFragmentOpeningListener.class.getSimpleName());
        }
    }

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        handleRecentlyPlayed();
        handleCategories();
    }
}
