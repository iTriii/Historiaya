package com.example.log_in;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

    import androidx.activity.OnBackPressedCallback;
    import androidx.activity.OnBackPressedDispatcher;
    import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

    public class LogOut_PO extends AppCompatActivity {
        private FirebaseAuth mAuth;
        Button yes, no;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log_out_po);
            OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    goBack();
                }
            };
            onBackPressedDispatcher.addCallback(this, callback);

        mAuth = FirebaseAuth.getInstance();

        yes = findViewById(R.id.yes);
        if (yes != null) {
            yes.setOnClickListener(view -> yes());
        }
        no = findViewById(R.id.no);
        no.setOnClickListener(view -> no());


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.7), (int) (height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    private void yes() {
        try {
            // Clear user data or preferences
            clearUserData();
            mAuth.signOut();

            // Show a success message with a Toast
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent startScreen = new Intent(this, StartScreen.class);
            startScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startScreen);
            finish();
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
        private void no() {
            Intent intent = new Intent(this, Main2.class);
            startActivity(intent);
        }
        private void goBack() {
            // For instance, you can navigate to another activity or finish the current one
            Intent intent = new Intent(this, Main2.class);
            startActivity(intent);
            finish();
        }
    }