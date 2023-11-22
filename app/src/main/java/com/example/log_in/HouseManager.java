package com.example.log_in;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.log_in.adapters.upcoming_reservation_adpater;
import com.example.log_in.adapters.upcoming_reservation_of_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HouseManager extends AppCompatActivity {
    CalendarView CalendarHouseManager;
    RadioButton UpcomingHouseManager_Tab, HistoryHouseManager_tab;
    ScrollView UpcomingHouse_ScrollView, HistoryHouse_ScrollView;
    View wanHouse, toHouse;
    Button EditHousebtn;
    TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView housemanager_upcomingRV;
    ArrayList<upcoming_reservation_of_user> userArrayList;
    upcoming_reservation_adpater upcoming_reservation_adapter;
    private ListenerRegistration userDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manager);

        EditHousebtn = findViewById(R.id.EditHousebtn);
        wanHouse = findViewById(R.id.wanHouse);
        toHouse = findViewById(R.id.toHouse);


        userArrayList = new ArrayList<>();
        upcoming_reservation_adapter = new upcoming_reservation_adpater(this, userArrayList, db);
        setUpRecyclerView();
    }


    // Add data listener to load data from Firestore
    public void EventChangeListener() {
        db.collection("users")
                .orderBy("reservedDate", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Error", error.getMessage());
                        return;
                    }

                    // Handle data changes
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            upcoming_reservation_of_user user = dc.getDocument().toObject(upcoming_reservation_of_user.class);
                            userArrayList.add(user);
                        }
                    }

                    // Update the RecyclerView adapter
                    // Assuming your custom adapter is named upcoming_reservation_adapter
                    if (upcoming_reservation_adapter instanceof upcoming_reservation_adpater) {
                        upcoming_reservation_adpater customAdapter = (upcoming_reservation_adpater) upcoming_reservation_adapter;
                        customAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("HouseManager", "Adapter is not of expected type");
                    }

                });



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

            } catch (Exception e) {
                Log.e("ReceptionistActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
            }
        } else {
            finish();
        }
    }



    private void setUpRecyclerView() {

        housemanager_upcomingRV = findViewById(R.id.housemanager_upcomingRV);
        housemanager_upcomingRV.setHasFixedSize(true);
        housemanager_upcomingRV.setLayoutManager(new LinearLayoutManager(this));
        housemanager_upcomingRV.setAdapter((RecyclerView.Adapter) upcoming_reservation_adapter);
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