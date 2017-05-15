package com.example.nguyenduchai.mydiary.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nguyenduchai.mydiary.R;
import com.example.nguyenduchai.mydiary.controller.NoteDataSource;
import com.example.nguyenduchai.mydiary.utility.Config;
import com.example.nguyenduchai.mydiary.utility.Util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewNoteActivity extends AppCompatActivity {

    //Constants
    public static final int PIC_PHOTO_FOR_NOTE = 0;

    private Toolbar myToolbar;
    private ImageView imgAttach;
    private EditText edtTitle;
    private EditText edtContent;
    private Bitmap bmpAttach = null;

    private NoteDataSource noteDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // find view id
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        imgAttach = (ImageView) findViewById(R.id.img_attach);
        edtTitle = (EditText) findViewById(R.id.edt_title);
        edtContent = (EditText) findViewById(R.id.edt_content);

        // get action bar
        myToolbar.setTitle("New Note");
        setSupportActionBar(myToolbar);

        // create database
        noteDataSource = new NoteDataSource(this);
        noteDataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.save_note:
                if(edtTitle.getText().toString().trim().length() <= 0)
                {
                    Toast.makeText(this, "Please enter title of note", Toast.LENGTH_LONG).show();
                    return true;
                }
                if(edtContent.getText().toString().trim().length() <= 0)
                {
                    Toast.makeText(this, "Please enter content of note", Toast.LENGTH_LONG).show();
                    return true;
                }
                String dateTime = Util.getCurrentDateTime();
                String imageName = Util.convertStringDateTimeToFileName(dateTime) + ".png";
                // save note in database
                noteDataSource.addNewNote(edtTitle.getText().toString(), edtContent.getText().toString(), imageName, dateTime);
                // save image in SD card
                if(bmpAttach != null)
                {
                    Util.saveImageToSDCard(bmpAttach, Config.FOLDER_IMAGES, imageName);
                }
                this.finish();
                Toast.makeText(this, "Add new note success", Toast.LENGTH_LONG).show();
                break;
            case R.id.attach_image:
                pickImage();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PIC_PHOTO_FOR_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIC_PHOTO_FOR_NOTE && resultCode == Activity.RESULT_OK)
        {
            if(data == null)
            {
                Toast.makeText(this, "There no select picture", Toast.LENGTH_LONG).show();
                return;
            }
            // get image for result
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmpAttach = BitmapFactory.decodeStream(bufferedInputStream);

                // show image in screen
                imgAttach.setImageBitmap(bmpAttach);
                imgAttach.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteDataSource.close();
    }
}
