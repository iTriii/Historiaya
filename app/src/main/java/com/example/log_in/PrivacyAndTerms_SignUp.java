package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyAndTerms_SignUp extends AppCompatActivity {
    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_terms_sign_up);
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
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        });
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }
}