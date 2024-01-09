package com.example.log_in;

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

public class BookingRefund extends AppCompatActivity {
    //FOR UPDATE ONLY
    Spinner spinRefund;
    Button btnsub;
    ImageButton back;
    Dialog dialog;
    TextView detailsclick, amountRefundText, pickhouseRefundText, nameRefundText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String selectedOption;
    private Object userDataListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_refund);

        spinRefund = findViewById(R.id.spinRefund);
        back = findViewById(R.id.backbtnRefund);
        btnsub = findViewById(R.id.btnsub);
        detailsclick = findViewById(R.id.detailsclick);
        amountRefundText = findViewById(R.id.amountRefundText);
        pickhouseRefundText = findViewById(R.id.pickhouseRefundText);
        nameRefundText = findViewById(R.id.nameRefundText);


        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Back button
        back.setOnClickListener(v -> {
            Intent intent = new Intent(BookingRefund.this, BookingDetailMain.class);
            startActivity(intent);
        });

        // view button
        detailsclick.setOnClickListener(v -> {
            Intent intent = new Intent(BookingRefund.this, BookingDetailMain.class);
            startActivity(intent);
        });

        // Initialize the dialog for continue btn
        dialog = new Dialog(BookingRefund.this);
        dialog.setContentView(R.layout.dialog_refund);
        dialog.setCancelable(false);

        Button confirmDialogbtn = dialog.findViewById(R.id.confirmDialogbtn);

        confirmDialogbtn.setOnClickListener(v -> {
            Intent confirmIntent = new Intent(BookingRefund.this, RefundUserCopy.class); // NAVIGATE TO REFUND USER COPY ACTIVITY
            startActivity(confirmIntent);
            Toast.makeText(BookingRefund.this, "Confirm Refund, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.show();
        });

        // SUBMIT BUTTON
        btnsub.setOnClickListener(v -> {
            dialog.show();
            saveOptionToFirestore(); //SAVE TO THE FIRESTORE
        });

        // spinner adapter for refund
        ArrayAdapter<CharSequence> refundAdapter = ArrayAdapter.createFromResource(
                this, R.array.refund_options, android.R.layout.simple_spinner_item);
        refundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRefund.setAdapter(refundAdapter);
        spinRefund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected refund option
                selectedOption = parentView.getItemAtPosition(position).toString();
               // Toast.makeText(BookingRefund.this, "Selected Refund Option: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });// Retrieve and display data from Firestore
        retrieveDataFromFirestore();
        // Update cancellation status to "pendingCancellation"
        updateRefundStatus("pendingRefund");
    }

    private void updateRefundStatus(String status) {
            // Update cancellation status in Firestore
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users")
                    .document(userId)
                    .update("RefundStatus", status)
                    .addOnSuccessListener(aVoid -> {
                        //   showToast("Refund status updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        //   showToast("Failed to update Refund status: " + e.getMessage());
                    });
        }



    private void retrieveDataFromFirestore() {
        // Save the selected option to Firestore
            String userId = mAuth.getCurrentUser().getUid();
            DocumentReference userDocRef = db.collection("users").document(userId);
            userDataListener = userDocRef.addSnapshotListener((documentSnapshot, error) -> {


                if (documentSnapshot.exists()) {
                    try {
                        String selectedTour = documentSnapshot.getString("selectedTour");
                        String selectedOption = documentSnapshot.getString("selectedRefundOption");
                        String E_mail = documentSnapshot.getString("Email");
                        double total = documentSnapshot.getDouble("totalAmount");

                        // TextViews with the retrieved data
                        if (amountRefundText != null) {
                            amountRefundText.setText(String.format("₱%.2f", total));
                        }
                        if (pickhouseRefundText != null) {
                            pickhouseRefundText.setText(selectedTour);
                        }
                        if (nameRefundText != null) {
                            nameRefundText.setText(E_mail);
                        }
                    } catch (Exception e) {
                        Log.e("BookingRefund", "Error in retrieveDataFromFirestore: " + e.getMessage());
                    }
                }
            });
        }


    // Save the selected option to Firestore
    private void saveOptionToFirestore() {
        // Initialize Firestore references
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userDocRef
                    .update("selectedRefundOption", selectedOption)  // Update the selected refund option in Firestore
                    .addOnSuccessListener(aVoid -> {
                        //   Toast.makeText(BookingRefund.this, "Selected refund option saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(BookingRefund.this, "Error saving refund option: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
