package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PopUpSettings extends AppCompatActivity {
    Button Credits, Feedback, PrivacyandTerms,LogOut;
    ImageButton Tutorial1, closeBut;
    TextView Tutorial;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_settings);

        Credits = findViewById(R.id.Credits);
        Credits.setOnClickListener(v -> Credits());

        Feedback = findViewById(R.id.Feedback);
        Feedback.setOnClickListener(v -> Feedback());

        PrivacyandTerms = findViewById(R.id.PrivacyandTerms);
       PrivacyandTerms.setOnClickListener(v -> PrivacyandTerms());

        LogOut = findViewById(R.id.Logout);
        LogOut.setOnClickListener(v -> LogOut());

        Tutorial = findViewById(R.id.Tutorial);
        Tutorial.setOnClickListener(v -> Tutorial());

       Tutorial1 = findViewById(R.id.Tutorial1);
        Tutorial1.setOnClickListener(v -> Tutorial1());

        closeBut= findViewById(R.id.closeBut);
        closeBut.setOnClickListener(v -> closeBut());
    }

    public void Credits() {
        Intent intent =new Intent(this, Credits.class);
        startActivity(intent);
    }
    public void Feedback() {
        Intent intent =new Intent(this, Feedback.class);
        startActivity(intent);
    }
    public void PrivacyandTerms() {
        Intent intent =new Intent(this, PrivacyandTerms.class);
        startActivity(intent);
    }
    public void LogOut() {
        Intent intent =new Intent(this, LogOut.class);
        startActivity(intent);
    }
    public void Tutorial() {
        Intent intent =new Intent(this, Tutorial.class);
        startActivity(intent);
    }
    public void Tutorial1() {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }
    public void closeBut() {
            Intent intent =new Intent(this, Main2.class);
            startActivity(intent);
        }
    }
