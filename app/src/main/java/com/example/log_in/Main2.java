package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2 extends AppCompatActivity {
 TextView Start, BookNow, Store, Map;
 ImageView Share, Settings, Profile;

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

        Settings = findViewById(R.id.EditProfile);
        Settings.setOnClickListener(v -> Settings());

        Profile =findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());
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
    public void Profile(){
        Intent intent= new Intent(this, Profile.class);
        startActivity(intent);
    }
}