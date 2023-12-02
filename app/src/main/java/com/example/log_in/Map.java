package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
//FOR UPDATE ONLY
public class Map extends AppCompatActivity {
    ImageButton backbutt;
    ConstraintLayout doncat1, gala1, doncatExpanded, galaExpanded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        backbutt = findViewById(R.id.backbutt);
        backbutt.setOnClickListener(v -> Main2());

        doncat1 = findViewById(R.id.doncat1);
        gala1 = findViewById(R.id.gala1);
        doncatExpanded = findViewById(R.id.doncatExpanded);
        galaExpanded = findViewById(R.id.galaExpanded);

        doncat1.setOnClickListener(v -> toggleExpandedViews(doncatExpanded, galaExpanded));
        gala1.setOnClickListener(v -> toggleExpandedViews(galaExpanded, doncatExpanded));
    }

    public void Main2() {
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    public void toggleExpandedViews(View viewToExpand, View viewToCollapse) {
        if (viewToCollapse.getVisibility() == View.VISIBLE) {
            viewToCollapse.setVisibility(View.GONE);
        }

        if (viewToExpand.getVisibility() == View.VISIBLE) {
            viewToExpand.setVisibility(View.GONE);
        } else {
            viewToExpand.setVisibility(View.VISIBLE);
        }
    }

    public void put(String selectedRefundOption, String selectedOption) {
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        finish();
    }
}