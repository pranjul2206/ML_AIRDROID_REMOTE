package com.example.sensorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MouseActivity extends AppCompatActivity {
    Context context;
    Button playPauseButton;
    Button nextButton;
    Button previousButton;
    Button mark,snap;
    TextView mousePad;

    private boolean isConnected=false;
    private boolean mouseMoved=false;
    private Socket socket;
    private PrintWriter out;
    public LinearLayout linearLayout;

    private float initX =0;
    private float initY =0;
    private float disX =0;
    private float disY =0;

    int markFlag=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse);

        context = this;
        playPauseButton =findViewById(R.id.playPauseButton);
        nextButton =findViewById(R.id.nextButton);
        previousButton =findViewById(R.id.previousButton);
        mark=findViewById(R.id.mark);
        snap=findViewById(R.id.snap);

        linearLayout=(LinearLayout) findViewById(R.id.activity_main);
        pranjulconn();
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markFlag==-1){
                    markFlag=1;
                    if (isConnected && out!=null) {
                        new MouseActivity.SendMessage().execute(Constants.LEFTDOWN);
                    }
                }
                else{
                    markFlag=-1;
                    if (isConnected && out!=null) {
                        new MouseActivity.SendMessage().execute(Constants.LEFTUP);
                    }
                }
            }
        });
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected && out!=null) {
                    new MouseActivity.SendMessage().execute(Constants.SNAP);
                }
            }
        });
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        linearLayout.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
                if (isConnected && out!=null) {
                    new MouseActivity.SendMessage().execute(Constants.PLAY);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected && out!=null) {
                    new MouseActivity.SendMessage().execute(Constants.START);
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected && out!=null) {
                    new MouseActivity.SendMessage().execute(Constants.CLEAR);
                }
            }
        });

        mousePad = (TextView)findViewById(R.id.mousePad);
        mousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isConnected && out!=null){
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            //save X and Y positions when user touches the TextView
                            initX =event.getX();
                            initY =event.getY();
                            mouseMoved=false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            disX = event.getX()- initX;
                            disY = event.getY()- initY;
                            initX = event.getX();
                            initY = event.getY();
                            if(disX !=0|| disY !=0){
                                new MouseActivity.SendMessage().execute(disX +","+ disY);
                            }
                            mouseMoved=true;
                            break;
                        case MotionEvent.ACTION_UP:
                            //consider a tap only if usr did not move mouse after ACTION_DOWN
                            if(!mouseMoved){
                                new MouseActivity.SendMessage().execute(Constants.MOUSE_LEFT_CLICK);
                            }
                    }
                }
                return true;
            }
        });
    }

    public class SendMessage extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... params) {
            out.println(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("SendMessage","message sent");
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(event.getUnicodeChar()!=0)
            new MouseActivity.SendMessage().execute("key-"+event.getUnicodeChar());
        return super.onKeyUp(keyCode, event);
    }

    void pranjulconn(){
        MouseActivity.ConnectPhoneTask connectPhoneTask = new MouseActivity.ConnectPhoneTask();
        connectPhoneTask.execute("192.168.29.190");
        Log.i("message@@","in pranjulconn");
    }

    public class ConnectPhoneTask extends AsyncTask<String,Void,Boolean> {

        private final String TAG = this.getClass().getSimpleName();

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            try {
                Log.i("message@@","in connectphonetask");
                InetAddress serverAddr = InetAddress.getByName(params[0]);

                socket = new Socket("192.168.29.190", Constants.SERVER_PORT);//Open socket on server IP and port
                Log.i("message@@",Constants.SERVER_PORT+"");

            } catch (IOException e) {
                Log.e(TAG, "Error while connecting", e);
                result = false;
            }
            if(result){
                SharedPreferences.Editor editor=getSharedPreferences("remote",MODE_PRIVATE).edit();
                editor.putString("server_ip",params[0]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            isConnected = result;
            Toast.makeText(context,isConnected?"Connected to server!":"Error while connecting", Toast.LENGTH_LONG).show();
            try {
                if(isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true); //create output stream to send data to server
                }
            }catch (IOException e){
                Log.e(TAG, "Error while creating OutWriter", e);
                Toast.makeText(context,"Error while connecting",Toast.LENGTH_LONG).show();
            }
        }
    }
}
