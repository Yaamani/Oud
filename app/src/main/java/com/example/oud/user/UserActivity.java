package com.example.oud.user;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OfflineFragment;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.user.fragments.home.HomeFragment;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener, PlaylistFragmentOpeningListener {

    private static final String TAG = UserActivity.class.getSimpleName();

    public static final String HOME_FRAGMENT_TAG = "HOME";
    public static final String SEARCH_FRAGMENT_TAG = "SEARCH";
    public static final String LIBRARY_FRAGMENT_TAG = "LIBRARY";
    public static final String PREMIUM_FRAGMENT_TAG = "PREMIUM";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String OFFLINE_FRAGMENT_TAG = "OFFLINE";
    public static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST";

    private Toast mConnectionFailedToast;

    private boolean backButtonPressed = false;
    //private boolean navigationItemReselected = false;
    private Stack<Integer> bottomNavViewBackStack = new Stack<>(); // Menu Item Ids


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().hide();



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(item -> {
            if (backButtonPressed)
                return true;

            /*if (navigationItemReselected) {
                navigationItemReselected = false;
                return true;
            }*/

            return UserActivity.this.inflateFragmentBasedOnMenuItem(item.getItemId());
        });

        navView.setOnNavigationItemReselectedListener(item -> {
            Log.i(TAG, "onNavigationItemReselected: ");
            //navigationItemReselected = true;
            if (backButtonPressed) return;

            handleBottomNavViewItemReselected();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Log.i(TAG, "Back stack : " + "Changed.");
            handleBottomNavViewBackStack(navView);
        });



        FragmentTransaction homeTransaction = getSupportFragmentManager().beginTransaction();
        homeTransaction.replace(R.id.nav_host_fragment, new HomeFragment(), HOME_FRAGMENT_TAG);
        homeTransaction.addToBackStack(null);
        homeTransaction.commit();

        //navView.setSelectedItemId(R.id.navigation_search);

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

    private void handleBottomNavViewBackStack(BottomNavigationView navView) {
        if (backButtonPressed & !bottomNavViewBackStack.isEmpty()) { // pop & peak
            bottomNavViewBackStack.pop();
            int itemId = bottomNavViewBackStack.peek();
            navView.setSelectedItemId(itemId);
        } else  // push
            bottomNavViewBackStack.push(navView.getSelectedItemId());

        backButtonPressed = false;
    }

    private void handleBottomNavViewItemReselected() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (f instanceof HomeFragment) return;
        else if (f instanceof SearchFragment) return;
        else if (f instanceof LibraryFragment) return;
        else if (f instanceof PremiumFragment) return;
        else if (f instanceof SettingsFragment) return;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
    }

    private boolean inflateFragmentBasedOnMenuItem(int itemId) {
        //Fragment selected = null;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        switch (itemId) {
            case R.id.navigation_home:
                //selected = new HomeFragment();
                HomeFragment homeFragment = (HomeFragment) manager.findFragmentByTag(HOME_FRAGMENT_TAG);
                if (homeFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new HomeFragment(), HOME_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, homeFragment, HOME_FRAGMENT_TAG);

                break;
            case R.id.navigation_search:
                //selected = new SearchFragment();
                SearchFragment searchFragment = (SearchFragment) manager.findFragmentByTag(SEARCH_FRAGMENT_TAG);
                if (searchFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new SearchFragment(), SEARCH_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, searchFragment, SEARCH_FRAGMENT_TAG);

                break;
            case R.id.navigation_library:
                //selected = new LibraryFragment();
                LibraryFragment libraryFragment = (LibraryFragment) manager.findFragmentByTag(LIBRARY_FRAGMENT_TAG);
                if (libraryFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new LibraryFragment(), LIBRARY_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment,libraryFragment, LIBRARY_FRAGMENT_TAG);


                break;
            case R.id.navigation_premium:
                //selected = new PremiumFragment();
                PremiumFragment premiumFragment = (PremiumFragment) manager.findFragmentByTag(PREMIUM_FRAGMENT_TAG);
                if (premiumFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new PremiumFragment(), PREMIUM_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, premiumFragment, PREMIUM_FRAGMENT_TAG);


                break;
            case R.id.navigation_settings:
                //selected = new SettingsFragment();
                SettingsFragment settingsFragment = (SettingsFragment) manager.findFragmentByTag(SETTINGS_FRAGMENT_TAG);
                if (settingsFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new SettingsFragment(), SETTINGS_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, settingsFragment, SETTINGS_FRAGMENT_TAG);


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

        Log.i(TAG, "Back stack : " + "Back button pressed.");
        backButtonPressed = true;
        super.onBackPressed();

        /*BottomNavigationView navView = findViewById(R.id.nav_view);

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
            navView.getMenu().getItem(4).setChecked(true);*/

        //navView.setAlpha(0.5f);

        /*findViewById(R.id.nav_view).animate().alphaBy(.5f).setDuration(700).start();*/
        /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(-getResources().getDimension(R.dimen.offline_layout_size), 0).setDuration(700);
        valueAnimator.addUpdateListener(animation -> findViewById(R.id.fragment_offline).setY((Float) animation.getAnimatedValue()));
        valueAnimator.start();*/
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: ");
    }

    @Override
    public void onConnectionSuccess() {
        hideOfflineFragment();

        if (mConnectionFailedToast != null)
            mConnectionFailedToast.cancel();
    }

    @Override
    public void onConnectionFailure() {
        if (getSupportFragmentManager().findFragmentByTag(OFFLINE_FRAGMENT_TAG) == null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.replace(R.id.fragment_offline_container, new OfflineFragment(), OFFLINE_FRAGMENT_TAG);
            offlineFragmentTransaction.commitNow();
        }

        if (mConnectionFailedToast == null) {
            mConnectionFailedToast = Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT);
        }
        mConnectionFailedToast.show();

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

    @Override
    public void onOpeningPlaylistFragment(Constants.PlaylistFragmentType type, String id) {
        FragmentManager manager = getSupportFragmentManager();
        PlaylistFragment playlistFragment = (PlaylistFragment) manager.findFragmentByTag(PLAYLIST_FRAGMENT_TAG);
        if (playlistFragment == null)
            playlistFragment = PlaylistFragment.newInstance(type, id);


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, playlistFragment, PLAYLIST_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    /*public interface UserActivityCommunicationListener {
        void onReconnecting();
    }*/
}
