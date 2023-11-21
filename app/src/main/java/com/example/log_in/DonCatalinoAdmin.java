package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class DonCatalinoAdmin extends AppCompatActivity {
    RadioButton UpcomingDonCat_Tab, HistoryDonCat_tab;
    ScrollView UpcomingDonCat_ScrollView, HistoryDonCat_ScrollView;
    View wanDonCat, toDonCat;
    TextView MonthUpcomingDonCatText,BahayUpcomingDonCatText,ArawUpcomingDonCatText,MontHistoDonCatText,BahayHistoDonCatTex,ArawHistoDonCatText;
    Button EditDonCatbtn, bakbtn;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    public ListenerRegistration userDataListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_catalino_admin);


        BahayUpcomingDonCatText = findViewById(R.id.BahayUpcomingDonCatText);
        ArawUpcomingDonCatText = findViewById(R.id.ArawUpcomingDonCatText);
        MonthUpcomingDonCatText = findViewById(R.id.MonthUpcomingDonCatText);
        MontHistoDonCatText = findViewById(R.id.MontHistoDonCatText);
        BahayHistoDonCatTex = findViewById(R.id.BahayHistoDonCatText);
        ArawHistoDonCatText = findViewById(R.id.ArawHistoDonCatText);

        wanDonCat = findViewById(R.id.wanDonCat);
        toDonCat = findViewById(R.id.toDonCat);



        UpcomingDonCat_ScrollView = findViewById(R.id.UpcomingDonCat_ScrollView);
        UpcomingDonCat_Tab = findViewById(R.id.UpcomingDonCat_Tab);
        UpcomingDonCat_Tab.setOnClickListener(v -> UpcomingDonCat_Tab());
        HistoryDonCat_ScrollView= findViewById(R.id.HistoryDonCat_ScrollView);
        HistoryDonCat_tab= findViewById(R.id.HistoryDonCat_tab);
        HistoryDonCat_tab.setOnClickListener(v -> HistoryDonCat_tab());


        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        // Back button
        bakbtn .setOnClickListener(v -> {
            Intent intent = new Intent(DonCatalinoAdmin.this,Admin.class);
            startActivity(intent);
        });

        if (user != null) {
            // Fetch and display user data from Firestore
            try {
                fetchAndDisplayUserData();
            } catch (Exception e) {
                Log.e("ReceptionistActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
            }
        } else {
            finish();
        }
    }



    private void HistoryDonCat_tab() {
        UpcomingDonCat_Tab.setChecked(true);
        UpcomingDonCat_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        UpcomingDonCat_ScrollView.setVisibility(View.VISIBLE);
        HistoryDonCat_tab.setChecked(false);
        HistoryDonCat_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryDonCat_ScrollView.setVisibility(View.GONE);
        wanDonCat.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        toDonCat.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void UpcomingDonCat_Tab() {
        UpcomingDonCat_Tab.setChecked(false);
        UpcomingDonCat_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        UpcomingDonCat_ScrollView.setVisibility(View.GONE);
        HistoryDonCat_tab.setChecked(true);
        HistoryDonCat_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryDonCat_ScrollView.setVisibility(View.VISIBLE);
        wanDonCat.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        toDonCat.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }


    //getting the data from firestore (users booking)
    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("ProfileActivity", "Error fetching user data: " + error.getMessage());
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        try {

                            String selectedTour = documentSnapshot.getString("selectedTour");// display data in texview
                            String reservedDate = documentSnapshot.getString("reservedDate");
                            //TextViews with the retrieved data
                            MonthUpcomingDonCatText.setText(reservedDate); //UPCOMING TEXT
                            if ( MonthUpcomingDonCatText!= null) {
                                MonthUpcomingDonCatText.setText(reservedDate);
                            }
                            ArawUpcomingDonCatText.setText(reservedDate);
                            if (ArawUpcomingDonCatText != null) {
                                ArawUpcomingDonCatText.setText(reservedDate);
                            }
                            BahayUpcomingDonCatText.setText(selectedTour);
                            if (BahayUpcomingDonCatText != null) {
                                BahayUpcomingDonCatText.setText(selectedTour);
                            }
                            MontHistoDonCatText.setText(reservedDate); // HISTORY TEXTVIEW
                            if (MontHistoDonCatText  != null){
                                MontHistoDonCatText.setText(reservedDate);
                            }
                            BahayHistoDonCatTex.setText(selectedTour);
                            if (BahayHistoDonCatTex != null){
                                BahayHistoDonCatTex.setText(selectedTour);
                            }
                            ArawHistoDonCatText.setText(reservedDate);
                            if (ArawHistoDonCatText != null){
                                ArawHistoDonCatText.setText(reservedDate);
                            }
                        } catch (Exception e) {
                            Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());

                        }

                    }
                });
    }
}






