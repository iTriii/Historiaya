package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class BookingCancellation5 extends AppCompatActivity {
    Button withdrawbtn, continuebtn;
    Dialog dialog;

    TextView TotalTouristsText, nameText, amountText, pickhouseText, detailsclick;
    Spinner spinRefund;
    ImageButton backbutton;
    FirebaseAuth auth;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;
    private String selectedOption5; // Declare this variable at the class level


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_cancellation5);

        // Initializing views
        backbutton = findViewById(R.id.backbtncancellation);
        withdrawbtn = findViewById(R.id.withdrawbtn);
        TotalTouristsText = findViewById(R.id.TotalTouristsText);
        nameText = findViewById(R.id.nameText);
        amountText = findViewById(R.id.amountText);
        pickhouseText = findViewById(R.id.pickhouseText);
        detailsclick = findViewById(R.id.detailsclick);
        continuebtn = findViewById(R.id.continuebtn);
        spinRefund = findViewById(R.id.spinRefund);

        // Initializing Firebase instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Spinner adapter for refund
        ArrayAdapter<CharSequence> refundAdapter = ArrayAdapter.createFromResource(
                this, R.array.refund_options, android.R.layout.simple_spinner_item);
        refundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRefund.setAdapter(refundAdapter);

        spinRefund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected refund option
                selectedOption5 = parentView.getItemAtPosition(position).toString();
                // Toast.makeText(BookingRefund.this, "Selected Refund Option: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Click listener for viewing profile
        detailsclick.setOnClickListener(v -> {
            Intent intent = new Intent(BookingCancellation5.this, BookingDetailMain.class);
            startActivity(intent);
        });

        // view button
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(BookingCancellation5.this, BookingDetailMain.class);
            startActivity(intent);
        });

        // Initialization and actions for withdrawal dialog
        dialog = new Dialog(BookingCancellation5.this);
        dialog.setContentView(R.layout.dialog_cancellation);
        dialog.setCancelable(false);

// Buttons for withdrawal dialog
        Button notnowbtn = dialog.findViewById(R.id.notnowbtn);
        Button confirmbtn = dialog.findViewById(R.id.confirmbtn);

        notnowbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingCancellation5.this, BookingDetailMain5.class);
            startActivity(backIntent);
            Toast.makeText(BookingCancellation5.this, "Not now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

// CONFIRMATION BUTTON, NAVIGATE TO THE BOOKING DETAIL MAIN
        confirmbtn.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingCancellation5.this, BookingDetailMain5.class);
            startActivity(confirmIntent);
            //Toast.makeText(BookingCancellation.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

// SHOW THE DIALOG OF WITHDRAWAL
        withdrawbtn.setOnClickListener(v -> {
            dialog.show();
        });

// Initialization and actions for continue dialog
        dialog = new Dialog(BookingCancellation5.this);
        dialog.setContentView(R.layout.dialog_cancellation_continue);
        dialog.setCancelable(false);

        Button btnNotnow = dialog.findViewById(R.id.btnNotnow);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        continuebtn.setOnClickListener(v -> {
            dialog.show();
        });

        btnNotnow.setOnClickListener(v -> {
            Intent notnowIntent = new Intent(BookingCancellation5.this, BookingDetailMain5.class);
            startActivity(notnowIntent);
            Toast.makeText(BookingCancellation5.this, "Not Now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            // Update cancellation status to "pendingCancellation"
            updateCancellationStatus("pendingCancellation");
            Intent confirmIntent = new Intent(BookingCancellation5.this, BookingDetailMain5.class);
            startActivity(confirmIntent);
            saveSelectedOptionToFirestore(); //SAVE TO THE FIRESTORE
            Toast.makeText(BookingCancellation5.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });


        // Fetching user data from Firestore
        retrieveDataFromFirestore();
        saveOptionToFirestore();
    }

    // Save the selected option to Firestore
    private void saveSelectedOptionToFirestore() {
        // Initialize Firestore references
        String userId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userDocRef
                    .update("selectedRefundOption5", selectedOption5)
                    .addOnSuccessListener(aVoid -> {
                        // Show a toast message indicating that the option has been saved successfully
                       Toast.makeText(BookingCancellation5.this, "Selected refund option saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Show a toast message indicating an error in saving the option
                      Toast.makeText(BookingCancellation5.this, "Error saving refund option: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Spinner adapter for refund



    private void updateCancellationStatus(String status) {
        // Update cancellation status in Firestore
        String userId = auth.getCurrentUser().getUid();
        db.collection("users")
                .document(userId)
                .update("status5", status)
                .addOnSuccessListener(aVoid -> {
                    //   showToast("Cancellation status updated successfully");
                })
                .addOnFailureListener(e -> {
                    //   showToast("Failed to update cancellation status: " + e.getMessage());
                });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDataListener != null) {
            userDataListener.remove();
        }
    }

    // Method to retrieve data from Firestore
    @SuppressLint("DefaultLocale")
    private void retrieveDataFromFirestore() {
        // Fetching data from Firestore
        userDataListener = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    // Handling data retrieval and updating UI
                    if (error != null) {
                        Log.e("BookingCancellation5Activity", "Error fetching user data: " + error.getMessage());
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String selectedTour5 = documentSnapshot.getString("selectedTour5");
                        String E_mail = documentSnapshot.getString("Email");
                        String selectedOption5 = documentSnapshot.getString("selectedRefundOption5");
                        double total5 = documentSnapshot.getDouble("totalAmount5");
                        // Use getDouble for numeric values
                        String selectedTouristNum5 = documentSnapshot.getString("selectedTouristNum5");

                        // Check if the views are not null before setting values
                        if (TotalTouristsText != null) {
                            TotalTouristsText.setText(selectedTouristNum5);
                        }
                        if (amountText != null) {
                            amountText.setText(String.format("₱%.2f", total5));
                        }

                        if (pickhouseText != null) {
                            pickhouseText.setText(selectedTour5);
                        }
                        if (nameText != null) {
                            nameText.setText(E_mail);
                        }

                    } else {
                        // showToast("No booking data found in Firestore.");
                    }
                });
    }
    // Save the selected option to Firestore
    private void saveOptionToFirestore() {
        // Initialize Firestore references
        String userId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            userDocRef
                    .update("selectedRefundOption5", selectedOption5)  // Update the selected refund option in Firestore
                    .addOnSuccessListener(aVoid -> {
                        // Show a toast message indicating that the option has been saved successfully
                        //Toast.makeText(BookingCancellation.this, "Selected refund option saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Show a toast message indicating an error in saving the option
                        //   Toast.makeText(BookingCancellation.this, "Error saving refund option: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Method to display toast messages
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}