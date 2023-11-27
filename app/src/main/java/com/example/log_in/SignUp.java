package com.example.log_in;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.log_in.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
//FOR UPDATE ONLY

public class SignUp extends AppCompatActivity {


    ImageView icon;
    EditText firstname, lastname, E_mail, contact, pass, reenter;
    Button signUpButton;
    FirebaseAuth mAuth;
    ProgressBar progressbar;
    private FirebaseFirestore db;
    int SELECT_PICTURE = 1;
    private String imageUrl = "";
    private Uri selectedImageUri;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> icon());

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);
        pass = findViewById(R.id.pass);
        reenter = findViewById(R.id.reenter);
        progressbar = findViewById(R.id.progressbar);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> signUp());
    }

    // Method to handle image selection
    // Inside the icon() method
    void icon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    // Handle image selection result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData(); // Assign the selected image URI to the class-level variable
                if (selectedImageUri != null) {
                    ImageView imageView = findViewById(R.id.icon);

                    // Apply Glide to load and crop the image
                    RequestOptions requestOptions = new RequestOptions()
                            .transforms(new CenterCrop(), new CircleCrop()); // Apply circular crop
                    Glide.with(this)
                            .load(selectedImageUri)
                            .apply(requestOptions)
                            .into(imageView);

                }
            }
        }
    }
    private void uploadImageToFirebaseStorage(Uri imageUri, FirebaseUser user, String firstName, String lastName, String userEmail, String userContact, int points) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images"); // Set your desired storage path


        // Generate a unique file name for the image
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";

        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, now get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL in Firestore or perform any other necessary operations
                        String imageUrl = uri.toString();
                        // You can now save the imageUrl in Firestore along with other user data
                        saveUserDataToFirestore(user.getUid(), firstName, lastName, userEmail, userContact, imageUrl, points);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    //sign up
    private void signUp() {
        String fName = firstname.getText().toString().trim();
        String lName = lastname.getText().toString().trim();
        String email = E_mail.getText().toString().trim();
        String contactNo = contact.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String reenterPass = reenter.getText().toString().trim();
        String imageUrl = "";

        //firstname lastname email
        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) ||
               // contactnum password // reenter
                TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(reenterPass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(reenterPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }
        // Check if an image has been selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressbar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            // Send email verification and save user data
                            sendEmailVerificationAndSaveData(user, fName, lName, email, contactNo, 20);
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendEmailVerificationAndSaveData(FirebaseUser user, String firstName, String lastName, String userEmail, String userContact, int points) {
        user.sendEmailVerification().addOnCompleteListener(this, emailVerificationTask -> {
            if (emailVerificationTask.isSuccessful()) {
                // Save user data to Firestore and upload image
                uploadImageToFirebaseStorage(selectedImageUri, user, firstName, lastName, userEmail, userContact, points);
                // Show a Toast message
                Toast.makeText(SignUp.this, "Please check your email for verification link", Toast.LENGTH_LONG).show();

                // Navigate to the login screen
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish(); // Close the sign-up activity
            } else {
                Toast.makeText(SignUp.this, "Authentication failed: " + emailVerificationTask.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    // Save user data to Firestore, including the image URL
    private void saveUserDataToFirestore(String userId, String firstName, String lastName, String userEmail, String userContact, String imageUrl, int points) {
        DocumentReference userDocRef = db.collection("users").document(userId);

        Map<String, Object> user = new HashMap<>();
        user.put("FirstName", firstName);
        user.put("LastName", lastName);
        user.put("Email", userEmail);
        user.put("ContactNo", userContact);
        user.put("ImageUrl", imageUrl);
        user.put("HistoriaPoints", points);

        // Add a Log statement to check the imageUrl
        Log.d(TAG, "Image URL: " + imageUrl);

        if (!TextUtils.isEmpty(imageUrl)) {
            // Save the image URL if it is not empty
            user.put("ImageUrl", imageUrl);
        }

        userDocRef.set(user)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Handle the failure case (if needed)
                        Log.e(TAG, "Failed to save user data to Firestore: " + task.getException().getMessage());
                    }
                });
    }
}

