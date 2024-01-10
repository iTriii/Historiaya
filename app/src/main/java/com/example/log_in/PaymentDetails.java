//PaymentDetails
package com.example.log_in;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;
//FOR UPDATE ONLY
public class PaymentDetails extends AppCompatActivity {
    // Declare a shared preferences variable
    private SharedPreferences sharedPreferences;
    RadioButton DonCat_Radio, Gala_Radio;
    ScrollView ScrollViewDonCata, ScrollViewGalaRod;
    View lineone, linetwo;

    ImageButton backbtnpaymentdetails, chatbtn;
    Button donebtn, donebtn2;
    private FirebaseFirestore db;
    public ListenerRegistration userDataListener;

    FirebaseAuth auth;
    FirebaseUser user;

    TextView dlbtn, dlbtn2, selectedHouseDoncat, SubtotalDonCat, RFTourGuideDonCat, SChargeDoncat, TotalDonCat, selectedHouseGala, SubtotalGala, RFTourGuideGala, SChargeGala, TotalGala;


    private static int REQUEST_CODE = 100;
    ImageView qrcodegcash,qrcodegcash2;
    OutputStream outputStream;


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);


        // Initialize ImageView
        qrcodegcash = findViewById(R.id.qrcodegcash);
        qrcodegcash2 = findViewById(R.id.qrcodegcash2);

        dlbtn = findViewById(R.id.dlbtn);
        dlbtn2 = findViewById(R.id.dlbtn2);


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

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();


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


        // Retrieve data from Intent
        Intent intent = getIntent();
        String selectedTour = intent.getStringExtra("selectedTour");
        String selectedTouristNum = intent.getStringExtra("selectedTouristNum");
        String reservedDate = intent.getStringExtra("reservedDate");
        double total = intent.getDoubleExtra("total", 0.0);
        String selectedTime = intent.getStringExtra("selectedTime");
        double subtotal = intent.getDoubleExtra("Subtotal", 0.0);
        double serviceCharge = intent.getDoubleExtra("serviceCharge", 0.0);
        double RfTourguide = intent.getDoubleExtra("TourGuide", 0.0);

        // Log the values to help with debugging
        Log.d("PaymentDetails", "selectedTour: " + selectedTour);
        Log.d("PaymentDetails", "selectedTouristNum: " + selectedTouristNum);
        Log.d("PaymentDetails", "reservedDate: " + reservedDate);
        Log.d("PaymentDetails", "total: " + total);
        Log.d("PaymentDetails", "selectedTime: " + selectedTime);
        Log.d("PaymentDetails", "subtotal: " + subtotal);
        Log.d("PaymentDetails", "serviceCharge: " + serviceCharge);
        Log.d("PaymentDetails", "RfTourguide: " + RfTourguide);

        // initialize textviews
        selectedHouseDoncat = findViewById(R.id.selectedHouseDoncat);
        selectedHouseDoncat.setText(selectedTour);
        RFTourGuideDonCat = findViewById(R.id.RFTourGuideDonCat);
        RFTourGuideDonCat.setText("₱" + String.format("%.2f", RfTourguide));
        SChargeDoncat = findViewById(R.id.SChargeDoncat);
        SChargeDoncat.setText(" ₱" + String.format("%.2f", serviceCharge));
        SubtotalDonCat = findViewById(R.id.SubtotalDonCat);
        SubtotalDonCat.setText("₱" + String.format("%.2f", subtotal));
        TotalDonCat = findViewById(R.id.TotalDonCat);
        TotalDonCat.setText(" ₱" + String.format("%.2f", total));

        selectedHouseGala = findViewById(R.id.selectedHouseGala);
        selectedHouseGala.setText(selectedTour);
        RFTourGuideGala = findViewById(R.id.RFTourGuideGala);
        RFTourGuideGala.setText("₱" + String.format("%.2f", RfTourguide));
        SChargeGala = findViewById(R.id.SChargeGala);
        SChargeGala.setText(" ₱" + String.format("%.2f", serviceCharge));
        TotalGala = findViewById(R.id.TotalGala);
        TotalGala.setText(" ₱" + String.format("%.2f", total));
        SubtotalGala = findViewById(R.id.SubtotalGala);
        SubtotalGala.setText("₱" + String.format("%.2f", subtotal));


        //buttons listener to download the qr code
        dlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermission();
            }
        });

        dlbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermission();
            }
        });
    }


    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(PaymentDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveImage();
        } else {
            askPermission();
        }
    }
        //ask permission to download the image/qrcode
        private void askPermission () {
            ActivityCompat.requestPermissions(PaymentDetails.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }


    //ask permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
            //    Toast.makeText(PaymentDetails.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    private void saveImage() {
//        File dir = new File(Environment.getExternalStorageDirectory(), "Historiaya QR Code");
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//
//        BitmapDrawable drawable = (BitmapDrawable) qrcodegcash.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//
//        File file = new File(dir, System.currentTimeMillis() + ".jpg");
//        try {
//            outputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            Toast.makeText(PaymentDetails.this, "Successfully Saved", Toast.LENGTH_LONG).show();
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void saveImage() {
        File dir = new File(Environment.getExternalStorageDirectory(), "Historiaya_QR_Code SaveImage");
        if (!dir.exists()) {
            dir.mkdir();
        }

        BitmapDrawable drawable = (BitmapDrawable) qrcodegcash.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        BitmapDrawable drawable2 = (BitmapDrawable) qrcodegcash2.getDrawable();
        Bitmap bitmap2 = drawable.getBitmap();

        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = Files.newOutputStream(file.toPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(PaymentDetails.this, "Successfully Saved", Toast.LENGTH_LONG).show();
            outputStream.flush();
            outputStream.close();

            // Notify the gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{file.getAbsolutePath()},
                    new String[]{"image/jpeg"},
                    null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void Gala_Radio() {
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