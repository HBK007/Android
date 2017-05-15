package com.example.nguyenduchai.mydiary.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.nguyenduchai.mydiary.model.NoteModel;

import java.util.ArrayList;

/**
 * Created by Nguyen Duc Hai on 4/11/2017.
 */

public class NoteDataSource {
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private String[] allColumn = {
            NoteSQLiteOpenHelper.COLUMN_ID,
            NoteSQLiteOpenHelper.COLUMN_TITLE,
            NoteSQLiteOpenHelper.COLUMN_CONTENT,
            NoteSQLiteOpenHelper.COLUMN_DATETIME,
            NoteSQLiteOpenHelper.COLUMN_IMAGE
    };
    private Context context;

    public NoteDataSource(Context context) {
        this.context = context;
        sqLiteOpenHelper = new NoteSQLiteOpenHelper(context);
    }

    public void open() throws SQLiteException
    {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() throws SQLiteException
    {
        sqLiteOpenHelper.close();
    }

    public void addNewNote(String title, String content, String image, String dateTime)
    {
        // put data component of table
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_TITLE, title);
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_CONTENT, content);
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_DATETIME, dateTime);
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_IMAGE, image);

        // insert data
        sqLiteDatabase.insert(NoteSQLiteOpenHelper.TABLE_NAME, null, contentValues);
        Toast.makeText(context, "add new note success", Toast.LENGTH_LONG).show();
    }

    public void deleteNote(int id)
    {
        sqLiteDatabase.delete(NoteSQLiteOpenHelper.TABLE_NAME, NoteSQLiteOpenHelper.COLUMN_ID + " = " + id, null);
        Toast.makeText(this.context, "delete note success", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<NoteModel> getAllNotes()
    {
        ArrayList<NoteModel> arrAllNote = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(NoteSQLiteOpenHelper.TABLE_NAME, allColumn, null, null, null, null, null);
        if(cursor == null)
        {
            return null;
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            NoteModel noteModel = cursorToNoteModel(cursor);
            arrAllNote.add(noteModel);
            cursor.moveToNext();
        }
        return arrAllNote;
    }

    private NoteModel cursorToNoteModel(Cursor cursor) {
        NoteModel noteModel = new NoteModel();
        noteModel.setId(cursor.getInt(0));
        noteModel.setTitle(cursor.getString(1));
        noteModel.setContent(cursor.getString(2));
        noteModel.setDatetime(cursor.getString(3));
        noteModel.setImage(cursor.getString(4));
        return noteModel;
    }
}
