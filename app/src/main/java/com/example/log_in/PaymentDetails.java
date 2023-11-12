package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;

public class PaymentDetails extends AppCompatActivity {

    RadioButton DonCat_Radio, Gala_Radio;
    ScrollView ScrollViewDonCata, ScrollViewGalaRod;
    View lineone, linetwo;

    ImageButton backbtn, chatbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        backbtn = findViewById(R.id.backbtn);
        chatbtn = findViewById(R.id.chatbtn);
        ScrollViewDonCata = findViewById(R.id.ScrollViewDonCata); //scrollview
        DonCat_Radio = findViewById(R.id.DonCat_Radio);
        DonCat_Radio.setOnClickListener(v -> DonCat_Radio());

        ScrollViewGalaRod = findViewById(R.id.ScrollViewGalaRod); //scrollview
        Gala_Radio = findViewById(R.id.Gala_Radio);
        Gala_Radio.setOnClickListener(v -> Gala_Radio());
        lineone = findViewById(R.id.lineone);
        linetwo = findViewById(R.id.linetwo);

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

    }
    public void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    private void Gala_Radio() {
        DonCat_Radio.setChecked(true);
        DonCat_Radio.setTextColor(ContextCompat.getColor(this, R.color.green));
        ScrollViewDonCata.setVisibility(View.VISIBLE);
        Gala_Radio.setChecked(false);
        Gala_Radio.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        ScrollViewGalaRod.setVisibility(View.GONE);
        lineone.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        linetwo.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void DonCat_Radio() {
        DonCat_Radio.setChecked(true);
        DonCat_Radio.setTextColor(ContextCompat.getColor(this, R.color.green));
        ScrollViewDonCata.setVisibility(View.GONE);
        Gala_Radio.setChecked(false);
        Gala_Radio.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        ScrollViewGalaRod.setVisibility(View.VISIBLE);
        lineone.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        linetwo.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }
}
