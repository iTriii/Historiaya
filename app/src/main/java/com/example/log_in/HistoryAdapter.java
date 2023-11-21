package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<historyList> historyList;

    public HistoryAdapter(Context context, ArrayList<historyList> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        historyList currentHistory = historyList.get(position);

        holder.SelectedHouseHistory.setText(currentHistory.getSelectedTour()); // HISTORY
        holder.TotalNumberHistory.setText(currentHistory.getSelectedTouristNum());
        holder.MontHistoText.setText(currentHistory.getReservedDate());
        holder.BahayHistoText.setText(currentHistory.getSelectedTour());
        holder.ArawHistoText.setText(currentHistory.getReservedDate());
        holder.bookebyNameHistory.setText(currentHistory.getEmail());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookebyNameHistory, ArawHistoText, BahayHistoText, MontHistoText, TotalNumberHistory, SelectedHouseHistory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            SelectedHouseHistory = itemView.findViewById(R.id.SelectedHouseHistory); // HISTORY
            TotalNumberHistory = itemView.findViewById(R.id.TotalNumberHistory);
            MontHistoText = itemView.findViewById(R.id.MontHistoText);
            BahayHistoText = itemView.findViewById(R.id.BahayHistoText);
            ArawHistoText = itemView.findViewById(R.id.ArawHistoText);
            bookebyNameHistory = itemView.findViewById(R.id.bookebyNameHistory);
        }
    }
}
