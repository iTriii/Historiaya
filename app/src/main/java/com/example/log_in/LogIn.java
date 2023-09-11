package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;
public class LogIn extends AppCompatActivity {
    TextView forgotpass, inputemail, inputpass, regnow;
    Button li;
    FirebaseAuth mAuth;
    ProgressBar progressbar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), Main2.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("Resource-type")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.inputemail);
        inputpass = findViewById(R.id.inputpass);
        li = findViewById(R.id.li);
        progressbar = findViewById(R.id.progressbar);

        forgotpass = findViewById(R.id.forgotpass);
        regnow = findViewById(R.id.regnow);

        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String inputEmail = inputemail.getText().toString();
                String inputPass = inputpass.getText().toString();

                if (TextUtils.isEmpty(inputEmail)) {
                    Toast.makeText(LogIn.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inputPass)) {
                    Toast.makeText(LogIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(inputEmail, inputPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Main2.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
