package com.example.log_in.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.log_in.R;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class historyHM_adapter extends RecyclerView.Adapter<historyHM_adapter.MyViewHolder> {

    Context context;
    ArrayList <upcoming_reservation_of_user> userArrayList;

    private final FirebaseFirestore db;

    public historyHM_adapter(Context context, ArrayList <upcoming_reservation_of_user> userArrayList, FirebaseFirestore db){
        this.context = context;
        this.userArrayList =userArrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public historyHM_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upcoming_reservation,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull historyHM_adapter.MyViewHolder holder, int position) {

        upcoming_reservation_of_user upcoming_reservation_of_user = userArrayList.get(position);

        holder. MonthHouseManagerText.setText(upcoming_reservation_of_user.getReservedDate());
        holder.BahayHouseManagerText.setText(upcoming_reservation_of_user.getSelectedTour());
        holder.ArawHouseManagerText.setText(upcoming_reservation_of_user.getReservedDate() + " - " + upcoming_reservation_of_user.getselectedTime());
        holder.TouristHouseManagerText.setText(upcoming_reservation_of_user.getFirstName() + " " + upcoming_reservation_of_user.getLastName());
        holder.TouristNumber.setText(upcoming_reservation_of_user.getselectedTouristNum() + " persons");
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText, TouristHouseManagerText, TouristNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthHouseManagerText = itemView.findViewById(R.id.MonthHouseManagerText);
            BahayHouseManagerText = itemView.findViewById(R.id.BahayHouseManagerText);
            ArawHouseManagerText = itemView.findViewById(R.id.ArawHouseManagerText);
            TouristHouseManagerText = itemView.findViewById(R.id.TouristHouseManagerText);
            TouristNumber = itemView.findViewById(R.id.TouristNumber);
        }
    }
}
