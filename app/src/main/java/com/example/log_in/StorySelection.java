package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
//FOR UPDATE ONLY

public class StorySelection extends AppCompatActivity {
    ImageButton backbutt, Profile;
    TextView rizalstart, doncatStart, galaStart, natalioStart;
    LinearLayout RizalSubchapters, DonSubchapters, GalaSubchapters, NatalioSubchapters;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_selection);


        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> StartScreen());

        Profile = findViewById(R.id.Profile);
        Profile.setOnClickListener(v -> Profile());

        RizalSubchapters = findViewById(R.id.RizalSubchapters);
        DonSubchapters = findViewById(R.id.DonSubchapters);
        GalaSubchapters = findViewById(R.id.GalaSubchapters);
        NatalioSubchapters = findViewById(R.id.NatalioSubchapters);


        rizalstart = findViewById(R.id.rizalstart);
        rizalstart.setOnClickListener(v -> toggleRizalSubchapters(RizalSubchapters));

        doncatStart = findViewById(R.id.doncatStart);
        doncatStart.setOnClickListener(v -> toggleDonSubchapters(DonSubchapters));

        galaStart = findViewById(R.id.galaStart);
        galaStart.setOnClickListener(v -> toggleGalaSubchapters(GalaSubchapters));

        natalioStart = findViewById(R.id.natalioStart);
        natalioStart.setOnClickListener(v -> toggleNatalioSubchapters(NatalioSubchapters));
    }

    public void StartScreen() {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void Profile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void toggleRizalSubchapters(View RizalSubchapters) {
        if (RizalSubchapters.getVisibility() == View.VISIBLE) {
            RizalSubchapters.setVisibility(View.GONE);
        } else {
            RizalSubchapters.setVisibility(View.VISIBLE);
        }
    }
        public void toggleDonSubchapters(View DonSubchapters) {
            if (DonSubchapters.getVisibility() == View.VISIBLE) {
                DonSubchapters.setVisibility(View.GONE);
            } else {
                DonSubchapters.setVisibility(View.VISIBLE);
            }
    }
    public void toggleGalaSubchapters(View GalaSubchapters) {
        if (GalaSubchapters.getVisibility() == View.VISIBLE) {
            GalaSubchapters.setVisibility(View.GONE);
        } else {
            GalaSubchapters.setVisibility(View.VISIBLE);
        }
    }
    public void toggleNatalioSubchapters(View NatalioSubchapters) {
        if (NatalioSubchapters.getVisibility() == View.VISIBLE) {
            NatalioSubchapters.setVisibility(View.GONE);
        } else {
            NatalioSubchapters.setVisibility(View.VISIBLE);
        }
    }
}
