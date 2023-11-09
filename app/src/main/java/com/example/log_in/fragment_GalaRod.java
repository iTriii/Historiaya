package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_GalaRod extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__gala_rod, container, false);

        // Find the button by its ID
        Button donebtn = rootView.findViewById(R.id.donebtn);

        // Set an OnClickListener for the button
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Handle the button click action here */
                Intent intent = new Intent(getActivity(), Payment.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
