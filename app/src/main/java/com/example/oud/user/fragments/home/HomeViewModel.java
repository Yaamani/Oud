package com.example.oud.user.fragments.home;

import com.example.oud.Constants;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private NestedRecyclerViewOuterItemSupplier homeRepository;

    private OuterItemLiveData recentlyPlayedLiveData;
    private OuterItemLiveData[] categoriesLiveData;

    public HomeViewModel() {
        if (Constants.MOCK)
            homeRepository = new HomeRepositoryMockService();
        else homeRepository = new HomeRepository();
    }

    public OuterItemLiveData getRecentlyPlayedLiveData() {
        if (recentlyPlayedLiveData == null)
            //recentlyPlayedLiveData = homeRepository.loadRecentlyPlayed();
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
        private MutableLiveData<String> mIcon;
        private MutableLiveData<String> mTitle;
        private InnerItemLiveData[] mInnerItems;

        public OuterItemLiveData(MutableLiveData<String> mIcon,
                                 MutableLiveData<String> mTitle,
                                 InnerItemLiveData[] mInnerItems) {
            this.mIcon = mIcon;
            this.mTitle = mTitle;
            this.mInnerItems = mInnerItems;
        }

        public MutableLiveData<String> getIcon() {
            return mIcon;
        }

        public MutableLiveData<String> getTitle() {
            return mTitle;
        }

        public InnerItemLiveData[] getInnerItems() {
            return mInnerItems;
        }
    }

    public static class InnerItemLiveData {
        private MutableLiveData<String> mImage;
        private MutableLiveData<String> mTitle;
        private MutableLiveData<String> mSubTitle;

        public InnerItemLiveData() {
            mImage = new MutableLiveData<>();
            mTitle = new MutableLiveData<>();
            mSubTitle = new MutableLiveData<>();
        }

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