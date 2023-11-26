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

public class Receptionist_Adapter_History extends RecyclerView.Adapter<Receptionist_Adapter_History .MyViewHolder> {
    Context context;
    ArrayList<User> userArrayList;
    private FirebaseFirestore db;

    public Receptionist_Adapter_History (Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = firestore;
    }


    @NonNull
    @Override
    public Receptionist_Adapter_History.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.receptionist_history_item, parent, false);
        return new Receptionist_Adapter_History.MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Receptionist_Adapter_History.MyViewHolder holder, int position) {
        User HistoryUsers = userArrayList.get(position);

        holder.MontHistoRecepText.setText(HistoryUsers.getReservedDate());
        holder.BahayHistoRecepText.setText(HistoryUsers.getSelectedTour());
        holder.ArawHistoRecepText.setText(HistoryUsers.getReservedDate());
        holder.bookebyNameReceptionist_History.setText(HistoryUsers.getEmail());
        holder.TotalNumberReceptionist_History.setText(HistoryUsers.getSelectedTouristNum());
        holder.SelectedHouseReceptionist_History.setText(HistoryUsers.getSelectedTour());
        holder.AmountText.setText(String.valueOf(HistoryUsers.getTotalAmount()));


    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MontHistoRecepText, BahayHistoRecepText, ArawHistoRecepText,bookebyNameReceptionist_History,TotalNumberReceptionist_History,SelectedHouseReceptionist_History,AmountText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MontHistoRecepText = itemView.findViewById(R.id.MontHistoRecepText);
            BahayHistoRecepText = itemView.findViewById(R.id.BahayHistoRecepText);
            ArawHistoRecepText = itemView.findViewById(R.id.ArawHistoRecepText);
            bookebyNameReceptionist_History = itemView.findViewById(R.id.bookebyNameReceptionist_History);
            TotalNumberReceptionist_History = itemView.findViewById(R.id.TotalNumberReceptionist_History);
            SelectedHouseReceptionist_History = itemView.findViewById(R.id.SelectedHouseReceptionist_History);
            AmountText = itemView.findViewById(R.id.AmountText);
        }
    }
}
