package com.mirea.kt.ribo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "meanings.db";

    public static final String TABLE_MEANING = "meaning";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_WORD = "word";

    public static final String COLUMN_SPEECH = "speech";
    public static final String COLUMN_DEFINITION = "definition";
    public static final String COLUMN_EXAMPLE = "example";
    public static final String COLUMN_FAVORITE = "favorite";

    public MySQLiteHelper(@Nullable Context context,
                            @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MEANING_TABLE = "CREATE TABLE " + TABLE_MEANING + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WORD + " TEXT,"
                + COLUMN_SPEECH + " TEXT,"
                + COLUMN_DEFINITION + " TEXT,"
                + COLUMN_EXAMPLE + " TEXT,"
                + COLUMN_FAVORITE + " INT"
                + ")";
        sqLiteDatabase.execSQL(CREATE_MEANING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
