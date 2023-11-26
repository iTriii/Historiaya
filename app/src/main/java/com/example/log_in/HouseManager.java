package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

public class HouseManager extends AppCompatActivity {


    private ImageButton chatbtn;
    RadioButton UpcomingHouseManager_Tab, HistoryHouseManager_tab;
    ScrollView UpcomingHouse_ScrollView, HistoryHouse_ScrollView;
    View wanHouse, toHouse;
    Button EditHousebtn, done;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView UpcomingHouseMager_RecyclerView, HistoryHouseManager_RecyclerView;

    private ArrayList<User> userArrayList;

    private ProgressDialog progressDialog;
    historyAdapterHM historyAdapterHM;
    upcoming_reservation_adapter upcoming_reservation_adapter;

    private CalendarView CalendarHouseManager;
    private Button editbtnTH;
    private Object Email;
    private ListenerRegistration userDataListener;
    ImageView Event_Sched, calendarV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manager);

        // Configure Crisp
        Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");

        // Chat button
        chatbtn = findViewById(R.id.chatbtn);
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(HouseManager.this, ChatActivity.class);
            startActivity(intent);
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Check if currentUser is not null before setting Crisp user email
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            // Set user attributes in Crisp
            Crisp.setUserEmail(userEmail);
        }

        wanHouse = findViewById(R.id.wanHouse);
        toHouse = findViewById(R.id.toHouse);
        // Inside onCreate or wherever you initialize your FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        Event_Sched = findViewById(R.id.Event_Sched);
        calendarV = findViewById(R.id.calendarV);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Calendar/calendar_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(Calendar-> {
            Glide.with(this)
                    .load(Calendar) // Provide the actual download URL obtained from Firebase Storage
                    .into(Event_Sched);

            calendarV.setVisibility(View.GONE);
        }).addOnFailureListener(exception -> {
            showToast("Failed to fetch image: " + exception.getMessage());
        });



        // Initialize RecyclerViews and Adapters
        UpcomingHouseMager_RecyclerView = findViewById(R.id.UpcomingHouseMager_RecyclerView);
        HistoryHouseManager_RecyclerView = findViewById(R.id.HistoryHouseManager_RecyclerView);
        userArrayList = new ArrayList<>();


        upcoming_reservation_adapter = new upcoming_reservation_adapter(this, userArrayList, db);
        historyAdapterHM = new historyAdapterHM(this, userArrayList, db);

        FirebaseAuth auth = FirebaseAuth.getInstance();


        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference usersDocRef = db.collection("users");
            DocumentReference userDocRef = usersDocRef.document(userId);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Document exists, update fields
                        userDocRef.update(User.FIELD_STATUS, "")
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));
                    } else {
                        Map<String, Object> user = new HashMap<>();
                        user.put(User.FIELD_USER_ID, userId);
                        user.put(User.FIELD_EMAIL, currentUser.getEmail()); // Ensure Email is not null
                        usersDocRef.document(userId).set(user)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully written!"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));

                    }
                } else {
                    // Handle the exception
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("Firestore", "Error getting document", exception);
                    }
                }
            });
        }


        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        EventChangeListener();// Add data listener to load data from Firestore
        setUpRecyclerView(); // Set up RecyclerView and Adapter
        setUpTabsAndViews(); // Set up tabs and views
    }

    private void showToast(String s) {
    }


    //CRISP
    private void startCrispChat() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();

            // Set user attributes in Crisp
            Crisp.setUserEmail(userEmail);
        }

        // Start Crisp chat
        Intent chatIntent = new Intent(this, ChatActivity.class);
        startActivity(chatIntent);
    }

    private void setUpTabsAndViews() {
        UpcomingHouse_ScrollView = findViewById(R.id.UpcomingHouse_ScrollView);
        UpcomingHouseManager_Tab = findViewById(R.id.UpcomingHouseManager_Tab);
        UpcomingHouseManager_Tab.setOnClickListener(v -> UpcomingHouseManager_Tab());
        HistoryHouse_ScrollView = findViewById(R.id.HistoryHouse_ScrollView);
        HistoryHouseManager_tab = findViewById(R.id.HistoryHouseManager_tab);
        HistoryHouseManager_tab.setOnClickListener(v -> HistoryHouseManager_tab());
        UpcomingHouseManager_Tab.setOnClickListener(v -> UpcomingHouseManager_Tab());

    }

    private void setUpRecyclerView() {
        // RecyclerView setup
        UpcomingHouseMager_RecyclerView.setHasFixedSize(true);
        UpcomingHouseMager_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        UpcomingHouseMager_RecyclerView.setAdapter(upcoming_reservation_adapter);

        HistoryHouseManager_RecyclerView.setHasFixedSize(true);
        HistoryHouseManager_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        HistoryHouseManager_RecyclerView.setAdapter(historyAdapterHM);
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

    // Add data listener to load data from Firestore
    public void EventChangeListener() {
        db.collection("users").orderBy("reservedDate", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    // Initialize HashMaps for users
                    final HashMap<String, User> userHashMap = new HashMap<>();
                    final HashMap<String, User> upcomingUserHashMap = new HashMap<>();
                    final HashMap<String, User> historyUserHashMap = new HashMap<>();


                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            try {
                                fetchAndDisplayUserData();
                            } catch (Exception e) {
                                Log.e("HouseManager", "Error in fetchAndDisplayUserData: " + e.getMessage());
                            }

                            if (!progressDialog.isShowing()) progressDialog.show();
                            Log.e("Error", error.getMessage());
                            return;
                        }

                        // Handle data changes
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    User user = dc.getDocument().toObject(User.class);
                                    // Set the user ID
                                    user.setUserId(dc.getDocument().getId());
                                    userHashMap.put(user.getEmail(), user);
                                    if (user.isUpcoming()) {
                                        upcomingUserHashMap.put(user.getEmail(), user);
                                    } else {
                                        historyUserHashMap.put(user.getEmail(), user);
                                    }
                                    break;
                                case REMOVED:
                                    User userRemoved = dc.getDocument().toObject(User.class);
                                    userHashMap.remove(userRemoved.getEmail());
                                    upcomingUserHashMap.remove(userRemoved.getUserId());
                                    historyUserHashMap.remove(userRemoved.getUserId());
                                    break;
                            }
                        }

                        userArrayList.clear();
                        userArrayList.addAll(userHashMap.values());

                        upcoming_reservation_adapter.notifyDataSetChanged();
                        historyAdapterHM.notifyDataSetChanged();

                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                });
    }

    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("HouseManager", "Error fetching user data: " + error.getMessage());
                        return;
                    }

                    // Handle the document snapshot here
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Extract data from the document snapshot
                        if (documentSnapshot.contains("status")) {
                            String status = documentSnapshot.getString("status");

                        }
                    }
                });
    }
}

