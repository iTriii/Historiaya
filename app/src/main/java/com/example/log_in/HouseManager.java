package com.example.log_in;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.log_in.adapters.upcoming_reservation_adapter;
import com.example.log_in.adapters.upcoming_reservation_of_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HouseManager extends AppCompatActivity {
    RadioButton UpcomingHouseManager_Tab, HistoryHouseManager_tab;
    ScrollView UpcomingHouse_ScrollView, HistoryHouse_ScrollView;
    View wanHouse, toHouse;
    Button EditHousebtn, done;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView housemanager_upcomingRV, HistoryRV;
    ArrayList<upcoming_reservation_of_user> userArrayList;
    com.example.log_in.adapters.upcoming_reservation_adapter upcoming_reservation_adapter;
    com.example.log_in.adapters.historyHM_adapter historyHM_adapter;
    private ListenerRegistration userDataListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manager);

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


        // Set up RecyclerView and Adapter
        userArrayList = new ArrayList<>();
        upcoming_reservation_adapter = new upcoming_reservation_adapter(this, userArrayList, db);
        setUpRecyclerView();

        // Initialize Views
        done.setOnClickListener(view -> HistoryRV());

        EditHousebtn = findViewById(R.id.EditHousebtn);
        wanHouse = findViewById(R.id.wanHouse);
        toHouse = findViewById(R.id.toHouse);
        UpcomingHouse_ScrollView = findViewById(R.id.UpcomingHouse_ScrollView);
        UpcomingHouseManager_Tab = findViewById(R.id.UpcomingHouseManager_Tab);
        HistoryHouse_ScrollView = findViewById(R.id.HistoryHouse_ScrollView);
        HistoryHouseManager_tab = findViewById(R.id.HistoryHouseManager_tab);

        // Set Click Listeners
        HistoryHouseManager_tab.setOnClickListener(v -> HistoryHouseManager_tab());
        UpcomingHouseManager_Tab.setOnClickListener(v -> UpcomingHouseManager_Tab());

        // Start listening for data changes
        EventChangeListener();
        // Check for user authentication and initiate data retrieval
        if (user != null) {
            // Fetch and display user data from Firestore
            EventChangeListener();
        } else {
            finish();
        }
    }

    private void HistoryRV() {

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

                    userArrayList.clear(); // Clear the list before adding new data

                    // Handle data changes
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            upcoming_reservation_of_user user = dc.getDocument().toObject(upcoming_reservation_of_user.class);

                            // Fetching specific fields from the Firestore document
                            String reservedDate = user.getReservedDate();
                            String selectedTime = user.getselectedTime();
                            String selectedTour = user.getSelectedTour();

                            // Now you can use these fields as needed, for example, log them
                            Log.d("HouseManager", "Reserved Date: " + reservedDate);
                            Log.d("HouseManager", "Selected Time: " + selectedTime);
                            Log.d("HouseManager", "Selected Tour: " + selectedTour);

                            userArrayList.add(user);
                        }
                    }

                    // Update the RecyclerView adapter
                    upcoming_reservation_adapter.notifyDataSetChanged();
                });
    }

    private void setUpRecyclerView() {
        housemanager_upcomingRV = findViewById(R.id.housemanager_upcomingRV);
        housemanager_upcomingRV.setHasFixedSize(true);
        housemanager_upcomingRV.setBackgroundColor(Color.TRANSPARENT);
        housemanager_upcomingRV.setLayoutManager(new LinearLayoutManager(this));
        housemanager_upcomingRV.setAdapter(upcoming_reservation_adapter);

        HistoryRV = findViewById(R.id.HistoryRV);
        HistoryRV.setHasFixedSize(true);
        HistoryRV.setBackgroundColor(Color.TRANSPARENT);
        HistoryRV.setLayoutManager(new LinearLayoutManager(this));
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
        HistoryRV.setAdapter(historyHM_adapter);
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
