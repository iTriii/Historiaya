package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateNewPassword extends AppCompatActivity {

   Button Reset;
   ImageButton back;
   EditText newpassword, confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        Reset = findViewById(R.id.Reset);
        Reset.setOnClickListener(v -> LogIn());
        back=findViewById(R.id.back);
        back.setOnClickListener(v -> Reset_Password());

        newpassword=findViewById(R.id.newpassword);
        confirmpassword=findViewById(R.id.confirmpassword);
    }

    public void LogIn() {
        String newpass = newpassword.getText().toString().trim();
        String confirmpass = confirmpassword.getText().toString().trim();

        if (!containsDigits(newpass) || !containsLetters(newpass)) {
            newpassword.setError("Password should contain at least one digit and one letter");
            newpassword.requestFocus();
            return;
        } else {
            newpassword.setError(null);
        }


        if (!confirmpass.equals(newpass)) {
            confirmpassword.setError("Password do not match");
            confirmpassword.requestFocus();
            return;
        } else {
            confirmpassword.setError(null);
        }

        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
    public void Reset_Password(){

        Intent intent = new Intent(this,Reset_Password.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
    private boolean containsLetters(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigits(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}