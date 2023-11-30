package com.example.log_in;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ImageView icon;
    EditText firstname, lastname, E_mail, contact, pass, reenter;
    Button signUpButton;
    FirebaseAuth mAuth;
    TextView SiUp;
    ProgressBar progressbar;
    CheckBox checkBox;
    private FirebaseFirestore db;
    int SELECT_PICTURE = 1;
    private Uri selectedImageUri;
    private SharedPreferences sharedPreferences;
    private static final String CHECKBOX_PREF = "checkbox_preference";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        };
        onBackPressedDispatcher.addCallback(this, callback);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> icon());

        firstname = findViewById(R.id.firstname);
        firstname.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetter(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        lastname = findViewById(R.id.lastname);
        lastname.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetter(source.charAt(i))) {
                            return "";
                        }
                    }
                    return null;
                }
        });

        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);
        contact.setInputType(InputType.TYPE_CLASS_NUMBER); // Set input type to accept only numbers

// Listen for text changes to maintain the "+639" prefix
        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().startsWith("+639")) {
                    // Ensure the prefix remains at the beginning
                    contact.setText("+639");
                    contact.setSelection(contact.getText().length()); // Set cursor position to the end
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

// Set maximum length and restrict modification of the prefix
        contact.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(13) // Restrict input to length 13
        });


        pass = findViewById(R.id.pass);
        reenter = findViewById(R.id.reenter);
        progressbar = findViewById(R.id.progressbar);

        checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(sharedPreferences.getBoolean(CHECKBOX_PREF, false));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Enable or disable the sign-up button based on checkbox status
            signUpButton.setEnabled(isChecked);

            // Show or hide the progress bar based on checkbox status
            if (isChecked) {
                progressbar.setVisibility(View.GONE); // Show progress bar if checked
            } else {
                progressbar.setVisibility(View.GONE); // Hide progress bar if unchecked
            }
        });



        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> signUp());

        SiUp = findViewById(R.id.SiUp);
        SiUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, PrivacyAndTerms_SignUp.class);
            startActivity(intent);
        });

    }

    void icon() {
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
                    ImageView imageView = findViewById(R.id.icon);
                    RequestOptions requestOptions = new RequestOptions()
                            .transforms(new CenterCrop(), new CircleCrop());
                    Glide.with(this)
                            .load(selectedImageUri)
                            .apply(requestOptions)
                            .into(imageView);
                }
            }
        }
    }


    private void signUp() {
        if (!checkBox.isChecked()) {
            Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_LONG).show();
            return;
        }

        String fName = firstname.getText().toString().trim();
        String lName = lastname.getText().toString().trim();
        String email = E_mail.getText().toString().trim();
        String contactNo = contact.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String reenterPass = reenter.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(reenterPass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(reenterPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if an image is selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show();
            return;
        }

        // Validate contact number format
        if (!contactNo.startsWith("+639") || contactNo.length() != 13) {
            Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_LONG).show();
            return;
        }

        progressbar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressbar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (user != null) {
                            sendEmailVerificationAndSaveData(user, fName, lName, email, contactNo, 20);
                        }
                        user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                            if (emailVerificationTask.isSuccessful()) {
                                // Verification email sent successfully
                                Toast.makeText(SignUp.this, "Verification email sent. Please check your email.", Toast.LENGTH_LONG).show();
                                // Prompt the user to log in after email verification
                                startActivity(new Intent(SignUp.this, LogIn.class));
                                finish(); // Finish the current activity
                            } else {
                                // Email verification sending failed
                                Toast.makeText(SignUp.this, "Failed to send verification email: " + emailVerificationTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                    } else {
                        String errorMessage = task.getException().getMessage();
                        if (errorMessage != null && errorMessage.contains("already in use")) {
                            Toast.makeText(SignUp.this, "Email is already in use", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUp.this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendEmailVerificationAndSaveData(FirebaseUser user, String firstName, String lastName, String userEmail, String userContact, int points) {
        user.sendEmailVerification().addOnCompleteListener(this, emailVerificationTask -> {
            if (emailVerificationTask.isSuccessful()) {
                uploadImageToFirebaseStorage(selectedImageUri, user, firstName, lastName, userEmail, userContact, points);
                Toast.makeText(SignUp.this, "Please check your email for verification link", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            } else {
                Toast.makeText(SignUp.this, "Authentication failed: " + emailVerificationTask.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void uploadImageToFirebaseStorage(Uri imageUri, FirebaseUser user, String firstName, String lastName, String userEmail, String userContact, int points) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");

        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveUserDataToFirestore(user.getUid(), firstName, lastName, userEmail, userContact, imageUrl, points);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        Log.d(TAG, "Image URL: " + imageUrl);

        if (!TextUtils.isEmpty(imageUrl)) {
            user.put("ImageUrl", imageUrl);
        }

        userDocRef.set(user)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e(TAG, "Failed to save user data to Firestore: " + task.getException().getMessage());
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Save user input to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstName", firstname.getText().toString().trim());
        editor.putString("lastName", lastname.getText().toString().trim());
        editor.putString("email", E_mail.getText().toString().trim());
        editor.putString("contactNo", contact.getText().toString().trim());
        editor.putString("password", pass.getText().toString().trim());
        editor.putString("reenterPassword", reenter.getText().toString().trim());
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Restore user input from SharedPreferences
        firstname.setText(sharedPreferences.getString("firstName", ""));
        lastname.setText(sharedPreferences.getString("lastName", ""));
        E_mail.setText(sharedPreferences.getString("email", ""));
        contact.setText(sharedPreferences.getString("contactNo", ""));
        pass.setText(sharedPreferences.getString("password", ""));
        reenter.setText(sharedPreferences.getString("reenterPassword", ""));
    }
    private void goBack() {
        // For instance, you can navigate to another activity or finish the current one
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish();
    }
}
