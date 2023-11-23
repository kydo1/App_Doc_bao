package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Intro extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getSharedPreferences("infoUser",MODE_PRIVATE);
        Boolean isLogin = sharedPreferences.getBoolean("isLogin",false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin){
                    Intent intent1 = new Intent(Intro.this, MainActivity.class);
                    startActivity(intent1);
                    finish();

                }else {
                    Intent intent = new Intent(Intro.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);
    }
}