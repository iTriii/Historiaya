package com.example.log_in;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingDetails extends AppCompatActivity {

    private Spinner spinTour, spinNum;
    private Button btnnext;
    private FirebaseAuth mAuth;
    private String userId;
    private FirebaseFirestore db;
    TextView Date;
    private Calendar calendar;
    ImageButton reschedcalendarbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.spinNum);
        btnnext = findViewById(R.id.btnnext);
        reschedcalendarbtn = findViewById(R.id.reschedcalendarbtn);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        calendar = Calendar.getInstance();
        setupSpinners();
        setupButtonClickListener();
        setupDatePicker();
    }

    private void setupDatePicker() {
        reschedcalendarbtn.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingDetails.this,
                    (view, year1, month1, dayOfMonth1) -> {
                        String selectedDate = (month1 + 1) + "/" + dayOfMonth1 + "/" + year1;
                        Date.setText(selectedDate);
                    },
                    year,
                    month,
                    dayOfMonth);

            datePickerDialog.show();

        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> heritageHouseAdapter = ArrayAdapter.createFromResource(
                this, R.array.HeritageHouses, android.R.layout.simple_spinner_item);
        heritageHouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(heritageHouseAdapter);

        ArrayAdapter<CharSequence> touristNumAdapter = ArrayAdapter.createFromResource(
                this, R.array.TouristNumbers, android.R.layout.simple_spinner_item);
        touristNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNum.setAdapter(touristNumAdapter);

        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showToast("Heritage House Selected: " + spinTour.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Heritage House Selected. Please, Select a Tour");
            }
        });

        spinNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showToast("Tourist Number Selected: " + spinNum.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("No Tourist Number Selected");
            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void setupButtonClickListener() {
        btnnext.setOnClickListener(view -> {
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();

            // Validate the selected tour and tourist number
            if (selectedTour.equals("Select Tour") || selectedTouristNumStr.equals("Number of Tourists")) {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Initialize Firestore references
            userId = mAuth.getCurrentUser().getUid();
            DocumentReference userDocRef = db.collection("users").document(userId);
            CollectionReference rescheduleBookingCollectionRef = userDocRef.collection("rescheduleBooking");

            // Create a new document in the "rescheduleBooking" collection
            DocumentReference newBookingDocRef = rescheduleBookingCollectionRef.document();

            // Add data to Firestore
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("selectedTour for Reschedule", selectedTour);
            bookingData.put("selectedTouristNum for Reschedule", selectedTouristNumStr);

            newBookingDocRef.set(bookingData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Rescheddule uploaded. Please wait for the admin", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), BookNowCancellation.class));
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(getApplicationContext(), "Error creating new booking document: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


    }


}