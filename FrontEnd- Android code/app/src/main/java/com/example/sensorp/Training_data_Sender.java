package com.example.sensorp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Training_data_Sender extends AsyncTask<String,Void,Void> {
    Socket s;
    DataOutputStream dos;
    PrintWriter pw;
    @Override
    protected Void doInBackground(String... voids) {
        Log.d("message@@","background");
        String message=voids[0];
        try{
            s=new Socket("192.168.43.190",7800);
            pw=new PrintWriter(s.getOutputStream());
            Log.d("message@@","u;this is message");
            pw.write(message);
            pw.flush();
            pw.close();
            s.close();

        }
        catch (IOException e){
            e.printStackTrace();
            Log.d("message@@",e.toString());
        }
        return null;
    }
}
