package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class facebookAuth extends AppCompatActivity {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_auth);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Check if the user's email exists in Firestore or is authenticated
                                checkUserInFirestore(user.getEmail());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(facebookAuth.this, "" + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkUserInFirestore(final String userEmail) {
        // Query Firestore to check if the user's email exists
        // Replace 'users' with the actual Firestore collection name
        firestore.collection("users")
                .document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // User email exists in Firestore, proceed to the next activity
                                updateUI();
                            } else {
                                // User email doesn't exist in Firestore, or not authenticated
                                // Display an error message or handle it as needed
                                Toast.makeText(facebookAuth.this,
                                        "Email is not verified. Please sign up",
                                        Toast.LENGTH_LONG).show();
                                // You can also sign out the user here if needed
                                mAuth.signOut();
                            }
                        } else {
                            // Error while querying Firestore
                            Toast.makeText(facebookAuth.this, "Error: " + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(facebookAuth.this, Main2.class);
        startActivity(intent);
        finish();
    }
}
