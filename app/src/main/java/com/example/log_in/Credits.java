package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Credits extends AppCompatActivity {


    ImageButton backbtn;
    //FOR UPDATE ONLY
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        backbtn = findViewById(R.id.backbtn);

        // Back button
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Credits.this, Settings.class);
            startActivity(intent);
        });
    }
}