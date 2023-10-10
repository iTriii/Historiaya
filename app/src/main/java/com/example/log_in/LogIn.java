package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LogIn extends AppCompatActivity {
    TextView forgotpass, inputemail, inputpass, regnow;
    Button li;
    FirebaseAuth mAuth;
    ImageView googleBtn, facebookBtn;
    ProgressBar progressbar;
    CallbackManager callbackManager;
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

        facebookBtn = findViewById(R.id.facebookBtn);

        googleBtn = findViewById(R.id.googleBtn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this,gso);

        callbackManager = CallbackManager.Factory.create();

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user is already signed in with Google
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LogIn.this);
                if (account != null) {
                    // User is signed in with Google, sign them out
                    signOutGoogle();
                } else {
                    // User is not signed in with Google, initiate Google sign-in
                    signIn();

                }
            }
        });



        // Register a callback for Facebook login
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Handle successful Facebook login
                handleFacebookLoginSuccess(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Handle canceled Facebook login
                Toast.makeText(LogIn.this, "Facebook login canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // Handle Facebook login error
                Toast.makeText(LogIn.this, "Facebook login error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user is already authenticated with Firebase
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                    // User is already authenticated with Firebase and email is verified
                    // Proceed with Facebook login
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                    if (isLoggedIn) {
                        // User is already authenticated with Facebook, navigate to Main2
                        navigateToSecondActivity();
                    } else {
                        // User is not authenticated with Facebook, initiate Facebook login
                        LoginManager.getInstance().logInWithReadPermissions(LogIn.this, Arrays.asList("public_profile"));
                    }
                } else {
                    // User is not authenticated with Firebase or email is not verified
                    Toast.makeText(LogIn.this, "Email is not verified. Please sign up or verify your email.", Toast.LENGTH_LONG).show();
                }
            }
        });



        li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String inputEmail = inputemail.getText().toString();
                String inputPass = inputpass.getText().toString();

                if (TextUtils.isEmpty(inputEmail)) {
                    Toast.makeText(LogIn.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inputPass)) {
                    Toast.makeText(LogIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Authenticate with Firebase using email and password
                mAuth.signInWithEmailAndPassword(inputEmail, inputPass)
                        .addOnCompleteListener(task -> {
                            progressbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    if (user.isEmailVerified()) {
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Main2.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LogIn.this, "Email is not verified. Please check your email for verification instructions.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        regnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user wants to register, navigate to the signup activity
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, Reset_Password.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();

        // Check if the user is authenticated with Facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            // User is authenticated with Facebook, log them out
            LoginManager.getInstance().logOut();
        }
    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    // Handle successful Facebook login
    private void handleFacebookLoginSuccess(AccessToken accessToken) {
        // You can use the Facebook access token to integrate with your app's functionality
        // For example, you can fetch user data from Facebook and link it to Firebase user account
        Toast.makeText(LogIn.this, "Facebook login successful", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(LogIn.this, Main2.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    // Check if the Google account is linked to a Firebase account
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null && firebaseUser.getEmail().equals(account.getEmail())) {
                        // The Google account is linked to Firebase
                        // Check if the email is verified
                        if (firebaseUser.isEmailVerified()) {
                            // Email is verified, navigate to Main2
                            navigateToSecondActivity();
                        } else {
                            // Email is not verified
                            Toast.makeText(LogIn.this, "Email is not verified. Please sign up.", Toast.LENGTH_LONG).show();
                            // You may want to sign out the user here if needed
                            signOutGoogle();
                        }
                    } else {
                        // The Google account is not linked to Firebase
                        // You might want to handle this case differently
                        Toast.makeText(LogIn.this, "Email is not verified. Please sign up", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(LogIn.this, Main2.class);
        startActivity(intent);
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
