package com.example.nguyenduchai.mynote;

/**
 * Created by Nguyen Duc Hai on 4/9/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;


/**
 * Created by Nguyen Duc Hai on 4/8/2017.
 */

public class NoteSQLiteDatabase {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] allColumnsNote = {NoteSQLiteOpenHelper.COLUMN_ID, NoteSQLiteOpenHelper.COLUMN_NOTE_CONTENT, NoteSQLiteOpenHelper.COLUMN_DATE_TIME};
    private Context context;

    public NoteSQLiteDatabase(Context context)
    {
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

    public void addNewNote(String note)
    {
        String datetime = DateFormat.getDateTimeInstance().format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_NOTE_CONTENT, note);
        contentValues.put(NoteSQLiteOpenHelper.COLUMN_DATE_TIME, datetime);
        // insert
        sqLiteDatabase.insert(NoteSQLiteOpenHelper.TABLE_NAME, null, contentValues);
        Toast.makeText(this.context, "add new note success", Toast.LENGTH_SHORT).show();
    }

    public void deleteNote(NoteModel note)
    {
        sqLiteDatabase.delete(NoteSQLiteOpenHelper.TABLE_NAME, NoteSQLiteOpenHelper.COLUMN_ID + " = " + note.getId(), null);
        Toast.makeText(this.context, "delete note success", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<NoteModel> getAllNotes()
    {
        ArrayList<NoteModel> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(NoteSQLiteOpenHelper.TABLE_NAME, allColumnsNote, null, null, null, null, null);
        if(cursor == null)
        {
            return null;
        }
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            NoteModel noteModel = cursorToNoteMode(cursor);
            arrayList.add(noteModel);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public NoteModel cursorToNoteMode(Cursor cursor)
    {
        NoteModel noteModel = new NoteModel();
        noteModel.setId(cursor.getInt(0));
        noteModel.setNote(cursor.getString(1));
        noteModel.setDatetime(cursor.getString(2));
        return noteModel;
    }
}
