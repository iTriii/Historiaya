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
    TextView ProfileName, selectedTourText, MonthText, DateText, MonthTextt, selectedTourTextt, DateHisto, UpdatingtheTouristText; // Adjusted the order
    FirebaseUser user;
    FirebaseAuth auth;
    RadioButton Achievements_Tab, MyBooking_Tab, History_Tab;
    ScrollView AchievementsTab, MyBookingTab, HistoryTab;
    View uno, dos, tres;
    ProgressBar quest_progressbar, scavenger_progressbar, quiz_progressbar;
    int counter = 0;
    LinearLayout LLBooked1, LLBooked2, LLBooked3, LLBooked4, LLBooked5, LLcancelled1, LLcancelled2, LLcancelled3, LLcancelled4, LLcancelled5;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

    Button upcomingbtn, upcomingbtn2, upcomingbtn3, upcomingbtn4, upcomingbtn5;
    private boolean Cancelled;
    private String userId;
    TextView DateText2, selectedTourText2, MonthText2, UpdatingtheStatus2; //Booked2
    TextView DateText3, selectedTourText3, MonthText3, UpdatingtheStatus3; // Booked 3
    TextView DateText4, selectedTourText4, MonthText4, UpdatingtheStatus4; //Booked 4
    TextView DateText5, selectedTourText5, MonthText5, UpdatingtheStatus5; //Booked 5
    TextView selectedTourText22,selectedTourText33, selectedTourText44;
    private boolean cancelled;
    private int bookingIndex;
    private LinearLayout clickedLayout;
    private Object[] statuses;

    @SuppressLint("MissingInflatedId")
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

        //Buttons
        Button upcomingbtn = findViewById(R.id.upcomingbtn);
        Button upcomingbtn2 = findViewById(R.id.upcomingbtn2);
        Button upcomingbtn3 = findViewById(R.id.upcomingbtn3);
        Button upcomingbtn4 = findViewById(R.id.upcomingbtn4);
        Button upcomingbtn5 = findViewById(R.id.upcomingbtn5);



//
         LLcancelled1 = findViewById(R.id.LLcancelled1);
        LLcancelled2 = findViewById(R.id.LLcancelled2);
       LLcancelled3 = findViewById(R.id.LLcancelled3);
//        LLcancelled4 = findViewById(R.id.LLcancelled4);
//        LLcancelled5 = findViewById(R.id.LLcancelled5);

        //        Textview for Booked 2
        DateText2 = findViewById(R.id.DateText2);
        selectedTourText2 = findViewById(R.id.selectedTourText2);
        MonthText2 = findViewById(R.id.MonthText2);
        UpdatingtheStatus2 = findViewById(R.id.UpdatingtheStatus2);

        //        Textview for Booked 3
        DateText3 = findViewById(R.id.DateText3);
        selectedTourText3 = findViewById(R.id.selectedTourText3);
        MonthText3 = findViewById(R.id.MonthText3);
        UpdatingtheStatus3 = findViewById(R.id.UpdatingtheStatus3);


//        Textview for Booked 4
        DateText4 = findViewById(R.id.DateText4);
        selectedTourText4 = findViewById(R.id.selectedTourText4);
        MonthText4 = findViewById(R.id.MonthText4);
        UpdatingtheStatus4 = findViewById(R.id.UpdatingtheStatus4);

        //        Textview for Booked 5
        DateText5 = findViewById(R.id.DateText5);
        selectedTourText5 = findViewById(R.id.selectedTourText5);
        MonthText5 = findViewById(R.id.MonthText5);
        UpdatingtheStatus5 = findViewById(R.id.UpdatingtheStatus5);

        MonthText = findViewById(R.id.MonthText);
        selectedTourText = findViewById(R.id.selectedTourText);
        DateText = findViewById(R.id.DateText);
        MonthTextt = findViewById(R.id.MonthTextt);
        selectedTourTextt = findViewById(R.id.selectedTourTextt);
//        DateHisto = findViewById(R.id.DateHisto);
        UpdatingtheTouristText = findViewById(R.id.UpdatingtheStatus1);

       selectedTourText22 = findViewById(R.id. selectedTourText22);
        selectedTourText33 = findViewById(R.id. selectedTourText33);
        selectedTourText44 = findViewById(R.id.selectedTourText44);


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

        EditProfile = findViewById(R.id.EditProfile);
        EditProfile.setOnClickListener(v -> Profile_Edit());
        ProfileName = findViewById(R.id.ProfileName);
        icon = findViewById(R.id.icon);

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        // Set OnClickListener for upcomingbtn actions
        upcomingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBookingNavigation(1);
            }
        });

        // Set OnClickListener for upcomingbtn actions
        upcomingbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBookingNavigation(2);
            }
        });
