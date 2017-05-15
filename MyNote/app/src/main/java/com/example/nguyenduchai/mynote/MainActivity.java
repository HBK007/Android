package com.example.nguyenduchai.mynote;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnAdd;
    private EditText editNote;
    private ListView lstNote;
    private ProgressBar proLoading;

    private NoteSQLiteDatabase noteSQLiteDatabase;
    private ArrayList<NoteModel> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find view by id
        btnAdd = (Button) findViewById(R.id.btn_add);
        editNote = (EditText) findViewById(R.id.edt_note);
        lstNote = (ListView) findViewById(R.id.lst_note);
        proLoading = (ProgressBar) findViewById(R.id.pro_loading);
        // take event onclick for button btnAdd
        btnAdd.setOnClickListener(this);
        lstNote.setOnItemClickListener(this);

        // create database source
        noteSQLiteDatabase = new NoteSQLiteDatabase(this);
        noteSQLiteDatabase.open();

        // get all notes in database and view them in listview
        updateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_backup:
                // backup database
                break;
            case R.id.id_setting:
                // setting app
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (arrayList != null && arrayList.size() > 0) {
                proLoading.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.VISIBLE);
                // view all notes to listview
                NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, arrayList);
                lstNote.setAdapter(noteAdapter);

            } else {
                proLoading.setVisibility(View.VISIBLE);
                lstNote.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void updateListView() {
        // create new thread read all note of database
        new Thread() {
            @Override
            public void run() {
                // get all notes in database
                arrayList = noteSQLiteDatabase.getAllNotes();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteSQLiteDatabase.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            String contentNote = editNote.getText().toString();
            if (contentNote.trim().length() > 0) {
                // add new note in database
                noteSQLiteDatabase.addNewNote(contentNote);
                // refresh listview
                updateListView();
                // reset edtNote
                editNote.setText("");
            } else {
                Toast.makeText(this, "Please input your note !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteNote(NoteModel noteModel) {
        noteSQLiteDatabase.deleteNote(noteModel);
        updateListView();
        Toast.makeText(this, "Delete note success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteModel noteModel = arrayList.get(position);
        Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
        intent.putExtra("edit_note", noteModel);
        startActivity(intent);
    }
}
