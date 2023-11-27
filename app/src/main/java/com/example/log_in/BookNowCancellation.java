package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookNowCancellation extends AppCompatActivity {

    //FOR UPDATE ONLY
    Button  withdrawbtn,notnowbtn,confirmbtn;
    ImageButton backbutton;
    TextView detailsclick;
    Dialog dialog;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_cancellation);


        dialog = new Dialog(BookNowCancellation.this);
        dialog.setContentView(R.layout.dialog_cancellation);
        dialog.setCancelable(false);



        backbutton = findViewById(R.id.backbutton);
        detailsclick = findViewById(R.id.detailsclick);

        notnowbtn = dialog.findViewById(R.id.notnowbtn);
        confirmbtn = dialog.findViewById(R.id.confirmbtn);


        notnowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookNowCancellation.this, BookingDetailMain.class);
                startActivity(intent);
                Toast.makeText(BookNowCancellation.this,"Not Now", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookNowCancellation.this, Main2.class);
                startActivity(intent);
                Toast.makeText(BookNowCancellation.this,"Confirm  Cancellation please wait for approval", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        withdrawbtn = findViewById(R.id.withdrawbtn);
       withdrawbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.show();
           }
       });


        detailsclick.setOnClickListener(v -> {
            Intent intent = new Intent(BookNowCancellation.this, Profile.class);
            startActivity(intent);
        });

        backbutton.setOnClickListener(v -> {
            Intent intent = new Intent(BookNowCancellation.this, Profile.class);
            startActivity(intent);
        });
    }
}

