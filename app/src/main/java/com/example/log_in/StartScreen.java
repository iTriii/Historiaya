package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//FOR UPDATE ONLY

public class StartScreen extends AppCompatActivity {
    ImageButton backbutt, Profile;
    TextView Story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> Main2());

        Profile = findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());

        Story =findViewById(R.id.Story);
        Story.setOnClickListener(v ->StorySelection());

    }
        public void StorySelection(){
        Intent intent = new Intent(this, StorySelection.class);
        startActivity(intent);
    }
    public void Main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void Profile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }
}