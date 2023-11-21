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

public class GalaRodriguezAdmin extends AppCompatActivity {

    RadioButton UpcomingGala_Tab, HistoryGala_tab;
    ScrollView UpcomingGala_ScrollView, HistoryGala_ScrollView;
    View wanGala,toGala;
    TextView MonthUpcomingGalaText, BahayUpcomingGalaText,ArawUpcomingGalaText,MontHistoGalaText,BahayHistoGalaText,ArawHistoGalaText;
     Calendar CalendarGala;
    FirebaseUser user;
    Button EditGalabtn;
    FirebaseAuth auth;
    FirebaseFirestore db;
    public ListenerRegistration userDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gala_rodriguez_admin);



        EditGalabtn = findViewById(R.id.EditGalabtn);
        wanGala = findViewById(R.id.wanGala);
        toGala = findViewById(R.id.toGala);
        MonthUpcomingGalaText = findViewById(R.id.MonthUpcomingGalaText);
        BahayUpcomingGalaText = findViewById(R.id.BahayUpcomingGalaText);
        ArawUpcomingGalaText = findViewById(R.id.ArawUpcomingGalaText);
        MontHistoGalaText = findViewById(R.id.MontHistoGalaText);
        BahayHistoGalaText = findViewById(R.id.BahayHistoGalaText);
        ArawHistoGalaText = findViewById(R.id.ArawHistoGalaText);



        UpcomingGala_ScrollView = findViewById(R.id.UpcomingGala_ScrollView);
        UpcomingGala_Tab = findViewById(R.id.UpcomingGala_Tab);
        UpcomingGala_Tab.setOnClickListener(v -> UpcomingGala_Tab());
        HistoryGala_ScrollView = findViewById(R.id.HistoryGala_ScrollView);
        HistoryGala_tab = findViewById(R.id.HistoryGala_tab);
        HistoryGala_tab.setOnClickListener(v -> HistoryGala_tab());


        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


        if (user != null) {
            // Fetch and display user data from Firestore
            try {
                fetchAndDisplayUserData();
            } catch (Exception e) {
                Log.e("GalaRodriguezAdminActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
            }
        } else {
            finish();
        }
    }

    private void HistoryGala_tab() {
        UpcomingGala_Tab.setChecked(true);
        UpcomingGala_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        UpcomingGala_ScrollView.setVisibility(View.VISIBLE);
        HistoryGala_tab.setChecked(false);
        HistoryGala_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryGala_ScrollView.setVisibility(View.GONE);
        wanGala.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        toGala.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void UpcomingGala_Tab() {
        UpcomingGala_Tab.setChecked(false);
        UpcomingGala_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        UpcomingGala_ScrollView.setVisibility(View.GONE);
        HistoryGala_tab .setChecked(true);
        HistoryGala_tab .setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryGala_ScrollView.setVisibility(View.VISIBLE);
        wanGala.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        toGala.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
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
                            MonthUpcomingGalaText.setText(reservedDate); // month Upcoming
                            if (MonthUpcomingGalaText != null) {
                                MonthUpcomingGalaText.setText(reservedDate);
                            }
                            BahayUpcomingGalaText.setText(reservedDate);
                            if (BahayUpcomingGalaText != null) {
                                BahayUpcomingGalaText.setText(reservedDate);
                            }
                            ArawUpcomingGalaText.setText(selectedTour);
                            if (ArawUpcomingGalaText != null) {
                                ArawUpcomingGalaText.setText(selectedTour);
                            }
                            MontHistoGalaText.setText(reservedDate);    // Month history
                            if (MontHistoGalaText != null){
                                MontHistoGalaText.setText(reservedDate);
                            }
                            BahayHistoGalaText.setText(selectedTour);
                            if (BahayHistoGalaText != null){
                                BahayHistoGalaText.setText(selectedTour);
                            }
                            ArawHistoGalaText.setText(reservedDate);
                            if (ArawHistoGalaText != null){
                                ArawHistoGalaText.setText(reservedDate);
                            }
                        } catch (Exception e) {
                            Log.e("GalaRodriguezActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());

                        }

                    }
                });
    }
}






