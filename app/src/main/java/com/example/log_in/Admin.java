package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {

    // Declaring TextView variables
    TextView StoreManager, DonCatbtn, Galarodbtn, Recep, TourismHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initializing TextView variables by finding their respective views
        StoreManager = findViewById(R.id.StoreManager);
        DonCatbtn = findViewById(R.id.DonCatbtn);
        Galarodbtn = findViewById(R.id.Galarodbtn);
        Recep = findViewById(R.id.Recep);
        TourismHead = findViewById(R.id.TourismHead);

        // Click listener for Receptionist TextView
        Recep.setOnClickListener(v -> {
            // Creating intent to navigate to Receptionist class
            Intent intent = new Intent(Admin.this, Receptionist.class);
            startActivity(intent);
        });

        // Click listener to navigate to DonCatalinoAdmin activity
        DonCatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, DonCatalinoAdmin.class);
            startActivity(intent);
        });

        // Click listener to navigate to GalaRodriguezAdmin activity
        Galarodbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, GalaRodriguezAdmin.class);
            startActivity(intent);
        });

        // Click listener to navigate to TourismHeadAdmin activity
        TourismHead.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, TourismHeadAdmin.class);
            startActivity(intent);
        });

        // Click listener to navigate to StoreManager activity
        StoreManager.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, StoreManager.class);
            startActivity(intent);
        });
    }
}
