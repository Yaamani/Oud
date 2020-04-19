package com.example.oud.user.fragments.library;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oud.Constants;
import com.example.oud.LoadMoreAdapter;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.user.player.PlayerInterface;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LibrarySubFragment<Item,
        ConnectionAwareRepo extends ConnectionAwareRepository,
        SubViewModel extends LibrarySubFragmentViewModel<ConnectionAwareRepo, Item>> extends ConnectionAwareFragment<SubViewModel> {

    private static final String TAG = LibrarySubFragment.class.getSimpleName();

    protected String token;

    protected RecyclerView mRecyclerViewItems;
    protected LoadMoreAdapter mItemsAdapter;

    private TextView mTextViewNoItems;

    protected PlayerInterface talkToPlayer;


    // private int ItemToBeRemovedPosition;
    protected String removedItemId;
    protected String removedItemName;
    protected String removedItemImage;

    @IdRes
    private int recyclerViewId;
    @IdRes
    private int textViewNoItemsId;

    public LibrarySubFragment(Class<SubViewModel> viewModelClass,
                              int layoutId,
                              int progressBarId,
                              @Nullable Integer viewBlockUiId,
                              @Nullable Integer swipeRefreshLayoutId,
                              @IdRes int recyclerViewId,
                              int textViewNoItemsId) {

        super(viewModelClass, layoutId, progressBarId, viewBlockUiId, swipeRefreshLayoutId);

        this.recyclerViewId = recyclerViewId;
        this.textViewNoItemsId = textViewNoItemsId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PlayerInterface) {
            talkToPlayer = (PlayerInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement" + PlayerInterface.class.getSimpleName() + ".");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleToken();

        mViewModel.clearTheDataThatHasThePotentialToBeChangedOutside();

        initializeUiStuff(view);

        handleItems();
    }

    private void handleToken() {
        token = OudUtils.getToken(getContext());
    }

    private void initializeUiStuff(@NonNull View view) {
        mRecyclerViewItems = view.findViewById(recyclerViewId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewItems.setLayoutManager(layoutManager);
        mRecyclerViewItems.setItemAnimator(new DefaultItemAnimator());

        mTextViewNoItems = view.findViewById(textViewNoItemsId);
    }

    /**
     * Handles the data and the logic behind the item list.
     */
    private void handleItems() {

        if (mViewModel.getLoadedItems().size() < Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT)
            loadMoreItems();
        else {
            observerLoadedItems();
            unBlockUi();
        }
    }

    /**
     * Observes loadedItems in {@link SubViewModel} and adds the newly loaded items to the loaded ones.
     */
    protected void loadMoreItems() {
        mViewModel.loadMoreItems(token).observe(getViewLifecycleOwner(), itemOudList -> {
            if (itemOudList.getTotal() == 0 & mViewModel.getLoadedItems().size() == 0) {
                mRecyclerViewItems.setVisibility(View.GONE);
                mTextViewNoItems.setVisibility(View.VISIBLE);
            }

            if (mItemsAdapter != null)
                mItemsAdapter.setLoaded();

            for (Item Item : itemOudList.getItems()) {
                mViewModel.getLoadedItems().add(new MutableLiveData<>(Item));
            }

            observerLoadedItems();
        });
    }

    /**
     * Observes loadedItems in {@link SubViewModel}. and populates {@link #mItemsAdapter}.
     */
    protected abstract void observerLoadedItems();

    @Override
    public void onTryingToReconnect() {
        super.onTryingToReconnect();

        handleItems();
    }
}
