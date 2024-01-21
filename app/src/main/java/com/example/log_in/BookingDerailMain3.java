package com.example.log_in;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class BookingDerailMain3 extends AppCompatActivity {
    Button notnowbtn, confirmbtn, Nextbtn, Donebut;
    ImageButton backbutton, reschedbtn, cancelbtn, refundbtn;
    TextView selectedTourText, totalText, selectedtouristsText, datetext;
    Dialog dialog;

    FirebaseAuth auth;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_derail_main3);

// Retrieve the button index from the Intent
        // Retrieve the bookingIndex extra from the Intent
        int bookingIndex = getIntent().getIntExtra("bookingIndex", -3);


        // Initializing FirebaseFirestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initializing TextViews and Buttons
        backbutton = findViewById(R.id.backbtnDetailMain);
        selectedTourText = findViewById(R.id.selectedTourText);
        totalText = findViewById(R.id.totalText);
        selectedtouristsText = findViewById(R.id.selectedtouristsText);
        datetext = findViewById(R.id.datetext);
        reschedbtn = findViewById(R.id.reschedbtn);
        cancelbtn = findViewById(R.id.cancelbtn);
        Donebut = findViewById(R.id.Donebut);

        // Initializing the dialog
        dialog = new Dialog(BookingDerailMain3.this);
        dialog.setContentView(R.layout.dialog_cancellation);
        dialog.setCancelable(false);

        notnowbtn = dialog.findViewById(R.id.notnowbtn);
        confirmbtn = dialog.findViewById(R.id.confirmbtn);

        // Click listeners for dialog buttons
        notnowbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingDerailMain3.this, Main2.class);
            startActivity(backIntent);
            Toast.makeText(BookingDerailMain3.this, "Not Now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Now you can use the bookingIndex as needed
        if (bookingIndex != -3) {

            Log.d("BookingDerailMain3", "Received bookingIndex: " + bookingIndex);
        } else {
            Log.e("BookingDerailMain3", "Invalid or missing bookingIndex");

        }


        confirmbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingDerailMain3.this, Main2.class);
            startActivity(backIntent);

            Toast.makeText(BookingDerailMain3.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Click listeners for navigation buttons
        reschedbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookingDerailMain3.this, BookNow.class);
            startActivity(intent);
        });

        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(BookingDerailMain3.this, Profile.class);
            startActivity(intent);
        });


        cancelbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookingDerailMain3.this, BookingCancellation.class);
            startActivity(intent);
        });

        Donebut.setOnClickListener(v -> {
            Intent intent = new Intent(BookingDerailMain3.this, Profile.class);
            startActivity(intent);
        });

        // Retrieve and display data from Firestore
        retrieveDataFromFirestore();
    }

    // Method to retrieve data from Firestore
    @SuppressLint("DefaultLocale")
    private void retrieveDataFromFirestore() {
        userDataListener = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("BookingDetailMainActivity", "Error fetching user data: " + error.getMessage());
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Retrieve data from documentSnapshot
                        String selectedTour3 = documentSnapshot.getString("selectedTour3");
                        Double totalAmount3 = documentSnapshot.getDouble("totalAmount3");
                        String selectedTouristNum3 = documentSnapshot.getString("selectedTouristNum3");
                        String reservedDate3 = documentSnapshot.getString("reservedDate3");

                        // Check for null values before using them
                        if (selectedTour3 != null && totalAmount3 != null && selectedTouristNum3 != null && reservedDate3 != null) {
                            // Set TextViews with the retrieved data
                            datetext.setText(reservedDate3);
                            selectedtouristsText.setText(selectedTouristNum3);
                            totalText.setText(String.format("₱%.2f", totalAmount3));
                            selectedTourText.setText(selectedTour3);
                        } else {
                            showToast("Some data is null in Firestore document.");
                        }
                    } else {
                        showToast("No booking data found in Firestore.");
                    }
                });
    }

    // Method to display toast messages
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
