package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;

public class OTP extends AppCompatActivity {
    ImageButton back;
    TextView EditNow,Confirm, countingNum;
    CountDownTimer countDownTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> SignUp());

        EditNow = findViewById(R.id.EditNow);
        EditNow.setOnClickListener(v -> SignUp());

        Confirm = findViewById(R.id.Confirm);
        Confirm.setOnClickListener(v -> Main2());

        countingNum = findViewById(R.id.countingNum);
        startCountdownTimer();

    }
    private void startCountdownTimer() {
        new CountDownTimer(90000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Duration
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeRemaining = String.format("%02d:%02d", minutes, seconds);
                countingNum.setText(timeRemaining);
            }

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
    public void Main2(){
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
    }

}