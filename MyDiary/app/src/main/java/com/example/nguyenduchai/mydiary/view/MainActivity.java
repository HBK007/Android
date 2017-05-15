package com.example.nguyenduchai.mydiary.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nguyenduchai.mydiary.R;
import com.example.nguyenduchai.mydiary.controller.NoteAdapter;
import com.example.nguyenduchai.mydiary.controller.NoteDataSource;
import com.example.nguyenduchai.mydiary.model.NoteModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar myToolbar;
    private ProgressBar proLoading;
    private TextView lblNoContent;
    private ListView lstNote;
    private NoteDataSource noteDataSource;
    private ArrayList<NoteModel> arrNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find view id
        lstNote = (ListView)findViewById(R.id.lst_note);
        lblNoContent = (TextView)findViewById(R.id.lbl_no_content);
        proLoading = (ProgressBar)findViewById(R.id.prb_load);

        // get action bar
        myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        myToolbar.setTitle("My Diary");
        setSupportActionBar(myToolbar);

        // create data source
        noteDataSource = new NoteDataSource(this);
        noteDataSource.open();

        // read all note from database
        viewAllNote();

        // create adapter notes
        NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, arrNotes);
        lstNote.setAdapter(noteAdapter);
        lstNote.setOnItemClickListener(this);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // update UI - display all notes
            if(arrNotes != null && arrNotes.size() > 0)
            {
                proLoading.setVisibility(View.INVISIBLE);
                lblNoContent.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.VISIBLE);
                // view all notes in listview
                NoteAdapter noteAdapter = new NoteAdapter(MainActivity.this, arrNotes);
                lstNote.setAdapter(noteAdapter);
            }else{
                proLoading.setVisibility(View.VISIBLE);
                lblNoContent.setVisibility(View.VISIBLE);
                lstNote.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void viewAllNote() {
        // create new thread to get all notes run background task
        new Thread(new Runnable() {
            @Override
            public void run() {
                // read all notes from database
                arrNotes = noteDataSource.getAllNotes();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.new_note:
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewNoteActivity.class);
        NoteModel noteModel = arrNotes.get(position);
        intent.putExtra("note_id", noteModel.getId());
        intent.putExtra("note_title", noteModel.getTitle());
        intent.putExtra("note_content", noteModel.getContent());
        intent.putExtra("note_image", noteModel.getImage());
        intent.putExtra("note_datetime", noteModel.getDatetime());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewAllNote();
    }
}
