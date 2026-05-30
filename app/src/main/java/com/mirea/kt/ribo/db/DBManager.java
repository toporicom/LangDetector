package com.mirea.kt.ribo.db;

import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_DEFINITION;
import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_EXAMPLE;
import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_FAVORITE;
import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_ID;
import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_SPEECH;
import static com.mirea.kt.ribo.db.MySQLiteHelper.COLUMN_WORD;
import static com.mirea.kt.ribo.db.MySQLiteHelper.TABLE_MEANING;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mirea.kt.ribo.model.Meaning;

import java.util.ArrayList;

public class DBManager {
    private final SQLiteOpenHelper sqLiteOpenHelper;

    public DBManager(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public boolean isDBContainDefinition(String definition){
        Log.i("DB", "search in db: " + definition);
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEANING, new String[]{COLUMN_DEFINITION},null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String definitionCursor = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_DEFINITION)
                );
                if (definition.equals(definitionCursor)) {
                    db.close();
                    cursor.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return false;
    }
    public boolean isFavorite(String word) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MEANING,
                new String[]{COLUMN_FAVORITE},
                COLUMN_WORD + " = ?",
                new String[]{word},
                null,
                null,
                null,
                "1"
        );

        boolean result = false;

        if (cursor.moveToFirst()) {
            int favorite = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)
            );

            result = favorite == 1;
        }

        cursor.close();
        db.close();

        return result;
    }
    public void save(Meaning meaning) {
        Log.i("DB", "save to db");
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_WORD, meaning.getWord());
        values.put(COLUMN_SPEECH, meaning.getPartOfSpeech());
        values.put(COLUMN_DEFINITION, meaning.getDefinition());
        values.put(COLUMN_EXAMPLE, meaning.getExample());
        values.put(COLUMN_FAVORITE, meaning.getFavorite());

        db.insert(TABLE_MEANING, null, values);

        db.close();
    }
    public int toggleFavorite(String word) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_MEANING,
                new String[]{COLUMN_FAVORITE},
                COLUMN_WORD + " = ?",
                new String[]{word},
                null,
                null,
                null,
                "1"
        );

        int newFavorite = 1;

        if (cursor.moveToFirst()) {
            int currentFavorite = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)
            );

            newFavorite = currentFavorite == 1 ? 0 : 1;
        }

        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE, newFavorite);

        db.update(
                TABLE_MEANING,
                values,
                COLUMN_WORD + " = ?",
                new String[]{word}
        );

        db.close();

        return newFavorite;
    }
    public ArrayList<Meaning> getAll() {
        Log.i("DB", "get all from db");

        ArrayList<Meaning> meanings = new ArrayList<>();

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MEANING,
                new String[]{
                        COLUMN_ID,
                        COLUMN_WORD,
                        COLUMN_SPEECH,
                        COLUMN_DEFINITION,
                        COLUMN_EXAMPLE,
                        COLUMN_FAVORITE
                },
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String word = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_WORD)
                );

                String speech = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_SPEECH)
                );

                String definition = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_DEFINITION)
                );

                String example = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXAMPLE)
                );

                int favorite = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)
                );

                Meaning meaning = new Meaning(
                        id,
                        word,
                        speech,
                        definition,
                        example,
                        favorite
                );

                meanings.add(meaning);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return meanings;
    }
    public ArrayList<Meaning> getFavorites() {
        Log.i("DB", "get favorites from db");

        ArrayList<Meaning> meanings = new ArrayList<>();

        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_MEANING,
                new String[]{
                        COLUMN_ID,
                        COLUMN_WORD,
                        COLUMN_SPEECH,
                        COLUMN_DEFINITION,
                        COLUMN_EXAMPLE,
                        COLUMN_FAVORITE
                },
                COLUMN_FAVORITE + " = ?",
                new String[]{"1"},
                null,
                null,
                COLUMN_WORD + " ASC"
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String word = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_WORD)
                );

                String speech = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_SPEECH)
                );

                String definition = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_DEFINITION)
                );

                String example = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXAMPLE)
                );

                int favorite = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)
                );

                meanings.add(new Meaning(
                        id,
                        word,
                        speech,
                        definition,
                        example,
                        favorite
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return meanings;
    }
    public void updateMeaning(int id, String partOfSpeech, String definition, String example) {
        Log.i("DB", "update meaning");

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SPEECH, partOfSpeech);
        values.put(COLUMN_DEFINITION, definition);
        values.put(COLUMN_EXAMPLE, example);

        db.update(
                TABLE_MEANING,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
    }
    public void addCustomMeaning(String word, String partOfSpeech, String definition, String example) {
        Log.i("DB", "add custom meaning");

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_WORD, word);
        values.put(COLUMN_SPEECH, partOfSpeech);
        values.put(COLUMN_DEFINITION, definition);
        values.put(COLUMN_EXAMPLE, example);
        values.put(COLUMN_FAVORITE, 1);

        db.insert(TABLE_MEANING, null, values);

        db.close();
    }
}