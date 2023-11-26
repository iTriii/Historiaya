package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Receptionist extends AppCompatActivity {


    RadioButton UpcomingRecep_Tab, HistoryRecep_tab;
    ScrollView UpcomingRecep_ScrollView, HistoryRecep_ScrollView;
    View wanRecep, toRecep;

    public ListenerRegistration userDataListener;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView ReceptionistUpcoming_RecyclerView, ReceptionistHistory_RecyclerView;
    private ProgressDialog progressDialog;
    private ArrayList<User> userArrayList;
    Receptionist_Adapter_History Receptionist_Adapter_History;
    Receptionist_Upcoming_Adapter Receptionist_Upcoming_Adapter;
    ImageView Event_Sched, calendarV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist);
    // Inside onCreate or wherever you initialize your FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        //VIEW
        wanRecep = findViewById(R.id.wanRecep);
        toRecep = findViewById(R.id.toRecep);

    // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        
        // Initialize RecyclerViews and Adapters
        ReceptionistUpcoming_RecyclerView = findViewById(R.id.ReceptionistUpcoming_RecyclerView);
        ReceptionistHistory_RecyclerView = findViewById(R.id.ReceptionistHistory_RecyclerView);
        userArrayList = new ArrayList<>();


        Receptionist_Adapter_History = new Receptionist_Adapter_History(this, userArrayList, db);
        Receptionist_Upcoming_Adapter = new Receptionist_Upcoming_Adapter(this, userArrayList, db);

        Event_Sched = findViewById(R.id.Event_Sched);
        calendarV = findViewById(R.id.calendarV);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Calendar/calendar_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(Calendar-> {
            Glide.with(this)
                    .load(Calendar) // Provide the actual download URL obtained from Firebase Storage
                    .into(Event_Sched);

            // Hide calendarV ImageView after loading the image into Event_Sched
            calendarV.setVisibility(View.GONE);
        }).addOnFailureListener(exception -> {
            // Handle any errors that may occur while fetching the image
            showToast("Failed to fetch image: " + exception.getMessage());
        });



        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


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

    private void setUpTabsAndViews() {
        UpcomingRecep_ScrollView = findViewById(R.id.Upcoming_ScrollView);
        UpcomingRecep_Tab = findViewById(R.id.UpcomingRecep_Tab);
        UpcomingRecep_Tab.setOnClickListener(v -> UpcomingRecep_Tab());
        HistoryRecep_ScrollView = findViewById(R.id.History_ScrollView);
        HistoryRecep_tab = findViewById(R.id.HistoryRecep_tab);
        HistoryRecep_tab.setOnClickListener(v -> HistoryRecep_tab());

    }

    private void setUpRecyclerView() {
        // RecyclerView setup
        ReceptionistUpcoming_RecyclerView.setHasFixedSize(true);
        ReceptionistUpcoming_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReceptionistUpcoming_RecyclerView.setAdapter(Receptionist_Upcoming_Adapter);

        ReceptionistHistory_RecyclerView.setHasFixedSize(true);
        ReceptionistHistory_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReceptionistHistory_RecyclerView.setAdapter(Receptionist_Adapter_History);
    }

    private void EventChangeListener() {
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
                                Log.e("Receptionist", "Error in fetchAndDisplayUserData: " + e.getMessage());
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

                        Receptionist_Upcoming_Adapter.notifyDataSetChanged();
                        Receptionist_Adapter_History.notifyDataSetChanged();

                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
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


    private void HistoryRecep_tab() {
        UpcomingRecep_Tab.setChecked(false);
        UpcomingRecep_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        UpcomingRecep_ScrollView.setVisibility(View.GONE);
        HistoryRecep_tab.setChecked(false);
        HistoryRecep_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryRecep_ScrollView.setVisibility(View.VISIBLE);
        wanRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        toRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }

    private void UpcomingRecep_Tab() {
        UpcomingRecep_Tab.setChecked(true);
        UpcomingRecep_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        UpcomingRecep_ScrollView.setVisibility(View.VISIBLE);
        HistoryRecep_tab.setChecked(true);
        HistoryRecep_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryRecep_ScrollView.setVisibility(View.GONE);
        wanRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        toRecep.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

    }

    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("Receptionist", "Error fetching user data: " + error.getMessage());
                        return;
                    }

                    // Handle the document snapshot here
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Extract data from the document snapshot
                        if (documentSnapshot.contains("status")) {
                            String status = documentSnapshot.getString("status");

                            // Display the data or perform other actions
                            // For example, you can update a TextView with the fetched data
                            // textView.setText(userData);
                        }
                    }
                });
    }



    @Override
    protected void onDestroy() {
        // Remove the snapshot listener when the activity is destroyed
        if (userDataListener != null) {
            userDataListener.remove();
        }

        super.onDestroy();

    }
}