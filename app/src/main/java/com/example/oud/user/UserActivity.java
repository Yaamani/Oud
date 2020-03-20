package com.example.oud.user;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.oud.R;
import com.example.oud.user.fragments.home.HomeFragment;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = UserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Fragment selected = null;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //selected = new HomeFragment();
                        transaction.replace(R.id.nav_host_fragment, HomeFragment.class, null, "HOME");

                        break;
                    case R.id.navigation_search:
                        //selected = new SearchFragment();
                        transaction.replace(R.id.nav_host_fragment, SearchFragment.class, null, "SEARCH");

                        break;
                    case R.id.navigation_library:
                        //selected = new LibraryFragment();
                        transaction.replace(R.id.nav_host_fragment, LibraryFragment.class, null, "LIBRARY");

                        break;
                    case R.id.navigation_premium:
                        //selected = new PremiumFragment();
                        transaction.replace(R.id.nav_host_fragment, PremiumFragment.class, null, "PREMIUM");

                        break;
                    case R.id.navigation_settings:
                        //selected = new SettingsFragment();
                        transaction.replace(R.id.nav_host_fragment, SettingsFragment.class, null, "SETTINGS");

                        break;
                }

                //transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();

                return true;
            }
        });

        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Log.i(TAG, "onNavigationItemReselected: ");
            }
        });

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
    }
}
