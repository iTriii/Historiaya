package com.example.log_in;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyandTerms extends AppCompatActivity {

    private static final String CHECKBOX_STATE = "checkbox_state";
    CheckBox checkBoxTNC;
    Button ConfirmTnC;
    ImageButton back;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacyand_terms);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        checkBoxTNC = findViewById(R.id.checkboxTnC);
        checkBoxTNC.setChecked(sharedPreferences.getBoolean(CHECKBOX_STATE, false)); // Restore checkbox state

        checkBoxTNC.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConfirmTnC.setEnabled(isChecked);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHECKBOX_STATE, isChecked); // Save checkbox state
            editor.apply();
        });

        ConfirmTnC = findViewById(R.id.ConfirmTnC);
        ConfirmTnC.setOnClickListener(view -> main2());
        ConfirmTnC.setEnabled(checkBoxTNC.isChecked()); // Enable ConfirmTnC button based on checkbox state

        back = findViewById(R.id.back);
        back.setOnClickListener(view -> main2());
    }

    private void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
    }
}