package com.mirea.kt.ribo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.db.DBManager;
import com.mirea.kt.ribo.db.MySQLiteHelper;
import com.mirea.kt.ribo.runnable.HTTPLangDetector;
import com.mirea.kt.ribo.model.Meaning;
import com.mirea.kt.ribo.adapter.MeaningsAdapter;
import com.mirea.kt.ribo.R;

import java.util.ArrayList;

public class LangDetectorFragment extends Fragment {
    private DBManager dbManager;
    private ArrayList<Meaning> meanings;
    private ArrayList<Meaning> getMeanings(String word) {

        String address = "https://api.dictionaryapi.dev/api/v2/entries/en/";
        HTTPLangDetector httpLangDetector = new HTTPLangDetector(address, word);
        Thread th = new Thread(httpLangDetector);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return httpLangDetector.getResponseBody();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_langdetector, container, false);
        dbManager = new DBManager(new MySQLiteHelper(inflater.getContext(), MySQLiteHelper.DATABASE_NAME, null, 1));

        EditText inputText = view.findViewById(R.id.input_text);
        Button button = view.findViewById(R.id.detect_text_btn);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        button.setOnClickListener(v -> {
            meanings = getMeanings(inputText.getText().toString());
            MeaningsAdapter adapter = new MeaningsAdapter(
                    requireContext(),
                    meanings,
                    dbManager,
                    "home",
                    () -> {
                    }
            );
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(adapter);
            for (Meaning meaning : meanings) {
                if (!dbManager.isDBContainDefinition(meaning.getDefinition())) {
                    dbManager.save(meaning);
                }
            }
        });


        return view;
    }
}
