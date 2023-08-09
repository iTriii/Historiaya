package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Notifications1 extends AppCompatActivity {
    ImageButton backbutt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications1);

        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> backbutt());
    }
    public void backbutt(){
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
    }
}