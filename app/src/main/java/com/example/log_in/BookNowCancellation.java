package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BookNowCancellation extends AppCompatActivity {

    EditText inputname, inputemail, Reason;

    Button cancelbtn;

    TextView CancellationMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now_cancellation);

        inputname = findViewById(R.id.inputname);
    cancelbtn = findViewById(R.id.cancelbtn);


        String bookingId = getIntent().getStringExtra("bookingId");

        CancellationMessage.setText("Are you sure you want to cancel your booking with ID: " + bookingId);

       cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Booking canceled successfully");
            }
        });
    }

    private void showToast(String message) {
        // Implement showToast method as needed
    }
}

