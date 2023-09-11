package com.example.log_in;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    ImageView icon;
    EditText firstname, lastname, E_mail, contact, pass, reenter;
    TextView uploadimage;
    Button signUpButton;
    FirebaseAuth mAuth;
    ProgressBar progressbar;

    int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        icon = findViewById(R.id.icon);
        uploadimage = findViewById(R.id.uploadimage);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon();
            }
        });

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);
        pass = findViewById(R.id.pass);
        reenter = findViewById(R.id.reenter);

        signUpButton = findViewById(R.id.signUpButton);
        progressbar = findViewById(R.id.progressbar);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String fName = firstname.getText().toString().trim();
        String lName = lastname.getText().toString().trim();
        String email = E_mail.getText().toString().trim();
        String contactNo = contact.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String reenterPass = reenter.getText().toString().trim();

        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(reenterPass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // You can add more validation checks here if needed, e.g., email format, password strength, etc.

        if (!password.equals(reenterPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Now, you can create a user account in Firebase Authentication
        progressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (task.isSuccessful() && user != null) {
                            // Send a verification email
                            user.sendEmailVerification()

                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Email verification link sent successfully
                                                Toast.makeText(SignUp.this, "Verification email sent to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                                // Navigate to the OTP verification activity
                                                Intent intent = new Intent(SignUp.this, OTP.class);
                                                intent.putExtra("email", email); // Pass email to OTP verification activity
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Email verification link sending failed
                                                Toast.makeText(SignUp.this, "Failed to send verification email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Registration failed, display a message to the user.
                            Toast.makeText(SignUp.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void icon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Handle the selected image
                    // You can set it to your ImageView or do something else with it
                }
            }
        }
    }
}
