package com.example.log_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
//FOR UPDATE ONLY

public class TourismHeadAdmin extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private RadioButton Upcoming_Tab, History_tab, Pending_Tab, Cancel_Tab;
    private ScrollView Upcoming_ScrollView, History_ScrollView, Pending_ScrollView, Cancellation_ScrollView;
    private View wan, to, tre, por;
    private FirebaseFirestore db;


    private RecyclerView Pending_RecyclerView, Upcoming_RecyclerView, History_RecyclerView, Cancellation_RecyclerView;
    private ArrayList<User> userArrayList;
    private ArrayList<User> upcomingList;
    private ArrayList<User> cancellationList;
    private ArrayList<User> historyList ;
    private MyAdapter myAdapter;
    private com.example.log_in.UpcomingAdapter UpcomingAdapter;
    private com.example.log_in.HistoryAdapter HistoryAdapter;
    private CancellationAdapter CancellationAdapter;
    FirebaseUser user;
    FirebaseAuth auth;

    private ProgressDialog progressDialog;

    private Button uploadImageTH_btn;
    private Object Email;
    ImageView eventSched, addTM;
    private ListenerRegistration userDataListener;
    private Object cancellationStatus;

    private int currentAdapterType = MY_ADAPTER; // Add this variable and initialize with the default adapter type

    // Add constants for adapter types
    private static final int MY_ADAPTER = 1;
    private static final int UPCOMING_ADAPTER = 2;
    private static final int HISTORY_ADAPTER = 3;
    private static final int CANCELLATION_ADAPTER = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_head_admin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        addTM = findViewById(R.id.addTM);
        eventSched = findViewById(R.id.eventSched);
        uploadImageTH_btn = findViewById(R.id.uploadImageTH_btn);
        uploadImageTH_btn.setOnClickListener(v -> {
            openGallery();
        });

        // Initialize Firebase Authentication and Firestore
        wan = findViewById(R.id.wan);
        to = findViewById(R.id.to);
        tre = findViewById(R.id.tre);
        por = findViewById(R.id.por);



        // Initialize RecyclerViews and Adapters
        Pending_RecyclerView = findViewById(R.id.Pending_RecyclerView);
        Upcoming_RecyclerView = findViewById(R.id.Upcoming_RecyclerView);
        History_RecyclerView = findViewById(R.id.History_RecyclerView);
        Cancellation_RecyclerView = findViewById(R.id.Cancellation_RecyclerView);


        userArrayList = new ArrayList<>();



        myAdapter = new MyAdapter(this, userArrayList, db);
        UpcomingAdapter = new UpcomingAdapter(this,  userArrayList, db);
        HistoryAdapter = new HistoryAdapter(this, userArrayList, db);
        CancellationAdapter = new CancellationAdapter(this, userArrayList, db);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference usersDocRef = db.collection("users");
            DocumentReference userDocRef = usersDocRef.document(userId);
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        // Document exists, update fields
                        userDocRef.update(User.FIELD_STATUS, "")
                                .addOnSuccessListener(aVoid -> Log.d("TourismHeadAdmin", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.e("TourismHeadAdmin", "Error updating document", e));
                    } else {
                        Log.d("TourismHeadAdmin", "Document does not exist. Creating a new one.");
                        Map<String, Object> user = new HashMap<>();
                        user.put(User.FIELD_USER_ID, userId);
                        user.put(User.FIELD_EMAIL, Email); // Ensure Email is not null
                        usersDocRef.document(userId).set(user)
                                .addOnSuccessListener(aVoid -> Log.d("TourismHeadAdmin", "DocumentSnapshot successfully written!"))
                                .addOnFailureListener(e -> Log.e("TourismHeadAdmin", "Error writing document", e));
                    }
                } else {
                    // Handle the exception
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("TourismHeadAdmin", "Error getting document", exception);
                    }
                }
            });
        }

        fetchImageFromStorage();


        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        EventChangeListener();// Add data listener to load data from Firestore
        setUpRecyclerView(); // Set up RecyclerView and Adapter
        setUpTabsAndViews(); // Set up tabs and views
    }

    private void fetchImageFromStorage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Calendar/calendar_image.jpg");

        // Fetch the image and load it into the eventSched ImageView
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the retrieved image using Glide or set it directly to the ImageView
            Glide.with(this)
                    .load(uri)
                    .into(eventSched);
            addTM.setVisibility(View.GONE);
        }).addOnFailureListener(exception -> {
            // Handle any errors that may occur while fetching the image
            showToast("Failed to fetch image: " + exception.getMessage());
        });
    }

    private void showToast(String s) {
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the selected image from the gallery
            Uri selectedImageUri = data.getData();

            // Set the selected image to the eventSched ImageView
            eventSched.setImageURI(selectedImageUri);

            // Hide the addTM ImageView
            addTM.setVisibility(View.GONE);

            // Upload the selected image to Firebase Storage under the "Calendar" document
            uploadImageToStorage(selectedImageUri);
        }
    }

    private void uploadImageToStorage(Uri imageUri) {
        if (imageUri != null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String imageName = "calendar_image.jpg"; // Set a name for the image in storage

                // Get a reference to the Firebase Storage location
                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("Calendar")
                        .child(imageName);

                // Upload the image to Firebase Storage
                UploadTask uploadTask = storageReference.putFile(imageUri);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(this, "Image uploaded to Firebase Storage", Toast.LENGTH_SHORT).show();
                    // You can also retrieve the download URL of the uploaded image here if needed
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        // You can use the download URL for further operations if required
                    });
                }).addOnFailureListener(e -> {
                    // Handle any errors during the upload process
                    Log.e("FirebaseStorage", "Error uploading image: " + e.getMessage());
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }

    private void setUpTabsAndViews() {

        // Set up tabs and views
        Upcoming_ScrollView = findViewById(R.id.Upcoming_ScrollView); //Upcoming_ScrollView
        Upcoming_Tab = findViewById(R.id.Upcoming_Tab);
        Upcoming_Tab.setOnClickListener(v -> Upcoming_Tab());
        History_ScrollView = findViewById(R.id.History_ScrollView); //History Scrollview
        History_tab = findViewById(R.id.History_tab);
        History_tab.setOnClickListener(v -> History_tab());
        Pending_ScrollView = findViewById(R.id.Pending_ScrollView); //Pending Scrollview
        Pending_Tab = findViewById(R.id.Pending_Tab);
        Pending_Tab.setOnClickListener(v -> Pending_Tab());
        Cancellation_ScrollView = findViewById(R.id.Cancellation_ScrollView); //Cancellation Scrollview
        Cancel_Tab = findViewById(R.id.Cancel_Tab);
        Cancel_Tab.setOnClickListener(v -> Cancel_Tab());

    }

    private void Upcoming_Tab() {
        Upcoming_Tab.setChecked(true);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        Upcoming_ScrollView.setVisibility(View.VISIBLE);
        History_tab.setChecked(false);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        History_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setChecked(false);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Pending_ScrollView.setVisibility(View.GONE);
        Cancel_Tab.setChecked(false);
        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Cancellation_ScrollView.setVisibility(View.GONE);

        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

        // Show only Upcoming RecyclerView
        Upcoming_RecyclerView.setVisibility(View.VISIBLE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.GONE);
    }


    private void History_tab() {
        Upcoming_Tab.setChecked(false);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Upcoming_ScrollView.setVisibility(View.GONE);
        History_tab.setChecked(true);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        History_ScrollView.setVisibility(View.VISIBLE);
        Pending_Tab.setChecked(false);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Pending_ScrollView.setVisibility(View.GONE);
        Cancel_Tab.setChecked(false);
        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Cancellation_ScrollView.setVisibility(View.GONE);
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

        // Show only History RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.VISIBLE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.GONE);

    }


    private void Pending_Tab() {
        Upcoming_Tab.setChecked(false);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Upcoming_ScrollView.setVisibility(View.GONE);
        History_tab.setChecked(false);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        History_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setChecked(true);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        Pending_ScrollView.setVisibility(View.VISIBLE);
        Cancel_Tab.setChecked(false);
        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Cancellation_ScrollView.setVisibility(View.GONE);
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

        // Show only Pending RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.VISIBLE);
        Cancellation_RecyclerView.setVisibility(View.GONE);

    }

    private void Cancel_Tab() {
        Upcoming_Tab.setChecked(false);
        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Upcoming_ScrollView.setVisibility(View.GONE);
        History_tab.setChecked(false);
        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        History_ScrollView.setVisibility(View.GONE);
        Pending_Tab.setChecked(false);
        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
        Pending_ScrollView.setVisibility(View.GONE);
        Cancel_Tab.setChecked(true);
        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
        Cancellation_ScrollView.setVisibility(View.VISIBLE);
        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.green));


        // Show only Cancellation RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.VISIBLE);
    }


    // RecyclerView setup
    private void setUpRecyclerView() {
        Pending_RecyclerView.setHasFixedSize(true);
        Pending_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Pending_RecyclerView.setAdapter(myAdapter);

        Upcoming_RecyclerView.setHasFixedSize(true);
        Upcoming_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Upcoming_RecyclerView.setAdapter(UpcomingAdapter);

        History_RecyclerView.setHasFixedSize(true);
        History_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        History_RecyclerView.setAdapter(HistoryAdapter);

        Cancellation_RecyclerView.setHasFixedSize(true);
        Cancellation_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cancellation_RecyclerView.setAdapter(CancellationAdapter);


    }

    private void EventChangeListener() {
        // Access the "users" collection in Firestore, order by "reservedDate" in ascending order
        db.collection("users").orderBy("reservedDate", Query.Direction.ASCENDING)
                // Add a snapshot listener to listen for changes in the collection
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    // Initialize HashMaps for users
                    final HashMap<String, User> userHashMap = new HashMap<>();
                    final HashMap<String, User> upcomingUserHashMap = new HashMap<>();
                    final HashMap<String, User> historyUserHashMap = new HashMap<>();
                    final HashMap<String, User> cancellationHashMap = new HashMap<>();

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    // This method is called when there is a change in the Firestore collection
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        // Check if there's an error
                        if (error != null) {
                            // If there is an error, try fetching and displaying user data
                            try {
                                fetchAndDisplayUserData();
                            } catch (Exception e) {
                                // Log the error if fetching and displaying user data fails
                                Log.e("TourismHeadAdmin", "Error in fetchAndDisplayUserData: " + e.getMessage());
                            }

                            // Show a progress dialog if it's not already showing
                            if (!progressDialog.isShowing()) progressDialog.show();
                            // Log the error message
                            Log.e("Error", Objects.requireNonNull(error.getMessage()));
                            return; // Exit the method if there's an error
                        }

                        // Handle data changes in the Firestore collection
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    // Convert Firestore document to a User object
                                    User user = dc.getDocument().toObject(User.class);

                                    // Set the user ID
                                    user.setUserId(dc.getDocument().getId());

                                    // Add the user to the userHashMap using their email as the key
                                    userHashMap.put(user.getEmail(), user);

                                    // Check if the user is upcoming or not and add to the respective HashMap
                                    if (user.isUpcoming()) {
                                        upcomingUserHashMap.put(user.getEmail(), user);
                                    } else {
                                        historyUserHashMap.put(user.getEmail(), user);
                                    }

                                    // Check if the user is canceled based on the cancellationStatus field
                                    if (user.iscancellationStatus()) {
                                        cancellationHashMap.put(user.getEmail(), user);
                                    }
                                    break;
                                case REMOVED:
                                    // If a user is removed, remove them only from UserHashMap
                                    User userRemoved = dc.getDocument().toObject(User.class);
                                    String userEmail = userRemoved.getEmail();

                                    // Check if the reservedDate is in the past for upcoming users
                                    if (upcomingUserHashMap.containsKey(userEmail) && !userRemoved.isUpcoming()) {
                                        upcomingUserHashMap.remove(userEmail);
                                    }
                                    // Always remove from the userHashMap
                                    userHashMap.remove(userEmail);

                                    // Check if the user is canceled based on the cancellationStatus field
                                    if (userRemoved.iscancellationStatus()) {
                                        cancellationHashMap.remove(userEmail);
                                    }
                                    break;
                            }
                        }

                        // Filter data based on the selected adapter
                        switch (currentAdapterType) {
                            case MY_ADAPTER:
                                userArrayList.clear();
                                userArrayList.addAll(userHashMap.values());
                                myAdapter.notifyDataSetChanged();
                                break;
                            case UPCOMING_ADAPTER:
                                upcomingList.clear();
                                upcomingList.addAll(upcomingUserHashMap.values());
                                UpcomingAdapter.notifyDataSetChanged();
                                break;
                            case HISTORY_ADAPTER:
                                // Sort historyUserHashMap based on "reservedDate"
                                List<User> historyList = new ArrayList<>(historyUserHashMap.values());
                                Collections.sort(historyList, new Comparator<User>() {
                                    @Override
                                    public int compare(User user1, User user2) {
                                        // Compare based on reservedDate
                                        return user1.getReservedDate().compareTo(user2.getReservedDate());
                                    }
                                });

                                // Clear and re-populate the userArrayList
                                historyList.clear();
                                historyList.addAll(historyList);

                                // Notify the HistoryAdapter of the data change
                                HistoryAdapter.notifyDataSetChanged();
                                break;

                            case CANCELLATION_ADAPTER:
                                // Clear and re-populate the cancellationList
                                cancellationList.clear();

                                // Filter by cancellationStatus
                                for (User user : cancellationHashMap.values()) {
                                    // Add only users with cancellationStatus not "Cancellation (Approved)" or "Cancellation (Rejected)"
                                    if (!user.iscancellationStatus()) {
                                        userArrayList.add(user);
                                    }
                                }

                                // Notify the CancellationAdapter of the data change
                                CancellationAdapter.notifyDataSetChanged();
                                break;
                        }

                        // Dismiss the progress dialog if it's showing
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                });
    }


