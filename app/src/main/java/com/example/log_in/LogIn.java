package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class LogIn extends AppCompatActivity {
    TextView forgotpass, inputemail, inputpass, regnow;
    Button li;
    FirebaseFirestore firestore;

    @SuppressLint("Resource-type")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firestore = FirebaseFirestore.getInstance();

        inputemail = findViewById(R.id.inputemail);
        inputpass = findViewById(R.id.inputpass);
        li = findViewById(R.id.li);
        li.setOnClickListener(v -> Main2());

        forgotpass = findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(v -> Reset_Password());

        regnow = findViewById(R.id.regnow);
        regnow.setOnClickListener(v -> SignUp());

    }

    public void Reset_Password() {
        Intent intent = new Intent(this, Reset_Password.class);
        startActivity(intent);
    }

    public void SignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }


    public void Main2() {
        String Inputemail = inputemail.getText().toString().trim();
        String Inputpass = inputpass.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Inputemail ).matches()) {
            inputemail.setError("Invalid Email Format");
            inputemail.requestFocus();
            return;
        } else {
            inputemail.setError(null);
        }
        if (Inputemail.length() < 10) {
            inputemail.setError("Valid Email Required");
            inputemail.requestFocus();
            return;
        } else {
            inputemail.setError(null);
        }

        if (Inputpass.length() == 0) {
            inputpass.setError("Password Required");
            inputpass.requestFocus();
            return;
        } else {
            inputpass.setError(null);
        }

        if (!containsDigits(Inputpass) || !containsLetters(Inputpass)) {
            inputpass.setError("Password should contain at least one digit and one letter");
            inputpass.requestFocus();
            return;
        } else {
            inputpass.setError(null);
        }
        saveEmailAndPasswordToFirestore(Inputemail, Inputpass);
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
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

    private void saveEmailAndPasswordToFirestore(String email, String password) {
        firestore.collection("LogIn").document("Email")
                .set(getUserData(email, password));
    }


    private Map<String, Object> getUserData(String email, String password) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        return userData;
    }
}


