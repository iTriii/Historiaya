package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Timer;
import java.util.TimerTask;
//FOR UPDATE ONLY
public class Profile extends AppCompatActivity {
    ImageButton back, EditProfile;
    ShapeableImageView icon;
    LinearLayout cancelled,Reschedule,Booked1, cancelled1;
    TextView selectedTourText3,Month2,selectedTourTextt,selectedTourText2,Month1,selectedTourText1,ProfileName, selectedTourText, MonthText, DateText, MonthTextt,DateHisto, UpdatingtheTouristText; // Adjusted the order
    FirebaseUser user;
    FirebaseAuth auth;
    RadioButton Achievements_Tab, MyBooking_Tab, History_Tab;
    ScrollView AchievementsTab, MyBookingTab, HistoryTab;
    View uno, dos, tres;
    ProgressBar quest_progressbar, scavenger_progressbar, quiz_progressbar;
    int counter = 0;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

    Button upcomingbtn, adminView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.backbtnprofile);
        back.setOnClickListener(v -> main2());
        upcomingbtn = findViewById(R.id.upcomingbtn);

        MonthText = findViewById(R.id.MonthText);
        selectedTourText = findViewById(R.id.selectedTourText);
        DateText = findViewById(R.id.DateText);
        MonthTextt = findViewById(R.id.MonthTextt);
        selectedTourTextt = findViewById(R.id.selectedTourTextt);


        //   DateHisto = findViewById(R.id.DateHisto);
        UpdatingtheTouristText = findViewById(R.id.UpdatingtheTouristText);
        Month1 = findViewById(R.id.Month1);
        selectedTourText2 = findViewById(R.id.selectedTourText2);
        Month2 = findViewById(R.id.Month2);
        cancelled = findViewById(R.id.cancelled);
        Reschedule = findViewById(R.id.Reschedule);
        Booked1 = findViewById(R.id.Booked1);
        cancelled1 = findViewById(R.id.cancelled1);

        prog();

        AchievementsTab = findViewById(R.id.AchievementsTab);
        Achievements_Tab = findViewById(R.id.Achievements_Tab);
        Achievements_Tab.setOnClickListener(v -> Achievements_Tab());
        MyBookingTab = findViewById(R.id.MyBookingTab);
        MyBooking_Tab = findViewById(R.id.MyBooking_Tab);
        MyBooking_Tab.setOnClickListener(v -> MyBooking_Tab());
        HistoryTab = findViewById(R.id.HistoryTab);
        History_Tab = findViewById(R.id.History_Tab);
        History_Tab.setOnClickListener(v -> History_Tab());
        uno = findViewById(R.id.uno);
        dos = findViewById(R.id.dos);
        tres = findViewById(R.id.tres);
        selectedTourText1 = findViewById(R.id.selectedTourText1);

        EditProfile = findViewById(R.id.EditProfile);
        EditProfile.setOnClickListener(v -> Profile_Edit());
        ProfileName = findViewById(R.id.ProfileName);
        icon = findViewById(R.id.icon);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        upcomingbtn.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, BookingDetailMain.class);
            startActivity(intent);
        });

        if (user != null) {
            // Fetch and display user data from Firestore
            try {
                fetchAndDisplayUserData();
            } catch (Exception e) {
                Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
            }
        } else {
            // Redirect to login if the user is not authenticated
            startActivity(new Intent(Profile.this, LogIn.class));
            finish();
        }
    }

    private void prog() {
        quest_progressbar = (ProgressBar) findViewById(R.id.quest_progressbar);
        scavenger_progressbar = (ProgressBar) findViewById(R.id.scavenger_progressbar);
        quiz_progressbar = (ProgressBar) findViewById(R.id.quiz_progressbar);

        final Timer timer = new Timer();
        TimerTask timertask = new TimerTask() {
            public void run() {
                counter++;
                quest_progressbar.setProgress(counter);
                scavenger_progressbar.setProgress(counter);
                quiz_progressbar.setProgress(counter);

                if (counter == 10)
                    timer.cancel();
            }
        };
        timer.schedule(timertask, 0, 50);
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

                            String firstName = documentSnapshot.getString("FirstName");
                            String lastName = documentSnapshot.getString("LastName");
                            String imageUrl = documentSnapshot.getString("ImageUrl");
                            String selectedTour = documentSnapshot.getString("selectedTour");// display data in texview
                            String reservedDate = documentSnapshot.getString("reservedDate");
                            String status = documentSnapshot.getString("status");
                            String selectedRefundOption = documentSnapshot.getString("selectedRefundOption");


                                //TextViews with the retrieved data
                            MonthText.setText(reservedDate);
                            if (MonthText != null) {
                                MonthText.setText(reservedDate);
                            }
                            MonthTextt.setText(reservedDate);
                            if (MonthTextt != null) {
                                MonthTextt.setText(reservedDate);
                            }
                            selectedTourTextt.setText(selectedTour);
                            if(selectedTourTextt !=null){
                                selectedTourTextt.setText(selectedTour);
                            }
                            selectedTourText.setText(selectedTour);
                            if (selectedTourText != null){
                                selectedTourText.setText(selectedTour);
                            }
                            Month1.setText(reservedDate);
                            if(Month1 != null){
                                Month1.setText(reservedDate);
                            }
                            selectedTourText1.setText(selectedTour);
                            if (selectedTourText1 != null){
                                selectedTourText1.setText(selectedTour);
                            }
                            selectedTourText2.setText(selectedTour);
                            if (selectedTourText2 != null){
                                selectedTourText2.setText(selectedTour);
                            }
//                            Month2.setText(selectedRefundOption);
//                            if (Month2 != null){
//                                Month2.setText(selectedRefundOption);
//                            }
                            // Update reservation status in the UI
                            if (UpdatingtheTouristText != null) {
                                UpdatingtheTouristText.setText(status);
                            }
                            // Set the visibility of the 'cancelled' LinearLayout based on selectedRefundOption
                            if (selectedRefundOption != null && !selectedRefundOption.isEmpty()) {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayout to VISIBLE");
                                cancelled.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayout to GONE");
                                cancelled.setVisibility(View.GONE);
                            }

                            // Set the visibility of the 'cancelled' LinearLayout based on the status
                            if ("Pending".equals(status)) {
                                Log.d("ProfileActivity", "Setting Reschedule LinearLayout to VISIBLE");
                                Reschedule.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("ProfileActivity", "Setting reschedule LinearLayout to GONE");
                                Reschedule.setVisibility(View.GONE);
                            }
                            //ADD AS LONG AS MAY IAADD
                            if (firstName != null && lastName != null) {
                                ProfileName.setText(firstName + " " + lastName);
                            } else {
                                ProfileName.setText("No Name Available");
                            }

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .transforms(new CenterCrop(), new CircleCrop());
                                Glide.with(Profile.this)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into(icon);
                            }
                        } catch (Exception e) {
                            Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore snapshot listener when the activity is destroyed
        if (userDataListener != null) {
            try {
                userDataListener.remove();
            } catch (Exception e) {
                Log.e("ProfileActivity", "Error in onDestroy: " + e.getMessage());
            }
        }
    }

    public void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }



    public void Profile_Edit() {
        Intent intent = new Intent(this, Profile_Edit.class);
        startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Check if the result code and request code match
            if (data != null) {
                // Retrieve the edited data from the Intent
                String editedFirstName = data.getStringExtra("editedFirstName");
                String editedLastName = data.getStringExtra("editedLastName");

                // Update the UI in Profile activity with the edited data
                ProfileName.setText(editedFirstName + " " + editedLastName);
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Intent data = result.getData();
                        String editedFirstName = data.getStringExtra("editedFirstName");
                        String editedLastName = data.getStringExtra("editedLastName");
                        // Update the UI with the edited data
                        ProfileName.setText(editedFirstName + " " + editedLastName);
                    }
                }
            }
    );

    private void Achievements_Tab() {
        Achievements_Tab.setChecked(true);
        Achievements_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        AchievementsTab.setVisibility(View.VISIBLE);
        MyBooking_Tab.setChecked(false);
        MyBooking_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        MyBookingTab.setVisibility(View.GONE);
        History_Tab.setChecked(false);
        History_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryTab.setVisibility(View.GONE);
        uno.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        dos.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tres.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void MyBooking_Tab() {
        Achievements_Tab.setChecked(false);
        Achievements_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        AchievementsTab.setVisibility(View.GONE);
        MyBooking_Tab.setChecked(true);
        MyBooking_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        MyBookingTab.setVisibility(View.VISIBLE);
        History_Tab.setChecked(false);
        History_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        HistoryTab.setVisibility(View.GONE);
        History_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        uno.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        dos.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        tres.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

    }

    private void History_Tab() {
        Achievements_Tab.setChecked(false);
        Achievements_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        AchievementsTab.setVisibility(View.GONE);
        MyBooking_Tab.setChecked(false);
        MyBooking_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        MyBookingTab.setVisibility(View.GONE);
        History_Tab.setChecked(true);
        History_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        HistoryTab.setVisibility(View.VISIBLE);
        History_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        uno.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        dos.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tres.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(Profile.this, Main2.class);
        startActivity(intent);
        finish();
    }

//    private void fetchAndDisplayUserData() {
//        userDataListener = db.collection("users")
//                .document(user.getUid())
//                .addSnapshotListener((documentSnapshot, error) -> {
//                    if (error != null) {
//                        Log.e("ProfileActivity", "Error fetching user data: " + error.getMessage());
//                        return;
//                    }
//
//                    if (documentSnapshot.exists()) {
//                        try {
//                            String firstName = documentSnapshot.getString("FirstName");
//                            String lastName = documentSnapshot.getString("LastName");
//                            String imageUrl = documentSnapshot.getString("ImageUrl");
//                            String selectedTour = documentSnapshot.getString("selectedTour");
//                            String reservedDate = documentSnapshot.getString("reservedDate");
//                            String status = documentSnapshot.getString("status");
//                            String selectedRefundOption = documentSnapshot.getString("selectedRefundOption");
//
//                            // Fetch and display data from the "bookings" field
//                            List<Map<String, Object>> bookingsList = (List<Map<String, Object>>) documentSnapshot.get("bookings");
//                            if (bookingsList != null && !bookingsList.isEmpty()) {
//                                // Assume we want to display data from the first booking in the list
//                                Map<String, Object> firstBooking = bookingsList.get(0);
//                                String selectedTour1 = (String) firstBooking.get("selectedTour");
//                                String reservedDate1 = (String) firstBooking.get("reservedDate");
//
//                                // TextViews with the retrieved data
//                                MonthText.setText(reservedDate);
//                                if (MonthText != null) {
//                                    MonthText.setText(reservedDate);
//                                }
//                                MonthTextt.setText(reservedDate);
//                                if (MonthTextt != null) {
//                                    MonthTextt.setText(reservedDate);
//                                }
//                                selectedTourTextt.setText(selectedTour);
//                                if (selectedTourTextt != null) {
//                                    selectedTourTextt.setText(selectedTour);
//                                }
//                                selectedTourText.setText(selectedTour);
//                                if (selectedTourText != null) {
//                                    selectedTourText.setText(selectedTour);
//                                }
//                                Month1.setText(reservedDate);
//                                if (Month1 != null) {
//                                    Month1.setText(reservedDate);
//                                }
//                                selectedTourText1.setText(selectedTour);
//                                if (selectedTourText1 != null) {
//                                    selectedTourText1.setText(selectedTour);
//                                }
//                                selectedTourText2.setText(selectedTour1);
//                                if (selectedTourText2 != null) {
//                                    selectedTourText2.setText(selectedTour1);
//                                }
//
//                                // Update reservation status in the UI
//                                if (UpdatingtheTouristText != null) {
//                                    UpdatingtheTouristText.setText(status);
//                                }
//
//                                // Set the visibility of the 'cancelled' LinearLayout based on selectedRefundOption
//                                if (selectedRefundOption != null && !selectedRefundOption.isEmpty()) {
//                                    Log.d("ProfileActivity", "Setting cancelled LinearLayout to VISIBLE");
//                                    cancelled.setVisibility(View.VISIBLE);
//                                } else {
//                                    Log.d("ProfileActivity", "Setting cancelled LinearLayout to GONE");
//                                    cancelled.setVisibility(View.GONE);
//                                }
//
//                                // Set the visibility of the 'cancelled' LinearLayout based on the status
//                                if ("Pending".equals(status)) {
//                                    Log.d("ProfileActivity", "Setting Reschedule LinearLayout to VISIBLE");
//                                    Reschedule.setVisibility(View.VISIBLE);
//                                } else {
//                                    Log.d("ProfileActivity", "Setting reschedule LinearLayout to GONE");
//                                    Reschedule.setVisibility(View.GONE);
//                                }
//
//                                // ADD AS LONG AS MAY IAADD
//                                if (firstName != null && lastName != null) {
//                                    ProfileName.setText(firstName + " " + lastName);
//                                } else {
//                                    ProfileName.setText("No Name Available");
//                                }
//
//                                if (imageUrl != null && !imageUrl.isEmpty()) {
//                                    RequestOptions requestOptions = new RequestOptions()
//                                            .transforms(new CenterCrop(), new CircleCrop());
//                                    Glide.with(Profile.this)
//                                            .load(imageUrl)
//                                            .apply(requestOptions)
//                                            .into(icon);
//                                }
//                            }
//                        } catch (Exception e) {
//                            Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
//                        }
//                    }
//                });
//    }
}