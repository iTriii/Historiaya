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

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class BookingCancellation extends AppCompatActivity {
//FOR UPDATE ONLY
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

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        // Initializing views
        backbutton = findViewById(R.id.backbtncancellation);
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

        //buttons for dialog
        Button notnowbtn = dialog.findViewById(R.id.notnowbtn);
        Button confirmbtn = dialog.findViewById(R.id.confirmbtn);

        notnowbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(backIntent);
            Toast.makeText(BookingCancellation.this, "Not now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        //CONFIRMATION BUTTON, NAVIGATE TO THE BOOKING DETAIL MAIN
        confirmbtn.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(confirmIntent);
            Toast.makeText(BookingCancellation.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        //SHOW THE DIALOG OF WITHDRAWAL
        withdrawbtn.setOnClickListener(v -> {
            dialog.show();
        });



        // Initialization and actions for continue dialog
        dialog = new Dialog(BookingCancellation.this);
        dialog.setContentView(R.layout.dialog_cancellation_continue);
        dialog.setCancelable(false);

        Button btnNotnow = dialog.findViewById(R.id.btnNotnow);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        continuebtn.setOnClickListener(v -> {
            dialog.show();
        });


        btnNotnow.setOnClickListener(v -> {
            Intent notnowIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(notnowIntent);
            Toast.makeText(BookingCancellation.this, "Not Now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingCancellation.this, BookingDetailMain.class);
            startActivity(confirmIntent);
            Toast.makeText(BookingCancellation.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
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
    @SuppressLint("DefaultLocale")
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
                        String E_mail = documentSnapshot.getString("Email");
                        double total = documentSnapshot.getDouble("totalAmount");
                        // Use getDouble for numeric values
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
                        if (nameText != null) {
                            nameText.setText(E_mail);
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
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, BookingDetailMain.class);
        startActivity(intent);
        finish();
    }

}
