package com.example.log_in;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    ShapeableImageView icon;
    EditText firstname, lastname, E_mail, contact, pass, reenter;
    TextView uploadimage;
    Button signUpButton;

    FirebaseFirestore firestore;

    int SELECT_PICTURE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firestore = FirebaseFirestore.getInstance();

        icon = findViewById(R.id.icon);
        uploadimage = findViewById(R.id.uploadimage);


        uploadimage.setOnClickListener(v -> icon());


        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> OTP());

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);
        pass = findViewById(R.id.pass);
        reenter = findViewById(R.id.reenter);


    }

    public void OTP() {
        String firstnameText = firstname.getText().toString().trim();
        String lastnameText = lastname.getText().toString().trim();
        String E_mailText = E_mail.getText().toString().trim();
        String contactNum = contact.getText().toString().trim();
        String Password = pass.getText().toString().trim();
        String Reenter = reenter.getText().toString().trim();

        if (TextUtils.isEmpty(firstnameText)) {
            firstname.setError("This field cannot be empty");
            firstname.requestFocus();
            return;
        } else {
            firstname.setError(null);
        }


        if (containsDigits(firstnameText)) {
            firstname.setError("Must contain letter characters");
            firstname.requestFocus();
            return;
        } else {
            firstname.setError(null);
        }


        if (TextUtils.isEmpty(lastnameText)) {
            lastname.setError("This field cannot be empty");
            lastname.requestFocus();
            return;
        } else {
            lastname.setError(null);
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(E_mailText).matches()) {
            E_mail.setError("Invalid Email Format");
            E_mail.requestFocus();
            return;
        } else {
            E_mail.setError(null);
        }


        if (TextUtils.isEmpty(contactNum)) {
            contact.setError("Contact number cannot be empty");
            contact.requestFocus();
            return;
        } else if (!TextUtils.isDigitsOnly(contactNum) || contactNum.length() < 10) {
            contact.setError("Contact number should contain exactly 10 digits");
            contact.requestFocus();
            return;
        } else {
            contact.setError(null);
        }


        if (!containsDigits(Password) || !containsLetters(Password)) {
            pass.setError("Password should contain at least one digit and one letter");
            pass.requestFocus();
            return;
        } else {
            pass.setError(null);
        }


        if (!Reenter.equals(Password)) {
            reenter.setError("Password do not match");
            reenter.requestFocus();
            return;
        } else {
            reenter.setError(null);
        }


        Intent intent = new Intent(this, OTP.class);
        startActivity(intent);
        overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
    }

    private boolean containsLetters(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigits(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isDigit(text.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    void icon() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    icon.setImageURI(selectedImageUri);

                    saveUserDataToFireStore(firstname.getText().toString().trim(),
                            lastname.getText().toString().trim(),
                            E_mail.getText().toString().trim(),
                            contact.getText().toString().trim(),
                            pass.getText().toString().trim(),
                            reenter.getText().toString().trim());

                }
            }
        }
    }
    private void saveUserDataToFireStore(String firstname, String lastname, String E_mail, String contact, String pass, String reenter) {
        // ... (rest of your code)

        firestore.collection("Sign Up").document("Details")
                .set(getUserData(firstname, lastname, E_mail,contact, pass,reenter))
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to add data", Toast.LENGTH_LONG).show();
                });
    }

    private java.util.Map<String, Object> getUserData(String firstname, String lastname, String E_mail, String contact, String pass, String reenter) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("firstname", firstname);
        userData.put("lastname", lastname);
        userData.put("E_mail", E_mail);
        userData.put("contact", contact);
        userData.put("pass", pass);
        userData.put("reenter", reenter);
        return userData;

    }
}









