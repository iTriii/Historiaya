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
import java.util.HashMap;
import java.util.Map;
//FOR UPDATE ONLY

public class TourismHeadAdmin extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;

    private RadioButton Upcoming_Tab, History_tab, Pending_Tab, Cancel_Tab, Refund_Tab, reschedule_Tab;
    private ScrollView Upcoming_ScrollView, History_ScrollView, Pending_ScrollView, Cancellation_ScrollView, Reschedule_ScrollView, Refund_ScrollView;
    private View wan, to, tre, por, payb, six;
    private FirebaseFirestore db;


    private RecyclerView Pending_RecyclerView, Upcoming_RecyclerView, History_RecyclerView, Refund_RecyclerView, Reschedule_RecyclerView, Cancellation_RecyclerView;
    private ArrayList<User> userArrayList;
    private MyAdapter myAdapter;
    private com.example.log_in.UpcomingAdapter UpcomingAdapter;
    private com.example.log_in.HistoryAdapter HistoryAdapter;
    private RefundAdapter RefundAdapter;
    private RescheduleAdapter RescheduleAdapter;
    private CancellationAdapter CancellationAdapter;
    FirebaseUser user;
    FirebaseAuth auth;

    private ProgressDialog progressDialog;

    private Button uploadImageTH_btn;
    private Object Email;
    ImageView eventSched, addTM;
    private ListenerRegistration userDataListener;


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
//        payb = findViewById(R.id.payb);
//        payb = findViewById(R.id.payb);
//        six = findViewById(R.id.six);


        // Initialize RecyclerViews and Adapters
        Pending_RecyclerView = findViewById(R.id.Pending_RecyclerView);
        Upcoming_RecyclerView = findViewById(R.id.Upcoming_RecyclerView);
        History_RecyclerView = findViewById(R.id.History_RecyclerView);
        Cancellation_RecyclerView = findViewById(R.id.Cancellation_RecyclerView);
//        Reschedule_RecyclerView = findViewById(R.id.Reschedule_RecyclerView);
//        Refund_RecyclerView = findViewById(R.id.Refund_RecyclerView);


        userArrayList = new ArrayList<>();

        myAdapter = new MyAdapter(this, userArrayList, db);
        //    PendingAdapter = new PendingAdapter(this, userArrayList, db);
        UpcomingAdapter = new UpcomingAdapter(this, userArrayList, db);
        HistoryAdapter = new HistoryAdapter(this, userArrayList, db);
        CancellationAdapter = new CancellationAdapter(this, userArrayList, db);
        RefundAdapter = new RefundAdapter(this, userArrayList, db);
        RescheduleAdapter = new RescheduleAdapter(this, userArrayList, db);

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
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully updated!"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));
                    } else {
                        Map<String, Object> user = new HashMap<>();
                        user.put(User.FIELD_USER_ID, userId);
                        user.put(User.FIELD_EMAIL, Email); // Ensure Email is not null
                        usersDocRef.document(userId).set(user)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully written!"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));

                    }
                } else {
                    // Handle the exception
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("Firestore", "Error getting document", exception);
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


//        Refund_ScrollView = findViewById(R.id.Refund_ScrollView); //Refund ScrollView
//        Refund_Tab = findViewById(R.id.Refund_Tab);
//        Refund_Tab.setOnClickListener(v -> Refund_Tab());
//        Reschedule_ScrollView = findViewById(R.id.Reschedule_ScrollView); //REschedule ScrollView
//        reschedule_Tab = findViewById(R.id.reschedule_Tab);
//        reschedule_Tab.setOnClickListener(v -> reschedule_Tab());

    }
