package com.example.nguyenduchai.joggerdemo.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nguyen Duc Hai on 5/13/2017.
 */

public class JoggerSQLiteOpenHelper extends SQLiteOpenHelper {

    // define information database
    public static final String DATABASE_NAME = "Jogger.db";
    public static final String TABLE_NAME_STEPS_SUMMARY_DAILY = "StepsSummaryDaily";
    public static final int DATABASE_VERSION = 1;

    // define table of steps summary daily
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STEPS_COUNT = "stepscount";
    public static final String COLUMN_CREATION_DATE = "creationdate";

    // string create table of steps summary daily
    public static final String CREATE_TABLE_STEPS_SUMMARY_DAILY = "CREATE TABLE " + TABLE_NAME_STEPS_SUMMARY_DAILY + "( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_STEPS_COUNT + "INTEGER, "
            + COLUMN_CREATION_DATE + "TEXT NOT NULL)";

    // function constructor database
    public JoggerSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STEPS_SUMMARY_DAILY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + TABLE_NAME_STEPS_SUMMARY_DAILY);
        onCreate(db);
    }
}
