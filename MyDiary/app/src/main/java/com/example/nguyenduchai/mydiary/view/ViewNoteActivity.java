package com.example.nguyenduchai.mydiary.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyenduchai.mydiary.R;
import com.example.nguyenduchai.mydiary.controller.NoteDataSource;
import com.example.nguyenduchai.mydiary.utility.Config;
import com.example.nguyenduchai.mydiary.utility.Util;

public class ViewNoteActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ImageView imgAttach;
    private TextView lblContent;
    private String strTitle, strContent, strImg, strDateTime;
    private int id;
    private NoteDataSource noteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);


        // find view id
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        imgAttach = (ImageView) findViewById(R.id.img_attach);
        lblContent = (TextView) findViewById(R.id.lbl_content);

        // get intent
        getIntentData();
        // get action bar
        myToolbar.setTitle(strTitle);
        myToolbar.setSubtitle(strDateTime);
        setSupportActionBar(myToolbar);
        // open database
        noteDataSource = new NoteDataSource(this);
        noteDataSource.open();

        // set data for component of view note
        setContentForViewNote();
    }

    private void setContentForViewNote() {
        Util.setBitmapToImage(this, Config.FOLDER_IMAGES, strImg, imgAttach);
        lblContent.setText(strContent);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("note_id", 0);
        strTitle = intent.getStringExtra("note_title");
        strContent = intent.getStringExtra("note_content");
        strDateTime = intent.getStringExtra("note_datetime");
        strImg = intent.getStringExtra("note_image");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_note:
                noteDataSource.deleteNote(id);
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDataSource.close();
    }
}
