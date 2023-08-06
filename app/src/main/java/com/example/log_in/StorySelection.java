package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class StorySelection extends AppCompatActivity {
    ImageButton backbutt, Profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_selection);


        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> StartScreen());

        Profile = findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());

    }

    public void StartScreen() {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }   

    public void Profile() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
}
