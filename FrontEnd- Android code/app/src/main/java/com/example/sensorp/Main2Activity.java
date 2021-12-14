package com.example.sensorp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements SensorEventListener {
    View hidden_layout,all_layout,MainLayout;
    EditText hiddenET;
    TextView x, y, z;
    Button left, right, top, bottom, stop, predict,forward,backward,train,hiddenButton;
    private Sensor mySensor;
    private SensorManager SM;
    int l = 0, r = 0, t = 0, b = 0, flag = 0, recordata = 0,Prediction_status=-1,VolumeControl=-1;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds;
    Handler handler;
    String records ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        hidden_layout=findViewById(R.id.hidden_widget);
        all_layout=findViewById(R.id.all_widgets);
        MainLayout=findViewById(R.id.MainLayout);

        hiddenET=findViewById(R.id.hiddenEditText);
        hiddenButton=findViewById(R.id.hiddenButton);
        x = findViewById(R.id.xaxis);
        y = findViewById(R.id.yaxis);
        z = findViewById(R.id.zaxis);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        top = findViewById(R.id.top);
        bottom = findViewById(R.id.bottom);
        stop = findViewById(R.id.stop);
        predict = findViewById(R.id.predict);
        forward=findViewById(R.id.forward);
        backward=findViewById(R.id.backward);
        train=findViewById(R.id.train);
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
                startClock();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l = -1;
                r = -1;
                t = -1;
                b = -1;
                stopClock();
                Log.d("message@@", "clicked");
            }
        });
        hiddenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass=hiddenET.getText().toString();
                if(pass.equals("0000")){
                    all_layout.setVisibility(View.VISIBLE);
                    hidden_layout.setVisibility(View.GONE);
                    MainLayout.setBackgroundColor(Color.WHITE);
                    Prediction_status=-1;
                }
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Prediction_status==-1){
                    all_layout.setVisibility(View.GONE);
                    hidden_layout.setVisibility(View.VISIBLE);
                    MainLayout.setBackgroundColor(Color.BLACK);
                    Prediction_status=1;
                }
//                try {
//                    Intent k = new Intent(getApplicationContext(), Predict.class);
//                    startActivity(k);
//                    finish();
//                    Log.d("message@@","predict clicked");
//                } catch(Exception e) {
//                    e.printStackTrace();
//                }

                //--------------------------------------------
//                if (flag == 0) {
//                    flag = -1;
//                    recordata = -1;
//                } else {
//                    flag = 0;
//                    recordata = 0;
//                    String newstring="predicting\n";
//                    newstring+=CalculateVariance(records);
//                    MessageSender ms = new MessageSender();
////                    ms.execute("predict");
//                    ms.execute(newstring);
//                    Log.d("message@@",newstring );
//                }
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageSender ms = new MessageSender();
//                    ms.execute("predict");
                ms.execute("forward");
            }
        });
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageSender ms = new MessageSender();
//                    ms.execute("predict");
                ms.execute("backward");
            }
        });
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Main2Activity.this,Train.class);
                startActivity(intent);
                finish();
            }
        });
    }
// locking navigation button
    @Override
    protected void onPause() {
        super.onPause();
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
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

    public String CalculateVariance(List<Double[]> records) {
        double sumx = 0;
        double sumy = 0;
        double sumz = 0;
        for (int i = 0; i < records.size(); i++) {

            // = new String[5];
            Double[] myString = records.get(i);
            // System.out.println("printing"+myString.length);
//            System.out.println(i + 1);
            Log.d("message@@", (i + 1)+"");
            for(int j = 0; j < myString.length; j++) {
//                System.out.print(myString[j] + " * ");
                Log.d("message@@", myString[j]+"");
            }
            sumx += myString[0];
            sumy += myString[1];
            sumz += myString[2];

        }
        double meanx = (double)sumx /(double)records.size();
        double meany = (double)sumy /(double)records.size();
        double meanz = (double)sumz /(double)records.size();
        double sqDiffx = 0;
        double sqDiffy = 0;
        double sqDiffz = 0;
        for(int i=0;i<records.size();i++){

            Double[] myString= new Double[5];
            myString=records.get(i);
            sqDiffx += (myString[0] - meanx) *  (myString[0] - meanx);
            sqDiffy += (myString[1] - meany) *  (myString[1] - meany);
            sqDiffz += (myString[2] - meanz) *  (myString[2] - meanz);


        }
        double varx=sqDiffx/(double)records.size();
        double vary=sqDiffy/(double)records.size();
        double varz=sqDiffz/(double)records.size();
        String newstring=varx+" "+vary+" "+varz;
        return newstring;
    }

// CONTROLLING ALL THE HARDWARE KEYS
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action=event.getAction();
        int keycode=event.getKeyCode();
        switch(keycode){
            case KeyEvent.KEYCODE_VOLUME_UP:{
                if(KeyEvent.ACTION_DOWN==action && Prediction_status==1){
                    if(VolumeControl==-1) {
                        records = "Predicting";
                        flag = -1;
                        recordata = -1;
                        Log.d("volume", "down");
                        VolumeControl=1;
                    }
                }
                else{
                    VolumeControl=-1;
                    flag = 0;
                    recordata = 0;
                    records = records+"\nend";
                    if(records.length()>0){
                        MessageSender ms = new MessageSender();
                        ms.execute(records);
                        Log.d("volume",records );
                        records="";
                    }
                    Log.d("volume","up");
                }
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN:{
                if(KeyEvent.ACTION_DOWN==action && Prediction_status==1){
                    if(VolumeControl==-1) {
//                        records = "Predicting";
//                        flag = -1;
//                        recordata = -1;
//                        Log.d("volume", "down");
//                        VolumeControl=1;
                        Intent intent=new Intent(Main2Activity.this,MouseActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
//                else{
//                    VolumeControl=-1;
//                    flag = 0;
//                    recordata = 0;
//                    records = records+"\nend";
//                    if(records.length()>0){
//                        MessageSender ms = new MessageSender();
//                        ms.execute(records);
//                        Log.d("volume",records );
//                        records="";
//                    }
//                    Log.d("volume","up");
//                }
                break;
            }
            case  KeyEvent.KEYCODE_BACK:{
                return true;
            }
//            case KeyEvent.KEYCODE_NAVIGATE_IN:{
//                return true;
//            }
//            case KeyEvent.KEYCODE_NAVIGATE_OUT:{
//                return true;
//            }
        }
        //returning true because we dont want to increase volume
        return true;
//        return super.dispatchKeyEvent(event);
    }
}
