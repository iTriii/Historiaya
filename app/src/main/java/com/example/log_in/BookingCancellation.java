package com.example.log_in;

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

public class BookingCancellation extends AppCompatActivity {

    Button withdrawbtn,continuebtn;
    Dialog dialog;

    TextView TotalTouristsText,nameText,amountText, pickhouseText, detailsclick;
    ImageButton backbutton;
    FirebaseAuth auth;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_cancellation);

        // Initializing views
        backbutton = findViewById(R.id.backbutton);
        withdrawbtn = findViewById(R.id.withdrawbtn);
        TotalTouristsText = findViewById(R.id.TotalTouristsText);
        nameText = findViewById(R.id.nameText);
        amountText = findViewById(R.id.amountText);
        pickhouseText = findViewById(R.id.pickhouseText);
        detailsclick = findViewById(R.id.detailsclick);
        continuebtn = findViewById(R.id.continuebtn);

        // Initializing Firebase instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backbutton.setOnClickListener(v -> {
                    Intent backIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
                    startActivity(backIntent);
                });

        // Click listener for viewing profile
        detailsclick.setOnClickListener(v -> {
            Intent intent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(intent);
        });
        // view button
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(intent);
        });
        // Initialization and actions for withdrawal dialog
        dialog = new Dialog(BookingCancellation.this);
        dialog.setContentView(R.layout.dialog_cancellation);
        dialog.setCancelable(false);

        Button notnowbtn = dialog.findViewById(R.id.notnowbtn);
        Button confirmbtn = dialog.findViewById(R.id.confirmbtn);

        notnowbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(backIntent);
            Toast.makeText(BookingCancellation.this, "Not now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        confirmbtn.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(confirmIntent);
            Toast.makeText(BookingCancellation.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        withdrawbtn.setOnClickListener(v -> {
            dialog.show();
        });

        // Initialization and actions for continue dialog
        dialog = new Dialog(BookingCancellation.this);
        dialog.setContentView(R.layout.dialog_cancellation_continue);
        dialog.setCancelable(false);

        Button btnNotnow = dialog.findViewById(R.id.btnNotnow);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnNotnow.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(backIntent);
            Toast.makeText(BookingCancellation.this, "Not Now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(confirmIntent);
            Toast.makeText(BookingCancellation.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        continuebtn.setOnClickListener(v -> {
            dialog.show();
        });

        // Fetching user data from Firestore
        retrieveDataFromFirestore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDataListener != null) {
            userDataListener.remove();
        }
    }

    // Method to retrieve data from Firestore
    private void retrieveDataFromFirestore() {
        // Fetching data from Firestore
        userDataListener = db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    // Handling data retrieval and updating UI
                    if (error != null) {
                        Log.e("BookingCancellationActivity", "Error fetching user data: " + error.getMessage());
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String selectedTour = documentSnapshot.getString("selectedTour");
                        // Use getDouble for numeric values
                        double total = documentSnapshot.getDouble("totalAmount");
                        String selectedTouristNum = documentSnapshot.getString("selectedTouristNum");

                        // Check if the views are not null before setting values
                        if (TotalTouristsText != null) {
                            TotalTouristsText.setText(selectedTouristNum);
                        }

                        if (amountText != null) {
                            amountText.setText(String.format("₱%.2f", total));
                        }

                        if (pickhouseText != null) {
                            pickhouseText.setText(selectedTour);
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
