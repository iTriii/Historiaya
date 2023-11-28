package com.example.log_in;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//FOR UPDATE ONLY
public class Profile_Edit extends AppCompatActivity {
    ImageButton back;
    ShapeableImageView profile_icon;
    EditText firstname, lastname, E_mail, contact;
    TextView Save;
    int SELECT_PICTURE = 1;
    private Uri selectedImageUri;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ListenerRegistration userDataListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        back = findViewById(R.id.backbtneditprofile);
        back.setOnClickListener(v -> Profile());

        Save = findViewById(R.id.Save);
        Save.setOnClickListener(v -> saveChanges());

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);

        profile_icon = findViewById(R.id.profile_icon);
        profile_icon.setOnClickListener(v -> Profile_icon());

        // Check if the user is authenticated
        if (user != null) {
            // Fetch and display user data from Firestore
            fetchAndDisplayUserData();
        } else {
            // If the user is not authenticated, redirect to the login activity
            startActivity(new Intent(Profile_Edit.this, LogIn.class));
            finish();
        }
    }

    private void fetchAndDisplayUserData() {
        userDataListener = db.collection("users")
                .document(user.getUid())
                .addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@NonNull DocumentSnapshot documentSnapshot, @NonNull FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(Profile_Edit.this, "Error fetching user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (documentSnapshot.exists()) {
                            // Retrieve user data from Firestore
                            String firstName = documentSnapshot.getString("FirstName");
                            String lastName = documentSnapshot.getString("LastName");
                            String email = documentSnapshot.getString("Email");
                            String userContact = documentSnapshot.getString("ContactNo");

                            // Display user data in the EditText fields
                            firstname.setText(firstName);
                            lastname.setText(lastName);
                            E_mail.setText(email);
                            contact.setText(userContact);

                            // Load and display the profile image using Glide
                            String imageUrl = documentSnapshot.getString("ImageUrl");
                            Log.d("Profile_Edit", "Image URL: " + imageUrl);
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .transform(new CenterCrop(), new CircleCrop()); // Apply circular crop
                                Glide.with(Profile_Edit.this)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into(profile_icon);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the Firestore snapshot listener when the activity is destroyed
        if (userDataListener != null) {
            userDataListener.remove();
        }
    }

    public void Profile() {
        // Return to the previous activity
        finish();
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    void Profile_icon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Load and display the selected image into profile_icon
                    RequestOptions requestOptions = new RequestOptions()
                            .transforms(new CenterCrop(), new CircleCrop()); // Apply circular crop
                    Glide.with(this)
                            .load(selectedImageUri)
                            .apply(requestOptions)
                            .into(profile_icon);

                    // Now, upload the selected image to Firebase Storage
                    uploadImageToFirebaseStorage(selectedImageUri);
                }
            }
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");

            // Generate a unique file name for the image
            String imageName = "image_" + System.currentTimeMillis() + ".jpg";

            StorageReference imageRef = storageRef.child(imageName);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, now get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the download URL in Firestore or perform any other necessary operations
                            String imageUrl = uri.toString();
                            // Update the user's Firestore document with the new image URL
                            updateImageUrlInFirestore(imageUrl);
                        }).addOnFailureListener(e -> {
                            // Handle the error when retrieving the download URL
                            Log.e("Profile_Edit", "Error getting download URL: " + e.getMessage());
                            Toast.makeText(this, "Error getting download URL", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error when uploading the image
                        Log.e("Profile_Edit", "Image upload failed: " + e.getMessage());
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Handle the case where imageUri is null (no image selected)
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateImageUrlInFirestore(String imageUrl) {
        // Update the user's Firestore document with the new image URL
        db.collection("users")
                .document(user.getUid())
                .update("ImageUrl", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(Profile_Edit.this, "Failed to update profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveChanges() {
        // Retrieve edited values from EditText fields
        String newFirstName = firstname.getText().toString().trim();
        String newLastName = lastname.getText().toString().trim();
        String newContact = contact.getText().toString().trim();

        // Check if the new values are not empty
        if (newFirstName.isEmpty() || newLastName.isEmpty() || newContact.isEmpty()) {
            Toast.makeText(Profile_Edit.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Don't proceed if any of the fields are empty
        }

        // Check if a new image was selected and uploaded
        if (selectedImageUri != null) {
            // Image was changed, update the Firestore document with the edited values and new image URL
            db.collection("users")
                    .document(user.getUid())
                    .update("FirstName", newFirstName, "LastName", newLastName,
                            "ContactNo", newContact, "ImageUrl", selectedImageUri.toString())
                    .addOnSuccessListener(aVoid -> {
                        // Update successful

                        // Fetch and display the updated user data
                        fetchAndDisplayUserData();

                        // Return to the previous activity (Profile)
                        Intent resultIntent = new Intent(Profile_Edit.this, Profile.class);
                        resultIntent.putExtra("editedFirstName", newFirstName);
                        resultIntent.putExtra("editedLastName", newLastName);
                        startActivity(resultIntent);
                        setResult(RESULT_OK, resultIntent);

                        finish(); // Finish the Profile_Edit activity
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        Toast.makeText(Profile_Edit.this, "Failed to save changes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // No image change, update the Firestore document with only the edited values
            db.collection("users")
                    .document(user.getUid())
                    .update("FirstName", newFirstName, "LastName", newLastName, "ContactNo", newContact)
                    .addOnSuccessListener(aVoid -> {
                        // Update successful
                        Toast.makeText(Profile_Edit.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();

                        // Fetch and display the updated user data
                        fetchAndDisplayUserData();

                        // Return to the previous activity (Profile)
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("editedFirstName", newFirstName);
                        resultIntent.putExtra("editedLastName", newLastName);
                        setResult(RESULT_OK, resultIntent);
                        finish(); // Finish the Profile_Edit activity
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                        Toast.makeText(Profile_Edit.this, "Failed to save changes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

}

