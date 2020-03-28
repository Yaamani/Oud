package com.example.oud.user.fragments.home;

import com.example.oud.Constants;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private HomeRepository homeRepository;

    private MutableLiveData<Boolean> areThereRecentlyPlayedTracks;
    private OuterItemLiveData recentlyPlayedLiveData;
    private OuterItemLiveData[] categoriesLiveData;

    public HomeViewModel() {
        homeRepository = HomeRepository.getInstance();
        if (Constants.MOCK)
            homeRepository.setBaseUrl(Constants.YAMANI_MOCK_BASE_URL);
    }

    public MutableLiveData<Boolean> getAreThereRecentlyPlayedTracks() {
        if (areThereRecentlyPlayedTracks == null)
            areThereRecentlyPlayedTracks = homeRepository.areThereRecentlyPlayedTracks();
        return areThereRecentlyPlayedTracks;
    }

    public OuterItemLiveData getRecentlyPlayedLiveData() {
        if (recentlyPlayedLiveData == null)
            recentlyPlayedLiveData = homeRepository.loadRecentlyPlayed();

        return recentlyPlayedLiveData;
    }

    public OuterItemLiveData getCategoryLiveData(int position) {
        if (categoriesLiveData == null)
            categoriesLiveData = new OuterItemLiveData[Constants.USER_HOME_CATEGORIES_COUNT];

        if (categoriesLiveData[position] == null)
            categoriesLiveData[position] = homeRepository.loadCategory(position);

        return categoriesLiveData[position];
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

        public InnerItemLiveData() {
            mImage = new MutableLiveData<>();
            mTitle = new MutableLiveData<>();
            mSubTitle = new MutableLiveData<>();
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
    }

}