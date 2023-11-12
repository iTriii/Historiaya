package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class Profile extends AppCompatActivity {
    ImageButton back, EditProfile;
    ShapeableImageView icon;
    TextView ProfileName;
    FirebaseUser user;
    FirebaseAuth auth;
    RadioButton Achievements_Tab,MyBooking_Tab,History_Tab;
    ScrollView AchievementsTab,MyBookingTab,HistoryTab;
    View uno, dos, tres;
    ProgressBar quest_progressbar,scavenger_progressbar,quiz_progressbar;
    int counter=0;
    private FirebaseFirestore db;
    private ListenerRegistration userDataListener;
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;

    Button btnongoing,btnresched;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> main2());


        btnongoing = findViewById(R.id.btnongoing);
        btnresched = findViewById(R.id.btnresched);

            prog();

        AchievementsTab  = findViewById(R.id.AchievementsTab);
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



        //navigate to cancellation activity
        btnongoing.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, BookNowCancellation.class);
            startActivity(intent);
        });

        //navigate to resched activity
        btnresched.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, BookingDetails.class);
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
        TimerTask timertask = new TimerTask(){
            public void run(){
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
    private void Achievements_Tab(){
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
    private void MyBooking_Tab(){
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
    private void History_Tab(){
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

}
