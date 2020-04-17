package com.example.oud.user.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.fragments.home.nestedrecyclerview.NestedRecyclerViewHelper;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.player.PlayerInterface;

@Deprecated
public class HomeFragment extends ConnectionAwareFragment<HomeViewModel> {

    private static final String TAG = HomeFragment.class.getSimpleName();


    private PlaylistFragmentOpeningListener playlistFragmentOpeningListener;

    private NestedRecyclerViewHelper recyclerViewHelper;


    private PlayerInterface talkToPlayer;

    private RecyclerView recyclerView;

    public HomeFragment() {
        super(HomeViewModel.class, R.layout.fragment_home, R.id.progress_home, R.id.swipe_refresh_home);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");


        recyclerViewHelper = new NestedRecyclerViewHelper(this.getContext());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


       HomeViewModel homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        homeViewModel.getConnectionStatus().observe(this, connectionStatus -> {
            if (context instanceof ConnectionStatusListener) {

                ConnectionStatusListener connectionStatusListener = ((ConnectionStatusListener) context);
                talkToPlayer = (PlayerInterface) context;

                if (connectionStatus == Constants.ConnectionStatus.SUCCESSFUL)
                    connectionStatusListener.onConnectionSuccess();
                else
                    connectionStatusListener.onConnectionFailure();



            } else {
                throw new RuntimeException(context.toString() +
                        " must implement " + ConnectionStatusListener.class.getSimpleName() + PlayerInterface.class.getSimpleName());
            }
        });



        if (context instanceof PlaylistFragmentOpeningListener) {
            playlistFragmentOpeningListener = ((PlaylistFragmentOpeningListener) context);
        } else {
            throw new RuntimeException(getContext().toString()
                    + " must implement " + PlaylistFragmentOpeningListener.class.getSimpleName());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: ");


        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewHelper.setRecyclerView(recyclerView);



        // 1 not 0 because there's a dummy section at the top
        //if (recyclerViewHelper.getSectionCount() == 1) {
            handleRecentlyPlayed();
            handleCategories();
        //}
        /*else {
            for (int i = 0;i < recyclerViewHelper.getSectionCount(); i++) {
                for (int j = 0; j < recyclerViewHelper.getSection(i).getItemCount(); j++) {
                    if ()
                }
            }
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();



    }

    private void handleRecentlyPlayed() {
        MutableLiveData<Boolean> areThereRecentlyPlayedTracks = mViewModel.getAreThereRecentlyPlayedTracks();

        areThereRecentlyPlayedTracks.observe(getViewLifecycleOwner(), b -> {
            if (b) {

                /*if (recyclerViewHelper.getSectionCount() > 1)
                    if (recyclerViewHelper.getSection(1).getTitle() != null)
                        if (recyclerViewHelper.getSection(1).getTitle().equals("Recently played")) return; //already loaded*/

                // at position 1 not 0 because there's a dummy section at 0.
                handleRecentlyPlayedSection(1, mViewModel.getRecentlyPlayedLiveData());
            }
        });
    }

    private void handleCategories() {
        for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            HomeViewModel.OuterItemLiveData categoryData = mViewModel.getCategoryLiveData(i);
            handleCategorySection(i+1, categoryData);
        }
    }

    private void handleRecentlyPlayedSection(int position, HomeViewModel.OuterItemLiveData outerItemLiveData) {
        NestedRecyclerViewHelper.Section section = null;


        NestedRecyclerViewHelper.Section sec1 = recyclerViewHelper.getSection(1);
        if (sec1.getTitle() != null) {
            if (sec1.getTitle().equals(outerItemLiveData.getTitle().getValue())) {  // already loaded
                section = sec1;
            }
        }

        if (section == null) {
            section = new NestedRecyclerViewHelper.Section();
            recyclerViewHelper.addSection(position, section);
        }

        NestedRecyclerViewHelper.Section _section = section;

        outerItemLiveData.getIcon().observe(getViewLifecycleOwner(), section::setIcon);
        outerItemLiveData.getTitle().observe(getViewLifecycleOwner(), section::setTitle);

        int start = 0;
        for (HomeViewModel.InnerItemLiveData itemData : outerItemLiveData.getInnerItems().getValue()) {

            NestedRecyclerViewHelper.Item item = null;

            for (int i = start; i < _section.getItemCount(); i++) {
                NestedRecyclerViewHelper.Item current = _section.getItem(i);

                if (current.getTitle() != null) {
                    if (current.getTitle().equals(itemData.getTitle().getValue())) {
                        item = current;
                    }
                } else {
                    _section.removeItem(i);
                    i--;
                }
            }

            start++;

            NestedRecyclerViewHelper.Item _item = item;
            if (item == null) {
                item = new NestedRecyclerViewHelper.Item();
                itemData.getImage().observe(getViewLifecycleOwner(), imageUrl -> _item.setImage(imageUrl, false));
                itemData.getTitle().observe(getViewLifecycleOwner(), item::setTitle);
                _section.addItem(item);
            }

            itemData.getImage().observe(getViewLifecycleOwner(), imageUrl -> _item.setImage(imageUrl, false));
            itemData.getSubTitle().observe(getViewLifecycleOwner(), item::setSubtitle);
            itemData.getTitle().observe(getViewLifecycleOwner(), item::setTitle);

            //NestedRecyclerViewHelper.Item _item = item;
            itemData.getRelatedInfo().observe(getViewLifecycleOwner(), map -> {
                String trackId = (String) map.get(Constants.TRACK_ID_KEY);
                _item.getRelatedInfo().put(Constants.TRACK_ID_KEY, trackId);
                _item.setClickListener(v -> {
                    /*Toast.makeText(getContext(), trackId, Toast.LENGTH_SHORT).show();*/
                    talkToPlayer.configurePlayer(trackId,true);

                });
            });
        }

        //recyclerViewHelper.addSection(0, new NestedRecyclerViewHelper.Section());
    }

    private void handleCategorySection(int position, HomeViewModel.OuterItemLiveData outerItemLiveData) {

        NestedRecyclerViewHelper.Section section = null;

        for (int i = position; i < recyclerViewHelper.getSectionCount(); i++) {
            NestedRecyclerViewHelper.Section current = recyclerViewHelper.getSection(i);
            if (current.getTitle() != null) {
                if (current.getTitle().equals(outerItemLiveData.getTitle().getValue())) {  // already loaded
                    section = current;
                    break;
                }
            } else {
                recyclerViewHelper.removeSection(i);
                i--;
            }
        }

        if (section == null) {
            section = new NestedRecyclerViewHelper.Section();
            recyclerViewHelper.addSection(/*position,*/ section);
        }


        NestedRecyclerViewHelper.Section _section = section;
        outerItemLiveData.getInnerItems().observe(getViewLifecycleOwner(), innerItemLiveData -> {
            outerItemLiveData.getIcon().observe(getViewLifecycleOwner(), _section::setIcon);
            outerItemLiveData.getTitle().observe(getViewLifecycleOwner(), _section::setTitle);


            int start = 0;
            for (HomeViewModel.InnerItemLiveData itemData : innerItemLiveData) {
                NestedRecyclerViewHelper.Item item = null;

                for (int i = start; i < _section.getItemCount(); i++) {
                    NestedRecyclerViewHelper.Item current = _section.getItem(i);

                    if (current.getTitle() != null) {
                        if (current.getTitle().equals(itemData.getTitle().getValue())) {
                            item = current;
                        }
                    } else {
                        _section.removeItem(i);
                        i--;
                    }
                }

                start++;

                NestedRecyclerViewHelper.Item _item = item;
                if (item == null) {
                    item = new NestedRecyclerViewHelper.Item();
                    itemData.getImage().observe(getViewLifecycleOwner(), imageUrl -> _item.setImage(imageUrl, false));
                    itemData.getTitle().observe(getViewLifecycleOwner(), item::setTitle);
                    _section.addItem(item);
                }


                itemData.getSubTitle().observe(getViewLifecycleOwner(), item::setSubtitle);

                // NestedRecyclerViewHelper.Item _item = item;
                itemData.getRelatedInfo().observe(getViewLifecycleOwner(), map -> {
                    String playlistId = (String) map.get(Constants.PLAYLIST_ID_KEY);
                    _item.getRelatedInfo().put(Constants.PLAYLIST_ID_KEY, playlistId);
                    _item.setClickListener(v -> openPlaylistFragment(Constants.PlaylistFragmentType.PLAYLIST, playlistId));
                });
            }
        });

    }

    private void openPlaylistFragment(Constants.PlaylistFragmentType type, String id) {
        playlistFragmentOpeningListener.onOpeningPlaylistFragment(type, id);
    }

    @Override
    public void onTryingToReconnect() {
        //ProgressBar progressBar = getView().findViewById(R.id.progress_home);

        super.onTryingToReconnect();

        recyclerViewHelper.clearRecyclerView();
        handleRecentlyPlayed();
        handleCategories();

    }

    /*public interface UserHomeCommunicationListener {
        void onConnectionFailure();
    }*/
}
