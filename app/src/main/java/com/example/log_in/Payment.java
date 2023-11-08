package com.example.log_in;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Payment extends AppCompatActivity {
    Button btndone;
    ImageButton uploadbtn, selectbtn; // Change to ImageButton
    StorageReference storageRef;
    LinearProgressIndicator progress;
    ImageView imageView;
    Toolbar supportActionBar;
    FirebaseFirestore db;
    private Uri selectedImageUri;
    private String imageUrl = "";
    int SELECT_PICTURE = 1;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        FirebaseApp.initializeApp(this);
        storageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        supportActionBar = findViewById(R.id.toolbar);
        progress = findViewById(R.id.progress);
        imageView = findViewById(R.id.imgviewpayment);
        btndone = findViewById(R.id.okbtn);
        selectbtn = findViewById(R.id.select);


        // Add a click listener for the select button
        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start an activity to select an image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        // Add a click listener for the "Done" button
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an image has been selected
                if (selectedImageUri == null) {
                    Toast.makeText(Payment.this, "Please select an image", Toast.LENGTH_LONG).show();
                } else {
                    // Upload the selected image to Firebase Storage
                    uploadImageToFirebaseStorage(selectedImageUri);
                }
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
                    // Apply Glide to load and crop the image
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
                        saveUserDataToFirestore(mAuth.getCurrentUser(), imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        progress.setVisibility(View.VISIBLE);
    }

    private void saveUserDataToFirestore(FirebaseUser user, String imageUrl) {
        if (user != null) {
            DocumentReference userDocRef = db.collection("users").document(user.getUid()).getFirestore().document("proof");
            Map<String, Object> userData = new HashMap<>();
            userData.put("ImageUrl", imageUrl);

            // Add a Log statement to check the imageUrl
            Log.d(TAG, "Image URL: " + imageUrl);

            userDocRef.set(userData)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "Failed to save user data to Firestore: " + task.getException().getMessage());
                        }
                    });
        }
    }
}
