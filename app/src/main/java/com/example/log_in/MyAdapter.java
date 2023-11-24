package com.example.log_in;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private FirebaseFirestore db;

    public MyAdapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = firestore;
    }

    @Override
    public MyAdapter.@NonNull MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.@NonNull MyViewHolder holder, int position) {
        User user = userArrayList.get(position);

        // Handle button clicks
        holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(user));
        holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(user));

        holder.pendingMonthText.setText(user.getReservedDate());
        holder.bahayPendingText.setText(user.getSelectedTour());
        holder.arawPendingText.setText(user.getReservedDate());
        holder.bookedByPending.setText(user.getEmail());
        holder.totalNumberPending.setText(user.getSelectedTouristNum());
        holder.selectedHousePending.setText(user.getSelectedTour());
    }

    private void onAcceptButtonClick(User user) {
        updateStatusInDatabase(user, "Confirmed");
    }

    private void onRejectButtonClick(User user) {
        updateStatusInDatabase(user, "Cancelled");
    }

    // Method to handle updating the status in the database
    private void updateStatusInDatabase(User user, String status) {
        if (user != null && user.getUid() != null) {
            db.collection("users")
                    .document(user.getUid())
                    .update("status", status)
                    .addOnSuccessListener(aVoid -> {
                        user.setStatus(status);
                        userArrayList.remove(user);
                        notifyDataSetChanged();
                        Log.d("MyAdapter", "Item updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MyAdapter", "Error updating status: " + e.getMessage());
                    });
        } else {
            Log.e("MyAdapter", "User or UID is null");
        }
    }



    //ALL USERS
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button approvedbtn, rejectdbtn;
        TextView pendingMonthText, bahayPendingText, arawPendingText, bookedByPending, totalNumberPending, selectedHousePending;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pendingMonthText = itemView.findViewById(R.id.PendingMonthText);
            bahayPendingText = itemView.findViewById(R.id.BahayPendingText);
            arawPendingText = itemView.findViewById(R.id.ArawPendingText);
            bookedByPending = itemView.findViewById(R.id.bookebyNamePending);
            totalNumberPending = itemView.findViewById(R.id.TotalNumberPending);
            selectedHousePending = itemView.findViewById(R.id.SelectedHousePending);
            approvedbtn = itemView.findViewById(R.id.approvedbtn);
            rejectdbtn = itemView.findViewById(R.id.rejectdbtn);
        }
    }
}
