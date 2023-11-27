package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
//FOR UPDATE ONLY

public class Settings extends AppCompatActivity {
    Button Credits, Feedback, PrivacyandTerms, LogOut;
    ImageButton Tutorial1;
    TextView Tutorial;
    ImageView Speaker, Speaker_off;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch audio;
    Dialog dialog;
    FirebaseAuth mAuth;

    AudioManager audioManager;
    private int savedVolume = 0;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String AUDIO_STATE_KEY = "audioState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        audio = findViewById(R.id.audio);
        Speaker = findViewById(R.id.Speaker);
        Speaker_off = findViewById(R.id.Speaker_off);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean audioState = settings.getBoolean(AUDIO_STATE_KEY, false);
        audio.setChecked(audioState);

        audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, savedVolume, 0);
                    Speaker.setVisibility(View.VISIBLE);
                    Speaker_off.setVisibility(View.GONE);
                } else {
                    savedVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    Speaker.setVisibility(View.GONE);
                    Speaker_off.setVisibility(View.VISIBLE);
                }
            }
        });

        if (audioState) {
            Speaker.setVisibility(View.VISIBLE);
            Speaker_off.setVisibility(View.GONE);
        } else {
            Speaker.setVisibility(View.GONE);
            Speaker_off.setVisibility(View.VISIBLE);
        }


        Credits = findViewById(R.id.Credits);
        Credits.setOnClickListener(view -> openCredits());

        Feedback = findViewById(R.id.Feedback);
        Feedback.setOnClickListener(v -> openFeedback());

        PrivacyandTerms = findViewById(R.id.PrivacyandTerms);
        PrivacyandTerms.setOnClickListener(v -> openPrivacyAndTerms());

        LogOut = findViewById(R.id.Logout);
        LogOut.setOnClickListener(view -> logOut());
    }
        public void openCredits() {
            Intent intent = new Intent(this, Credits.class);
            startActivity(intent);
        }

        public void openFeedback() {
            Intent intent = new Intent(this, Feedback.class);
            startActivity(intent);
        }

        public void openPrivacyAndTerms() {
            Intent intent = new Intent(this, PrivacyandTerms.class);
            startActivity(intent);
        }

        public void logOut() {
            try {
                // Clear user data or preferences
                clearUserData();
                mAuth.signOut();

                // Show a success message with a Toast
                Toast.makeText(Settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                Intent startScreen = new Intent(Settings.this, StartScreen.class);
                startScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startScreen);
                finish();
            } catch (Exception e) {
                // An error occurred, show an error message with a Toast
                Toast.makeText(Settings.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    private void clearUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
