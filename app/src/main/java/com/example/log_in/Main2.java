package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2 extends AppCompatActivity {
 TextView Start, BookNow, Store, Map;
 ImageView Share, Settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Start = findViewById(R.id.Start);
        Start.setOnClickListener(v -> StartScreen());

        BookNow = findViewById(R.id.BookNow);
        BookNow.setOnClickListener(v -> BookNow());

        Store = findViewById(R.id.Store);
        Store.setOnClickListener(v -> Store());

        Map = findViewById(R.id.Map);
        Map.setOnClickListener(v -> Map());

        Share = findViewById(R.id.Share);
        Share.setOnClickListener(v -> Share());

        Settings = findViewById(R.id.Settings);
        Settings.setOnClickListener(v -> Settings());
    }

    public void StartScreen() {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }

    public void BookNow() {
        Intent intent = new Intent(this, BookNow.class);
        startActivity(intent);
    }

    public void Store() {
        Intent intent = new Intent(this, Store.class);
        startActivity(intent);
    }

    public void Map() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void Share() {
        Intent intent = new Intent(this, Share.class);
        startActivity(intent);
    }

    public void Settings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}