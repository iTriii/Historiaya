//PaymentDetails
package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

//FOR UPDATE ONLY
public class PaymentDetails extends AppCompatActivity {

    RadioButton DonCat_Radio, Gala_Radio;
    ScrollView ScrollViewDonCata, ScrollViewGalaRod;
    View lineone, linetwo;

    ImageButton backbtnpaymentdetails, chatbtn;
    Button donebtn, donebtn2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        // Configure Crisp
        Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        backbtnpaymentdetails = findViewById(R.id.backbtnpaymentdetails);

        donebtn = findViewById(R.id.donebtn);
        donebtn2 = findViewById(R.id.donebtn2);

        //Initialize the Tabs and scrollview also he radio if Don Catalino at GAla Rodriguez
        ScrollViewDonCata = findViewById(R.id.ScrollViewDonCata); //scrollview
        DonCat_Radio = findViewById(R.id.DonCat_Radio);
        DonCat_Radio.setOnClickListener(v -> DonCat_Radio());

        ScrollViewGalaRod = findViewById(R.id.ScrollViewGalaRod); //scrollview
        Gala_Radio = findViewById(R.id.Gala_Radio);
        Gala_Radio.setOnClickListener(v -> Gala_Radio());
        lineone = findViewById(R.id.lineone);
        linetwo = findViewById(R.id.linetwo);

        chatbtn = findViewById(R.id.chatbtn);


        //INITIALIZE THE CRISP
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        });


        //doncatbutton
        donebtn.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentDetails.this, Payment.class);
            startActivity(intent);
            finish();
        });

        //galabutton
        donebtn2.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentDetails.this, Payment.class);
            startActivity(intent);
            finish();
        });

        // Back button
        backbtnpaymentdetails.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentDetails.this, BookNow.class);
            startActivity(intent);
            finish();
        });

    }



    private void DonCat_Radio() {
        DonCat_Radio.setChecked(true);
        DonCat_Radio.setTextColor(ContextCompat.getColor(this, R.color.green));
        ScrollViewDonCata.setVisibility(View.VISIBLE);
        Gala_Radio.setChecked(false);
        Gala_Radio.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        ScrollViewGalaRod.setVisibility(View.GONE);
        lineone.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        linetwo.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
    }

    private void  Gala_Radio() {
        DonCat_Radio.setChecked(false);
        DonCat_Radio.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        ScrollViewDonCata.setVisibility(View.GONE);
        Gala_Radio.setChecked(true);
        Gala_Radio.setTextColor(ContextCompat.getColor(this, R.color.green));
        ScrollViewGalaRod.setVisibility(View.VISIBLE);
        lineone.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        linetwo.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
    }
}
