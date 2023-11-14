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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

        btntime1 = findViewById(R.id.btntime1);
        btntime2 = findViewById(R.id.btntime2);
        btntime3 = findViewById(R.id.btntime3);
        btntime4 = findViewById(R.id.btntime4);



        setupSpinners();
        setListeners();
        setupButtonClickListener();
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

            long selectedDateInMillis = calendarView.getDate();

            Calendar selectedDateTime = Calendar.getInstance();
            selectedDateTime.setTimeInMillis(selectedDateInMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
            reservedDate = dateFormat.format(selectedDateTime.getTime());
            Toast.makeText(this, "Reserved Date: " + reservedDate, Toast.LENGTH_SHORT).show();



            //spinner starts here
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();


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
                String userId = mAuth.getCurrentUser().getUid();
                addDataToFirestore(userId, selectedTour, selectedTouristNumStr, reservedDate, total);

                // Make the ScrollView visible
                BookScrollView();
            } else {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
            }
        });
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
    private void addDataToFirestore(String userId, String selectedTour, String selectedTouristNum, String reservedDate, double totalAmount) {
        if (!selectedTour.equals("Select Tour") && !selectedTouristNum.equals("Number of Tourists")) {
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
                    bookingData.put("totalAmount", totalAmount); // Add the total amount to Firestore

                    userDocRef.update(bookingData).addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Booking updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("selectedTour", selectedTour);
                    user.put("selectedTouristNum", selectedTouristNum);
                    user.put("reservedDate", reservedDate);
                    user.put("totalAmount", totalAmount); // Add the total amount to Firestore

                    // creating a book
                    userDocRef.set(user).addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Booking created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }


}
