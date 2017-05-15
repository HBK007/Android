package com.example.nguyenduchai.myconverttool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout distanceItem, speedItem, tempItem, weightItem;

    public static final int DISTANCE_CONVERT = 0;
    public static final int SPEED_CONVERT = 1;
    public static final int TEMP_CONVERT = 2;
    public static final int WEIGHT_CONVERT = 3;

    private int convertType = DISTANCE_CONVERT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distanceItem = (RelativeLayout) findViewById(R.id.distance_item);
        speedItem = (RelativeLayout) findViewById(R.id.speed_item);
        tempItem = (RelativeLayout) findViewById(R.id.temperature_item);
        weightItem = (RelativeLayout) findViewById(R.id.weight_item);

        distanceItem.setOnClickListener(this);
        speedItem.setOnClickListener(this);
        tempItem.setOnClickListener(this);
        weightItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.distance_item){
            convertType = DISTANCE_CONVERT;
            Intent intent = new Intent(this, ConvertActivityDistance.class);
            intent.putExtra("CONVERT_TYPE", convertType);
            startActivity(intent);
        }
    }
}
