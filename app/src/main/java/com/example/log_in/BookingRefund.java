package com.example.log_in;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class BookingRefund extends AppCompatActivity {

    Spinner spinRefund;
    TextView detailsclick, pickhouseText, amountText, nameText,AutomaticDateTxt;
    Button withdrawbtn;
    Dialog dialog;
    private FirebaseAuth mAuth;
    private String userId;
    Button notnowbtn, confirmbtn, Nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_refund);
        spinRefund = findViewById(R.id.spinRefund);
        withdrawbtn = findViewById(R.id.withdrawbtn);

        // Back button
        withdrawbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookingRefund.this, Main2.class);
            startActivity(intent);
        });

// Initialize the dialog for btn withdraw
        dialog = new Dialog(BookingRefund.this);
        dialog.setContentView(R.layout.dialog_refund);
        dialog.setCancelable(false);

        // Initialize the dialog
        dialog = new Dialog(BookingRefund.this);
        dialog.setContentView(R.layout.dialog_cancellation);
        dialog.setCancelable(false);

        notnowbtn = dialog.findViewById(R.id.notnowbtn);
        confirmbtn = dialog.findViewById(R.id.confirmbtn);

        notnowbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingRefund.this, Main2.class);
            startActivity(backIntent);
            Toast.makeText(BookingRefund.this, "Not Now", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        confirmbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingRefund.this, Main2.class);
            startActivity(backIntent);
            Toast.makeText(BookingRefund.this, "Confirm Cancellation, please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        withdrawbtn.setOnClickListener(v -> {
            dialog.show();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.refund_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinRefund.setAdapter(adapter);
        // Set item selection listener for the spinner
        spinRefund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected refund option
                String selectedOption = parentView.getItemAtPosition(position).toString();
                Toast.makeText(BookingRefund.this, "Selected Refund Option: " + selectedOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


    }
}