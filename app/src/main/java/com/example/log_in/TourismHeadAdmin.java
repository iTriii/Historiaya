package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TourismHeadAdmin extends AppCompatActivity {
    private RadioButton Upcoming_Tab, History_tab, Pending_Tab;
    private ScrollView Upcoming_ScrollView, History_ScrollView, Pending_ScrollView;
    private View wan, to, tre;
    private FirebaseFirestore db;
    private ImageButton backbutton;

    private RecyclerView Pending_RecyclerView, Upcoming_RecyclerView, History_RecyclerView;
    private ArrayList<User> userArrayList;
    private MyAdapter myAdapter;
    private com.example.log_in.UpcomingAdapter UpcomingAdapter;
    private com.example.log_in.HistoryAdapter HistoryAdapter;
    FirebaseUser user;
    FirebaseAuth auth;

    private ProgressDialog progressDialog;

    private CalendarView calendarTourismHead;
    private Button SaveTH;
    private Object Email;
    private ListenerRegistration userDataListener;

    private customizedCalendar customizedCalendar;
    private String selectedDate;
    EditText SaveSchedule;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_head_admin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        SaveSchedule = findViewById(R.id.SaveSchedule);

        calendarTourismHead = findViewById(R.id.CalendarTourismHead);
        calendarTourismHead.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofMonth) {

                selectedDate = Integer.toString(year) + Integer.toString(month) + Integer.toString(dayofMonth);
                ReadDatabase();
            }
        });
        try{
            customizedCalendar = new customizedCalendar(this, "CalendarDatabase", null ,1);
            sqLiteDatabase = customizedCalendar.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE EventCalendar (Date TEXT, Event TEXT)");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        SaveTH = findViewById(R.id.SaveTH);

        // Initialize Firebase Authentication and Firestore
        wan = findViewById(R.id.wan);
        to = findViewById(R.id.to);
        tre = findViewById(R.id.tre);

        backbutton = findViewById(R.id.backbutton);

        // back button
        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(TourismHeadAdmin.this, Admin.class);
            startActivity(intent);
        });

        // Initialize RecyclerViews and Adapters
        Pending_RecyclerView = findViewById(R.id.Pending_RecyclerView);
        Upcoming_RecyclerView = findViewById(R.id.Upcoming_RecyclerView);
        History_RecyclerView = findViewById(R.id.History_RecyclerView);

        userArrayList = new ArrayList<>();

        myAdapter = new MyAdapter(this, userArrayList, db);
        UpcomingAdapter = new UpcomingAdapter(this, userArrayList, db);
        HistoryAdapter = new HistoryAdapter(this, userArrayList, db);


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
                        user.put(User.FIELD_EMAIL, Email); // Ensure Email is not null
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
    private void setUpTabsAndViews() {

            // Set up tabs and views
            Upcoming_ScrollView = findViewById(R.id.Upcoming_ScrollView);
            Upcoming_Tab = findViewById(R.id.Upcoming_Tab);
            Upcoming_Tab.setOnClickListener(v -> Upcoming_Tab());
            History_ScrollView = findViewById(R.id.History_ScrollView);
            History_tab = findViewById(R.id.History_tab);
            History_tab.setOnClickListener(v -> History_tab());
            Pending_ScrollView = findViewById(R.id.Pending_ScrollView);
            Pending_Tab = findViewById(R.id.Pending_Tab);
            Pending_Tab.setOnClickListener(v -> Pending_Tab());
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
            // Show only Upcoming RecyclerView
            Pending_RecyclerView.setVisibility(View.GONE);
            Upcoming_RecyclerView.setVisibility(View.VISIBLE);
            History_RecyclerView.setVisibility(View.GONE);
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
            wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
            to.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
            // Show only History RecyclerView
            Pending_RecyclerView.setVisibility(View.GONE);
            Upcoming_RecyclerView.setVisibility(View.GONE);
            History_RecyclerView.setVisibility(View.VISIBLE);
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
            wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
            to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
            tre.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            // Show only Pending RecyclerView
            Pending_RecyclerView.setVisibility(View.VISIBLE);
            Upcoming_RecyclerView.setVisibility(View.GONE);
            History_RecyclerView.setVisibility(View.GONE);

            // Set a date change listener for the calendar view
            calendarTourismHead.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                // Handle date selection here
                String editDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                Toast.makeText(TourismHeadAdmin.this, "Selected Date: " + editDate, Toast.LENGTH_SHORT).show();
            });
        }

        public void InsertDatabase (View view){
            ContentValues contentValues = new ContentValues();
            contentValues.put("Date", selectedDate);
            contentValues.put("Event", SaveSchedule.getText().toString());
            sqLiteDatabase.insert("EventCalendar", null, contentValues);

        }
    public void ReadDatabase() {
        String query = "Select Event from EventCalendar where Date" + selectedDate;
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            SaveSchedule.setText(cursor.getString(0)); // Update the appropriate view here
        } catch (Exception e) {
            e.printStackTrace();
            SaveSchedule.setText("");
        }
    }

    private void setUpRecyclerView() {
            // RecyclerView setup
            Pending_RecyclerView.setHasFixedSize(true);
            Pending_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
            Pending_RecyclerView.setAdapter(myAdapter);

            Upcoming_RecyclerView.setHasFixedSize(true);
            Upcoming_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
            Upcoming_RecyclerView.setAdapter(UpcomingAdapter);

            History_RecyclerView.setHasFixedSize(true);
            History_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
            History_RecyclerView.setAdapter(HistoryAdapter);
        }



    // EventListener for data changes in Firestore
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
                                Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
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

                        myAdapter.notifyDataSetChanged();
                        UpcomingAdapter.notifyDataSetChanged();
                        HistoryAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                });
    }



    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("TourismHeadActivity", "Error fetching user data: " + error.getMessage());
                        return;
                    }
                    // Handle the document snapshot here
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Extract data from the document snapshot
                        String status = documentSnapshot.getString("status");

                        // Display the data or perform other actions
                        // For example, you can update a TextView with the fetched data
                        // textView.setText(userData);
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


        // Set a click listener for the edit button
        SaveTH.setOnClickListener(v -> {
            // Handle edit button click (implement your edit/update/delete logic here)
            Toast.makeText(TourismHeadAdmin.this, "Edit button clicked", Toast.LENGTH_SHORT).show();
        });
    }
}
