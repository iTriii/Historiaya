package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class OTP extends AppCompatActivity {
    ImageButton back;
    TextView EditNow, Confirm, countingNum, replaceable_email, ResendOTP;
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
        Confirm.setOnClickListener(v -> verifyEmailWithOTP());


        countingNum = findViewById(R.id.countingNum);
        startCountdownTimer();

        EditText otp1 = findViewById(R.id.otp1);
        EditText otp2 = findViewById(R.id.otp2);
        EditText otp3 = findViewById(R.id.otp3);
        EditText otp4 = findViewById(R.id.otp4);
        EditText otp5 = findViewById(R.id.otp5);
        EditText otp6 = findViewById(R.id.otp6);

        moveToNextEditText(otp1, otp2);
        moveToNextEditText(otp2, otp3);
        moveToNextEditText(otp3, otp4);
        moveToNextEditText(otp4, otp5);
        moveToNextEditText(otp5, otp6);


        replaceable_email = findViewById(R.id.replaceable_email);
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), OTP.class);
            startActivity(intent);
            finish();
        } else {
            replaceable_email.setText(user.getEmail());
        }

        // Get the OTP string from the Intent extras
        String otp = getIntent().getStringExtra("OTP");

        // Check if the OTP is not null and has exactly 6 characters
        if (otp != null && otp.length() == 6) {
            // Distribute each digit to the EditText fields
            setOtpDigit(R.id.otp1, otp.charAt(0));
            setOtpDigit(R.id.otp2, otp.charAt(1));
            setOtpDigit(R.id.otp3, otp.charAt(2));
            setOtpDigit(R.id.otp4, otp.charAt(3));
            setOtpDigit(R.id.otp5, otp.charAt(4));
            setOtpDigit(R.id.otp6, otp.charAt(5));
        }
    }

    private void moveToNextEditText(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    nextEditText.requestFocus();
                }
            }
        });
    }


    // Helper method to set the digit in the corresponding EditText field
    private void setOtpDigit(int editTextId, char digit) {
        EditText editText = findViewById(editTextId);

        if (editText != null) {
            editText.setText(String.valueOf(digit));
        }
    }

    private void verifyEmailWithOTP() {
        if (user != null) {
            // Get the entered OTP from the user
            StringBuilder enteredOTP = new StringBuilder();
            enteredOTP.append(getOtpDigit(R.id.otp1));
            enteredOTP.append(getOtpDigit(R.id.otp2));
            enteredOTP.append(getOtpDigit(R.id.otp3));
            enteredOTP.append(getOtpDigit(R.id.otp4));
            enteredOTP.append(getOtpDigit(R.id.otp5));
            enteredOTP.append(getOtpDigit(R.id.otp6));

            // Get the stored OTP from the previous activity
            String storedOTP = getIntent().getStringExtra("OTP");

            if (enteredOTP.toString().equals(storedOTP)) {
                // OTP is correct, mark the email as verified and proceed to the login screen
                user.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!user.isEmailVerified()) {
                            user.sendEmailVerification();
                        }
                        LogIn(); // Navigate to the login screen
                        finish(); // Close the OTP activity
                    } else {
                        // Error occurred while reloading user data
                        Toast.makeText(this, "Failed to reload user data: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // OTP is incorrect, display an error message
                Toast.makeText(this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Helper method to get the digit from the corresponding EditText field
    private String getOtpDigit(int editTextId) {
        EditText editText = findViewById(editTextId);

        if (editText != null) {
            return editText.getText().toString().trim();
        }

        return "";
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
    public void LogIn() {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
}
