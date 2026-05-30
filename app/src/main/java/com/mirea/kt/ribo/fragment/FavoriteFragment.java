package com.mirea.kt.ribo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.model.Meaning;
import com.mirea.kt.ribo.adapter.MeaningsAdapter;
import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.db.DBManager;
import com.mirea.kt.ribo.db.MySQLiteHelper;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private DBManager dbManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        dbManager = new DBManager(new MySQLiteHelper(requireContext(), MySQLiteHelper.DATABASE_NAME, null, 1));

        ArrayList<Meaning> favorites = dbManager.getFavorites();
        RecyclerView recyclerView = view.findViewById(R.id.favoriteRV);

        MeaningsAdapter adapter = new MeaningsAdapter(
                requireContext(),
                favorites,
                dbManager,
                "favorite",
                () -> {
                    favorites.clear();
                    favorites.addAll(dbManager.getFavorites());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
        );

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }
}
