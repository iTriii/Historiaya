package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    private EditText feedbackEditText;
    private Button submitButton;
    ImageButton backbtn, exitbtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        backbtn = findViewById(R.id.backbtnFeedback);
        exitbtn = findViewById(R.id.exitbtn);

        feedbackEditText = findViewById(R.id.editTextFeedback);
        submitButton = findViewById(R.id.Submitbtn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });

        // Back button
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Feedback.this, Main2.class);
            startActivity(intent);
        });

        exitbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Feedback.this, Main2.class);
            startActivity(intent);
        });

        // Get the current user's UID
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }
    }

    private void submitFeedback() {
        String feedback = feedbackEditText.getText().toString().trim();

        if (!feedback.isEmpty()) {
            // Save feedback to Firestore
            saveFeedbackToFirestore(feedback);
        } else {
            Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFeedbackToFirestore(String feedback) {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            // Initialize Firestore references
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("users").document(userId);
            CollectionReference feedbackCollectionRef = userDocRef.collection("Feedback");

            // Create a new document in the "Feedback" collection
            DocumentReference newFeedbackDocRef = feedbackCollectionRef.document();

            // Create a new feedback document
            Map<String, Object> userFeedback = new HashMap<>();
            userFeedback.put("userFeedback", feedback);

            // Add the document to the "Feedback" collection
            newFeedbackDocRef.set(userFeedback)
                    .addOnSuccessListener(documentReference -> {
                        // Feedback saved successfully
                        Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show();
                        Log.d("Feedback", "Feedback submitted successfully");
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                        Log.e("Feedback", "Error submitting feedback", e);
                    });
        } else {
            // Log an error if the current user is null
            Log.e("Feedback", "Current user is null");
            Toast.makeText(this, "Failed to submit feedback. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }
}