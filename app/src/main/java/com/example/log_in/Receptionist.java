package com.example.log_in;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class Receptionist extends AppCompatActivity {

    RadioButton UpcomingRecep_Tab, HistoryRecep_tab;
    ScrollView UpcomingRecep_ScrollView, HistoryRecep_ScrollView;
    View wanRecep, toRecep;
    TextView BahayRecepText, ArawRecepText, MonthRecepText, MonthText, ArawText, BahayRecepText2;

    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
   public ListenerRegistration userDataListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist);

        BahayRecepText2 = findViewById(R.id.BahayRecepText2);
        ArawRecepText = findViewById(R.id.ArawText);
        MonthRecepText = findViewById(R.id.MonthText);
        wanRecep = findViewById(R.id.wanRecep);
        toRecep = findViewById(R.id.toRecep);


        UpcomingRecep_ScrollView = findViewById(R.id.Upcoming_ScrollView);
        UpcomingRecep_Tab = findViewById(R.id.UpcomingRecep_Tab);
        UpcomingRecep_Tab.setOnClickListener(v -> UpcomingRecep_Tab());
        HistoryRecep_ScrollView = findViewById(R.id.History_ScrollView);
        HistoryRecep_tab = findViewById(R.id.HistoryRecep_tab);
        HistoryRecep_tab.setOnClickListener(v -> HistoryRecep_tab());


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

    private void UpcomingRecep_Tab() {
        UpcomingRecep_Tab.setChecked(true);
        UpcomingRecep_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        UpcomingRecep_ScrollView.setVisibility(View.VISIBLE);
        HistoryRecep_tab.setChecked(false);
        HistoryRecep_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryRecep_ScrollView.setVisibility(View.GONE);
        wanRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        toRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void HistoryRecep_tab() {
        UpcomingRecep_Tab.setChecked(false);
        UpcomingRecep_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        UpcomingRecep_ScrollView.setVisibility(View.GONE);
        HistoryRecep_tab.setChecked(true);
        HistoryRecep_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryRecep_ScrollView.setVisibility(View.VISIBLE);
        wanRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        toRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
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
                            MonthText.setText(reservedDate);
                            if (MonthText != null) {
                                MonthText.setText(reservedDate);
                            }
                            ArawText.setText(reservedDate);
                            if (ArawText != null) {
                                ArawText.setText(reservedDate);
                            }
                            BahayRecepText.setText(selectedTour);
                            if (BahayRecepText != null) {
                                BahayRecepText.setText(selectedTour);
                            }
                        } catch (Exception e) {
                            Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());

                        }

                    }
                });
    }
}






