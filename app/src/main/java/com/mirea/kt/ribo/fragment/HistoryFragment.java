package com.mirea.kt.ribo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.adapter.MeaningsAdapter;
import com.mirea.kt.ribo.db.DBManager;
import com.mirea.kt.ribo.db.MySQLiteHelper;
import com.mirea.kt.ribo.model.Meaning;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private DBManager dbManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        dbManager = new DBManager(new MySQLiteHelper(requireContext(), MySQLiteHelper.DATABASE_NAME, null, 1));

        RecyclerView recyclerView = view.findViewById(R.id.historyRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ArrayList<Meaning> history = dbManager.getAll();

        MeaningsAdapter adapter = new MeaningsAdapter(
                requireContext(),
                history,
                dbManager,
                "history",
                () -> {
                }
        );

        recyclerView.setAdapter(adapter);

        return view;
    }
}
