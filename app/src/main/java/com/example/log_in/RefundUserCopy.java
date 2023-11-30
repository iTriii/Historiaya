package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
//FOR UPDATE ONLY

public class RefundUserCopy extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView pickhouseRefText, nameRefText, AmountRefText;
    private Button Dbtn, backbutton;
    private ImageButton backbuttonRef;
    private TextView detailsclickRef, OptionText,ReasonRefText;  // Add OptionText
    private ListenerRegistration userDataListener;
    private FirebaseUser user;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_user_copy);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);


        pickhouseRefText = findViewById(R.id.pickhouseRefText);
        nameRefText = findViewById(R.id.nameRefText);
        Dbtn = findViewById(R.id.Dbtn);
        AmountRefText = findViewById(R.id.AmountRefText);
        backbuttonRef = findViewById(R.id.backbuttonRef);
        detailsclickRef = findViewById(R.id.detailsclickRef);
        ReasonRefText = findViewById(R.id.ReasonRefText);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        // NAVIGATE TO Main2
        Dbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RefundUserCopy.this, Main2.class);
            startActivity(intent);
        });

        detailsclickRef.setOnClickListener(v -> {
            Intent intent = new Intent(RefundUserCopy.this, Profile.class);
            startActivity(intent);
        });

        backbuttonRef.setOnClickListener(v -> {
            Intent intent = new Intent(RefundUserCopy.this, Profile.class);
            startActivity(intent);
        });

        // Retrieve and display data from Firestore
        retrieveDataFromFirestore();
    }

    private void retrieveDataFromFirestore() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("RefundUserCopy", "Error fetching user data: " + error.getMessage());
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        try {
                            String selectedTour = documentSnapshot.getString("selectedTour");
                            String selectedOption = documentSnapshot.getString("selectedRefundOption");
                            String E_mail = documentSnapshot.getString("Email");
                            double total = documentSnapshot.getDouble("totalAmount");

                            // TextViews with the retrieved data
                            if (AmountRefText != null) {
                                AmountRefText.setText(String.format("₱%.2f", total));
                            }
                            if (pickhouseRefText != null) {
                                pickhouseRefText.setText(selectedTour);
                            }
                            if (nameRefText != null) {
                                nameRefText.setText(E_mail);
                            }
                            if (ReasonRefText != null){
                                ReasonRefText.setText(selectedOption);
                            }
                        } catch (Exception e) {
                            Log.e("RefundUserCopy", "Error in retrieveDataFromFirestore: " + e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore snapshot listener when the activity is destroyed
        if (userDataListener != null) {
            // Remove the listener
            userDataListener.remove();
        }
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, BookingDetailMain.class);
        startActivity(intent);
        finish();
    }

}
