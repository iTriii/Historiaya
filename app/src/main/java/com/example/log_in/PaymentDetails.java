package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;  // Add this import
import android.widget.ImageButton;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentDetails extends AppCompatActivity {


    View DonCatalino_Tab, GalaRodriguez_Tab;

    ImageButton backbtn, chatbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        backbtn = findViewById(R.id.backbtn);
        DonCatalino_Tab = findViewById(R.id.DonCatalino_Tab);
        GalaRodriguez_Tab = findViewById(R.id.GalaRodriguez_Tab);
        chatbtn = findViewById(R.id.chatbtn);



        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentDetails.this, chat.class);
            startActivity(intent);
        });


        // Back button
        backbtn.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentDetails.this, BookNow.class);
            startActivity(intent);
        });


        DonCatalino_Tab.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Fragment for Don Catalino
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView4, fragmentDonCata.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();

        });

        GalaRodriguez_Tab.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Fragment for Gala Rodriguez
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView4, fragment_GalaRod.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();
        });
    }
}
