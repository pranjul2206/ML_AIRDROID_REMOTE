package com.example.sensorp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Train extends AppCompatActivity implements SensorEventListener {
    TextView x, y, z;
    Button left, right, top, bottom, CaptureData, train;
    private Sensor mySensor;
    private SensorManager SM;
    int l = 0, r = 0, t = 0, b = 0, flag = 0, recordata = 0;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds;
    Handler handler;
//    List<Double[]> records = new ArrayList<Double[]>();
    String records="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        x = findViewById(R.id.xaxis);
        y = findViewById(R.id.yaxis);
        z = findViewById(R.id.zaxis);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        top = findViewById(R.id.top);
        bottom = findViewById(R.id.bottom);
        CaptureData = findViewById(R.id.CaptureData);
        train = findViewById(R.id.train);
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        handler = new Handler();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = 1;
                r = 0;
                t = 0;
                b = 0;
                flag = -1;
                recordata = -1;
                records="CaptureData\nleft";
                startClock();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = 0;
                r = 1;
                t = 0;
                b = 0;
                flag = -1;
                recordata = -1;
                records="CaptureData\nright";
                startClock();
            }
        });
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = 0;
                r = 0;
                t = 1;
                b = 0;
                flag = -1;
                recordata = -1;
                records="CaptureData\ntop";
                startClock();
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = 0;
                r = 0;
                t = 0;
                b = 1;
                flag = -1;
                recordata = -1;
                records="CaptureData\nbottom";
                startClock();
            }
        });
        CaptureData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = -1;
                r = -1;
                t = -1;
                b = -1;
                Log.d("message@@", "clicked");
                flag = 0;
                recordata = 0;
                records = records+"\nend";
                if(records.length()>0){
                    MessageSender ms = new MessageSender();
                    ms.execute(records);
                    Log.d("message@@",records );
                }
                stopClock();
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageSender ms = new MessageSender();
                ms.execute("train");
            }
        });
    }

    public void startClock() {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    public void stopClock() {
        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            handler.postDelayed(this, 0);
        }

    };
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x.setText("X: " + sensorEvent.values[0]);
        y.setText("Y: " + sensorEvent.values[1]);
        z.setText("Z: " + sensorEvent.values[2]);
        if (recordata == -1) {
            records=records+"\n"+Double.toString((double)sensorEvent.values[0])+" "
                    +Double.toString((double)sensorEvent.values[1])+" "
                    +Double.toString((double)sensorEvent.values[2]);
        } else {
            if (l == 1) {
                Log.d("left@", "x: " + sensorEvent.values[0] + " y: " + sensorEvent.values[1] + " z: " + sensorEvent.values[2] + " " + "" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
            } else if (r == 1) {
                Log.d("right@", "x: " + sensorEvent.values[0] + " y: " + sensorEvent.values[1] + " z: " + sensorEvent.values[2] + " " + "" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
            } else if (t == 1) {
                Log.d("top@", "x: " + sensorEvent.values[0] + " y: " + sensorEvent.values[1] + " z: " + sensorEvent.values[2] + " " + "" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
            }
            if (b == 1) {
                Log.d("bottom@", "x: " + sensorEvent.values[0] + " y: " + sensorEvent.values[1] + " z: " + sensorEvent.values[2] + " " + "" + Minutes + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
