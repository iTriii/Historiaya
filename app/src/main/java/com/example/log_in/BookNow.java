package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;


public class BookNow extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageButton chatbtn, backbtn;
    private Spinner spinTour, spinNum;
    private Button btnsave;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements and set up spinners
        btnsave = findViewById(R.id.btnsubmit);
        chatbtn = findViewById(R.id.chatbtn);
        backbtn = findViewById(R.id.backbtn);
        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.touristnum);

        // Configure Crisp
        Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");

        // Set up spinner adapters
        ArrayAdapter<CharSequence> heritageHouseAdapter = ArrayAdapter.createFromResource(
                this, R.array.HeritageHouses, android.R.layout.simple_spinner_item);
        heritageHouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(heritageHouseAdapter);

        ArrayAdapter<CharSequence> touristNumAdapter = ArrayAdapter.createFromResource(
                this, R.array.TouristNumbers, android.R.layout.simple_spinner_item);
        touristNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNum.setAdapter(touristNumAdapter);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();
            String username= user.getDisplayName();

            // Set user attributes in Crisp
            assert userEmail != null;
            Crisp.setUserEmail(userEmail);
        }

        // Set listeners
        setListener();
    }

    private void setListener() {
        // Chat button
        chatbtn.setOnClickListener(v -> {
            // Start Crisp chat
            startCrispChat();
        });

        // Back button
        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });

        // Save button
        btnsave.setOnClickListener(view -> {
            // Replace the following placeholders with actual values
            String userId = mAuth.getCurrentUser().getUid();
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNum = spinNum.getSelectedItem().toString();
            addDataToFirestore(userId, selectedTour, selectedTouristNum);


        });
    }

    private void startCrispChat() {
        Crisp.setUserEmail(mAuth.getCurrentUser().getEmail());

        // Start Crisp chat
        Intent chatIntent = new Intent(this, ChatActivity.class);
        startActivity(chatIntent);
    }

    private void addDataToFirestore(String userId, String selectedTour, String selectedTouristNum) {
        if (!selectedTour.equals("Select Tour") && !selectedTouristNum.equals("Number of Tourists")) {
            DocumentReference userDocRef = db.collection("users").document(userId);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> bookingData = task.getResult().getData();

                    // If the user has already made a booking, update the existing booking.
                    if (bookingData != null) {
                        bookingData.put("selectedTour", selectedTour);
                        bookingData.put("selectedTouristNum", selectedTouristNum);

                        userDocRef.update(bookingData).addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Booking updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                        }).addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        // Create a new booking.
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("selectedTour", selectedTour);
                        user.put("selectedTouristNum", selectedTouristNum);

                        userDocRef.set(user).addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Booking created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                        }).addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    // Handle the failure.
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
