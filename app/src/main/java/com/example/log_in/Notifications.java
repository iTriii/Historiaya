package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//FOR UPDATE ONLY
public class Notifications extends AppCompatActivity {
    TextView ViewNotif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ViewNotif = findViewById(R.id.ViewNotif);
        ViewNotif.setOnClickListener(v -> ViewNotif());
    }
    public void ViewNotif(){
        Intent intent = new Intent(this, Notifications1.class);
        startActivity(intent);
    }
}