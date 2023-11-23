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
import com.example.log_in.User;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class historyHM_adapter extends RecyclerView.Adapter<historyHM_adapter.MyViewHolder> {

    Context context;
    ArrayList <User> userArrayList;

    private final FirebaseFirestore db;

    public historyHM_adapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore db){
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public historyHM_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_reservation,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull historyHM_adapter.MyViewHolder holder, int position) {

        User HistoryHMUsers = userArrayList.get(position);

        holder. MonthHouseManagerText.setText(HistoryHMUsers.getReservedDate());
        holder.BahayHouseManagerText.setText(HistoryHMUsers.getSelectedTour());
        holder.ArawHouseManagerText.setText(HistoryHMUsers.getReservedDate());
        holder.TouristHouseManagerText.setText(HistoryHMUsers.getEmail());
        holder.TouristNumber.setText(HistoryHMUsers.getSelectedTouristNum());
        holder.bookebyNameHMHistory.setText(HistoryHMUsers.getEmail());
        holder.SelectedHouseHMHistory.setText(HistoryHMUsers.getSelectedTour());


    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText, TouristHouseManagerText, TouristNumber,bookebyNameHMHistory,TotalNumberHM_History,SelectedHouseHMHistory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthHouseManagerText = itemView.findViewById(R.id.MonthHouseManagerText);
            BahayHouseManagerText = itemView.findViewById(R.id.BahayHouseManagerText);
            ArawHouseManagerText = itemView.findViewById(R.id.ArawHouseManagerText);
            bookebyNameHMHistory = itemView.findViewById(R.id.bookebyNameHM_History);
            TotalNumberHM_History = itemView.findViewById(R.id.TotalNumberHM_History);
            SelectedHouseHMHistory = itemView.findViewById(R.id.SelectedHouseHMHistory);
        }
    }
}
