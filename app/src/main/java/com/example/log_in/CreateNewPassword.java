package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class CreateNewPassword extends AppCompatActivity {
    //FOR UPDATE ONLY
    Button resetButton;
    EditText newPasswordEditText, confirmPasswordEditText;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        newPasswordEditText = findViewById(R.id.newpassword);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        resetButton = findViewById(R.id.Reset);

        resetButton.setOnClickListener(v -> updatePassword());
    }

    private void updatePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Passwords cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show the progress bar while updating the password
        progressBar.setVisibility(View.VISIBLE);

        // Update the password in Firebase
        mAuth.getCurrentUser().updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Hide the progress bar after the task completes
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            Toast.makeText(CreateNewPassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to the login page or your desired destination
                            startActivity(new Intent(CreateNewPassword.this, LogIn.class));
                            finish();
                        } else {
                            Toast.makeText(CreateNewPassword.this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, Reset_Password.class);
        startActivity(intent);
        finish();
    }
}

