package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.log_in.databinding.ActivityChatBinding;
import com.example.log_in.models.User;
import com.example.log_in.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

public class chat extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageButton  backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        backbtn = findViewById(R.id.imgback);

    // Back button
        backbtn.setOnClickListener(view -> {
        onBackPressed();
    });
    }
    }


