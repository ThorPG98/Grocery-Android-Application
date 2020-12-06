package com.example.bacon.retailsystem.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bacon.retailsystem.Fragment.CartFragment;
import com.example.bacon.retailsystem.Fragment.HomeFragment;
import com.example.bacon.retailsystem.Fragment.ProfileFragment;
import com.example.bacon.retailsystem.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomePageNav extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_nav);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new CartFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }
            };

    public void setNavigationVisibility(boolean visible) {
        if (bottomNav.isShown() && !visible) {
            bottomNav.setVisibility(View.GONE);
        }
        else if (!bottomNav.isShown() && visible){
            bottomNav.setVisibility(View.VISIBLE);
        }
    }

    public void setBottomNavHome() {
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}