//    private void Refund_Tab() {
//        Upcoming_Tab.setChecked(true);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Refund_ScrollView.setVisibility(View.VISIBLE);
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//
//
//        // Show only Refund RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    private void reschedule_Tab() {
//        Upcoming_Tab.setChecked(true);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Reschedule_ScrollView.setVisibility(View.VISIBLE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//
//        // Show only Reschedule RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.VISIBLE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//    }
//

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
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);


        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));


        // Show only Upcoming RecyclerView
        Upcoming_RecyclerView.setVisibility(View.VISIBLE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);

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
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);


        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));


        // Show only History RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.VISIBLE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
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
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);


        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

        // Show only Pending RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.VISIBLE);
        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
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
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);


        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
        por.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));

        // Show only Cancellation RecyclerView
        Upcoming_RecyclerView.setVisibility(View.GONE);
        History_RecyclerView.setVisibility(View.GONE);
        Pending_RecyclerView.setVisibility(View.GONE);
        Cancellation_RecyclerView.setVisibility(View.VISIBLE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
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

//        Reschedule_RecyclerView.setHasFixedSize(true);
//        Reschedule_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Reschedule_RecyclerView.setAdapter(RescheduleAdapter);
//
//        Refund_RecyclerView.setHasFixedSize(true);
//        Refund_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Refund_RecyclerView.setAdapter(RefundAdapter);

    }

    private void EventChangeListener() {
        db.collection("users").orderBy("reservedDate", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    // Initialize HashMaps for users
                    final HashMap<String, User> userHashMap = new HashMap<>();
                    final HashMap<String, User> upcomingUserHashMap = new HashMap<>();
                    final HashMap<String, User> historyUserHashMap = new HashMap<>();
                    final HashMap<String, User> cancellationHashMap = new HashMap<>();  // Corrected variable name

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            try {
                                fetchAndDisplayUserData();
                            } catch (Exception e) {
                                Log.e("ProfileActivity", "Error in fetchAndDisplayUserData: " + e.getMessage());
                            }

                            if (!progressDialog.isShowing()) progressDialog.show();
                            Log.e("Error", error.getMessage());
                            return;
                        }

                        // Handle data changes
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                case MODIFIED:
                                    User user = dc.getDocument().toObject(User.class);

                                    // Set the user ID
                                    user.setUserId(dc.getDocument().getId());

                                    userHashMap.put(user.getEmail(), user);

                                    if (user.isUpcoming()) {
                                        upcomingUserHashMap.put(user.getEmail(), user);
                                    } else {
                                        historyUserHashMap.put(user.getEmail(), user);
                                    }

                                    // Corrected 'else if' syntax
                                    if (user.isCancelled()) {
                                        cancellationHashMap.put(user.getEmail(), user);
                                    }
                                    break;
                                case REMOVED:
                                    User userRemoved = dc.getDocument().toObject(User.class);
                                    userHashMap.remove(userRemoved.getEmail());
                                    upcomingUserHashMap.remove(userRemoved.getUserId());
                                    historyUserHashMap.remove(userRemoved.getUserId());
                                    cancellationHashMap.remove(userRemoved.getUserId());
                                    break;
                            }
                        }

                        userArrayList.clear();
                        userArrayList.addAll(userHashMap.values());

                        myAdapter.notifyDataSetChanged();
                        UpcomingAdapter.notifyDataSetChanged();
                        HistoryAdapter.notifyDataSetChanged();
                        CancellationAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing()) progressDialog.dismiss();
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
                     //   String status = documentSnapshot.getString("status");
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


