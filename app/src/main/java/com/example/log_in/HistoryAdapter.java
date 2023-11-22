package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> HistoryUsersArrayList;
    private final FirebaseFirestore db;


    public HistoryAdapter(Context context, ArrayList<User> HistoryUsersArrayList, FirebaseFirestore db) {
        this.context = context;
        this.HistoryUsersArrayList = HistoryUsersArrayList;
        this.db = db;
    }


    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        User HistoryUsers = HistoryUsersArrayList.get(position);

        holder.MontHistoText.setText(HistoryUsers.getReservedDate());
        holder.BahayHistoText.setText(HistoryUsers.getSelectedTour());
        holder.ArawHistoText.setText(HistoryUsers.getReservedDate());
        holder.bookebyNameHistory.setText(HistoryUsers.getEmail());
        holder.TotalNumberHistory.setText(HistoryUsers.getSelectedTouristNum());
        holder.SelectedHouseHistory.setText(HistoryUsers.getSelectedTour());
    }

    @Override
    public int getItemCount() {
        return HistoryUsersArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MontHistoText, BahayHistoText, ArawHistoText,bookebyNameHistory,TotalNumberHistory,SelectedHouseHistory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MontHistoText = itemView.findViewById(R.id.MontHistoText);
            BahayHistoText= itemView.findViewById(R.id.BahayHistoText);
            ArawHistoText= itemView.findViewById(R.id.ArawHistoText);
            bookebyNameHistory = itemView.findViewById(R.id.bookebyNameHistory);
            TotalNumberHistory= itemView.findViewById(R.id.TotalNumberHistory);
            SelectedHouseHistory= itemView.findViewById(R.id.SelectedHouseHistory);


        }
    }
}


