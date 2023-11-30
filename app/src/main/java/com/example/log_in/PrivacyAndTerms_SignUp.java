package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyAndTerms_SignUp extends AppCompatActivity {
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_terms_sign_up);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
    }
}