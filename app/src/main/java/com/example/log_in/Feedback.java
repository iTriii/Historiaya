package com.example.log_in;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Feedback extends AppCompatActivity {
    private EditText feedbackEditText;
    private Button done, sendEmailbtn;
    ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        backbtn = findViewById(R.id.backbtnFeedback);
//        done = findViewById(R.id.done);

        feedbackEditText = findViewById(R.id.editTextFeedback);
        sendEmailbtn = findViewById(R.id.sendEmailbtn);


////done button
//        done.setOnClickListener(v -> {
//            Intent intent = new Intent(Feedback.this, Main2.class);
//            startActivity(intent);  // Add this line to start the activity
//        });


        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Feedback.this, Main2.class);
            startActivity(intent);  // Add this line to start the activity
        });

        sendEmailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedbackByEmail();
            }
        });
    }

    private void sendFeedbackByEmail() {
        String feedback = feedbackEditText.getText().toString().trim();

        if (!feedback.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "itri.acc@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Historiaya");
            emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                // Log statement for debugging
                Log.d("Feedback", "Starting email activity...");

                startActivity(Intent.createChooser(emailIntent, "Send email..."));

                // Log statement for debugging
                Log.d("Feedback", "Email activity started.");

            } else {
                Toast.makeText(this, "No email client installed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
        }

    }
}
