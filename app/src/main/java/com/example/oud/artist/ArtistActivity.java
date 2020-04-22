package com.example.oud.artist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OfflineFragment;
import com.example.oud.OptionsFragment;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.artist.fragments.albums.MyAlbumsFragment;
import com.example.oud.artist.fragments.bio.BioFragment;
import com.example.oud.artist.fragments.home.ArtistHomeFragment;
import com.example.oud.artist.fragments.settings.ArtistSettingsFragment;
import com.example.oud.user.UserActivity;
import com.example.oud.user.fragments.home.HomeFragment2;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Stack;

public class ArtistActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener {
    private BottomNavigationView bottomNavigationView;

    private boolean backButtonPressed = false;
    private Toast mConnectionFailedToast;


    private Stack<Integer> bottomNavViewBackStack = new Stack<>(); // Menu Item Ids
    private static final String TAG = ArtistActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        getSupportActionBar().hide();
        initializeViews();



        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (OptionsFragment.doesOptionsFragmentExist(this, R.id.container_options)) {
                OptionsFragment.hideOptionsFragment(this, R.id.container_options);
            }

            if (backButtonPressed)
                return true;

            return ArtistActivity.this.inflateFragmentBasedOnMenuItem(item.getItemId());
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Log.i(TAG, "onNavigationItemReselected: ");
            //navigationItemReselected = true;

            if (OptionsFragment.doesOptionsFragmentExist(this, R.id.container_options)) {
                OptionsFragment.hideOptionsFragment(this, R.id.container_options);
            }

            if (backButtonPressed) return;

            handleBottomNavViewItemReselected();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Log.i(TAG, "Back stack : " + "Changed.");
            handleBottomNavViewBackStack(bottomNavigationView);
        });

        FragmentTransaction homeTransaction = getSupportFragmentManager().beginTransaction();
        homeTransaction.replace(R.id.nav_host_fragment_artist,new ArtistHomeFragment(), Constants.ARTIST_HOME_FRAGMENT_TAG);
        bottomNavViewBackStack.push(R.id.navigation_home);
        homeTransaction.commit();



    }

    private void initializeViews(){
        bottomNavigationView = findViewById(R.id.bottom_navigation_artist);
    }


    private boolean inflateFragmentBasedOnMenuItem(int itemId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();



        switch (itemId) {
            case R.id.navigation_artist_home:
                ArtistHomeFragment homeFragment = (ArtistHomeFragment) manager.findFragmentByTag(Constants.ARTIST_HOME_FRAGMENT_TAG);
                if (homeFragment == null)
                    transaction.replace(R.id.nav_host_fragment_artist, ArtistHomeFragment.newInstance(), Constants.ARTIST_HOME_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment_artist, homeFragment, Constants.ARTIST_HOME_FRAGMENT_TAG);
                break;

            case R.id.navigation_artist_albums:
                MyAlbumsFragment myAlbumsFragment = (MyAlbumsFragment) manager.findFragmentByTag(Constants.MY_ALBUMS_FRAGMENT_TAG);
                if (myAlbumsFragment == null)
                    transaction.replace(R.id.nav_host_fragment_artist, new MyAlbumsFragment(this), Constants.MY_ALBUMS_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment_artist, myAlbumsFragment, Constants.MY_ALBUMS_FRAGMENT_TAG);


                break;
            case R.id.navigation_artist_info:

                BioFragment bioFragment = (BioFragment) manager.findFragmentByTag(Constants.BIO_FRAGMENT_TAG);
                if (bioFragment == null)
                    transaction.replace(R.id.nav_host_fragment_artist, new BioFragment(), Constants.BIO_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment_artist, bioFragment, Constants.BIO_FRAGMENT_TAG);
                break;

            case R.id.navigation_artist_settings:
                 ArtistSettingsFragment artistSettingsFragment = (ArtistSettingsFragment) manager.findFragmentByTag(Constants.ARTIST_SETTINGS_FRAGMENT_TAG);
                if (artistSettingsFragment == null)
                    transaction.replace(R.id.nav_host_fragment_artist, new ArtistSettingsFragment(), Constants.ARTIST_SETTINGS_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment_artist, artistSettingsFragment, Constants.SETTINGS_FRAGMENT_TAG);
                break;
        }


        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        return true;
    }

    private void handleBottomNavViewBackStack(BottomNavigationView navView) {
        if (backButtonPressed & !bottomNavViewBackStack.isEmpty()) { // pop & peak

            bottomNavViewBackStack.pop();

            if (!bottomNavViewBackStack.isEmpty()) {
                int itemId = bottomNavViewBackStack.peek();
                navView.setSelectedItemId(itemId);
            }

        } else  // push
            bottomNavViewBackStack.push(navView.getSelectedItemId());

        backButtonPressed = false;
    }
    private void handleBottomNavViewItemReselected() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (f instanceof HomeFragment2) return;
        else if (f instanceof SearchFragment) return;
        else if (f instanceof LibraryFragment) return;
        else if (f instanceof PremiumFragment) return;
        else if (f instanceof SettingsFragment) return;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
    }


    @Override
    public void onConnectionSuccess() {
        hideOfflineFragment();

        if (mConnectionFailedToast != null)
            mConnectionFailedToast.cancel();
    }

    @Override
    public void onConnectionFailure() {
        if (getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FRAGMENT_TAG) == null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.replace(R.id.fragment_offline_container_artist, new OfflineFragment(), Constants.OFFLINE_FRAGMENT_TAG);
            offlineFragmentTransaction.commit();
        }

        if (mConnectionFailedToast == null) {
            mConnectionFailedToast = Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT);
        }
        mConnectionFailedToast.show();

    }

    @Override
    public void onTryingToReconnect() {

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof ReconnectingListener) {
                ((ReconnectingListener) fragment).onTryingToReconnect();
                Log.i(TAG, "onTryingToReconnect: " + fragment);
                //break;
            }
        }


    }


    private void hideOfflineFragment() {
        OfflineFragment offlineFragment = (OfflineFragment) getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FRAGMENT_TAG);
        if (offlineFragment != null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.remove(offlineFragment);
            offlineFragmentTransaction.commit();
        }
    }




}
