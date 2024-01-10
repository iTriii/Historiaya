package com.example.log_in;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
//FOR UPDATE ONLY
public class Payment extends AppCompatActivity {
    Button okbtn;
    ImageButton back, selectbtn; // Change to ImageButton
    StorageReference storageRef;
    LinearProgressIndicator progress;
    ImageView imageView;
    Toolbar supportActionBar;
    FirebaseFirestore db;
    private Uri selectedImageUri;
    int SELECT_PICTURE = 1;
    FirebaseAuth mAuth;
    private static final String TAG = "Payment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        // initialize the firebase
        FirebaseApp.initializeApp(this);
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // initialize the ui
        supportActionBar = findViewById(R.id.toolbar);
        progress = findViewById(R.id.progress);
        imageView = findViewById(R.id.imgviewpayment);
        okbtn = findViewById(R.id.okbtn);
        selectbtn = findViewById(R.id.select);
        back = findViewById(R.id.backbtnpayment);


// Back button
        back.setOnClickListener(v -> {
            Intent intent = new Intent(Payment.this, BookNow.class);
            startActivity(intent);  // Add this line to start the activity
        });


        selectbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PICTURE);

        });

//        okbtn.setOnClickListener(v -> {
//            if (selectedImageUri == null) {
//                Toast.makeText(Payment.this, "Please upload your proof of payment. Thank You!", Toast.LENGTH_LONG).show();
//            } else {
//                uploadImageToFirebaseStorage(selectedImageUri);
//                // Toast.makeText(Payment.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
////                progress.setVisibility(View.VISIBLE);
//                startActivity(new Intent(getApplicationContext(), Main2.class));
//            }
//        });
//    }
        okbtn.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(Payment.this, "Please upload your proof of payment. Thank You!", Toast.LENGTH_LONG).show();
            } else {
                uploadImageToFirebaseStorage(selectedImageUri);

                // Build and show an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Payment.this);
                builder.setTitle("Booking Successful");
                builder.setMessage("Your payment has been received. Please wait for confirmation of the Admin. Thank you!");

                // Add a positive button with a click listener
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        // Navigate to the next activity or perform any other actions
                        startActivity(new Intent(getApplicationContext(), Main2.class));
                    }
                });

                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    RequestOptions requestOptions = new RequestOptions();
                    Glide.with(Payment.this)
                            .load(selectedImageUri)
                            .apply(requestOptions)
                            .into(imageView);
                }
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("proofOfPayment");
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveUserDataToFirestore(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed. Please check your Internet connection " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserDataToFirestore(String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        String documentId = String.valueOf(System.currentTimeMillis()); // Use timestamp as the document ID
        DocumentReference userDocRef = db.collection("users").document(userId).collection("proofOfPayment").document(documentId);

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> user = new HashMap<>();
                if (task.getResult() != null && task.getResult().getData() != null) {
                    user.putAll(task.getResult().getData());
                }

                // If the user has already made a booking, update the existing picture.
                user.put("ImageUrl", imageUrl);

                userDocRef.set(user).addOnSuccessListener(documentReference -> {
                    startActivity(new Intent(getApplicationContext(), Main2.class));
                    finish();
                }).addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        // Add a Log statement to check the imageUrl
        Log.d(TAG, "Image URL: " + imageUrl);
    }

    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, PaymentDetails.class);
        startActivity(intent);
        finish();
    }
}