// Set OnClickListener for upcomingbtn actions
        upcomingbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBookingNavigation(3);
            }
        });

        // Set OnClickListener for upcomingbtn actions
        upcomingbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBookingNavigation(4);
            }
        });

        // Set OnClickListener for upcomingbtn actions
        upcomingbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBookingNavigation(5);
            }
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

    // Updated handleBookingNavigation method
// Updated handleBookingNavigation method
    private void handleBookingNavigation(int i) {
        Intent intent;

        switch (i) {
            case 1:
                intent = new Intent(Profile.this, BookingDetailMain.class);
                break;
            case 2:
                intent = new Intent(Profile.this, BookingdetailMain2.class);
                break;
            case 3:
                intent = new Intent(Profile.this, BookingDerailMain3.class);
                break;
            case 4:
                intent = new Intent(Profile.this, BookingDetailMain4.class);
                break;
            case 5:
                intent = new Intent(Profile.this, BookingDetailMain5.class);
                break;

            default:
                // Handle the default case or add an error message
                return;
        }

        intent.putExtra("bookingIndex", i);
        startActivity(intent);
        Log.d("ProfileActivity", "After navigation");
    }

    // Back button

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
                            String selectedTour1 = documentSnapshot.getString("selectedTour1");// display data in texview
                            String reservedDate1 = documentSnapshot.getString("reservedDate1");
                            String status1 = documentSnapshot.getString("status1");
                            String selectedTour2 = documentSnapshot.getString("selectedTour2");// display data in texview
                            String reservedDate2 = documentSnapshot.getString("reservedDate2");
                            String status2 = documentSnapshot.getString("status2");

                            String selectedTour3 = documentSnapshot.getString("selectedTour3");// display data in texview
                            String reservedDate3 = documentSnapshot.getString("reservedDate3");
                            String status3 = documentSnapshot.getString("status3");

                            String selectedTour4 = documentSnapshot.getString("selectedTour4");// display data in texview
                            String reservedDate4 = documentSnapshot.getString("reservedDate4");
                            String status4 = documentSnapshot.getString("status4");

                            String selectedTour5 = documentSnapshot.getString("selectedTour5");// display data in texview
                            String reservedDate5 = documentSnapshot.getString("reservedDate5");
                            String status5 = documentSnapshot.getString("status5");

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
                            DateText.setText(reservedDate1);
                            if (DateText != null) {
                                DateText.setText(reservedDate1);
                            }
                            //TextViews with the retrieved data
                            MonthText.setText(reservedDate1);
                            if (MonthText != null) {
                                MonthText.setText(reservedDate1);
                            }
                            MonthTextt.setText(reservedDate1);
                            if (MonthTextt != null) {
                                MonthTextt.setText(reservedDate1);
                            }
                            selectedTourTextt.setText(selectedTour1);
                            if (selectedTourTextt != null) {
                                selectedTourTextt.setText(selectedTour1);
                            }
                            selectedTourText.setText(selectedTour1);
                            if (selectedTourText != null) {
                                selectedTourText.setText(selectedTour1);
                            }
                            // Update reservation status in the UI
                            if (UpdatingtheTouristText != null) {
                                UpdatingtheTouristText.setText(status1);
                            }
                            //ADD AS LONG AS MAY IAADD


// Booked
                            DateText2.setText(reservedDate2);
                            if (DateText2 != null) {
                                DateText2.setText(reservedDate2);
                            }
                            MonthText2.setText(reservedDate2);
                            if (MonthText2 != null) {
                                MonthText2.setText(reservedDate2);
                            }
                            selectedTourText2.setText(selectedTour2);
                            if (selectedTourText2 != null) {
                                selectedTourText2.setText(selectedTour2);
                            }
                            // Update reservation status in the UI
                            if (UpdatingtheStatus2 != null) {
                                UpdatingtheStatus2.setText(status2);
                            }

                            // Booked 3
                            DateText3.setText(reservedDate3);
                            if (DateText3 != null) {
                                DateText3.setText(reservedDate3);
                            }
                            MonthText3.setText(reservedDate3);
                            if (MonthText3 != null) {
                                MonthText3.setText(reservedDate3);
                            }
                            selectedTourText3.setText(selectedTour3);
                            if (selectedTourText3 != null) {
                                selectedTourText3.setText(selectedTour3);
                            }
                            // Update reservation status in the UI
                            if (UpdatingtheStatus3 != null) {
                                UpdatingtheStatus3.setText(status3);
                            }

                            // Booked 4
                            DateText4.setText(reservedDate4);
                            if (DateText4 != null) {
                                DateText4.setText(reservedDate4);
                            }
                            MonthText4.setText(reservedDate4);
                            if (MonthText4 != null) {
                                MonthText4.setText(reservedDate4);
                            }
                            selectedTourText4.setText(selectedTour4);
                            if (selectedTourText4 != null) {
                                selectedTourText4.setText(selectedTour4);
                            }
                            // Update reservation status in the UI
                            if (UpdatingtheStatus4 != null) {
                                UpdatingtheStatus4.setText(status4);
                            }
                            // Booked 5
                            DateText5.setText(reservedDate5);
                            if (DateText5 != null) {
                                DateText5.setText(reservedDate5);
                            }
                            MonthText5.setText(reservedDate5);
                            if (MonthText5 != null) {
                                MonthText5.setText(reservedDate5);
                            }
                            selectedTourText5.setText(selectedTour5);
                            if (selectedTourText5 != null) {
                                selectedTourText5.setText(selectedTour5);
                            }
                            // Update reservation status in the UI
                            if (UpdatingtheStatus5 != null) {
                                UpdatingtheStatus5.setText(status5);
                            }
