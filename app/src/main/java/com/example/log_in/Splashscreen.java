package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
//FOR UPDATE ONLY

public class Splashscreen extends AppCompatActivity {

    public static int SPLASH_SCREEN = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);

    }
}