//
//
//package com.example.log_in;
//
//        import android.app.ProgressDialog;
//        import android.content.Intent;
//        import android.net.Uri;
//        import android.os.Bundle;
//        import android.provider.MediaStore;
//        import android.util.Log;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.ImageView;
//        import android.widget.RadioButton;
//        import android.widget.ScrollView;
//        import android.widget.Toast;
//
//        import androidx.annotation.Nullable;
//        import androidx.appcompat.app.AppCompatActivity;
//        import androidx.core.content.ContextCompat;
//        import androidx.recyclerview.widget.LinearLayoutManager;
//        import androidx.recyclerview.widget.RecyclerView;
//
//        import com.bumptech.glide.Glide;
//        import com.google.firebase.auth.FirebaseAuth;
//        import com.google.firebase.auth.FirebaseUser;
//        import com.google.firebase.firestore.CollectionReference;
//        import com.google.firebase.firestore.DocumentReference;
//        import com.google.firebase.firestore.DocumentSnapshot;
//        import com.google.firebase.firestore.FirebaseFirestore;
//        import com.google.firebase.firestore.ListenerRegistration;
//        import com.google.firebase.firestore.Query;
//        import com.google.firebase.firestore.QueryDocumentSnapshot;
//        import com.google.firebase.storage.FirebaseStorage;
//        import com.google.firebase.storage.StorageReference;
//        import com.google.firebase.storage.UploadTask;
//
//        import java.util.ArrayList;
//        import java.util.HashMap;
//        import java.util.List;
//        import java.util.Map;
//
//public class TourismHeadAdmin extends AppCompatActivity {
//    private static final int GALLERY_REQUEST_CODE = 123;
//
//
//    private RadioButton Upcoming_Tab, History_tab, Pending_Tab, Cancel_Tab, reschedule_Tab, Refund_Tab;
//    private ScrollView Upcoming_ScrollView, History_ScrollView, Pending_ScrollView, Cancellation_ScrollView, Refund_ScrollView, Reschedule_ScrollView;
//    private View wan, to, tre, por, payb, six;
//    private RecyclerView Pending_RecyclerView, Upcoming_RecyclerView, History_RecyclerView, Cancellation_RecyclerView, Reschedule_RecyclerView, Refund_RecyclerView;
//    private ArrayList<User> userArrayList;
//
//    private com.example.log_in.UpcomingAdapter UpcomingAdapter;
//    private com.example.log_in.HistoryAdapter HistoryAdapter;
//    private com.example.log_in.RefundAdapter RefundAdapter;
//    private RescheduleAdapter RescheduleAdapter;
//    private CancellationAdapter CancellationAdapter;
//    FirebaseUser user;
//    FirebaseAuth auth;
//    private FirebaseFirestore db;
//    private ProgressDialog progressDialog;
//
//    private Button uploadImageTH_btn;
//    private Object Email;
//    ImageView eventSched, addTM;
//    private ListenerRegistration userDataListener;
//    private PendingAdapter PendingAdapter;
//    private MyAdapter myAdapter;
//    private Object adapterType;
//    private List<User> data;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tourism_head_admin);
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        addTM = findViewById(R.id.addTM);
//        eventSched = findViewById(R.id.eventSched);
//        uploadImageTH_btn = findViewById(R.id.uploadImageTH_btn);
//        uploadImageTH_btn.setOnClickListener(v -> {
//            openGallery();
//        });
//
//        // Initialize Firebase Authentication and Firestore
//        wan = findViewById(R.id.wan);
//        to = findViewById(R.id.to);
//        tre = findViewById(R.id.tre);
//        por = findViewById(R.id.por);
//        payb = findViewById(R.id.payb);
//        six = findViewById(R.id.six);
//
//
//        // Initialize RecyclerViews and Adapters
//        Pending_RecyclerView = findViewById(R.id.Pending_RecyclerView);
//        Upcoming_RecyclerView = findViewById(R.id.Upcoming_RecyclerView);
//        History_RecyclerView = findViewById(R.id.History_RecyclerView);
//        Cancellation_RecyclerView = findViewById(R.id.Cancellation_RecyclerView);
//        Reschedule_RecyclerView = findViewById(R.id.Reschedule_RecyclerView);
//        Refund_RecyclerView = findViewById(R.id.Refund_RecyclerView);
//
//
//        userArrayList = new ArrayList<>();
//
//        myAdapter = new MyAdapter(this, userArrayList, db);
//        //    PendingAdapter = new PendingAdapter(this, userArrayList, db);
//        UpcomingAdapter = new UpcomingAdapter(this, userArrayList, db);
//        HistoryAdapter = new HistoryAdapter(this, userArrayList, db);
//        CancellationAdapter = new CancellationAdapter(this, userArrayList, db);
//        RefundAdapter = new RefundAdapter(this, userArrayList, db);
//        RescheduleAdapter = new RescheduleAdapter(this, userArrayList, db);
//
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            CollectionReference usersDocRef = db.collection("users");
//            DocumentReference userDocRef = usersDocRef.document(userId);
//            userDocRef.get().addOnCompleteListener(task -> {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null && document.exists()) {
//                        // Document exists, update fields
//                        userDocRef.update(User.FIELD_STATUS, "")
//                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully updated!"))
//                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));
//                    } else {
//                        Map<String, Object> user = new HashMap<>();
//                        user.put(User.FIELD_USER_ID, userId);
//                        user.put(User.FIELD_EMAIL, Email); // Ensure Email is not null
//                        usersDocRef.document(userId).set(user)
//                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully written!"))
//                                .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));
//
//                    }
//                } else {
//                    // Handle the exception
//                    Exception exception = task.getException();
//                    if (exception != null) {
//                        Log.e("Firestore", "Error getting document", exception);
//                    }
//                }
//            });
//        }
//
//        fetchImageFromStorage();
//
//        // Initialize ProgressDialog
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//
//
//        setUpRecyclerView(); // Set up RecyclerView and Adapter
//        setUpTabsAndViews(); // Set up tabs and views
//    }
//
//    private void fetchImageFromStorage() {
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Calendar/calendar_image.jpg");
//
//        // Fetch the image and load it into the eventSched ImageView
//        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//            // Load the retrieved image using Glide or set it directly to the ImageView
//            Glide.with(this)
//                    .load(uri)
//                    .into(eventSched);
//            addTM.setVisibility(View.GONE);
//        }).addOnFailureListener(exception -> {
//            // Handle any errors that may occur while fetching the image
//            showToast("Failed to fetch image: " + exception.getMessage());
//        });
//    }
//
//    private void showToast(String s) {
//    }
//
//    private void openGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            // Handle the selected image from the gallery
//            Uri selectedImageUri = data.getData();
//
//            // Set the selected image to the eventSched ImageView
//            eventSched.setImageURI(selectedImageUri);
//
//            // Hide the addTM ImageView
//            addTM.setVisibility(View.GONE);
//
//            // Upload the selected image to Firebase Storage under the "Calendar" document
//            uploadImageToStorage(selectedImageUri);
//        }
//    }
//
//    private void uploadImageToStorage(Uri imageUri) {
//        if (imageUri != null) {
//            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (currentUser != null) {
//                String imageName = "calendar_image.jpg"; // Set a name for the image in storage
//
//                // Get a reference to the Firebase Storage location
//                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                        .child("Calendar")
//                        .child(imageName);
//
//                // Upload the image to Firebase Storage
//                UploadTask uploadTask = storageReference.putFile(imageUri);
//                uploadTask.addOnSuccessListener(taskSnapshot -> {
//                    // Image uploaded successfully
//                    Toast.makeText(this, "Image uploaded to Firebase Storage", Toast.LENGTH_SHORT).show();
//                    // You can also retrieve the download URL of the uploaded image here if needed
//                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                        String downloadUrl = uri.toString();
//                        // You can use the download URL for further operations if required
//                    });
//                }).addOnFailureListener(e -> {
//                    // Handle any errors during the upload process
//                    Log.e("FirebaseStorage", "Error uploading image: " + e.getMessage());
//                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
//                });
//            }
//        }
//    }
//
//
//    private void setUpTabsAndViews() {
//
//        // Set up tabs and views
//        Upcoming_ScrollView = findViewById(R.id.Upcoming_ScrollView); //Upcoming_ScrollView
//        Upcoming_Tab = findViewById(R.id.Upcoming_Tab);
//        Upcoming_Tab.setOnClickListener(v -> Upcoming_Tab());
//        History_ScrollView = findViewById(R.id.History_ScrollView); //History Scrollview
//        History_tab = findViewById(R.id.History_tab);
//        History_tab.setOnClickListener(v -> History_tab());
//        Pending_ScrollView = findViewById(R.id.Pending_ScrollView); //Pending Scrollview
//        Pending_Tab = findViewById(R.id.Pending_Tab);
//        Pending_Tab.setOnClickListener(v -> Pending_Tab());
//        Cancellation_ScrollView = findViewById(R.id.Cancellation_ScrollView); //Cancellation Scrollview
//        Cancel_Tab = findViewById(R.id.Cancel_Tab);
//        Cancel_Tab.setOnClickListener(v -> Cancel_Tab());
//        Refund_ScrollView = findViewById(R.id.Refund_ScrollView); //Refund ScrollView
//        Refund_Tab = findViewById(R.id.Refund_Tab);
//        Refund_Tab.setOnClickListener(v -> Refund_Tab());
//        Reschedule_ScrollView = findViewById(R.id.Reschedule_ScrollView); //REschedule ScrollView
//        reschedule_Tab = findViewById(R.id.reschedule_Tab);
//        reschedule_Tab.setOnClickListener(v -> reschedule_Tab());
//
//    }
//
//    private void Refund_Tab() {
//        Upcoming_Tab.setChecked(true);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Refund_ScrollView.setVisibility(View.VISIBLE);
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//
//
//        // Show only Refund RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    private void reschedule_Tab() {
//        Upcoming_Tab.setChecked(true);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Reschedule_ScrollView.setVisibility(View.VISIBLE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//
//        // Show only Reschedule RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.VISIBLE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//    }
//
//
//    private void Upcoming_Tab() {
//        Upcoming_Tab.setChecked(true);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Upcoming_ScrollView.setVisibility(View.VISIBLE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//
//        // Show only Upcoming RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.VISIBLE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//
//    }
//
//
//    private void History_tab() {
//        Upcoming_Tab.setChecked(false);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(true);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        History_ScrollView.setVisibility(View.VISIBLE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//
//        // Show only History RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.VISIBLE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//    }
//
//
//    private void Pending_Tab() {
//        Upcoming_Tab.setChecked(false);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(true);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Pending_ScrollView.setVisibility(View.VISIBLE);
//        Cancel_Tab.setChecked(false);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Cancellation_ScrollView.setVisibility(View.GONE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//        // Show only Pending RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.VISIBLE);
//        Cancellation_RecyclerView.setVisibility(View.GONE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//    }
//
//    private void Cancel_Tab() {
//        Upcoming_Tab.setChecked(false);
//        Upcoming_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Upcoming_ScrollView.setVisibility(View.GONE);
//        History_tab.setChecked(false);
//        History_tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        History_ScrollView.setVisibility(View.GONE);
//        Pending_Tab.setChecked(false);
//        Pending_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Pending_ScrollView.setVisibility(View.GONE);
//        Cancel_Tab.setChecked(true);
//        Cancel_Tab.setTextColor(ContextCompat.getColor(this, R.color.green));
//        Cancellation_ScrollView.setVisibility(View.VISIBLE);
//        reschedule_Tab.setChecked(false);
//        reschedule_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Reschedule_ScrollView.setVisibility(View.GONE);
//        Refund_Tab.setChecked(false);
//        Refund_Tab.setTextColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        Refund_ScrollView.setVisibility(View.GONE);
//
//
//        wan.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        to.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        tre.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        por.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
//        payb.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//        six.setBackgroundColor(ContextCompat.getColor(this, R.color.fadedgreen));
//
//        // Show only Cancellation RecyclerView
//        Upcoming_RecyclerView.setVisibility(View.GONE);
//        History_RecyclerView.setVisibility(View.GONE);
//        Pending_RecyclerView.setVisibility(View.GONE);
//        Cancellation_RecyclerView.setVisibility(View.VISIBLE);
//        Reschedule_RecyclerView.setVisibility(View.GONE);
//        Refund_RecyclerView.setVisibility(View.GONE);
//    }
//
//
//    // RecyclerView setup
//    private void setUpRecyclerView() {
//        Pending_RecyclerView.setHasFixedSize(true);
//        Pending_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Pending_RecyclerView.setAdapter(PendingAdapter);
//
//        Upcoming_RecyclerView.setHasFixedSize(true);
//        Upcoming_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Upcoming_RecyclerView.setAdapter(UpcomingAdapter);
//
//        History_RecyclerView.setHasFixedSize(true);
//        History_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        History_RecyclerView.setAdapter(HistoryAdapter);
//
//        Cancellation_RecyclerView.setHasFixedSize(true);
//        Cancellation_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Cancellation_RecyclerView.setAdapter(CancellationAdapter);
//
//        Reschedule_RecyclerView.setHasFixedSize(true);
//        Reschedule_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Reschedule_RecyclerView.setAdapter(RescheduleAdapter);
//
//        Refund_RecyclerView.setHasFixedSize(true);
//        Refund_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Refund_RecyclerView.setAdapter(RefundAdapter);
//        filterData((AdapterType) adapterType);
//    }
//
//
//
//
//    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//
//    private void filterData(AdapterType adapterType) {
//        CollectionReference usersCollection = firebaseFirestore.collection("users"); // Replace "users" with your actual collection name
//
//        Query query;
//
//        switch (adapterType) {
//            case UPCOMING:
//                query = usersCollection.whereEqualTo("status", "upcoming");
//                break;
//            case PENDING:
//                query = usersCollection.whereEqualTo("status", "pending");
//                break;
//            case RESCHEDULE:
//                query = usersCollection.whereEqualTo("Reschedulestatus", "reschedule");
//                break;
//            case REFUND:
//                query = usersCollection.whereEqualTo("Refundstatus", "refund");
//                break;
//            case CANCELLATION:
//                query = usersCollection.whereEqualTo("Cancellationstatus", "cancellation");
//                break;
//            default:
//                // Handle the default case or throw an exception
//                return;
//        }
//
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<User> filteredList = new ArrayList<>();
//
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    // Convert each document to your User object
//                    User user = document.toObject(User.class);
//                    filteredList.add(user);
//                }
//
//                // Update the UI with the filtered list
//
//                // updateRecyclerView(ArrayList<userArrayList>data);
//            } else {
//                // Handle the error
//                Exception exception = task.getException();
//                if (exception != null) {
//                    exception.printStackTrace();
//                }
//            }
//        });
//    }
//
//
//    // Enum to represent different adapter types
//    private enum AdapterType {
//        UPCOMING,
//        PENDING,
//        RESCHEDULE,
//        REFUND,
//        CANCELLATION
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        // Remove the snapshot listener when the activity is destroyed
//        if (userDataListener != null) {
//            userDataListener.remove();
//        }
//
//        super.onDestroy();
//
//
//        // Set a click listener for the edit button
//        uploadImageTH_btn.setOnClickListener(v -> {
//            // Handle edit button click (implement your edit/update/delete logic here)
//            Toast.makeText(TourismHeadAdmin.this, "Edit button clicked", Toast.LENGTH_SHORT).show();
//        });
//
//    }
//
//
//}
