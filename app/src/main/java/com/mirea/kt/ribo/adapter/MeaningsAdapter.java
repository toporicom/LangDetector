package com.mirea.kt.ribo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kt.ribo.R;
import com.mirea.kt.ribo.db.DBManager;
import com.mirea.kt.ribo.db.MySQLiteHelper;
import com.mirea.kt.ribo.model.Meaning;

import java.util.ArrayList;

public class MeaningsAdapter extends RecyclerView.Adapter<MeaningsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Meaning> meanings;
    private DBManager dbManager;
    private String className;
    private OnMeaningChangedListener listener;
    private LayoutInflater inflater;

    public MeaningsAdapter(
            Context context,
            ArrayList<Meaning> meanings,
            DBManager dbManager,
            String className,
            OnMeaningChangedListener listener
    ) {
        this.context = context;
        this.meanings = meanings;
        this.dbManager = dbManager;
        this.className = className;
        this.listener = listener;

        this.inflater = LayoutInflater.from(context);
    }
    private void shareMeaning(Meaning meaning) {
        String text = "Word: " + meaning.getWord() + "\n"
                + "Part of speech: " + meaning.getPartOfSpeech() + "\n"
                + "Definition: " + meaning.getDefinition() + "\n";

        if (meaning.getExample() != null && !meaning.getExample().isEmpty()) {
            text += "Example: " + meaning.getExample();
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        context.startActivity(
                Intent.createChooser(intent, "Поделиться через")
        );
    }

    private void openWiki(String word) {
        String url = "https://en.wikipedia.org/wiki/" + word.trim().replace(" ", "_");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
    private void showEditDialog(Meaning meaning) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        EditText speechInput = new EditText(context);
        speechInput.setHint("Part of speech");
        speechInput.setText(meaning.getPartOfSpeech());

        EditText definitionInput = new EditText(context);
        definitionInput.setHint("Definition");
        definitionInput.setText(meaning.getDefinition());

        EditText exampleInput = new EditText(context);
        exampleInput.setHint("Example");
        exampleInput.setText(meaning.getExample());

        layout.addView(speechInput);
        layout.addView(definitionInput);
        layout.addView(exampleInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Редактирование слова: " + meaning.getWord())
                .setView(layout)
                .setPositiveButton("Сохранить", null)
                .setNeutralButton("Добавить новое", null)
                .setNegativeButton("Отмена", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button addButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            saveButton.setOnClickListener(v -> {
                String speech = speechInput.getText().toString().trim();
                String definition = definitionInput.getText().toString().trim();
                String example = exampleInput.getText().toString().trim();

                if (speech.isEmpty() || definition.isEmpty()) {
                    Toast.makeText(context, "Заполни часть речи и определение", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbManager.updateMeaning(
                        meaning.getId(),
                        speech,
                        definition,
                        example
                );

                meaning.setPartOfSpeech(speech);
                meaning.setDefinition(definition);
                meaning.setExample(example);

                notifyDataSetChanged();

                if (listener != null) {
                    listener.onChanged();
                }

                dialog.dismiss();
            });

            addButton.setOnClickListener(v -> {
                String speech = speechInput.getText().toString().trim();
                String definition = definitionInput.getText().toString().trim();
                String example = exampleInput.getText().toString().trim();

                if (speech.isEmpty() || definition.isEmpty()) {
                    Toast.makeText(context, "Заполни часть речи и определение", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbManager.addCustomMeaning(
                        meaning.getWord(),
                        speech,
                        definition,
                        example
                );

                if (listener != null) {
                    listener.onChanged();
                }

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    @NonNull
    @Override
    public MeaningsAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = inflater.inflate(R.layout.meaning_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeaningsAdapter.ViewHolder holder, int position) {
        Meaning meaning = meanings.get(position);

        holder.partOfSpeech.setText(meaning.getPartOfSpeech());
        holder.definition.setText(meaning.getDefinition());

        if (meaning.getExample() == null || meaning.getExample().isEmpty()) {
            holder.example.setVisibility(View.GONE);
        } else {
            holder.example.setVisibility(View.VISIBLE);
            holder.example.setText(meaning.getExample());
        }

        holder.word.setVisibility(View.GONE);
        holder.favorite.setVisibility(View.GONE);
        holder.favorite_filled.setVisibility(View.GONE);
        holder.edit.setVisibility(View.GONE);
        holder.share.setVisibility(View.GONE);
        holder.wiki.setVisibility(View.GONE);

        holder.favorite.setOnClickListener(null);
        holder.favorite_filled.setOnClickListener(null);
        holder.edit.setOnClickListener(null);
        holder.share.setOnClickListener(null);
        holder.wiki.setOnClickListener(null);

        String screen = className == null ? "" : className.trim();

        boolean isHome = "home".equals(screen);
        boolean isFavoriteScreen = "favorite".equals(screen);
        boolean isHistory = "history".equals(screen);

        boolean isFirstMeaningOfWord;

        if (position == 0) {
            isFirstMeaningOfWord = true;
        } else {
            Meaning previousMeaning = meanings.get(position - 1);
            isFirstMeaningOfWord = !meaning.getWord().equals(previousMeaning.getWord());
        }

        if (isFirstMeaningOfWord) {
            holder.word.setVisibility(View.VISIBLE);
            holder.word.setText("Слово: " + meaning.getWord());

            if (isHome || isFavoriteScreen) {
                boolean isFavorite = dbManager.isFavorite(meaning.getWord());

                if (isFavorite) {
                    holder.favorite.setVisibility(View.GONE);
                    holder.favorite_filled.setVisibility(View.VISIBLE);
                } else {
                    holder.favorite.setVisibility(View.VISIBLE);
                    holder.favorite_filled.setVisibility(View.GONE);
                }
            }

            if (!isHistory) {
                holder.share.setVisibility(View.VISIBLE);

                holder.wiki.setText("Открыть статью в Wikipedia");
                holder.wiki.setVisibility(View.VISIBLE);
                holder.wiki.setOnClickListener(v -> {
                    openWiki(meaning.getWord());
                });
            }
        }

        if (isFavoriteScreen) {
            holder.edit.setVisibility(View.VISIBLE);
        }

        if (isHistory) {
            holder.favorite.setVisibility(View.GONE);
            holder.favorite_filled.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.share.setVisibility(View.GONE);
            holder.wiki.setVisibility(View.GONE);
        }

        View.OnClickListener favoriteClickListener = v -> {
            int newFavorite = dbManager.toggleFavorite(meaning.getWord());

            for (Meaning item : meanings) {
                if (item.getWord().equals(meaning.getWord())) {
                    item.setFavorite(newFavorite);
                }
            }

            if (listener != null) {
                listener.onChanged();
            }

            notifyDataSetChanged();
        };

        holder.favorite.setOnClickListener(favoriteClickListener);
        holder.favorite_filled.setOnClickListener(favoriteClickListener);

        holder.share.setOnClickListener(v -> {
            shareMeaning(meaning);
        });

        holder.edit.setOnClickListener(v -> {
            showEditDialog(meaning);
        });

        if (!dbManager.isDBContainDefinition(meaning.getDefinition())) {
            dbManager.save(meaning);
        }
    }
    @Override
    public int getItemCount() {
        return meanings.size();
    }

    public interface OnMeaningChangedListener {
        void onChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView word, partOfSpeech, definition, example, wiki;
        ImageButton favorite, favorite_filled, edit, share;
        ViewHolder(View view) {
            super(view);
            word = itemView.findViewById(R.id.word);
            partOfSpeech = itemView.findViewById(R.id.part_of_speech);
            definition = itemView.findViewById(R.id.definition);
            example = itemView.findViewById(R.id.example);

            favorite = itemView.findViewById(R.id.favorite);
            favorite_filled = itemView.findViewById(R.id.favorite_filled);
            edit = itemView.findViewById(R.id.edit);
            share = itemView.findViewById(R.id.share);
            wiki = itemView.findViewById(R.id.wiki);
        }
    }

}
