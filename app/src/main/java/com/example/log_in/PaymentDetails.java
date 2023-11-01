package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;

public class PaymentDetails extends AppCompatActivity {


    View storeTabIndicator, purchasesTabIndicator;
    FirebaseFirestore firestore;
    AppCompatRadioButton DonCatalinoTab, GalaRodriguez_tab;
    ImageButton backbtn, donebtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
    }

    public void onRadioButtonClicked(View view) {
    }
}