package com.example.belajarandroid11pplg2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {

    BottomNavigationView bottomnav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        bottomnav = findViewById(R.id.bottomnav);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framlayout,new HomeFragment())
                .commit()
        ;

        bottomnav.setOnItemSelectedListener(menuItem -> {
            Fragment fragment = null;

            if(menuItem.getItemId() == R.id.menuHome){
                fragment = new HomeFragment();
            } else if (menuItem.getItemId() == R.id.menuProfile) {
                fragment = new ProfileFragment();
            }else if (menuItem.getItemId() == R.id.menuUser) {
                fragment = new UserFragment();
            }

            if(fragment != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framlayout,fragment)
                        .commit();
            }


            return true;

        });
    }
}