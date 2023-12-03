package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

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
    ImageView Event_Sched, calendarV;
    String reservedDate = "";
    private String userId;
    String selectedTime = "";
    private Button lastClickedButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);
        FirebaseApp.initializeApp(this);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);



        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Calendar currentTime = Calendar.getInstance();

        Event_Sched = findViewById(R.id.Event_Sched);
        calendarV = findViewById(R.id.calendarV);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Calendar/calendar_image.jpg");
        storageRef.getDownloadUrl().addOnSuccessListener(Calendar -> {
            Glide.with(this)
                    .load(Calendar) // Provide the actual download URL obtained from Firebase Storage
                    .into(Event_Sched);

            // Hide calendarV ImageView after loading the image into Event_Sched
            calendarV.setVisibility(View.GONE);
        }).addOnFailureListener(exception -> {
            // Handle any errors that may occur while fetching the image
            showToast("Failed to fetch image: " + exception.getMessage());
        });

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

        // Configure Crisp
        Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");

        // Set up spinner adapters
        btntime1 = findViewById(R.id.btntime1);
        btntime2 = findViewById(R.id.btntime2);
        btntime3 = findViewById(R.id.btntime3);
        btntime4 = findViewById(R.id.btntime4);

        setupSpinners();
        setListeners();
        setupButtonClickListener(btntime1, btntime2, btntime3, btntime4);
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

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userEmail = user.getEmail();
            String username = user.getDisplayName();

            // Set user attributes in Crisp
            assert userEmail != null;
            Crisp.setUserEmail(userEmail);
        }

        // Set listeners
        setListener();
    }

    private void setListener() {
        // listeners for both Spinners
        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // showToast("Heritage House Selected: " + spinTour.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // showToast("No Heritage House Selected. Please, Select a Tour");
            }
        });

        spinNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // showToast("Tourist Number Selected: " + spinNum.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // showToast("No Tourist Number Selected");
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
    private void setupButtonClickListener(Button btntime1, Button btntime2, Button btntime3, Button btntime4) {

        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(BookNow.this, ChatActivity.class);
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
// IMPLEMENT A BUTTON TIME
        btntime1.setOnClickListener(v -> {
            selectedTime = "10:00 AM";
            //   showToastAndStoreTime();
            updateButtonColors((Button) v);
        });

        btntime2.setOnClickListener(v -> {
            selectedTime = "11:00 AM";
            //  showToastAndStoreTime();
            updateButtonColors((Button) v);
        });

        btntime3.setOnClickListener(v -> {
            selectedTime = "1:00 PM";
            //  showToastAndStoreTime();
            updateButtonColors((Button) v);
        });

        btntime4.setOnClickListener(v -> {
            selectedTime = "2:00 PM";
            //   showToastAndStoreTime();
            updateButtonColors((Button) v);
            showToastAndStoreTime();
        });

        // Set up the date listener before the button click listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Get the current date
            Calendar currentDate = Calendar.getInstance();
            Calendar selectedDate = Calendar.getInstance();

            selectedDate.set(year, month, dayOfMonth);

            // Check if the selected date is in the past
            if (selectedDate.before(currentDate)) {
                showToast("Cannot select a past date");
                long currentDateInMillis = currentDate.getTimeInMillis();
                calendarView.setDate(currentDateInMillis, true, true);
            } else {
                // Handle date selection here
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                reservedDate = dateFormat.format(selectedDate.getTime());
                //showToast("Your reserve date is: " + reservedDate);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void updateButtonColors(Button clickedButton) {
        // Change the color of the last clicked button to its original color
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundColor(Color.parseColor("#757575"));
        }

        // Change the color of the clicked button to green
        clickedButton.setBackgroundColor(Color.parseColor("#00A197")); //btn color once the tourist click the button
        clickedButton.setTextColor(Color.WHITE); // textcolor
        lastClickedButton = clickedButton;      // Update the last clicked button


        btnsave.setOnClickListener(view -> {
            // Use the reservedDate directly in your button click listener
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
                double total = subtotal + rfTourGuide + serviceCharge;

                // Create an Intent to start the ViewActivity
                Intent intent = new Intent(BookNow.this, PaymentDetails.class);

                // Put the data into the Intent
                intent.putExtra("selectedTour", selectedTour);
                intent.putExtra("selectedTouristNum", selectedTouristNumStr);
                intent.putExtra("reservedDate", reservedDate);
                intent.putExtra("total", total);
                intent.putExtra("selectedTime", selectedTime);
                intent.putExtra("Subtotal", subtotal);
                intent.putExtra("TourGuide", rfTourGuide);
                intent.putExtra("serviceCharge", serviceCharge);

                // Start the new activity
                startActivity(intent);

                // Update UI with calculated values
                Subtotal.setText(String.format(" ₱%.2f", subtotal));
                selectedHouse.setText(String.format(" %s", selectedTour));
                RFTourGuide.setText(String.format(" ₱%.2f", rfTourGuide));
                SCharge.setText(String.format(" ₱%.2f", serviceCharge));
                Total.setText(String.format("₱%.2f", total));


                // Add data to Firestore
                userId = mAuth.getCurrentUser().getUid();
                addDataToFirestore(userId, selectedTour, selectedTouristNumStr, reservedDate, total, selectedTime);

                // Make the ScrollView visible
                BookScrollView();
            } else {
                Toast.makeText(getApplicationContext(), "Please select a valid tour and number of tourists.", Toast.LENGTH_LONG).show();
            }
        });
    }


    //CRISP
    private void startCrispChat() {
        Crisp.setUserEmail(mAuth.getCurrentUser().getEmail());

        // Start Crisp chat
        Intent chatIntent = new Intent(this, ChatActivity.class);
        startActivity(chatIntent);
    }

    //TOAST FOR SELECTED TIME OF THE TOURISTS
    private void showToastAndStoreTime() {
        Set<String> selectedTime = new HashSet<>();
        // Check if the time is already selected
        // showToast("Time selected: " + selectedTime);
        selectedTime.add(selectedTime.toString());
    }

    //calculation starts here
    private double calculateTotal(double rfTourGuide, double serviceCharge, String selectedTour) {
        double subtotal = calculateSubtotal(selectedTour);
        double total = subtotal + rfTourGuide + serviceCharge;
        return total;
    }

    double calculateSubtotal(String selectedTour) {
        double subtotal = 0.0;
        // subtotal based on the selected tour
        switch (selectedTour) {
            case "Don Catalino Rodriguez":
                subtotal = 500.0;
                break;
            case "Gala Rodriguez House":
                subtotal = 1000.0;
                break;
            case "Both":
                subtotal = 1500.0;
                break;
            default:
                // Default case if the selectedTour doesn't match any known cases
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
            case "Don Catalino Rodriguez":
                tourPrice = 500.0;
                break;
            case "Gala Rodriguez House ":
                tourPrice = 1000.0;
                break;
            case "Both":
                tourPrice = 1500.0;
                break;
        }
        return tourPrice;
    }


    private void addDataToFirestore(String userId, String selectedTour, String selectedTouristNum, String reservedDate, double totalAmount, String selectedTime) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> bookingData = new HashMap<>();
                if (task.getResult() != null && task.getResult().getData() != null) {
                    bookingData.putAll(task.getResult().getData());
                }
                if (hasBooking(bookingData)) {
                    // The user already has a booking
                    showToast("You already have a booking. Redirecting to your Booking Details");
                    navigateToBookingDetailMainActivity();
                } else {
                    // The user doesn't have a booking, create one
                    bookingData.put("selectedTour", selectedTour);
                    bookingData.put("selectedTouristNum", selectedTouristNum);
                    bookingData.put("reservedDate", reservedDate);
                    bookingData.put("totalAmount", totalAmount);
                    bookingData.put("selectedTime", selectedTime);

                    userDocRef.update(bookingData).addOnSuccessListener(documentReference -> {
                        showToast("Booking created");
                    }).addOnFailureListener(exception -> {
                        showToast("Failed to create booking: " + exception.getMessage());
                    });
                }
            } else {
                showToast("Error checking booking status: " + task.getException().getMessage());
            }
        });
    }

    private void navigateToBookingDetailMainActivity() {
        // Replace this with the intent for the activity you want to navigate to
        Intent intent = new Intent(BookNow.this, BookingDetailMain.class);
        startActivity(intent);
        finish(); // Finish the current activity to prevent the user from coming back to the booking screen
    }

    private boolean hasBooking(Map<String, Object> bookingData) {
        // Check if the user already has a booking
        return bookingData.containsKey("selectedTour") && bookingData.containsKey("selectedTouristNum")
                && bookingData.containsKey("reservedDate") && bookingData.containsKey("totalAmount")
                && bookingData.containsKey("selectedTime");
    }

    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        finish();
    }

}