package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class BookNow extends AppCompatActivity {

    private FirebaseFirestore db;
    private ImageButton chatbtn, backbtn;
    Spinner spinTour;
    Spinner spinNum;
    Button btnsave;
    FirebaseAuth mAuth;
    ScrollView BookScrollView;
    TextView GalaRodriguez, DonCatalino, Subtotal, RFTourGuide, RFHouse, Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements and set up spinners
        btnsave = findViewById(R.id.btnsubmit);
        chatbtn = findViewById(R.id.chatbtn);
        backbtn = findViewById(R.id.backbtn);
        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.touristnum);
        BookScrollView = findViewById(R.id.BookScrollView);

        GalaRodriguez = findViewById(R.id.GalaRodriguez);
        DonCatalino = findViewById(R.id.DonCatalino);
        Subtotal = findViewById(R.id.Subtotal);
        RFTourGuide = findViewById(R.id.RFTourGuide);
        RFHouse = findViewById(R.id.RFHouse);
        Total = findViewById(R.id.Total);


        // ArrayAdapter for the HeritageHouses Spinner
        ArrayAdapter<CharSequence> heritageHouseAdapter = ArrayAdapter.createFromResource(
                this, R.array.HeritageHouses, android.R.layout.simple_spinner_item);
        heritageHouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(heritageHouseAdapter);

        // ArrayAdapter for the TouristNumbers Spinner
        ArrayAdapter<CharSequence> touristNumAdapter = ArrayAdapter.createFromResource(
                this, R.array.TouristNumbers, android.R.layout.simple_spinner_item);
        touristNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNum.setAdapter(touristNumAdapter);

        // Set item selection listeners for both Spinners
        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedHouse = spinTour.getSelectedItem().toString();
                showToast("Heritage House Selected: " + selectedHouse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Heritage House Selected. Please, Select a Tour");
            }
        });

        spinNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTouristNum = spinNum.getSelectedItem().toString();
                showToast("Tourist Number Selected: " + selectedTouristNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                showToast("No Tourist Number Selected");
            }
        });

        setListeners();
        setupButtonClickListener();
    }

    private void setListeners() {
        // Add any necessary listeners
    }

    private void BookScrollView() {
        BookScrollView.setVisibility(View.VISIBLE);
    }

    // Toast message
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Button click listener setup
    @SuppressLint("SetTextI18n")
    private void setupButtonClickListener() {
        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookNow.this, chat.class);
            startActivity(intent);
        });

        // Back button
        backbtn.setOnClickListener(view -> {
            onBackPressed();
        });

        // Save button
        btnsave.setOnClickListener(view -> {
            String selectedTour = spinTour.getSelectedItem().toString();
            String selectedTouristNumStr = spinNum.getSelectedItem().toString();

            if (!selectedTour.equals("Select Tour") && !selectedTouristNumStr.equals("Number of Tourists")) {
                int selectedTouristNum = Integer.parseInt(selectedTouristNumStr);

                // Calculate payment details
                double tourPrice = calculateTourPrice(selectedTour);
                double rfTourGuide = calculateRFTourGuide(selectedTouristNum);
                double rfHouse = calculateRFHouse(selectedTour);
                double serviceCharge = 1000.00;
                double subtotal = calculateSubtotal(selectedTour, tourPrice);
                double total = calculateTotal(selectedTour, tourPrice, rfTourGuide, serviceCharge, subtotal);

// Set the text for TextViews
                GalaRodriguez.setText("Gala Rodriguez: " + calculateTourPrice("Gala Rodriguez"));
                DonCatalino.setText("Don Catalino: " + calculateTourPrice("Don Catalino"));
                Subtotal.setText("Subtotal: " + calculateSubtotal(selectedTour, tourPrice));
                RFTourGuide.setText("Reserve Fee (TourGuide): " + rfTourGuide);
                RFHouse.setText("Reserve Fee (House): " + rfHouse);
                Total.setText("Total: " + total);


                String userId = mAuth.getCurrentUser().getUid();

                // Add data to Firestore
                addDataToFirestore(userId, selectedTour, selectedTouristNumStr);

                // Make the ScrollView visible
                BookScrollView();
            } else {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateRFHouse(String selectedTour) {
        double reservationFeeHouse = 0.0;

        // Calculate the reservation fee for the house based on the selected tour
        if (selectedTour.equals("Don Catalino")) {
            reservationFeeHouse = 500.0;
        } else if (selectedTour.equals("Gala Rodriguez")) {
            reservationFeeHouse = 1000.0;
        } else if (selectedTour.equals("Both")) {
            reservationFeeHouse = 1500.0;
        }
        return reservationFeeHouse;
    }
    private double calculateServiceCharge() {
        double serviceCharge = 1000.0;
        return serviceCharge;
    }

    // Calculate the tour price based on the selected tour
    private double calculateTourPrice(String selectedTour) {
        double tourPrice = 0.0;
        if (selectedTour.equals("Don Catalino")) {
            tourPrice= 500.0;
        } else if (selectedTour.equals("Gala Rodriguez")) {
            tourPrice = 1000.0;
        } else if (selectedTour.equals("Both")) {
            tourPrice = 1500.0;
        }
        return tourPrice;
    }

    //  calculating the reservation fee for the tour guide
    private double calculateRFTourGuide(int selectedTouristNum) {
        double reservationFee = 0.0;
        if (selectedTouristNum >= 1 && selectedTouristNum <= 5) {
            reservationFee = 500.0;
        } else if (selectedTouristNum > 5) {
            reservationFee = 1000.0;
        }
        return reservationFee;
    }


// calculation in subtotal
private double calculateSubtotal(String selectedTour, double tourPrice) {
    double subtotal = 0.0;
    if (selectedTour.equals("Don Catalino") || selectedTour.equals("Gala Rodriguez") || selectedTour.equals("Both")) {
        subtotal = tourPrice;
    }
    return subtotal;
}


    // calculating the total here
    private double calculateTotal(String selectedTour, double tourPrice, double reservationFeeTourGuide, double serviceCharge, double subtotal) {
        double total = 0.0;
        if (selectedTour.equals("Don Catalino") || selectedTour.equals("Gala Rodriguez") || selectedTour.equals("Both")) {
            total = tourPrice + reservationFeeTourGuide + serviceCharge + subtotal;
        }
        return total;
    }

    // Add data to Firestore... Wag mo iirremove lea
    private void addDataToFirestore(String userId, String selectedTour, String selectedTouristNum) {
        if (!selectedTour.equals("Select Tour") && !selectedTouristNum.equals("Number of Tourists")) {
            DocumentReference userDocRef = db.collection("users").document(userId);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> bookingData = task.getResult().getData();

                    if (bookingData != null) {
                        bookingData.put("selectedTour", selectedTour);
                        bookingData.put("selectedTouristNum", selectedTouristNum);

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

                        userDocRef.set(user).addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Booking created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), PaymentDetails.class));
                        }).addOnFailureListener(exception -> {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }
    }
}
