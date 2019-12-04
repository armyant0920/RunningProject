package com.example.runningproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.Random;

public class ShakeService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        Log.d("ServiceStatue","ServiceOn");
        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (MainActivity.pause == false) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;

            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
//            Log.d("mAccel=",String.valueOf(mAccel));

            if (mAccel > 11) {
                MainActivity.count++;
                MainActivity.numText.setText(String.valueOf(MainActivity.count));
                MainActivity.percentage=(double)MainActivity.count/(double)MainActivity.goalnum*100;
                String s=String.valueOf(MainActivity.percentage);
                if(s.length()>5){s=s.substring(0,5);}

                Log.d("percentage=",s);
                MainActivity.seekBar.setProgress((int)MainActivity.percentage);
//                MainActivity.percentageText.setText(String.valueOf(MainActivity.percentage));
                MainActivity.percentageText.setText(s);
                if(MainActivity.count >= MainActivity.goalnum)
                {Toast.makeText(this,"已完成目標",Toast.LENGTH_SHORT).show();}

//                Log.d("AccelLast=",String.valueOf(mAccelLast));
//                Log.d("delta=",String.valueOf(delta));

            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
