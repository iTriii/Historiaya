package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Reset_Password extends AppCompatActivity {
    Button Reset;
    ImageButton back;
    EditText youremail;
    FirebaseAuth mAuth;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth =FirebaseAuth.getInstance();
        Reset = findViewById(R.id.Reset);
        Reset.setOnClickListener(v -> CreateNewPassword());

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> LogIn());

        progressbar = findViewById(R.id.progressbar);

        youremail = findViewById(R.id.youremail);
    }

    public void CreateNewPassword() {
        progressbar.setVisibility(View.VISIBLE); // Show the progress bar

        String email = youremail.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            youremail.setError("Invalid");
            youremail.requestFocus();
            progressbar.setVisibility(View.GONE); // Hide the progress bar
            return;
        }

        // Send password reset email
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressbar.setVisibility(View.GONE); // Hide the progress bar

            // After sending the password reset email successfully
            if (task.isSuccessful()) {
                Toast.makeText(Reset_Password.this, "Reset Password link has been sent to your email", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(Reset_Password.this, LogIn.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Reset_Password.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void LogIn() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
}