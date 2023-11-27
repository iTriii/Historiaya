package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//FOR UPDATE ONLY
public class LogIn extends AppCompatActivity {
    TextView forgotpass, inputemail, inputpass, regnow;
    Button li;
    FirebaseAuth mAuth;
    ImageView googleBtn;
    ProgressBar progressbar;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        inputemail = findViewById(R.id.inputemail);
        inputpass = findViewById(R.id.inputpass);
        li = findViewById(R.id.li);
        progressbar = findViewById(R.id.progressbar);

        forgotpass = findViewById(R.id.forgotpass);
        regnow = findViewById(R.id.regnow);

        googleBtn = findViewById(R.id.googleBtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(view -> {
            // Check if the user is already signed in with Google
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LogIn.this);
            if (account != null) {
                // User is signed in with Google, sign them out
                signOutGoogle();
            } else {
                // User is not signed in with Google, initiate Google sign-in
                signIn();

            }
        });
        li.setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE); // Show progress bar
            li.setVisibility(View.INVISIBLE); // Hide login button during progress

            String inputEmail = inputemail.getText().toString();
            String inputPass = inputpass.getText().toString();

            if (TextUtils.isEmpty(inputEmail)) {
                Toast.makeText(LogIn.this, "Enter email", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE); // Hide progress bar
                li.setVisibility(View.VISIBLE); // Show login button
                return;
            }
            if (TextUtils.isEmpty(inputPass)) {
                Toast.makeText(LogIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE); // Hide progress bar
                li.setVisibility(View.VISIBLE); // Show login button
                return;
            }

            // Authenticate with Firebase using email and password
            mAuth.signInWithEmailAndPassword(inputEmail, inputPass)
                    .addOnCompleteListener(task -> {
                        progressbar.setVisibility(View.GONE); // Hide progress bar
                        li.setVisibility(View.VISIBLE); // Show login button

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Check if the email is the specified admin email
                                if ("historiaya.acc@gmail.com".equals(user.getEmail())) {
                                    // Redirect to Admin class
                                    Toast.makeText(getApplicationContext(), "Store Manager Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), StoreManager.class);
                                    startActivity(intent);
                                    finish();
                                } else if ("itri.acc@gmail.com".equals(user.getEmail())) {
                                    // Handle the case for the additional email
                                    Toast.makeText(getApplicationContext(), "House ManagerLogin Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), HouseManager.class);
                                    startActivity(intent);
                                    finish();
                                } else if ("alcantaraleah914@gmail.com".equals(user.getEmail())) {
                                    // Handle the case for the additional email
                                    Toast.makeText(getApplicationContext(), "TourismHead Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TourismHeadAdmin.class);
                                    startActivity(intent);
                                    finish();
                                }else if ("touristarya@gmail.com".equals(user.getEmail())) {
                                    // Handle the case for the additional email
                                    Toast.makeText(getApplicationContext(), "Receptionist Login Successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), Receptionist.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Proceed with the login process for non-admin users
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Main2.class);
                                    startActivity(intent);
                                    finish();
                                }
                                //
                            }
                        } else {
                            Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        regnow.setOnClickListener(v -> {
            // If the user wants to register, navigate to the signup activity
            Intent intent = new Intent(LogIn.this, SignUp.class);
            startActivity(intent);
        });

        forgotpass.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, Reset_Password.class);
            startActivity(intent);
        });
    }

    private void signIn() {
        // Sign out any existing Google account to ensure account selection on button click
        gsc.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, 1000);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null && firebaseUser.getEmail().equals(account.getEmail())) {
                        if (firebaseUser.isEmailVerified()) {
                            if (isStoreManagerEmail(account.getEmail())) {
                                // historiaya.acc@gmail.com, navigate to StoreManager class
                                navigateToStoreManagerActivity();
                            } else if (isHouseManagerEmail(account.getEmail())) {
                                // itri.acc@gmail.com, navigate to HouseManager class
                                navigateToHouseManagerActivity();
                            } else if (isTourismHeadEmail(account.getEmail())) {
                                // alcantaraleah914@gmail.com, navigate to Tourism HeadHouse class
                                navigateToTourismHeadActivity();
                            }else if (isReceptionistEmail(account.getEmail())) {
                                // touristarya@gmail.com, navigate to RECEPTIONIST class
                                navigateToReceptionistActivity();
                            } else {
                                // Non-admin email, navigate to Main2
                                navigateToSecondActivity();
                            }
                        } else {
                            // Email is not verified
                            Toast.makeText(LogIn.this, "Email is not verified. Please sign up.", Toast.LENGTH_LONG).show();
                            signOutGoogle();
                        }
                    } else {
                        // The Google account is not linked to Firebase
                        Toast.makeText(LogIn.this, "Email is not verified. Please sign up", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean isStoreManagerEmail(String email) {
        return "historiaya.acc@gmail.com".equals(email);
    }
    //HOUSE MANAGER
    private boolean isHouseManagerEmail(String email) {
        return "itri.acc@gmail.com".equals(email);
    }

    //TOURISM HEAD
    private boolean isTourismHeadEmail(String email) {
        return "alcantaraleah914@gmail.com".equals(email);
    }

    //RECEPTIONIST
    private boolean isReceptionistEmail(String email) {
        return "touristarya@gmail.com".equals(email);
    }

    private void navigateToStoreManagerActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                if ("historiaya.acc@gmail.com".equals(user.getEmail())) {
                    // Admin email, navigate to Admin class
                    Toast.makeText(getApplicationContext(), "Store Manager Login Successful", Toast.LENGTH_LONG).show();
                    Log.d("Navigate", "Navigating to Admin.class");
                    finish();
                    Intent intent = new Intent(LogIn.this, StoreManager.class);
                    startActivity(intent);
                } else {
                    // Non-admin email, navigate to Main2
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.d("Navigate", "Navigating to Main2.class");
                    retrieveUserPoints();
                    finish();
                    Intent intent = new Intent(LogIn.this, Main2.class);
                    startActivity(intent);
                }
            } else {
                // Email is not verified, send verification email
                Toast.makeText(LogIn.this, "Email is not verified. Sending verification email.", Toast.LENGTH_LONG).show();

                user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                    if (emailVerificationTask.isSuccessful()) {
                        // Email verification sent
                        Toast.makeText(LogIn.this, "Verification email sent. Check your email.", Toast.LENGTH_SHORT).show();
                    }
                });

                // You may want to sign out the user here if needed
                signOutGoogle();
            }
        } else {
            // User is not logged in
            Toast.makeText(LogIn.this, "User not authenticated.", Toast.LENGTH_LONG).show();
            // You may want to sign out the user here if needed
            signOutGoogle();
        }
    }



    private void navigateToHouseManagerActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                if ("itri.acc@gmail.com".equals(user.getEmail())) {
                    // Admin email, navigate to Admin class
                    Toast.makeText(getApplicationContext(), "House Manager Login Successful", Toast.LENGTH_LONG).show();
                    Log.d("Navigate", "Navigating to HouseManager.class");
                    finish();
                    Intent intent = new Intent(LogIn.this, HouseManager.class);
                    startActivity(intent);
                } else {
                    // Non-admin email, navigate to Main2
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.d("Navigate", "Navigating to Main2.class");
                    retrieveUserPoints();
                    finish();
                    Intent intent = new Intent(LogIn.this, Main2.class);
                    startActivity(intent);
                }
            } else {
                // Email is not verified, send verification email
                Toast.makeText(LogIn.this, "Email is not verified. Sending verification email.", Toast.LENGTH_LONG).show();

                user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                    if (emailVerificationTask.isSuccessful()) {
                        // Email verification sent
                        Toast.makeText(LogIn.this, "Verification email sent. Check your email.", Toast.LENGTH_SHORT).show();
                    }
                });

                // You may want to sign out the user here if needed
                signOutGoogle();
            }
        } else {
            // User is not logged in
            Toast.makeText(LogIn.this, "User not authenticated.", Toast.LENGTH_LONG).show();
            // You may want to sign out the user here if needed
            signOutGoogle();
        }
    }

    //TOURISM HEAD
    private void navigateToTourismHeadActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                if ("alcantaraleah914@gmail.com".equals(user.getEmail())) {
                    // Admin email, navigate to Admin class
                    Toast.makeText(getApplicationContext(), "TourismHead Login Successful", Toast.LENGTH_LONG).show();
                    Log.d("Navigate", "Navigating to Admin.class");
                    finish();
                    Intent intent = new Intent(LogIn.this, TourismHeadAdmin.class);
                    startActivity(intent);
                } else {
                    // Non-admin email, navigate to Main2
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.d("Navigate", "Navigating to Main2.class");
                    retrieveUserPoints();
                    finish();
                    Intent intent = new Intent(LogIn.this, Main2.class);
                    startActivity(intent);
                }
            } else {
                // Email is not verified, send verification email
                Toast.makeText(LogIn.this, "Email is not verified. Sending verification email.", Toast.LENGTH_LONG).show();

                user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                    if (emailVerificationTask.isSuccessful()) {
                        // Email verification sent
                        Toast.makeText(LogIn.this, "Verification email sent. Check your email.", Toast.LENGTH_SHORT).show();
                    }
                });

                // You may want to sign out the user here if needed
                signOutGoogle();
            }
        } else {
            // User is not logged in
            Toast.makeText(LogIn.this, "User not authenticated.", Toast.LENGTH_LONG).show();
            // You may want to sign out the user here if needed
            signOutGoogle();
        }
    }

    //RECEPTIONIST
    private void navigateToReceptionistActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.isEmailVerified()) {
                if ("touristarya@gmail.com".equals(user.getEmail())) {
                    // Receptionist, navigate to Admin class
                    Toast.makeText(getApplicationContext(), "Receptionist Login Successful", Toast.LENGTH_LONG).show();
                    Log.d("Navigate", "Navigating to HouseManager.class");
                    finish();
                    Intent intent = new Intent(LogIn.this, Receptionist.class);
                    startActivity(intent);
                } else {
                    // Non-admin email, navigate to Main2
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.d("Navigate", "Navigating to Main2.class");
                    retrieveUserPoints();
                    finish();
                    Intent intent = new Intent(LogIn.this, Main2.class);
                    startActivity(intent);
                }
            } else {
                // Email is not verified, send verification email
                Toast.makeText(LogIn.this, "Email is not verified. Sending verification email.", Toast.LENGTH_LONG).show();

                user.sendEmailVerification().addOnCompleteListener(emailVerificationTask -> {
                    if (emailVerificationTask.isSuccessful()) {
                        // Email verification sent
                        Toast.makeText(LogIn.this, "Verification email sent. Check your email.", Toast.LENGTH_SHORT).show();
                    }
                });

                // You may want to sign out the user here if needed
                signOutGoogle();
            }
        } else {
            // User is not logged in
            Toast.makeText(LogIn.this, "User not authenticated.", Toast.LENGTH_LONG).show();
            // You may want to sign out the user here if needed
            signOutGoogle();
        }
    }

    //navigate to main2 activity
    private void navigateToSecondActivity() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            retrieveUserPoints();
            finish();
            Intent intent = new Intent(LogIn.this, Main2.class);
            startActivity(intent);
        } else {
            // User is not logged in or email not verified
            Toast.makeText(LogIn.this, "User not authenticated or email not verified.", Toast.LENGTH_LONG).show();
            // You may want to sign out the user here if needed
            signOutGoogle();
        }
    }

    private void retrieveUserPoints() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
        }
    }

    private void signOutGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(this, gso);
        gsc.signOut()
                .addOnCompleteListener(this, task -> {
                    // Google sign out successful
                });
    }
}