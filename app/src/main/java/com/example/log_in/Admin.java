package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {

    TextView StoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        StoreManager = findViewById(R.id.StoreManager);
        StoreManager.setOnClickListener(v -> StoreManager());

    }
    private void StoreManager() {
        Intent intent = new Intent(this, StoreManager.class);
        startActivity(intent);
    }

}