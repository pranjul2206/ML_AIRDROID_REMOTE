package com.example.sensorp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class Predict extends AppCompatActivity {
    TextView tv;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        Log.d("message@@","in predicted");
        tv=findViewById(R.id.pressed);
    }
    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action=event.getAction();
        int keycode=event.getKeyCode();
        switch(keycode){
            case KeyEvent.KEYCODE_VOLUME_UP:{
                if(KeyEvent.ACTION_UP==action){
                    count+=1;
                    tv.setText(count+"");


                    //tried running volume up and  down but it wasnt working
//                    Intent serviceIntent = new Intent(this, MyForegroundService.class);
//                    serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
//                    ContextCompat.startForegroundService(this, serviceIntent);
                }
                break;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN:{
                if(KeyEvent.ACTION_UP==action){
                    count-=1;
                    tv.setText(count+"");
//                    Intent serviceIntent = new Intent(this, MyForegroundService.class);
//                    stopService(serviceIntent);
                }
                break;
            }
            case  KeyEvent.KEYCODE_BACK:{
                return true;
            }
            case KeyEvent.KEYCODE_NAVIGATE_IN:{
                return true;
            }
            case KeyEvent.KEYCODE_NAVIGATE_OUT:{
                return true;
            }
        }
        //returning true because we dont want to increase volume
        return true;
//        return super.dispatchKeyEvent(event);
    }
}
