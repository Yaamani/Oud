package com.example.oud.user;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.OfflineFragment;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.user.fragments.home.HomeFragment;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class UserActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener {

    private static final String TAG = UserActivity.class.getSimpleName();

    public static final String HOME_FRAGMENT_TAG = "HOME";
    public static final String SEARCH_FRAGMENT_TAG = "SEARCH";
    public static final String LIBRARY_FRAGMENT_TAG = "LIBRARY";
    public static final String PREMIUM_FRAGMENT_TAG = "PREMIUM";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String OFFLINE_FRAGMENT_TAG = "OFFLINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().hide();



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(item -> inflateFragmentBasedOnMenuItem(item.getItemId()));

        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Log.i(TAG, "onNavigationItemReselected: ");
            }
        });


        /*FragmentContainerView container = findViewById(R.id.nav_host_fragment);
        Log.i(TAG, "onCreate: " + getSupportFragmentManager().findFragmentByTag("Offline"));
        FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
        //FragmentContainerView offlineFragmentContainer = findViewById(R.id.fragment_offline_container);
        offlineFragmentTransaction.replace(R.id.fragment_offline_container, new OfflineFragment(), "Offline");
        offlineFragmentTransaction.commit();
        Log.i(TAG, "onCreate: " + getSupportFragmentManager().findFragmentByTag("Offline"));*/

        /*FragmentContainerView container = findViewById(R.id.nav_host_fragment);

        //ObjectAnimator.ofInt(container, View.MA);


        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)(container.getLayoutParams());
        ValueAnimator marginAnimation = ValueAnimator.ofInt(params.topMargin, 150).setDuration(300);
        marginAnimation.addUpdateListener(animation -> params.setMargins(0, (int) animation.getAnimatedValue(), 0, 0));

        Button animateBtn = findViewById(R.id.btn_animate_test);
        animateBtn.setOnClickListener(v -> {
            marginAnimation.start();
            container.setLayoutParams(params);
        });*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator.ofFloat(findViewById(R.id.nav_view), View.ALPHA, 0, 1)
                    .setDuration(700)
                    .start();
        }*/



        /*final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_library,
                R.id.navigation_premium,
                R.id.navigation_settings).build();*/



        //appBarConfiguration.getDrawerLayout().

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /*NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/
    }

    private boolean inflateFragmentBasedOnMenuItem(int itemId) {
        //Fragment selected = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        switch (itemId) {
            case R.id.navigation_home:
                //selected = new HomeFragment();
                transaction.replace(R.id.nav_host_fragment, HomeFragment.class, null, HOME_FRAGMENT_TAG);

                break;
            case R.id.navigation_search:
                //selected = new SearchFragment();
                transaction.replace(R.id.nav_host_fragment, SearchFragment.class, null, SEARCH_FRAGMENT_TAG);

                break;
            case R.id.navigation_library:
                //selected = new LibraryFragment();
                transaction.replace(R.id.nav_host_fragment, LibraryFragment.class, null, LIBRARY_FRAGMENT_TAG);

                break;
            case R.id.navigation_premium:
                //selected = new PremiumFragment();
                transaction.replace(R.id.nav_host_fragment, PremiumFragment.class, null, PREMIUM_FRAGMENT_TAG);

                break;
            case R.id.navigation_settings:
                //selected = new SettingsFragment();
                transaction.replace(R.id.nav_host_fragment, SettingsFragment.class, null, SETTINGS_FRAGMENT_TAG);

                break;
        }

        //transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (f instanceof HomeFragment)
            navView.getMenu().getItem(0).setChecked(true);
        else if (f instanceof SearchFragment)
            navView.getMenu().getItem(1).setChecked(true);
        else if (f instanceof LibraryFragment)
            navView.getMenu().getItem(2).setChecked(true);
        else if (f instanceof PremiumFragment)
            navView.getMenu().getItem(3).setChecked(true);
        else if (f instanceof SettingsFragment)
            navView.getMenu().getItem(4).setChecked(true);

        //navView.setAlpha(0.5f);

        /*findViewById(R.id.nav_view).animate().alphaBy(.5f).setDuration(700).start();*/
        /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(-getResources().getDimension(R.dimen.offline_layout_size), 0).setDuration(700);
        valueAnimator.addUpdateListener(animation -> findViewById(R.id.fragment_offline).setY((Float) animation.getAnimatedValue()));
        valueAnimator.start();*/
    }

    @Override
    public void onConnectionSuccess() {
        hideOfflineFragment();
    }

    @Override
    public void onConnectionFailure() {
        if (getSupportFragmentManager().findFragmentByTag(OFFLINE_FRAGMENT_TAG) == null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.replace(R.id.fragment_offline_container, new OfflineFragment(), OFFLINE_FRAGMENT_TAG);
            offlineFragmentTransaction.commitNow();
        }

    }

    @Override
    public void onTryingToReconnect() {

        //hideOfflineFragment();

        Fragment fragment = null;

        String[] fragmentTags = {HOME_FRAGMENT_TAG,
                LIBRARY_FRAGMENT_TAG,
                SEARCH_FRAGMENT_TAG,
                PREMIUM_FRAGMENT_TAG,
                SETTINGS_FRAGMENT_TAG};

        for (String tag : fragmentTags) {
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null) break;
        }

        if (fragment != null) {


            if (fragment instanceof ReconnectingListener) {
                ((ReconnectingListener) fragment).onTryingToReconnect();
            } else {
                throw new RuntimeException("onRetryToConnect: " + fragment.toString()
                        + " must implement " + ReconnectingListener.class.getSimpleName());
            }


        } else {
            BottomNavigationView navView = findViewById(R.id.nav_view);

            inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
        }

    }

    private void hideOfflineFragment() {
        OfflineFragment offlineFragment = (OfflineFragment) getSupportFragmentManager().findFragmentByTag(OFFLINE_FRAGMENT_TAG);
        if (offlineFragment != null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.remove(offlineFragment);
            offlineFragmentTransaction.commitNow();
        }
    }

    /*public interface UserActivityCommunicationListener {
        void onReconnecting();
    }*/
}
