package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class MainActivity extends AppCompatActivity {
    private Button GetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetStarted = findViewById(R.id.GetStarted);
        GetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
        });
    }
}
