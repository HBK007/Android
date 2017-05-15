package com.example.nguyenduchai.joggerdemo.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nguyenduchai.joggerdemo.model.JoggerStepModel;
import com.example.nguyenduchai.joggerdemo.utility.Util;

import java.util.ArrayList;

/**
 * Created by Nguyen Duc Hai on 5/13/2017.
 */

public class JoggerDataStepSummaryDaily {
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private Context context;

    public JoggerDataStepSummaryDaily(Context context) {
        this.context = context;
        sqLiteOpenHelper = new JoggerSQLiteOpenHelper(context);
    }

    // open database
    public void open() throws SQLiteException {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    // close database
    public void close() throws SQLiteException {
        sqLiteDatabase.close();
    }

    // create steps entry daily
    public boolean createStepEntry() {
        boolean isDataAlreadyPresent = false;
        boolean createEntrySuccessful = false;
        int currentDateStepCounts = 0;

        // get date is today
        String todayDate = Util.getCurrentDateTime();

        // statement query steps counts present in today
        String selectQuery = "SELECT " + JoggerSQLiteOpenHelper.COLUMN_STEPS_COUNT + " FROM " + JoggerSQLiteOpenHelper.TABLE_NAME_STEPS_SUMMARY_DAILY +
                " WHERE " + JoggerSQLiteOpenHelper.COLUMN_CREATION_DATE + " = '" + todayDate + "'";
        try {
            sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    isDataAlreadyPresent = true;
                    currentDateStepCounts = cursor.getInt(cursor.getColumnIndex(JoggerSQLiteOpenHelper.COLUMN_STEPS_COUNT));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if data of today exits that update, if data of today not exits that insert date today
        try {
            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(JoggerSQLiteOpenHelper.COLUMN_CREATION_DATE, todayDate);
            if (isDataAlreadyPresent) {
                values.put(JoggerSQLiteOpenHelper.COLUMN_STEPS_COUNT, ++currentDateStepCounts);
                int row = sqLiteDatabase.update(JoggerSQLiteOpenHelper.TABLE_NAME_STEPS_SUMMARY_DAILY, values,
                        JoggerSQLiteOpenHelper.COLUMN_CREATION_DATE + " = '" + todayDate + "'", null);
                if(row == 1){
                    createEntrySuccessful = true;
                }
                sqLiteDatabase.close();
            }else{
                values.put(JoggerSQLiteOpenHelper.COLUMN_STEPS_COUNT, 1);
                long row = sqLiteDatabase.insert(JoggerSQLiteOpenHelper.TABLE_NAME_STEPS_SUMMARY_DAILY, null, values);
                if(row != -1){
                    createEntrySuccessful = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createEntrySuccessful;
    }

    // read data form entry
    public ArrayList<JoggerStepModel> readStepEntries(){
        ArrayList<JoggerStepModel> arrStepCount = new ArrayList<>();
        String strQuery = "SELECT * FROM " + JoggerSQLiteOpenHelper.TABLE_NAME_STEPS_SUMMARY_DAILY;
        try{
            sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(strQuery,null);
            if(cursor.moveToFirst()){
                do{
                    JoggerStepModel joggerStepModel = new JoggerStepModel();
                    joggerStepModel.setmDate(cursor.getString(cursor.getColumnIndex(JoggerSQLiteOpenHelper.COLUMN_CREATION_DATE)));
                    joggerStepModel.setmStep(cursor.getInt(cursor.getColumnIndex(JoggerSQLiteOpenHelper.COLUMN_STEPS_COUNT)));
                    arrStepCount.add(joggerStepModel);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  arrStepCount;
    }
}
