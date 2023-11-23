package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class historyAdapterHM extends RecyclerView.Adapter<historyAdapterHM.MyViewHolder> {
    Context context;
    ArrayList<User> userArrayList;
    private FirebaseFirestore db;

    public historyAdapterHM(Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = firestore;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_reservation, parent, false);
        return new historyAdapterHM.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User HistoryUsers = userArrayList.get(position);

        holder.MonthHouseManagerText.setText(HistoryUsers.getReservedDate());
        holder.BahayHouseManagerText.setText(HistoryUsers.getSelectedTour());
        holder.ArawHouseManagerText.setText(HistoryUsers.getReservedDate());
        holder.bookebyNameHMHistory.setText(HistoryUsers.getEmail());
        holder.TotalNumberHMHistory.setText(HistoryUsers.getSelectedTouristNum());
        holder.SelectedHouseHMHistory.setText(HistoryUsers.getSelectedTour());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText,bookebyNameHMHistory,TotalNumberHMHistory,SelectedHouseHMHistory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthHouseManagerText = itemView.findViewById(R.id.MonthHouseManagerText);
            BahayHouseManagerText = itemView.findViewById(R.id.BahayHouseManagerText);
            ArawHouseManagerText = itemView.findViewById(R.id.ArawHouseManagerText);
            bookebyNameHMHistory = itemView.findViewById(R.id.bookebyNameHMHistory);
            TotalNumberHMHistory = itemView.findViewById(R.id.TotalNumberHMHistory);
            SelectedHouseHMHistory = itemView.findViewById(R.id.SelectedHouseHMHistory);



        }
    }
}

