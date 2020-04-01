package com.example.oud.user.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.user.fragments.home.nestedrecyclerview.NestedRecyclerViewHelper;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.player.PlayerInterface;

public class HomeFragment extends Fragment implements ReconnectingListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeViewModel homeViewModel;

    private PlaylistFragmentOpeningListener playlistFragmentOpeningListener;

    private NestedRecyclerViewHelper recyclerViewHelper;

    private PlayerInterface talkToPlayer ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        recyclerViewHelper = new NestedRecyclerViewHelper(this.getContext());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        homeViewModel.getConnectionStatus().observe(this, connectionStatus -> {
            if (context instanceof ConnectionStatusListener && context instanceof PlayerInterface) {

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: ");
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), connectionStatus -> {
            ProgressBar progressBar = root.findViewById(R.id.progress_home);
            if (progressBar != null)
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated: ");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home);

        recyclerViewHelper.setRecyclerView(recyclerView);


        if (recyclerViewHelper.getSectionCount() == 0) {
            handleRecentlyPlayed();
            handleCategories();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void handleRecentlyPlayed() {
        MutableLiveData<Boolean> areThereRecentlyPlayedTracks = homeViewModel.getAreThereRecentlyPlayedTracks();

        areThereRecentlyPlayedTracks.observe(getViewLifecycleOwner(), b -> {
            if (b) {

                if (recyclerViewHelper.getSectionCount() > 0)
                    if (recyclerViewHelper.getSection(0).getTitle() != null)
                        if (recyclerViewHelper.getSection(0).getTitle().equals("Recently played")) return; //already loaded

                handleRecentlyPlayedSection(0, homeViewModel.getRecentlyPlayedLiveData());
            }
        });
    }

    private void handleCategories() {
        for (int i = 0; i < Constants.USER_HOME_CATEGORIES_COUNT; i++) {
            HomeViewModel.OuterItemLiveData categoryData = homeViewModel.getCategoryLiveData(i);
            handleCategorySection(recyclerViewHelper.getSectionCount(), categoryData);
        }
    }

    private void handleRecentlyPlayedSection(int position, HomeViewModel.OuterItemLiveData outerItemLiveData) {
        NestedRecyclerViewHelper.Section section = new NestedRecyclerViewHelper.Section();

        outerItemLiveData.getIcon().observe(getViewLifecycleOwner(), section::setIcon);
        outerItemLiveData.getTitle().observe(getViewLifecycleOwner(), section::setTitle);
        for (HomeViewModel.InnerItemLiveData itemData : outerItemLiveData.getInnerItems().getValue()) {

            NestedRecyclerViewHelper.Item item = new NestedRecyclerViewHelper.Item();
            section.addItem(item);

            itemData.getImage().observe(getViewLifecycleOwner(), item::setImageUrl);
            itemData.getSubTitle().observe(getViewLifecycleOwner(), item::setSubtitle);
            itemData.getTitle().observe(getViewLifecycleOwner(), item::setTitle);

            itemData.getRelatedInfo().observe(getViewLifecycleOwner(), map -> {
                String trackId = (String) map.get(Constants.TRACK_ID_KEY);
                item.getRelatedInfo().put(Constants.TRACK_ID_KEY, trackId);
                item.setClickListener(v -> {
                    Toast.makeText(getContext(), trackId, Toast.LENGTH_SHORT).show();
                    talkToPlayer.restAndPlay(true);
                    talkToPlayer.createSmallFragmentForFirstTime();
                });
            });
        }

        recyclerViewHelper.addSection(position, section);
    }

    private void handleCategorySection(int position, HomeViewModel.OuterItemLiveData outerItemLiveData) {
        NestedRecyclerViewHelper.Section section = new NestedRecyclerViewHelper.Section();

        outerItemLiveData.getInnerItems().observe(getViewLifecycleOwner(), innerItemLiveData -> {
            outerItemLiveData.getIcon().observe(getViewLifecycleOwner(), section::setIcon);
            outerItemLiveData.getTitle().observe(getViewLifecycleOwner(), section::setTitle);

            for (HomeViewModel.InnerItemLiveData itemData : innerItemLiveData) {
                NestedRecyclerViewHelper.Item item = new NestedRecyclerViewHelper.Item();
                section.addItem(item);

                itemData.getImage().observe(getViewLifecycleOwner(), item::setImageUrl);
                itemData.getTitle().observe(getViewLifecycleOwner(), item::setTitle);
                itemData.getSubTitle().observe(getViewLifecycleOwner(), item::setSubtitle);

                itemData.getRelatedInfo().observe(getViewLifecycleOwner(), map -> {
                    String playlistId = (String) map.get(Constants.PLAYLIST_ID_KEY);
                    item.getRelatedInfo().put(Constants.PLAYLIST_ID_KEY, playlistId);
                    item.setClickListener(v -> openPlaylistFragment(Constants.PlaylistFragmentType.PLAYLIST, playlistId));
                });
            }
        });


        recyclerViewHelper.addSection(position, section);
    }

    private void openPlaylistFragment(Constants.PlaylistFragmentType type, String id) {
        playlistFragmentOpeningListener.onOpeningPlaylistFragment(type, id);
    }

    @Override
    public void onTryingToReconnect() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_home);
        if (progressBar != null)
            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);

        recyclerViewHelper.clearRecyclerView();
        handleRecentlyPlayed();
        handleCategories();

    }

    /*public interface UserHomeCommunicationListener {
        void onConnectionFailure();
    }*/
}
