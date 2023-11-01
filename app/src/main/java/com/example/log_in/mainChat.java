package com.example.log_in;

import static com.google.android.gms.auth.zzl.getToken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.log_in.databinding.ActivityMainChatBinding;
import com.example.log_in.utilities.Constants;
import com.example.log_in.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class mainChat extends AppCompatActivity {


    private TextView textName;
    private Button fabNewChatbtn;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        preferenceManager = new PreferenceManager(getApplicationContext());

        textName = findViewById(R.id.textName);
        fabNewChatbtn = findViewById(R.id.fabNewChatbtn);

        loadUserDetails();
        setListeners();
    }

    private void loadUserDetails() {
        textName.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constants.KEY_COLLECTIONS_USERS).document(
                preferenceManager.getString(Constants.KEY_USER_ID)
        );

        String currentToken = preferenceManager.getString(Constants.KEY_FCM_TOKEN);
        if (!currentToken.equals(token)) {
            documentReference.update(Constants.KEY_FCM_TOKEN, token)
                    .addOnFailureListener(e -> showToast("Unable to update token"));

            preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        }
    }

    private void setListeners() {
        fabNewChatbtn.setOnClickListener(v -> {
            if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                Intent intent = new Intent(mainChat.this, chat.class);
                startActivity(intent);
            } else {
                showToast("Please log in to chat");
            }
        });
    }
}