 package com.example.log_in;

 import android.annotation.SuppressLint;
 import android.app.Dialog;
 import android.content.ClipData;
 import android.content.ClipboardManager;
 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.constraintlayout.widget.ConstraintLayout;
 //FOR UPDATE ONLY
public class Main2 extends AppCompatActivity {

    TextView Start, BookNow, Store, Map, AppLink;
    ImageView Share, Settings, Profile, notif;
    Button copylink;
    LinearLayout ShareApp;
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

        Settings = findViewById(R.id.EditProfile);
        Settings.setOnClickListener(v -> {
            dialog.setContentView(R.layout.activity_settings);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_background);
            dialog.show();
        });
        Profile = findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());


        ConstraintLayout main2 = findViewById(R.id.main2);

        main2.setOnTouchListener((view, motionEvent) -> {
            if (ShareApp.getVisibility() == View.VISIBLE) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();

                if (x < ShareApp.getLeft() || x > ShareApp.getRight() ||
                        y < ShareApp.getTop() || y > ShareApp.getBottom()) {
                    ShareApp.setVisibility(View.GONE);
                }
            }
            return false;
        });
    }
    private void ShareApp() {
    ShareApp.setVisibility(View.VISIBLE);
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
}