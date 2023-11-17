package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Psummary extends BookNow {
    private FirebaseFirestore db;
    private ImageButton backbtn;
    private Button proceed2payment;
    private FirebaseAuth mAuth;
    private TextView GalaRodriguez;
    private TextView DonCatalino;
    private TextView Subtotal;
    private TextView ReserveFee;
    private TextView ReserveFeeHouse;
    private TextView Total;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psummary);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements and set up spinners
        backbtn = findViewById(R.id.back);

        proceed2payment = findViewById(R.id.proceed);
        GalaRodriguez = findViewById(R.id.GalaRodriguez);
        DonCatalino = findViewById(R.id.DonCatalino);
        Subtotal = findViewById(R.id.Subtotal);
        ReserveFee = findViewById(R.id.ReserveFee);
        ReserveFeeHouse = findViewById(R.id.ReserveFeeHouse);
        Total = findViewById(R.id.Total);


        // Set listeners
        setListener();
    }

    private void setListener() {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Back button
        proceed2payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Psummary.this, PaymentDetails.class);
                startActivity(intent);
            }
        });


    }

}