package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class BookNow extends AppCompatActivity {
    private ImageButton chatbtn, backbtn;
    private FirebaseFirestore db;
    private Spinner spinTour;
    private Spinner spinNum;
    private Button btnsave, btncancel, btntime1, btntime2, btntime3, btntime4;
    private FirebaseAuth mAuth;
    private ScrollView BookScrollView;
    private TextView selectedHouse, Subtotal, RFTourGuide, Total, SCharge, SDate;
    CalendarView calendarView;
    private String reservedDate;
    private String userId;
    String selectedTime = "";

    // Set to store available times
    Set<String> availableTimes = new HashSet<>();

    // Sample available times (replace with your actual available times)
    String[] allTimes = {"9:00 AM", "11:00 AM", "1:00 PM", "3:00 PM"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Calendar currentTime = Calendar.getInstance();


        // Initialize UI elements
        btnsave = findViewById(R.id.btnsubmit);
        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.touristnum);
        BookScrollView = findViewById(R.id.BookScrollView);
        btncancel = findViewById(R.id.btncancel);
        chatbtn = findViewById(R.id.chatbtn);
        selectedHouse = findViewById(R.id.selectedHouse);
        Subtotal = findViewById(R.id.Subtotal);
        RFTourGuide = findViewById(R.id.RFTourGuide);
        Total = findViewById(R.id.Total);
        SCharge = findViewById(R.id.SCharge);
        backbtn = findViewById(R.id.backbtn);
        SDate = findViewById(R.id.SDate);
        calendarView = findViewById(R.id.Calendar);


        setupSpinners();
        setListeners();
        setupButtonClickListener();
        initializeAvailableTimes();
        createTimeButtons();
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

        // listeners for both Spinners
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
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Tourist Number Selected");
            }
        });
    }

    private void setListeners() {
        // Add any necessary listeners
    }

    private void BookScrollView() {
        BookScrollView.setVisibility(View.VISIBLE);
        Subtotal.setVisibility(View.VISIBLE);
        Total.setVisibility(View.VISIBLE);
    }


    // Toast message
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setupButtonClickListener() {
        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookNow.this, chat.class);
            startActivity(intent);
        });

        // Back button
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookNow.this, Main2.class);
            startActivity(intent);
        });

        //cancel btn
        btncancel.setOnClickListener(view -> {
            Intent intent = new Intent(BookNow.this, Main2.class);
            startActivity(intent);
        });


        //calendar


        // Spinner starts here
        reservedDate = "";
        btnsave.setOnClickListener(view -> {
            selectedTime = getSelectedTime();
            long selectedDateInMillis = calendarView.getDate();

            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.setTimeInMillis(selectedDateInMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            reservedDate = dateFormat.format(selectedDateTime.getTime());
            Toast.makeText(this, "Reserved Date: " + reservedDate, Toast.LENGTH_SHORT).show();


            //spinner starts here
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();
            String selectedTime = availableTimes.toString();

            if (!selectedTour.equals("Select Tour") && !selectedTouristNumStr.equals("Number of Tourists")) {
                int selectedTouristNum = Integer.parseInt(selectedTouristNumStr);
                // Calculate payment details

                double rfTourGuide = calculateTourGuide(selectedTouristNum);
                double serviceCharge = calculateServiceCharge(selectedTour);
                double tourPrice = calculateTourPrice(selectedTour);
                double subtotal = calculateSubtotal(selectedTour);
                double total = calculateTotal(rfTourGuide, serviceCharge, subtotal);

// Set the text of the Subtotal TextView
                Subtotal.setText(String.format(" ₱%.2f", subtotal));
                selectedHouse.setText(String.format(" %s", selectedTour));
                RFTourGuide.setText(String.format(" ₱%.2f", rfTourGuide));
                SCharge.setText(String.format(" ₱%.2f", serviceCharge));
                Total.setText(String.format("₱%.2f", total));


                // Add data to Firestore
                userId = mAuth.getCurrentUser().getUid();
                addDataToFirestore(userId, selectedTour, selectedTouristNumStr, reservedDate, total, selectedTime);

                // Check if the tour is done
                if (isTourDone(reservedDate)) {
                    // The tour is considered done
                    // Implement logic to move the booking to the "history" tab
                    moveToHistory(userId, selectedTour, reservedDate);
                } else {
                    // The tour is not yet done
                    // Continue with other logic or display a message
                }

                // Make the ScrollView visible
                BookScrollView();
            } else {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to retrieve the selected time
    private String getSelectedTime() {
        LinearLayout timeButtonContainer = findViewById(R.id.timeButtonContainer);
        for (int i = 0; i < timeButtonContainer.getChildCount(); i++) {
            View child = timeButtonContainer.getChildAt(i);
            if (child instanceof Button && !((Button) child).isEnabled()) {
                return ((Button) child).getText().toString();
            }
        }
        return "";
    }

    //calculation starts here
    private double calculateTotal(double rfTourGuide, double serviceCharge, double subtotal) {
        double total = 0.0;
        total = subtotal + rfTourGuide + serviceCharge;
        return total;
    }

    double calculateSubtotal(String selectedTour) {
        double subtotal = 0.0;
        switch (selectedTour) {
            case "Don Catalino":
                subtotal = 500.0;
                break;
            case "Gala Rodriguez":
                subtotal = 1000.0;
                break;
            case "Both":
                subtotal = 1500.0;
                break;
        }

        return subtotal;
    }


    //service charge
    private double calculateServiceCharge(String selectedTour) {
        double serviceCharge = 500;
        return serviceCharge;
    }

    //Tourguide
    private double calculateTourGuide(int selectedTouristNum) {
        double tourGuideFee = 0.0;
        if (selectedTouristNum >= 1 && selectedTouristNum <= 5) {
            tourGuideFee = 500.0;
        } else if (selectedTouristNum > 5) {
            tourGuideFee = 1000.0;
        }
        return tourGuideFee;
    }


    //Tourprice per house
    private double calculateTourPrice(String selectedTour) {
        double tourPrice = 0.0;
        switch (selectedTour) {
            case "Don Catalino":
                tourPrice = 500.0;
                break;
            case "Gala Rodriguez":
                tourPrice = 1000.0;
                break;
            case "Both":
                tourPrice = 1500.0;
                break;
        }
        return tourPrice;
    }


    // Add data to Firestore... Wag mo iirremove lea
    // Add data to Firestore
    private void addDataToFirestore(String userId, String selectedTour, String selectedTouristNum, String reservedDate, double totalAmount, String selectedTime) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> bookingData = new HashMap<>();
                if (task.getResult() != null && task.getResult().getData() != null) {
                    bookingData.putAll(task.getResult().getData());
                }
                bookingData.put("selectedTour", selectedTour);
                bookingData.put("selectedTouristNum", selectedTouristNum);
                bookingData.put("reservedDate", reservedDate);
                bookingData.put("totalAmount", totalAmount);
                bookingData.put("selected Time", selectedTime);
                userDocRef.update(bookingData).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Booking updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                    if (isTourDone(selectedTour)) {
                        moveToHistory(userId, selectedTour, reservedDate);
                    }
                }).addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Map<String, Object> user = new HashMap<>();
                user.put("selectedTour", selectedTour);
                user.put("selectedTouristNum", selectedTouristNum);
                user.put("reservedDate", reservedDate);
                user.put("totalAmount", totalAmount);
                user.put("Selected Time", selectedTime);

                userDocRef.set(user).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Booking created", Toast.LENGTH_SHORT).show();
                    if (isTourDone(selectedTour)) {
                        moveToHistory(userId, selectedTour, reservedDate);
                    }
                }).addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }


    private boolean isTourDone(String reservedDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(" MMM d, yyyy", Locale.getDefault());
            // Get the current date
            Date currentDate = new Date();
            Date tourDate = sdf.parse(reservedDate);
            // Compare the dates
            return currentDate.after(tourDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void moveToHistory(String userId, String selectedTour, String reservedDate) {
        // Get a reference to the current user's document
        DocumentReference userDocRef = db.collection("users").document(userId);

        // Get the data from the current user's document
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the data from the current document
                Map<String, Object> bookingData = task.getResult().getData();

                // Check if the tour is done
                if (isTourDone(reservedDate)) {
                    CollectionReference historyCollection = db.collection("history");

                    // Add the booking data to the history collection
                    historyCollection.add(bookingData).addOnSuccessListener(documentReference -> {
                        // Remove the booking data from the current user's document
                        userDocRef.delete().addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(), "Booking moved to history", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), "Failed to delete booking: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(getApplicationContext(), "Failed to move booking to history: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // The tour is not yet done
                    Toast.makeText(getApplicationContext(), "Tour is not yet done", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Failed to retrieve booking data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Method to initialize available times
    private void initializeAvailableTimes() {
        availableTimes.addAll(Arrays.asList(allTimes));
    }

    // Method to create time buttons dynamically
    private void createTimeButtons() {
        LinearLayout timeButtonContainer = findViewById(R.id.timeButtonContainer);
        for (String time : allTimes) {
            Button timeButton = new Button(this);
            timeButton.setText(time);
            timeButton.setOnClickListener(v -> onTimeButtonClick(time));
            timeButtonContainer.addView(timeButton);
        }
    }

    // Method to handle time button click
    private void onTimeButtonClick(String selectedTime) {
        if (availableTimes.contains(selectedTime)) {
            // Time is available, mark it as reserved
            availableTimes.remove(selectedTime);
            Toast.makeText(getApplicationContext(), "Time reserved: " + selectedTime, Toast.LENGTH_SHORT).show();

            // Disable all time buttons to prevent further selection
       //     disableAllTimeButtons();
        } else {
            // Time is already reserved, notify the user or handle accordingly
            Toast.makeText(getApplicationContext(), "Time not available: " + selectedTime, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to disable all time buttons
   // private void disableAllTimeButtons() {
     //   LinearLayout timeButtonContainer = findViewById(R.id.timeButtonContainer);
   //     for (int i = 0; i < timeButtonContainer.getChildCount(); i++) {
      //      View child = timeButtonContainer.getChildAt(i);
     //       if (child instanceof Button) {
     //           ((Button) child).setEnabled(false);
    //        }
        //}
  //  }
}