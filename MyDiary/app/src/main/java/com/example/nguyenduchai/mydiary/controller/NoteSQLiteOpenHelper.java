package com.example.nguyenduchai.mydiary.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nguyen Duc Hai on 4/11/2017.
 */

public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {

    // database information
    public static final String DATABASE_NAME = "mynotes.db";
    public static final String TABLE_NAME = "mydiary";
    public static final int DATABASE_VERSION = 1;

    // table information
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_IMAGE = "image";

    // create table information
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_CONTENT + " text not null, "
            + COLUMN_DATETIME + " text not null, "
            + COLUMN_IMAGE + " text)";


    public NoteSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME);
        onCreate(db);
    }
}
