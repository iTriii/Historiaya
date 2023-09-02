package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Map extends AppCompatActivity {
ImageButton backbutt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> Main2());
    }
    public void Main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void expand(View view) {
    }
}