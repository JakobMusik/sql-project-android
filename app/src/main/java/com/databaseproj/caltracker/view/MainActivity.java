package com.databaseproj.caltracker.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.databaseproj.caltracker.R;
import com.databaseproj.caltracker.controller.ProductsDatabaseManager;
import com.databaseproj.caltracker.controller.SettingsManager;
import com.databaseproj.caltracker.fragments.DashboardFragment;
import com.databaseproj.caltracker.fragments.FoodFragment;
import com.databaseproj.caltracker.fragments.ProfileFragment;
import com.databaseproj.caltracker.fragments.SearchFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity  {

    SharedPreferences prefs = null;

    private ProductsDatabaseManager manager;

    private SettingsManager settingsManager;

    static ChipNavigationBar bottomNavBar;
    Fragment fragment;
    FragmentManager fragmentManager;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.dashboard);

        manager = new ProductsDatabaseManager(this);
        manager.open();

        prefs = getSharedPreferences("useractivity", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).apply();
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        } else {


        }

        settingsManager = SettingsManager.getInstance(this);

        bottomNavBar = (ChipNavigationBar) findViewById(R.id.bottomNavBar);


        fragmentManager = getSupportFragmentManager();

        fragment = new DashboardFragment();
        bottomNavBar.setItemSelected(R.id.one, true);
        fragmentManager.beginTransaction()
                .replace(R.id.coordinatorLayout, fragment)
                .commit();


        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {

                fragment = null;

                switch (id) {

                    case R.id.one:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.two:
                        fragment = new FoodFragment();
                        break;
                    case R.id.three:
                        fragment = new SearchFragment();
                        break;
                    case R.id.four:
                        fragment = new ProfileFragment();

                    default:
                        break;

                }

                if (fragment != null) {

                    fragmentManager.beginTransaction()
                            .replace(R.id.coordinatorLayout, fragment)
                            .commit();

                } else {

                    Log.i("ERROR", "IN FRAGMENT LOOK HERE!!!!!!!!!!!!!!!");

                }

            }
        });






    }





}