//                            caNCELLED1
                            selectedTourText33.setText(selectedTour1);
                            if (selectedTourText33 != null) {
                                selectedTourText33.setText(selectedTour1);
                            }
//                            cancelled4
                            selectedTourText44.setText(selectedTour2);
                            if (selectedTourText44 != null) {
                                selectedTourText44.setText(selectedTour2);
                            }
//                            Cancelled3
                            selectedTourText22.setText(selectedTour3);
                            if (selectedTourText22 != null) {
                                selectedTourText22.setText(selectedTour3);
                            }
// Define arrays for status and LinearLayouts
                            String[] statuses = new String[5];
                            LinearLayout[] bookedLayouts = new LinearLayout[5];

// Assign values to status and get references to LinearLayouts
                            for (int i = 0; i < 5; i++) {
                                statuses[i] = documentSnapshot.getString("statusr" + (i + 1));
                                bookedLayouts[i] = findViewById(getResources().getIdentifier("LLBooked" + (i + 1), "id", getPackageName()));
                            }

// Update visibility based on status
                            for (int i = 0; i < 5; i++) {
                                if ("Pending".equals(statuses[i])) {
                                    Log.d("ProfileActivity", "Setting Reschedule LinearLayout " + (i + 1) + " to VISIBLE");
                                    bookedLayouts[i].setVisibility(View.VISIBLE);

                                    final int finalI = i; // Required for using i in inner class
                                    bookedLayouts[i].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Log the status without navigating
                                            Log.d("ProfileActivity", "Clicked on LinearLayout " + (finalI + 1) + " with statusr: " + statuses[finalI]);

                                            // Check if the clicked status is "Pending"
                                            if ("Pending".equals(statuses[finalI])) {
                                                Log.d("ProfileActivity", "Booking is pending for LinearLayout " + (finalI + 1));
                                            } else {
                                                // Optionally, log that the booking is not pending
                                                Log.d("ProfileActivity", "Booking is not pending for LinearLayout " + (finalI + 1));
                                            }
                                        }
                                    });
                                } else {
                                    Log.d("ProfileActivity", "Setting Reschedule LinearLayout " + (i + 1) + " to GONE");
                                    bookedLayouts[i].setVisibility(View.GONE);
                                }
                            }




                            boolean isCancelled1 = Boolean.TRUE.equals(documentSnapshot.getBoolean("Cancelled1"));

                            // Set the visibility of the 'cancelled' LinearLayout based on the Cancelled field
                            if (isCancelled1) {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to VISIBLE");
                                LLcancelled1.setVisibility(View.VISIBLE);

                            } else {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to GONE");
                                LLcancelled1.setVisibility(View.GONE);
                            }
                            boolean isCancelled2 = Boolean.TRUE.equals(documentSnapshot.getBoolean("Cancelled2"));

                            if (isCancelled2) {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to VISIBLE");
                                LLcancelled2.setVisibility(View.VISIBLE);

                            } else {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to GONE");
                                LLcancelled2.setVisibility(View.GONE);
                            }

                            boolean isCancelled3 = Boolean.TRUE.equals(documentSnapshot.getBoolean("Cancelled3"));
                            if (isCancelled3) {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to VISIBLE");
                                LLcancelled3.setVisibility(View.VISIBLE);

                            } else {
                                Log.d("ProfileActivity", "Setting cancelled LinearLayouts to GONE");
                                LLcancelled3.setVisibility(View.GONE);
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

}