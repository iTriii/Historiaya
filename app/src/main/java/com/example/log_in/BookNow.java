package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class BookNow extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageButton chatbtn, backbtn;
    Spinner spinTour;
    Spinner spinNum;
    Button btnsave;
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

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference userDocRef = FirebaseFirestore.getInstance().collection("users");


        // ArrayAdapter for the HeritageHouses Spinner
        ArrayAdapter<CharSequence> heritageHouseAdapter = ArrayAdapter.createFromResource(
                this, R.array.HeritageHouses, android.R.layout.simple_spinner_item);
        heritageHouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(heritageHouseAdapter);

        // ArrayAdapter for the TouristNumbers Spinner
        ArrayAdapter<CharSequence> touristNumAdapter = ArrayAdapter.createFromResource(
                this, R.array.TouristNumbers, android.R.layout.simple_spinner_item);
        touristNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNum.setAdapter(touristNumAdapter);

        // Set item selection listeners for both Spinners
        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedText = spinTour.getSelectedItem().toString();
                showToast("Heritage House Selected: " + selectedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Heritage House Selected. Please, Select a Tour");
            }
        });

        spinNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedText = spinNum.getSelectedItem().toString();
                showToast("Tourist Number Selected: " + selectedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Tourist Number Selected");
            }
        });


        // Set listeners for other elements (if needed)
        setListeners();
    }
    private void setListeners() {

    }

    //toast

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookNow.this, chat.class);
            startActivity(intent);
        });

        // Back button
        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });

        // Save button
        btnsave.setOnClickListener(view -> {
            // Retrieve the selected tour and number of tourists
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();

            // Check if the user has made valid selections
            if (!selectedTour.equals("Select Tour") && !selectedTouristNumStr.equals("Number of Tourists")) {
                int selectedTouristNum = Integer.parseInt(selectedTouristNumStr);
                String userId = mAuth.getCurrentUser().getUid();


                // Add data to Firestore
                addDataToFirestore(userId, selectedTour, String.valueOf(selectedTouristNum));
            } else {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
            }
        });
    }

                //DONT'T ERASE THIS.. IMPORTANTE TO!
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
                            startActivity(new Intent(getApplicationContext(), Psummary.class));
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
                            startActivity(new Intent(getApplicationContext(), Psummary.class));
                        }).addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                };
            });
        }
    }
}


