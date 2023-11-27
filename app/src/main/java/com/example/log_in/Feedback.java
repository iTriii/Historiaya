package com.example.log_in;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    private EditText feedbackEditText;
    private Button submitButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        feedbackEditText = findViewById(R.id.editTextFeedback);
        submitButton = findViewById(R.id.Submitbtn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
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
        // Create a new feedback document
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("feedback", feedback);

        // Add the document to the "feedback" collection using the user's UID
        db.collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    // Feedback saved successfully
                    Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Toast.makeText(this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                });
    }
}
