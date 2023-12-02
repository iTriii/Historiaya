package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyAndTerms_login extends AppCompatActivity {
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_terms_login);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(PrivacyAndTerms_login.this, LogIn.class);
            startActivity(intent);
            finish(); // Finish the current activity after starting the new one
        });
    }

    private void goBack() {
        Intent intent = new Intent(PrivacyAndTerms_login.this, LogIn.class);
        startActivity(intent);
        finish();
    }
}