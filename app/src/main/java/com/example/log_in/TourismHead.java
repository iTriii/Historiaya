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

public class TourismHead extends AppCompatActivity {

    RadioButton Upcoming_Tab, History_tab, Pending_Tab;
    ScrollView Upcoming_ScrollView, History_ScrollView, Pending_ScrollView;
    View wan, to, tre;
    TextView MonthTourHeadText,ArawTourHeadText,BahayTourHeadText;
    FirebaseUser user;
    FirebaseAuth auth;
    Button EditbtnTH;
    FirebaseFirestore db;

    private ListenerRegistration userDataListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_head);

        MonthTourHeadText = findViewById(R.id.MonthTourHeadText);
        BahayTourHeadText = findViewById(R.id.BahayTourHeadText);
        ArawTourHeadText = findViewById(R.id.ArawTourHeadText);
        wan = findViewById(R.id.wan);
        to = findViewById(R.id.to);
        tre = findViewById(R.id.tre);
        EditbtnTH = findViewById(R.id.EditbtnTH);


        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        Upcoming_ScrollView = findViewById(R.id.Upcoming_ScrollView);
        Upcoming_Tab = findViewById(R.id.Upcoming_Tab);
        Upcoming_Tab.setOnClickListener(v -> Upcoming_Tab());
        History_ScrollView = findViewById(R.id.History_ScrollView);
        History_tab = findViewById(R.id.History_tab);
        History_tab.setOnClickListener(v -> History_tab());
        Pending_ScrollView = findViewById(R.id.Pending_ScrollView);
        Pending_Tab = findViewById(R.id.Pending_Tab);
        Pending_Tab.setOnClickListener(v -> Pending_Tab());

        if (user != null) {
            // Fetch and display user data from Firestore
            try {
                fetchAndDisplayUserData();
            } catch (Exception e) {
                Log.e("TourismHeadActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
            }
        } else {
            finish();
        }
    }


    public void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("TourismHead", "Error fetching user data: " + error.getMessage());
                        return;
                    }

                    if (documentSnapshot.exists()) {
                        try {

                            String selectedTour = documentSnapshot.getString("selectedTour");// display data in texview
                            String reservedDate = documentSnapshot.getString("reservedDate");
                            //TextViews with the retrieved data
                            MonthTourHeadText.setText(reservedDate);
                            if (MonthTourHeadText != null) {
                                MonthTourHeadText.setText(reservedDate);
                            }
                            ArawTourHeadText.setText(reservedDate);
                            if (ArawTourHeadText != null) {
                                ArawTourHeadText.setText(reservedDate);
                            }
                            BahayTourHeadText.setText(selectedTour);
                            if (BahayTourHeadText != null) {
                                BahayTourHeadText.setText(selectedTour);
                            }
                        } catch (Exception e) {
                            Log.e("TourismHead", "Error in fetchAndDisplayUserData: " + e.getMessage());

                        }

                    }
                });

    }




    private void Upcoming_Tab() {
        Upcoming_Tab.setChecked(true);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        Upcoming_ScrollView.setVisibility(View.VISIBLE);
        History_tab.setChecked(false);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        History_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setChecked(false);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Pending_ScrollView.setVisibility(View.GONE);
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void History_tab() {
        Upcoming_Tab.setChecked(false);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Upcoming_ScrollView.setVisibility(View.GONE);
        History_tab.setChecked(true);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        History_ScrollView.setVisibility(View.VISIBLE);
        Pending_Tab.setChecked(false);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Pending_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

    }

    private void Pending_Tab() {
        Upcoming_Tab.setChecked(false);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Upcoming_ScrollView.setVisibility(View.GONE);
        History_tab.setChecked(false);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        History_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setChecked(true);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        Pending_ScrollView.setVisibility(View.VISIBLE);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }

    }




