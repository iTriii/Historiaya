package com.example.log_in;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyAndTerms_login extends AppCompatActivity {
    private static final String CHECKBOX_STATE = "checkbox_state";
    CheckBox checkBoxTNC;
    Button ConfirmTnC;
    ImageButton back;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_terms_sign_up);

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);

            sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

            checkBoxTNC.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ConfirmTnC.setEnabled(isChecked);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(CHECKBOX_STATE, isChecked); // Save checkbox state
                editor.apply();
            });
        });
    }
}