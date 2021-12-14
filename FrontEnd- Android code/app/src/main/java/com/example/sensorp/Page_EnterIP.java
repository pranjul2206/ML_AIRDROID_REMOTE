package com.example.sensorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Page_EnterIP extends AppCompatActivity {
    EditText et;
    Button b;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES="sharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page__enter_ip);
        et=findViewById(R.id.page_ip_et);
        b=findViewById(R.id.page_ip_b);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        b.setEnabled(false);
        b.setClickable(false);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip=et.getText().toString();
                if(ip.length()>0){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("IP", ip);
                    editor.apply();
                }
            }
        });
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setEnabled(true);
                b.setClickable(true);
            }
        });
    }
}
