package com.example.log_in;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragmentDonCata extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public fragmentDonCata() {
        // Required empty public constructor
    }

    public static fragmentDonCata newInstance(String param1, String param2) {
        fragmentDonCata fragment = new fragmentDonCata();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_don_cata, container, false);
        // Find the button by its ID
        Button donebtn = rootView.findViewById(R.id.donebtn);

        // Set an OnClickListener for the button
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the button click action here
                Intent intent = new Intent(getActivity(), Payment.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}