//    private void EventChangeListener() {
//        // Access the "users" collection in Firestore, order by "reservedDate" in ascending order
//        db.collection("users").orderBy("reservedDate" , Query.Direction.ASCENDING)
//                // Add a snapshot listener to listen for changes in the collection
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    // Initialize HashMaps for users
//                    final HashMap<String, User> userHashMap = new HashMap<>();
//                    final HashMap<String, User> upcomingUserHashMap = new HashMap<>();
//                    final HashMap<String, User> historyUserHashMap = new HashMap<>();
//                    final HashMap<String, User> cancellationHashMap = new HashMap<>();
//
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    // This method is called when there is a change in the Firestore collection
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        // Check if there's an error
//                        if (error != null) {
//                            // If there is an error, try fetching and displaying user data
//                            try {
//                                fetchAndDisplayUserData();
//                            } catch (Exception e) {
//                                // Log the error if fetching and displaying user data fails
//                                Log.e("TourismHeadAdmin", "Error in fetchAndDisplayUserData: " + e.getMessage());
//                            }
//
//                            // Show a progress dialog if it's not already showing
//                            if (!progressDialog.isShowing()) progressDialog.show();
//                            // Log the error message
//                            Log.e("Error", Objects.requireNonNull(error.getMessage()));
//                            return; // Exit the method if there's an error
//                        }
//
//                        // Handle data changes in the Firestore collection
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            switch (dc.getType()) {
//                                case ADDED:
//                                case MODIFIED:
//                                    // Convert Firestore document to a User object
//                                    User user = dc.getDocument().toObject(User.class);
//
//                                    // Set the user ID
//                                    user.setUserId(dc.getDocument().getId());
//
//                                    // Add the user to the userHashMap using their email as the key
//                                    userHashMap.put(user.getEmail(), user);
//
//                                    // Check if the user is upcoming or not and add to the respective HashMap
//                                    if (user.isUpcoming()) {
//                                        upcomingUserHashMap.put(user.getEmail(), user);
//                                    } else {
//                                        historyUserHashMap.put(user.getEmail(), user);
//                                    }
//
//                                    // Check if the user is cancelled and add to the cancellationHashMap
//                                    if (userRemoved.iscancellationStatus()) {
//                                        cancellationHashMap.put(user.getEmail(), user);
//                                    }
//                                    break;
//                                case REMOVED:
//                                    // If a user is removed, remove them only from UserHashMap
//                                    User userRemoved = dc.getDocument().toObject(User.class);
//                                    String userEmail = userRemoved.getEmail();
//
//                                    // Check if the reservedDate is in the past for upcoming users
//                                    if (upcomingUserHashMap.containsKey(userEmail) && !userRemoved.isUpcoming()) {
//                                        upcomingUserHashMap.remove(userEmail);
//                                    }
//                                    // Always remove from the userHashMap
//                                    userHashMap.remove(userEmail);
//                                    break;
//
//                            }
//                        }
//
//                        // Filter data based on the selected adapter
//                        switch (currentAdapterType) {
//                            case MY_ADAPTER:
//                                userArrayList.clear();
//                                userArrayList.addAll(userHashMap.values());
//                                myAdapter.notifyDataSetChanged();
//                                break;
//                            case UPCOMING_ADAPTER:
//                                upcomingList.clear();
//                                upcomingList.addAll(upcomingUserHashMap.values());
//                                UpcomingAdapter.notifyDataSetChanged();
//                                break;
//                            case HISTORY_ADAPTER:
//                                // Sort historyUserHashMap based on "reservedDate"
//                                List<User> historyList = new ArrayList<>(historyUserHashMap.values());
//                                Collections.sort(historyList, new Comparator<User>() {
//                                    @Override
//                                    public int compare(User user1, User user2) {
//                                        // Compare based on reservedDate
//                                        return user1.getReservedDate().compareTo(user2.getReservedDate());
//                                    }
//                                });
//
//                                // Clear and re-populate the userArrayList
//                                historyList.clear();
//                                historyList.addAll(historyList);
//
//                                // Notify the HistoryAdapter of the data change
//                                HistoryAdapter.notifyDataSetChanged();
//                                break;
//
//                            case CANCELLATION_ADAPTER:
////                                List<User> cancellationList = new ArrayList<>(cancellationHashMap.values());
////                                 cancellationList.clear();
////                                 cancellationList.addAll(cancellationHashMap.values());
////                                // Notify the CancellationAdapter of the data change
////                                CancellationAdapter.notifyDataSetChanged();
//                                retrieveCancellationDataFromFirestore();
//                                break;
//
//
//                        }
//                        // Dismiss the progress dialog if it's showing
//                        if (progressDialog.isShowing()) progressDialog.dismiss();
//                    }
//                });
//    }

    private void retrieveCancellationDataFromFirestore() {
        // Initialize Firestore references
        CollectionReference cancellationCollectionRef = db.collection("users");
        Query cancellationQuery = cancellationCollectionRef.orderBy("status", Query.Direction.ASCENDING);

        cancellationQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TourismHeadAdminCancellation", "Error fetching cancellation data: " + error.getMessage());
                    return;
                }

                // Create a new list to avoid modifying the existing list during iteration
                List<User> updatedCancellationList = new ArrayList<>();

                // Handle cancellation data changes
                for (DocumentSnapshot document : value.getDocuments()) {
                    // Deserialize the cancellation data into your CancellationUser class
                    User cancellationList = document.toObject(User.class);
                    if (cancellationList != null) {
                        // Add the cancellation to the updated list
                        updatedCancellationList.add(cancellationList);
                    }
                }

                // Update your cancellationList with the new data
                cancellationList.clear();
                cancellationList.addAll(updatedCancellationList);
                CancellationAdapter.notifyDataSetChanged();

            }
        });
    }


    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        Log.e("TourismHeadActivity", "Error fetching user data: " + error.getMessage());
                        return;
                    }
                    // Handle the document snapshot here
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Extract data from the document snapshot
                        String status = documentSnapshot.getString("status");
                    }
                });
    }


    @Override
    protected void onDestroy() {
        // Remove the snapshot listener when the activity is destroyed
        if (userDataListener != null) {
            userDataListener.remove();
        }

        super.onDestroy();

        // Set a click listener for the edit button
        uploadImageTH_btn.setOnClickListener(v -> {
            // Handle edit button click (implement your edit/update/delete logic here)
            Toast.makeText(TourismHeadAdmin.this, "Edit button clicked", Toast.LENGTH_SHORT).show();
        });





    }
}