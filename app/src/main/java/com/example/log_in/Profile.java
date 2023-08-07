package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    ImageButton back, EditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> main2());

        EditProfile = findViewById(R.id.EditProfile);
        EditProfile.setOnClickListener(v -> MyProfile());
    }

    public void main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);

    }

    public void MyProfile() {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }
}