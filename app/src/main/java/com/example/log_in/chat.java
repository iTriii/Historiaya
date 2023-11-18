package com.example.log_in;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import im.crisp.client.ChatActivity;
import im.crisp.client.Crisp;

        public class chat extends AppCompatActivity {

            private FirebaseFirestore db;
            private ImageButton imgback;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_chat);


                Crisp.configure(getApplicationContext(), "2a53b3b9-d275-4fb1-81b6-efad59022426");
                Intent chatbtn = new Intent(this, ChatActivity.class);
                startActivity(chatbtn);
            }
            public void BookNow() {
                Intent intent = new Intent(this, BookNow.class);
                startActivity(intent);
                overridePendingTransition(com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_in_left, com.blogspot.atifsoftwares.animatoolib.R.anim.animate_slide_out_right);
            }
        }
