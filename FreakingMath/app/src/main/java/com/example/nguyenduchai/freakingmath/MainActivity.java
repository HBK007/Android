package com.example.nguyenduchai.freakingmath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView btnPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // find id view
        btnPlay = (ImageView) findViewById(R.id.btn_play);

        // set onclick listener
        btnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_play){
            // jump to play activity
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        }
    }
}
