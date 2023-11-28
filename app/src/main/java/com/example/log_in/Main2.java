 package com.example.log_in;

 import android.annotation.SuppressLint;
 import android.app.Dialog;
 import android.content.ClipData;
 import android.content.ClipboardManager;
 import android.content.Context;
 import android.content.Intent;
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
 //FOR UPDATE ONLY
public class Main2 extends AppCompatActivity {

     TextView Start, BookNow, Store, Map, AppLink;
     ImageView Share, EditProfile, Profile, notif, Speaker, Speaker_off;
     Switch audio;
     Button copylink, Credits, PrivacyandTerms, Feedback, Logout;
     LinearLayout ShareApp;
     CardView settings_popup;
     Dialog dialog;


     @SuppressLint("ClickableViewAccessibility")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main2);


         dialog = new Dialog(this);

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

         Credits = findViewById(R.id.Credits);
         Credits.setOnClickListener(view -> Credits());
         Feedback = findViewById(R.id.Feedback);
         Feedback.setOnClickListener(view -> Feedback());
         PrivacyandTerms = findViewById(R.id.PrivacyandTerms);
         PrivacyandTerms.setOnClickListener(view -> PrivacyandTerms());
         Logout = findViewById(R.id.Logout);
         Logout.setOnClickListener(view -> openPopUpWindow());

         Speaker = findViewById(R.id.Speaker);
         Speaker_off = findViewById(R.id.Speaker_off);
         audio = findViewById(R.id.audio);
         audio.setOnCheckedChangeListener((buttonView, isChecked) -> {
             if (isChecked) {

                 // Open the sound settings to allow the user to manually mute the phone
                 Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                 startActivity(intent);

                 // Change speaker image to speaker_off
                 Speaker.setVisibility(View.GONE);
                 Speaker_off.setVisibility(View.VISIBLE);
             } else {

                 // Open the sound settings to allow the user to manually unmute the phone
                 Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                 startActivity(intent);

                 // Change speaker_off image to speaker
                 Speaker.setVisibility(View.VISIBLE);
                 Speaker_off.setVisibility(View.GONE);
             }
         });


         copylink.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Get the text from the AppLink TextView
                 String linkToCopy = AppLink.getText().toString();

                 // Copy the text to the clipboard
                 ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                 ClipData clip = ClipData.newPlainText("Link", linkToCopy);
                 if (clipboard != null) {
                     clipboard.setPrimaryClip(clip);
                 }
                 Toast.makeText(Main2.this, "Link copied to clipboard", Toast.LENGTH_SHORT).show();
             }
         });


         notif = findViewById(R.id.notif);
         notif.setOnClickListener(v -> {
             dialog.setContentView(R.layout.activity_notifications);
             dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_background);
             dialog.show();
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
             }
             return false; // Allow the touch event to propagate
         });
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

         private void openPopUpWindow() {
        Intent intent = new Intent(this, LogOut_PO.class);
        startActivity(intent);
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
     public void LogOut() {
     }
}
