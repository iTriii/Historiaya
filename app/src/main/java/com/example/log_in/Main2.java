package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

public class Main2 extends AppCompatActivity {

    private TextView Start, BookNow, Store, Map, AppLink;
    private ImageView Share, EditProfile, Profile, notif, Speaker, Speaker_off;
    private Switch audio;
    private Button copylink, Credits, PrivacyandTerms, Feedback;
    private LinearLayout ShareApp;
    private CardView settings_popup, Notifications;
    private Dialog dialog;
    private FirebaseAuth mAuth;
    private static final long DOUBLE_CLICK_INTERVAL = 1000; // 1 second interval
    private long lastBackPressTime = 0;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastBackPressTime < DOUBLE_CLICK_INTERVAL) {
            // If the interval between two back button presses is less than 1 second, exit the app
            super.onBackPressed();
            finishAffinity(); // Finish all activities in the current task
        } else {
            showToast("Press back again to exit");
            lastBackPressTime = currentTime;
        }
    }

    // Declare a constant for the time interval
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize FirebaseAuth and dialog
        mAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(this);

        initializeDialog();
        initializeUI();




    }

    private void initializeDialog() {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_logout);
            dialog.setCancelable(false);
        }


    @SuppressLint("ClickableViewAccessibility")
    private void initializeUI() {
        Start = findViewById(R.id.Start);
        Start.setOnClickListener(v -> StartScreen());

        BookNow = findViewById(R.id.BookNow);
        BookNow.setOnClickListener(v -> BookNow());

        Store = findViewById(R.id.Store);
        Store.setOnClickListener(v -> Store());

        Map = findViewById(R.id.Map);
        Map.setOnClickListener(v -> Map());

        Share = findViewById(R.id.Share);
        Share.setOnClickListener(view -> ShareApp());
        ShareApp = findViewById(R.id.ShareApp);
        AppLink = findViewById(R.id.AppLink);
        copylink = findViewById(R.id.copylink);


        // Find the logout button by ID
        Button Logout = findViewById(R.id.Logout);

        // Set a click listener on the logout button
        Logout.setOnClickListener(v -> showLogoutDialog());

        Credits = findViewById(R.id.Credits);
        Credits.setOnClickListener(view -> Credits());
        Feedback = findViewById(R.id.Feedback);
        Feedback.setOnClickListener(view -> Feedback());
        PrivacyandTerms = findViewById(R.id.PrivacyandTerms);
        PrivacyandTerms.setOnClickListener(view -> PrivacyandTerms());

        Speaker = findViewById(R.id.Speaker);
        Speaker_off = findViewById(R.id.Speaker_off);
        audio = findViewById(R.id.audio);
        audio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
            startActivity(intent);

            Speaker.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            Speaker_off.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        copylink.setOnClickListener(v -> {
            String linkToCopy = AppLink.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Link", linkToCopy);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(Main2.this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        Notifications = findViewById(R.id.Notifications);
        notif = findViewById(R.id.notif);
        notif = findViewById(R.id.notif);
        notif.setOnClickListener(v -> {
            // Make the Notifications CardView visible when the notif ImageView is clicked
            Notifications.setVisibility(View.VISIBLE);
        });

        settings_popup = findViewById(R.id.settings_popup);
        settings_popup.setVisibility(View.GONE);
        EditProfile = findViewById(R.id.EditProfile);
        EditProfile.setOnClickListener(v -> {
            settings_popup.setVisibility(View.VISIBLE);
        });

        Profile = findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());

        ConstraintLayout main2 = findViewById(R.id.main2);
        main2.setOnTouchListener((view, motionEvent) -> {
            if (SettingsAreVisible() && isTouchOutsideSettings(motionEvent)) {
                hideSettings();
                return true; // Consume the touch event
            } else if (ShareVisible() && isTouchOutsideShare(motionEvent)) {
                hideShare();
                return true; // Consume the touch event
            } else if (notifVisible() && isTouchOutsidenotif(motionEvent)) {
                hidenotif();
            }
            return false; // Allow the touch event to propagate
        });
    }
    private void hidenotif(){
        Notifications.setVisibility(View.GONE);
    }
    private boolean notifVisible(){
        return Notifications.getVisibility() == View.VISIBLE;
    }
    private boolean isTouchOutsidenotif(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x < Notifications.getLeft() || x > Notifications.getRight() ||
                y < Notifications.getTop() || y > Notifications.getBottom();
    }

    private void hideShare() {
        ShareApp.setVisibility(View.GONE);
    }

    private boolean SettingsAreVisible() {
        return settings_popup.getVisibility() == View.VISIBLE;
    }

    private boolean isTouchOutsideSettings(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x < settings_popup.getLeft() || x > settings_popup.getRight() ||
                y < settings_popup.getTop() || y > settings_popup.getBottom();
    }

    private boolean ShareVisible() {
        return ShareApp.getVisibility() == View.VISIBLE;
    }

    private boolean isTouchOutsideShare(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        return x < ShareApp.getLeft() || x > ShareApp.getRight() ||
                y < ShareApp.getTop() || y > ShareApp.getBottom();
    }

    private void hideSettings() {
        settings_popup.setVisibility(View.GONE);
    }


    //dialog for log out
    private void showLogoutDialog() {
        // Show the custom logout dialog
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);

        Button yesDialogbtn = dialog.findViewById(R.id.yesbtn);
        Button noDialogbtn = dialog.findViewById(R.id.nobtn);

        // Set click listener for Yes button
        yesDialogbtn.setOnClickListener(v -> {
            yes();
            dialog.dismiss();
        });

        // Set click listener for No button
        noDialogbtn.setOnClickListener(v -> {
            no();
            dialog.dismiss();
        });

        dialog.show();
    }



    private void yes() {
        try {
            // Clear user data or preferences
            clearUserData();
            mAuth.signOut();

            // Show a success message with a Toast
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Finish all activities in the current task
            finishAffinity();
        } catch (Exception e) {
            // An error occurred, show an error message with a Toast
            Toast.makeText(this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void clearUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void PrivacyandTerms() {
        Intent intent = new Intent(this, PrivacyandTerms.class);
        startActivity(intent);
    }

    private void Feedback() {
        Intent intent = new Intent(this, Feedback.class);
        startActivity(intent);
    }

    private void Credits() {
        Intent intent = new Intent(this, Credits.class);
        startActivity(intent);
    }

    private void ShareApp() {
        ShareApp.setVisibility(View.VISIBLE);
    }
    private void no() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
    }
    public void StartScreen() {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }
    public void BookNow() {
        Intent intent = new Intent(this, BookNow.class);
        startActivity(intent);
    }

    public void Store() {
        Intent intent = new Intent(this, Store.class);
        startActivity(intent);
    }

    public void Map() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void Profile(){
        Intent intent= new Intent(this, Profile.class);
        startActivity(intent);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
