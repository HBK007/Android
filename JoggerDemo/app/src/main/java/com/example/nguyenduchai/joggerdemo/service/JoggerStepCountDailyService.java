package com.example.nguyenduchai.joggerdemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.nguyenduchai.joggerdemo.controller.JoggerDataStepSummaryDaily;

/**
 * Created by Nguyen Duc Hai on 5/14/2017.
 */

public class JoggerStepCountDailyService extends Service implements SensorEventListener {

    private Sensor mStepDetectorSensor;
    private SensorManager mSensorManager;
    private JoggerDataStepSummaryDaily mJoggerDataStepSummaryDaily;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) (this).getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_NORMAL);
            mJoggerDataStepSummaryDaily = new JoggerDataStepSummaryDaily(this);
            mJoggerDataStepSummaryDaily.open();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mJoggerDataStepSummaryDaily.createStepEntry();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
}
