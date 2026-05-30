package com.mirea.kt.ribo.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.fragment.FavoriteFragment;
import com.mirea.kt.ribo.fragment.HistoryFragment;
import com.mirea.kt.ribo.fragment.LangDetectorFragment;

public class HolderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        LangDetectorFragment langDetectorFragment = new LangDetectorFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        HistoryFragment historyFragment = new HistoryFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, langDetectorFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, langDetectorFragment).commit();
                return true;
            }
            if (id == R.id.favorite) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, favoriteFragment).commit();
                return true;
            }
            if (id == R.id.history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, historyFragment).commit();
                return true;
            }
            return false;
        });
    }
}
