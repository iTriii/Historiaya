package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

public class RescheduleTourists extends AppCompatActivity {
    //FOR UPDATE ONLY
    private ImageButton chatbtn, backbtn;
    private FirebaseFirestore db;
    private Spinner spinTour;
    private Spinner spinNum;
    private Button btntime1, btntime2, btntime3, btntime4;
    private FirebaseAuth mAuth;
    private ScrollView BookScrollView;
    CalendarView calendarView;
    ImageView Event_Sched, calendarV;
    String reservedDate = "";
    String selectedTime = "";
    private String selectedTour = "";
    private String selectedTouristNumStr = "";
    private Button lastClickedButton;
    private SharedPreferences sharedPreferences;
    private Dialog dialog;
    private Button btnnext;
    private Button btncan;
    private Drawable btntime;
    private Object selectedDate;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_tourists);

// Initialize UI elements
        initializeUI();


        // Initialize the dialog
        dialog = new Dialog(RescheduleTourists.this);
        dialog.setContentView(R.layout.dialog_reschedule);

        // Set the dialog window size and make it not cancellable
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);


        // NAVIGATE TO BOOKING DETAILS MAIN
        btncan.setOnClickListener(v -> {
            Intent intent = new Intent(RescheduleTourists.this, BookingDetailMain.class);
            startActivity(intent);
        });


        // Find buttons in the dialog layou
        Button confirmbtn = dialog.findViewById(R.id.confirmbtn);

        //NAVIGATE TO REFUND USER COPY ONCE THE USER CONFIRM IT
        confirmbtn.setOnClickListener(v -> {
            Intent backIntent = new Intent(RescheduleTourists.this, Main2.class);  //NAVIGATE TO REFUND USER COPY ONCE THE USER CONFIRM IT
            startActivity(backIntent);
            Toast.makeText(RescheduleTourists.this, "Your new Request tour is successfully. Please wait for approval", Toast.LENGTH_LONG).show();
            dialog.show();
        });


        // Configure Crisp
        Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");

        // Set up spinner adapters
        btntime1 = findViewById(R.id.btntime1);
        btntime2 = findViewById(R.id.btntime2);
        btntime3 = findViewById(R.id.btntime3);
        btntime4 = findViewById(R.id.btntime4);


        FirebaseApp.initializeApp(this);
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };


        onBackPressedDispatcher.addCallback(this, callback);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RescheduleTourists.this);

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


        btntime = btntime1.getBackground();
        btntime = btntime2.getBackground();
        btntime = btntime3.getBackground();
        btntime = btntime4.getBackground();

        setupSpinners();
        setListeners();
        setupButtonClickListener(btntime1, btntime2, btntime3, btntime4);

        // Retrieve the last clicked button ID from SharedPreferences
        int lastClickedButtonId = sharedPreferences.getInt("selectedButtonId", -1);
        if (lastClickedButtonId != -1) {
            // Set the color of the last clicked button
            Button lastClickedButton = findViewById(lastClickedButtonId);
            if (lastClickedButton != null) {
                updateButtonColors(lastClickedButton);
            }
        }
    }

    private void initializeUI() {
        // Initialize UI elements here
        btnnext = findViewById(R.id.btnnext);
        spinTour = findViewById(R.id.spinTour);
        spinNum = findViewById(R.id.touristnum);
        BookScrollView = findViewById(R.id.BookScrollView);
        btncan = findViewById(R.id.btncan);
        chatbtn = findViewById(R.id.chatbtn);
        backbtn = findViewById(R.id.backbtn);
        calendarView = findViewById(R.id.Calendar);
    }

    @SuppressLint("DefaultLocale")
    private void updateButtonColors(Button clickedButton) {
        if (lastClickedButton != null) {
            lastClickedButton.setBackground(getBtntime(lastClickedButton));
            lastClickedButton.setTextColor(Color.parseColor("#00684E"));
        }

        // Change the color of the clicked button to green
        clickedButton.setBackgroundColor(Color.parseColor("#00A197")); //btn color once the tourist click the button
        clickedButton.setTextColor(Color.WHITE); // textcolor
        lastClickedButton = clickedButton;      // Update the last clicked button
    }

    private Drawable getBtntime(Button button) {
        if (button == btntime1) {
            return btntime;
        } else if (button == btntime2) {
            return btntime;
        } else if (button == btntime3) {
            return btntime;
        } else if (button == btntime4) {
            return btntime;
        }
        return null;
    }


    private void setupSpinners() {
        ArrayAdapter<CharSequence> heritageHouseAdapter = ArrayAdapter.createFromResource(
                this, R.array.HeritageHouses, android.R.layout.simple_spinner_item);
        heritageHouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTour.setAdapter(heritageHouseAdapter);

        // Create an adapter for the Tourist Numbers spinner
        ArrayAdapter<CharSequence> touristNumAdapter = ArrayAdapter.createFromResource(
                this, R.array.TouristNumbers, android.R.layout.simple_spinner_item);
        touristNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNum.setAdapter(touristNumAdapter);

        // Get the current user
        FirebaseUser user = mAuth.getCurrentUser();

        // Set user attributes in Crisp
        if (user != null) {
            String userEmail = user.getEmail();
            String username = user.getDisplayName();

            // Set user attributes in Crisp
            assert userEmail != null;
            Crisp.setUserEmail(userEmail);
        }
        setListener();
    }

    private void setListener() {
        // listeners for both Spinners
        spinTour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTour = spinTour.getSelectedItem().toString();
                saveToSharedPreferences("selectedTour", selectedTour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // showToast("No Heritage House Selected. Please, Select a Tour");
            }
        });

        spinNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTouristNum = spinNum.getSelectedItem().toString();
                saveToSharedPreferences("selectedTouristNum", selectedTouristNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // showToast("No Tourist Number Selected");
            }
        });
    }


    //SharedPreferences
    private void saveToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

        if (lastClickedButton != null) {
            editor.putInt("selectedButtonId", lastClickedButton.getId());
            editor.apply();
        }
    }


    private void setListeners() {
        // Add any necessary listeners
        BookScrollView();
    }

    private void BookScrollView() {
        BookScrollView.setVisibility(View.VISIBLE);

    }


    // Toast message
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setupButtonClickListener(Button btntime1, Button btntime2, Button btntime3, Button btntime4) {


        btnnext.setOnClickListener(view -> {
            dialog.show(); // show the dialogreschedule
        });


        // Chat button
        chatbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RescheduleTourists.this, ChatActivity.class);
            startActivity(intent);
        });


        // Back button
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RescheduleTourists.this, Main2.class);
            startActivity(intent);
        });

