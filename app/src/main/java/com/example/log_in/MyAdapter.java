package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        User user = userArrayList.get(position);

        holder.pendingMonthText.setText(user.getReservedDate());
        holder.bahayPendingText.setText(user.getSelectedTour());
        holder.arawPendingText.setText(user.getReservedDate());
        holder.bookedByPending.setText(user.getEmail());
        holder.totalNumberPending.setText(user.getSelectedTouristNum());
        holder.selectedHousePending.setText(user.getSelectedTour());
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView pendingMonthText, bahayPendingText, arawPendingText, bookedByPending, totalNumberPending, selectedHousePending;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pendingMonthText = itemView.findViewById(R.id.PendingMonthText);
            bahayPendingText = itemView.findViewById(R.id.BahayPendingText);
            arawPendingText = itemView.findViewById(R.id.ArawPendingText);
            bookedByPending = itemView.findViewById(R.id.bookebyNamePending);
            totalNumberPending = itemView.findViewById(R.id.TotalNumberPending);
            selectedHousePending = itemView.findViewById(R.id.SelectedHousePending);
        }
    }
}
