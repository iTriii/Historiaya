package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {

    TextView StoreManager, DonCatbtn, Galarodbtn, Recep, TourismHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        StoreManager = findViewById(R.id.StoreManager);

        DonCatbtn = findViewById(R.id.DonCatbtn);
        Galarodbtn = findViewById(R.id.Galarodbtn);
        Recep = findViewById(R.id.Recep);
        TourismHead = findViewById(R.id.TourismHead);

//RECEPTIONIST
        Recep.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, Receptionist.class);
            startActivity(intent);
        });
//NAVIGATE TO DON CATALINO ADIN ACTIVITY
        DonCatbtn .setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, DonCatalinoAdmin.class);
            startActivity(intent);
        });
        // NAVIGATE TO GALA RODRIGUEZ ADMIN ACTIVITY
        Galarodbtn .setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, GalaRodriguezAdmin.class);
            startActivity(intent);
        });
        // NAVIGATE TO TOURISMHEAD ADMIN ACTIVITY
        TourismHead.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, TourismHeadAdmin.class);
            startActivity(intent);
        });
        // NAVIGATE TO STORE MANAGER
        StoreManager.setOnClickListener(v -> {
            Intent intent = new Intent(Admin.this, StoreManager.class);
            startActivity(intent);
        });

    }

}