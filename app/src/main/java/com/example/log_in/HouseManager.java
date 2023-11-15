package com.example.log_in;

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

import java.util.Calendar;

public class HouseManager extends AppCompatActivity {
Calendar CalendarHouseManager;
    RadioButton UpcomingHouseManager_Tab, HistoryHouseManager_tab;
    ScrollView UpcomingHouse_ScrollView, HistoryHouse_ScrollView;
    View wanHouse, toHouse;
    Button EditHousebtn;
    TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private ListenerRegistration userDataListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manager);


        MonthHouseManagerText.findViewById(R.id.MonthHouseManagerText);
        BahayHouseManagerText.findViewById(R.id.BahayHouseManagerText);
        ArawHouseManagerText.findViewById(R.id.ArawHouseManagerText);

        EditHousebtn.findViewById(R.id.EditHousebtn);

        wanHouse.findViewById(R.id.wanHouse);
        toHouse.findViewById(R.id.toHouse);

        UpcomingHouse_ScrollView= findViewById(R.id.UpcomingHouse_ScrollView);
        UpcomingHouseManager_Tab = findViewById(R.id.UpcomingHouseManager_Tab);
        UpcomingHouseManager_Tab.setOnClickListener(v -> UpcomingHouseManager_Tab());
        HistoryHouse_ScrollView = findViewById(R.id.HistoryHouse_ScrollView);
        HistoryHouseManager_tab = findViewById(R.id.HistoryHouseManager_tab);
        HistoryHouseManager_tab.setOnClickListener(v -> HistoryHouseManager_tab());

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


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
                            MonthHouseManagerText.setText(reservedDate);
                            if (MonthHouseManagerText != null) {
                                MonthHouseManagerText.setText(reservedDate);
                            }
                            BahayHouseManagerText.setText(reservedDate);
                            if (BahayHouseManagerText != null) {
                                BahayHouseManagerText.setText(reservedDate);
                            }
                            ArawHouseManagerText.setText(selectedTour);
                            if (ArawHouseManagerText != null) {
                                ArawHouseManagerText.setText(selectedTour);
                            }
                        } catch (Exception e) {
                            Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());

                        }

                    }
                });

    }


    private void HistoryHouseManager_tab() {
        UpcomingHouseManager_Tab.setChecked(false);
        UpcomingHouseManager_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        UpcomingHouse_ScrollView.setVisibility(View.GONE);
        HistoryHouseManager_tab.setChecked(true);
        HistoryHouseManager_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryHouse_ScrollView.setVisibility(View.VISIBLE);
        wanHouse.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        toHouse.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }

    private void UpcomingHouseManager_Tab() {
        UpcomingHouseManager_Tab.setChecked(true);
        UpcomingHouseManager_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        UpcomingHouse_ScrollView.setVisibility(View.VISIBLE);
        HistoryHouseManager_tab.setChecked(false);
        HistoryHouseManager_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryHouse_ScrollView.setVisibility(View.GONE);
        wanHouse.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        toHouse.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }






}