// IMPLEMENT A BUTTON TIME
        btntime1.setOnClickListener(v -> {
            selectedTime = "10:00 AM";
            saveToSharedPreferences("selectedTime", selectedTime);
            updateButtonColors((Button) v);
        });

        btntime2.setOnClickListener(v -> {
            selectedTime = "11:00 AM";
            saveToSharedPreferences("selectedTime", selectedTime);
            updateButtonColors((Button) v);
        });

        btntime3.setOnClickListener(v -> {
            selectedTime = "1:00 PM";
            saveToSharedPreferences("selectedTime", selectedTime);
            updateButtonColors((Button) v);
        });

        btntime4.setOnClickListener(v -> {
            selectedTime = "2:00 PM";
            saveToSharedPreferences("selectedTime", selectedTime);
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
                return;
            } else {
                // Handle date selection here
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                reservedDate = dateFormat.format(selectedDate.getTime());
                //showToast("Your reserve date is: " + reservedDate);
            }

            // Save the selected date and time to SharedPreferences
            saveToSharedPreferences("reservedDate", reservedDate);
            saveToSharedPreferences("selectedTime", selectedTime);

            // Save data to Firestore asynchronously to prevent UI lag
            new Thread(() -> {
                try {
                    saveDateTimeToFirestore(String.valueOf(selectedDate), selectedTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> showToast("Error saving date and time: " + e.getMessage()));
                }

            }).start();
        });
    }


    private void startCrispChat() {
        //CRISP
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


    // Call this method to save values whenever needed
    private void saveValues() {
        saveToSharedPreferences("selectedTour", spinTour.getSelectedItem().toString());
        saveToSharedPreferences("touristnum", spinNum.getSelectedItem().toString());
        saveToSharedPreferences("selectedTourIndex", String.valueOf(spinTour.getSelectedItemPosition()));
    }

    // Call this method to retrieve values whenever needed
    private void retrieveValues() {
        String selectedTourIndex = getFromSharedPreferences("selectedTourIndex");
        if (!selectedTourIndex.isEmpty()) {
            int index = Integer.parseInt(selectedTourIndex);
            spinTour.setSelection(index);

        }
    }


    // Method to retrieve values from SharedPreferences
    private String getFromSharedPreferences(String key) {
        return sharedPreferences.getString(key, ""); // Default value "" can be changed to suit your needs
    }


    // Override onPause to save values when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        saveValues();
    }

    // Override onResume to retrieve values when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        retrieveValues();


        String savedSelectedTouristNum = getFromSharedPreferences("selectedTouristNum");
        if (!savedSelectedTouristNum.isEmpty()) {
            // Find the position of the saved item in the spinner's adapter and set it as selected
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinNum.getAdapter();
            if (adapter != null) {
                int position = adapter.getPosition(savedSelectedTouristNum);
                spinNum.setSelection(position);
            }
        }
        // Retrieve and restore the selected date and time from SharedPreferences
        // Restore the original backgrounds based on the last clicked button ID
        int lastClickedButtonId = sharedPreferences.getInt("selectedButtonId", -1);
        if (lastClickedButtonId != -1) {
            lastClickedButton = findViewById(lastClickedButtonId);
            if (lastClickedButton != null) {
                // Apply the color to the last clicked button
                updateButtonColors(lastClickedButton);

            }
        }


        // Retrieve and restore the selected date from SharedPreferences
        String savedReservedDate = getFromSharedPreferences("reservedDate");
        if (!savedReservedDate.isEmpty()) {
            // Parse the saved date and set it to the CalendarView
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            reservedDate = savedReservedDate; // Use the saved date directly
            try {
                Calendar savedDateCalendar = Calendar.getInstance();
                savedDateCalendar.setTime(dateFormat.parse(savedReservedDate));
                long savedDateInMillis = savedDateCalendar.getTimeInMillis();
                calendarView.setDate(savedDateInMillis, true, true);
            } catch (ParseException e) {
                e.printStackTrace();

            }

            String savedSelectedTime = getFromSharedPreferences("selectedTime");

            if (!savedSelectedTime.isEmpty()) {
                switch (savedSelectedTime) {
                    case "10:00 AM":
                        updateButtonColors(btntime1);
                        break;
                    case "11:00 AM":
                        updateButtonColors(btntime2);
                        break;
                    case "1:00 PM":
                        updateButtonColors(btntime3);
                        break;
                    case "2:00 PM":
                        updateButtonColors(btntime4);
                        break;
                    default:
                        break;
                }
            }
        }
    }



    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
        finish();
    }


    private void saveDateTimeToFirestore(String selectedDate, String selectedTime) {
        // Initialize Firestore references
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("users").document(userId);
//        CollectionReference rescheduleBookingCollectionRef = userDocRef.collection("rescheduleBooking");

        // Check if the selected date and time are null
        if (selectedDate == null || selectedTime == null || TextUtils.isEmpty(selectedTime) || spinTour == null || spinNum == null) {
            return;
        }

        // Get selected values from spinners
        String selectedTour = spinTour.getSelectedItem().toString();
        String selectedTouristNum = spinNum.getSelectedItem().toString();


        // Check if spinner values are null or empty
        if (TextUtils.isEmpty(selectedTour) || TextUtils.isEmpty(selectedTouristNum)) {
            return;
        }

        // Add data to Firestore
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("selectedTour_for_Reschedule", selectedTour);
        bookingData.put("selectedTouristNum_for_Reschedule", selectedTouristNum);
        bookingData.put("selectedDate_for_Reschedule", selectedDate);
        bookingData.put("selectedTime_for_Reschedule", selectedTime);

        // Create a new document in the "rescheduleBooking" collection
  //      DocumentReference newBookingDocRef = rescheduleBookingCollectionRef.document();

        // Set data and handle success/failure
        userDocRef.set(bookingData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Reschedule uploaded. Please wait for the admin", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), BookingDetailMain.class));
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Error creating a new booking document: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}