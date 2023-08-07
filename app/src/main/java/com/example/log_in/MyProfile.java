package com.example.log_in;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;

public class MyProfile extends AppCompatActivity {
    ImageButton back;
    ShapeableImageView icon;
    TextView Uploadanimage,firstname,lastname,E_mail,contact, Save;
    int SELECT_PICTURE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Uploadanimage = findViewById(R.id.Uploadanimage);
        Uploadanimage.setOnClickListener(v -> icon());
        icon = findViewById(R.id.icon);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> Profile());

        Save = findViewById(R.id.Save);
        Save.setOnClickListener(v -> Profile());

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        E_mail = findViewById(R.id.E_mail);
        contact = findViewById(R.id.contact);
    }
    public void Profile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
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


                }
            }
        }
    }
}