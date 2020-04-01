package com.example.oud.user.fragments.home;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.connectionaware.ConnectionAwareViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ConnectionAwareViewModel<HomeRepository> implements ConnectionStatusListener {

    private static final String TAG = HomeViewModel.class.getSimpleName();


    private MutableLiveData<Boolean> areThereRecentlyPlayedTracks;
    private OuterItemLiveData recentlyPlayedLiveData;
    private OuterItemLiveData[] categoriesLiveData;

    public HomeViewModel() {
        super(HomeRepository.getInstance(), Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Boolean> getAreThereRecentlyPlayedTracks() {
        if (areThereRecentlyPlayedTracks == null)
            areThereRecentlyPlayedTracks = repo.areThereRecentlyPlayedTracks();
        return areThereRecentlyPlayedTracks;
    }

    public OuterItemLiveData getRecentlyPlayedLiveData() {
        if (recentlyPlayedLiveData == null)
            recentlyPlayedLiveData = repo.loadRecentlyPlayed();

        return recentlyPlayedLiveData;
    }

    public OuterItemLiveData getCategoryLiveData(int position) {
        if (categoriesLiveData == null)
            categoriesLiveData = new OuterItemLiveData[Constants.USER_HOME_CATEGORIES_COUNT];

        if (categoriesLiveData[position] == null)
            categoriesLiveData[position] = repo.loadCategory(position);

        return categoriesLiveData[position];
    }

    @Override
    public void onConnectionFailure() {
        super.onConnectionFailure();

        clearData();
    }

    @Override
    public void clearData() {
        areThereRecentlyPlayedTracks = null;
        recentlyPlayedLiveData = null;
        categoriesLiveData = null;
    }


    public static class OuterItemLiveData {
        private MutableLiveData<Integer> mIcon;
        private MutableLiveData<String> mTitle;
        private MutableLiveData<ArrayList<InnerItemLiveData>> mInnerItems;

        public OuterItemLiveData(MutableLiveData<Integer> mIcon,
                                 MutableLiveData<String> mTitle,
                                 MutableLiveData<ArrayList<InnerItemLiveData>> mInnerItems) {
            this.mIcon = mIcon;
            this.mTitle = mTitle;
            this.mInnerItems = mInnerItems;
        }

        public MutableLiveData<Integer> getIcon() {
            return mIcon;
        }

        public MutableLiveData<String> getTitle() {
            return mTitle;
        }

        public MutableLiveData<ArrayList<InnerItemLiveData>> getInnerItems() {
            return mInnerItems;
        }
    }

    public static class InnerItemLiveData {
        //private MutableLiveData<Integer> position;

        private MutableLiveData<String> mImage;
        private MutableLiveData<String> mTitle;
        private MutableLiveData<String> mSubTitle;

        private MutableLiveData<HashMap<String, Object>> mRelatedInfo;

        public InnerItemLiveData() {
            mImage = new MutableLiveData<>();
            mTitle = new MutableLiveData<>();
            mSubTitle = new MutableLiveData<>();
            mRelatedInfo = new MutableLiveData<>();
        }

        /*public MutableLiveData<Integer> getPosition() {
            return position;
        }*/

        public MutableLiveData<String> getImage() {
            return mImage;
        }

        public MutableLiveData<String> getTitle() {
            return mTitle;
        }

        public MutableLiveData<String> getSubTitle() {
            return mSubTitle;
        }

        public MutableLiveData<HashMap<String, Object>> getRelatedInfo() {
            return mRelatedInfo;
        }
    }

}