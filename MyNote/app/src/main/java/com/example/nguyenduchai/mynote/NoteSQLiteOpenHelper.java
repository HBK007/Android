package com.example.nguyenduchai.mynote;

/**
 * Created by Nguyen Duc Hai on 4/9/2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nguyen Duc Hai on 4/8/2017.
 */

public class NoteSQLiteOpenHelper extends SQLiteOpenHelper{
    // define name of database
    public static final String DATABASE_NAME = "mynotes.db";
    // define column of table name notes
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE_CONTENT = "content";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final int DATABASE_VERSION = 1;

    // define string create table
    private static final String CREATE_TABLE_NOTE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_NOTE_CONTENT + " TEXT NOT NULL, "
            + COLUMN_DATE_TIME + " TEXT)";

    public NoteSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST" + TABLE_NAME);
        onCreate(db);
    }
}

