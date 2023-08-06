package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class Reset_Password extends AppCompatActivity {
    Button Reset;
    ImageButton back;
    EditText youremail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Reset = findViewById(R.id.Reset);
        Reset.setOnClickListener(v -> CreateNewPassword());

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> LogIn());

        youremail = findViewById(R.id.youremail);
    }

    public void CreateNewPassword() {
        String email = youremail.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            youremail.setError("Invalid");
            youremail.requestFocus();
            return;
        }
        else {
            youremail.setError(null);
        }
        Intent intent = new Intent(this, CreateNewPassword.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void LogIn() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
}