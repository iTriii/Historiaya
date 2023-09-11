package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class OTP extends AppCompatActivity {
    ImageButton back;
    TextView EditNow, Confirm, countingNum, replaceable_email;
    FirebaseUser user;
    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        auth = FirebaseAuth.getInstance();

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> SignUp());

        EditNow = findViewById(R.id.EditNow);
        EditNow.setOnClickListener(v -> SignUp());

        Confirm = findViewById(R.id.Confirm);
        Confirm.setOnClickListener(v -> LogIn());

        countingNum = findViewById(R.id.countingNum);
        startCountdownTimer();

        replaceable_email = findViewById(R.id.replaceable_email);
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();

        }
        else {

            replaceable_email.setText(user.getEmail());
        }
    }
    // Generate a random OTP (6 digits)
    String generatedOTP = generateOTP();

    // Function to generate a random 6-digit OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
        return String.valueOf(otp);
    }


    private void startCountdownTimer() {
        new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                @SuppressLint("DefaultLocale") String timeRemaining = String.format("%02d:%02d", minutes, seconds);
                countingNum.setText(timeRemaining);

            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                countingNum.setText("00:00");
            }
        }.start();
    }

    public void SignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);


    }

    private void LogIn() {
        EditText otp1EditText = findViewById(R.id.otp1);
        EditText otp2EditText = findViewById(R.id.otp2);
        EditText otp3EditText = findViewById(R.id.otp3);
        EditText otp4EditText = findViewById(R.id.otp4);
        EditText otp5EditText = findViewById(R.id.otp5);
        EditText otp6EditText = findViewById(R.id.otp6);

        String enteredOTP1 = otp1EditText.getText().toString().trim();
        String enteredOTP2 = otp2EditText.getText().toString().trim();
        String enteredOTP3 = otp3EditText.getText().toString().trim();
        String enteredOTP4 = otp4EditText.getText().toString().trim();
        String enteredOTP5 = otp5EditText.getText().toString().trim();
        String enteredOTP6 = otp6EditText.getText().toString().trim();

        // Combine the individual OTP digits into one OTP string
        String enteredOTP = enteredOTP1 + enteredOTP2 + enteredOTP3 + enteredOTP4 + enteredOTP5 + enteredOTP6;

        String email = getIntent().getStringExtra("email"); // Get email passed from sign-up activity

        // Compare entered OTP with the one sent to the user's email
        if (enteredOTP.equals(generatedOTP)) {
            Intent intent = new Intent(OTP.this, LogIn.class);
            intent.putExtra("email", email); // Pass email to OTP verification activity
            startActivity(intent);
            finish();
        } else {
            // OTP is incorrect, display an error message
            Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
        }
    }
}

