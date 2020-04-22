package com.example.oud.user.fragments.library;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;

public abstract class LibrarySubFragmentViewModel<ConnectionAwareRepo extends ConnectionAwareRepository, Item> extends ConnectionAwareViewModel<ConnectionAwareRepo> {

    protected MutableLiveData<OudList<Item>> lastSetOfLoadedItems;
    protected ArrayList<MutableLiveData<Item>> loadedItems = new ArrayList<>();


    protected int removedItemPosition;

    public LibrarySubFragmentViewModel(ConnectionAwareRepo repoInstance) {
        super(repoInstance, Constants.YAMANI_MOCK_BASE_URL);
    }

    /**
     *
     * <p>Asks {@link ConnectionAwareRepo} to fetch for a new set of Items starting from the last one found in {@link #loadedItems}.</p>
     * <p>The number of items fetched equals {@link Constants#USER_LIBRARY_SINGLE_FETCH_LIMIT}.</p>
     * @param token
     * @return A {@link MutableLiveData} containing a list of the newly loaded items.
     */
    public MutableLiveData<OudList<Item>> loadMoreItems(String token) {
        if (lastSetOfLoadedItems == null)
            lastSetOfLoadedItems = repoFetchItems(token, Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0);
        else {
            if (lastSetOfLoadedItems.getValue() != null) {
                int prevOffset = lastSetOfLoadedItems.getValue().getOffset();
                int prevLimit = lastSetOfLoadedItems.getValue().getLimit();

                int offset = prevOffset + prevLimit, limit = Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT;

                lastSetOfLoadedItems = repoFetchItems(token, limit, offset);
            } /*else
                lastSetOfLoadedItems = repoFetchItems(token, Constants.USER_LIBRARY_SINGLE_FETCH_LIMIT, 0);*/

        }

        return lastSetOfLoadedItems;
    }

    public ArrayList<MutableLiveData<Item>> getLoadedItems() {
        if (loadedItems == null)
            loadedItems = new ArrayList<>();
        return loadedItems;
    }

    /**
     * Repository's call to fetch for items.
     * @param token
     * @param limit
     * @param offset
     * @return
     */
    protected abstract MutableLiveData<OudList<Item>> repoFetchItems(String token, int limit, int offset);



    public void removeItem(String token,
                           String id,
                           int position,
                           ConnectionStatusListener undoUiAndUpdateLiveData) {

        if (loadedItems.isEmpty()) return;

        removedItemPosition = position;

        repoRemoveItem(token, id, undoUiAndUpdateLiveData);
    }

    /**
     *  Repository's call to remove items.
     * @param token
     * @param id
     * @param undoUiAndUpdateLiveData
     */
    public abstract void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData);

    public void updateLiveDataUponRemovingItemFromLikedItems() {
        loadedItems.remove(removedItemPosition);
    }

    public void clearLoadedItems() {
        loadedItems = new ArrayList<>();
    }

    public void clearLastSetOfLoadedItems() {
        lastSetOfLoadedItems = null;
    }

    public void clearTheDataThatHasThePotentialToBeChangedOutside() {
        clearLastSetOfLoadedItems();
        clearLoadedItems();
    }

    @Override
    public void clearData() {
        clearLastSetOfLoadedItems();
        clearLoadedItems();
    }

}
