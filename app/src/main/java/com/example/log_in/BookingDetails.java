package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
    Dialog dialog;
    private Button btnnext, btncancel, confirmbtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView Date, Time;
    private Calendar calendar;
    private ImageButton reschedcalendarbtn, reschedtimebtn;
    public Object selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.spinNum);
        btnnext = findViewById(R.id.btnnext);
        btncancel = findViewById(R.id.btncancel);
        reschedcalendarbtn = findViewById(R.id.reschedcalendarbtn);
        reschedtimebtn = findViewById(R.id.reschedtimebtn);
        Date = findViewById(R.id.Date);
        Time = findViewById(R.id.Time);


        // Initialize the dialog
        dialog = new Dialog(BookingDetails.this);
        dialog.setContentView(R.layout.dialog_reschedule);

// Set the dialog window size and make it not cancellable
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

// Find buttons in the dialog layout
        confirmbtn = dialog.findViewById(R.id.confirmbtn);



        confirmbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(BookingDetails.this, Main2.class);
            startActivity(backIntent);
            Toast.makeText(BookingDetails.this, "Your Booking is successfully schedule. please wait for approval", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });


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
                        selectedDate = (month1 + 1) + "/" + dayOfMonth1 + "/" + year1;
                        Date.setText("Selected Date: " );
                    },
                    year,
                    month,
                    dayOfMonth);
            datePickerDialog.show();
        });

        btncancel.setOnClickListener(v -> {
            Intent intent = new Intent(BookingDetails.this, Profile.class);
            startActivity(intent);
        });

        // Initialize TimePickerDialog
        reschedtimebtn.setOnClickListener(v -> {
            @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BookingDetails.this,
                    (view, hourOfDay, minute) -> {
                        selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        Time.setText("Selected Time: ");
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false);
            timePickerDialog.show();
        });
    }

    private void saveDateTimeToFirestore(Object selectedDate, String selectedTime) {
        // Initialize Firestore references
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);
        CollectionReference rescheduleBookingCollectionRef = userDocRef.collection("rescheduleBooking");

        // Create a new document in the "rescheduleBooking" collection
        DocumentReference newBookingDocRef = rescheduleBookingCollectionRef.document();

        // Check if the selected date and time are null
        if (selectedDate == null || TextUtils.isEmpty(selectedTime)) {
            Toast.makeText(getApplicationContext(), "Please select a date and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the selected date and time
        if (!isValidDate(selectedDate) || !isValidTime(selectedTime)) {
            Toast.makeText(getApplicationContext(), "Please select a valid date and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add data to Firestore
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("selectedTour for Reschedule", spinTour.getSelectedItem().toString());
        bookingData.put("selectedTouristNum for Reschedule", spinNum.getSelectedItem().toString());
        bookingData.put("selectedDate for Reschedule", selectedDate);
        bookingData.put("selectedTime for Reschedule", selectedTime);

        newBookingDocRef.set(bookingData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Reschedule uploaded. Please wait for the admin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Main2.class));
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Error creating new booking document: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean isValidTime(String selectedTime) {
        return !TextUtils.isEmpty(selectedTime);
    }

    private boolean isValidDate(Object selectedDate) {
        return selectedDate != null;
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

    // btn next.
    private void setupButtonClickListener() {
        btnnext.setOnClickListener(view -> {
            dialog.show();
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();

            // Validate the selected tour and tourist number
            if (selectedTour.equals("Select Tour") || selectedTouristNumStr.equals("Number of Tourists")) {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected date
            Object selectedDate = Date.getText().toString();

            // Check if the selected date is null
            if (TextUtils.isEmpty(selectedDate.toString())) {
                Toast.makeText(getApplicationContext(), "Please select a date.", Toast.LENGTH_SHORT).show();
                return;
            }
            saveDateTimeToFirestore(selectedDate, selectedTime);
        });
    }
}
