package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookNowCancellation extends AppCompatActivity {

    EditText inputname, inputemail, Reason;

    Button cancelbtn;

    TextView CancellationMessage;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now_cancellation);

        inputname = findViewById(R.id.inputname);
        inputemail = findViewById(R.id.inputemail);
        Reason = findViewById(R.id.Reason);
        cancelbtn = findViewById(R.id.cancelbtn);


        String bookingId = getIntent().getStringExtra("bookingId");
        CancellationMessage.setText("Are you sure you want to cancel your booking with ID: " + bookingId);
       cancelbtn.setOnClickListener(v -> showToast());
    }
    private void showToast() {
        Toast.makeText(getApplicationContext(), "Booking Cancellation Failed.", Toast.LENGTH_SHORT).show();
    }
}

