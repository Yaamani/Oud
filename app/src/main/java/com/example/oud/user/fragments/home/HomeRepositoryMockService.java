package com.example.oud.user.fragments.home;

import com.example.oud.Constants;
import com.example.oud.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.lifecycle.MutableLiveData;

@Deprecated
public class HomeRepositoryMockService implements NestedRecyclerViewOuterItemSupplier{

    private static final String TAG = HomeRepositoryMockService.class.getSimpleName();

    ArrayList<String> landscapes = new ArrayList<>();

    public HomeRepositoryMockService() {
        landscapes.add("https://i.redd.it/glin0nwndo501.jpg");
        landscapes.add("https://preview.redd.it/b2mqfbyvlqn41.jpg?width=960&crop=smart&auto=webp&s=271f821c58119107901d647b960848fc5c947958");
        landscapes.add("https://preview.redd.it/2b0p6as65rn41.jpg?width=640&crop=smart&auto=webp&s=476a42612c502304474204d855d928a01ab1078a");
        landscapes.add("https://preview.redd.it/bds3fhhrrqn41.jpg?width=640&crop=smart&auto=webp&s=d3bf25502c14578e9cad5fa55a6e75e7158f9e09");
        landscapes.add("https://preview.redd.it/n34wxklkrsn41.jpg?width=640&crop=smart&auto=webp&s=3a4323f65dcba771b2a028c1599521b89becf1bb");
        landscapes.add("https://preview.redd.it/azm0y9fb2qn41.jpg?width=640&crop=smart&auto=webp&s=416cb66e9ed7be365d97dece1424863d36cfaa37");
        landscapes.add("https://preview.redd.it/nqw556wuetn41.jpg?width=640&crop=smart&auto=webp&s=d0183f8e4ed9968d1470135df36f80c9d2b34ff8");
        landscapes.add("https://preview.redd.it/65pzpv5xjpn41.jpg?width=640&crop=smart&auto=webp&s=7172a8017dc408a5de0b7713082fe5ebac1dd9a7");
        landscapes.add("https://preview.redd.it/s7rvxf2iysn41.jpg?width=640&crop=smart&auto=webp&s=dc565e5e7e958fe06d2e8b2a4a81ab9a3f68ef71");
        landscapes.add("https://preview.redd.it/dp0puez88nn41.jpg?width=640&crop=smart&auto=webp&s=e623ad56fe26f8c07be619a92801f2f1d3e9e3b9");
        landscapes.add("https://preview.redd.it/rrp9g2d9ion41.jpg?width=640&crop=smart&auto=webp&s=98b5c73fa465f11ca38c5764a2913790129a0a3d");
        landscapes.add("https://preview.redd.it/etg5khuj7nn41.jpg?width=640&crop=smart&auto=webp&s=03cbe6c50053adfa0aed835160824e169795a55b");
        landscapes.add("https://preview.redd.it/iedw74lmvmn41.jpg?width=640&crop=smart&auto=webp&s=c997b05eb8546997de016945c3a7554f6e3d4e6e");
        landscapes.add("https://preview.redd.it/byvn226ussn41.jpg?width=640&crop=smart&auto=webp&s=146babb4751e8c5014634124e5ead467e0df3710");
        landscapes.add("https://external-preview.redd.it/m4sxlFwtHxjlon8U0TewCEWFjJNLIQtcAOLEgiI0-zk.jpg?width=640&crop=smart&auto=webp&s=b3c7d596f77e708ad826141f1425e233b505ca56");
        landscapes.add("https://preview.redd.it/2sr4btdqssn41.jpg?width=640&crop=smart&auto=webp&s=9939aafc21601a0c13fb14268d0458393c4a816a");
    }

    @Override
    public HomeViewModel.OuterItemLiveData loadRecentlyPlayed() {
        return loadDummy("Recently played");
    }

    @Override
    public HomeViewModel.OuterItemLiveData loadCategory(int position) {
        return loadDummy("Category " + position);
    }

    private HomeViewModel.OuterItemLiveData loadDummy(String dummyTitle) {
        Integer recentlyPlayedIcon = R.drawable.ic_history2;
        Integer categoryIcon = R.drawable.ic_category;

        MutableLiveData<Integer> icon = new MutableLiveData<>();

        if (dummyTitle.equals( "Recently played"))
            icon.setValue(recentlyPlayedIcon);
        else icon.setValue(categoryIcon);

        MutableLiveData<String> title = new MutableLiveData<>(dummyTitle);
        ArrayList<HomeViewModel.InnerItemLiveData> innerItems = new ArrayList<>();

        for (int i = 0; i < innerItems.size(); i++) {
            innerItems.set(i, new HomeViewModel.InnerItemLiveData());

            Random random = new Random();
            innerItems.get(i).getImage().setValue(landscapes.get(random.nextInt(landscapes.size())));
            innerItems.get(i).getTitle().setValue("Title " + i);
            innerItems.get(i).getSubTitle().setValue("Sub title " + i);
        }

        return new HomeViewModel.OuterItemLiveData(icon, title, new MutableLiveData<>(innerItems));
    }

}
