package com.example.log_in;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.log_in.databinding.ActivityPaymentBinding;

public class Payment extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    private void setListener(){

    }

}
