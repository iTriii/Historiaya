package com.example.log_in;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private FirebaseFirestore db;
    private String status;

    public MyAdapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = firestore;
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

    // Method to handle accepting an item
    private void onAcceptButtonClick(User user) {
        // Update the status in the database (Firestore in this example)
        db.collection("users")
                .document(user.getEmail()) // Assuming email is a unique identifier
                .update("status", "Confirmed")
                .addOnSuccessListener(aVoid -> {
                    // Update the local user object's status
                    user.setStatus("Confirmed");

                    // Remove the item from the list
                    userArrayList.remove(user);

                    // Notify the adapter about the data change
                    notifyDataSetChanged();

                    // You can add further UI feedback or actions here

                    Log.d("MyAdapter", "Item accepted successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle failure, log error, show toast, etc.
                    Log.e("MyAdapter", "Error updating status: " + e.getMessage());

                    // If updating the status fails, update it to "Rejected" as a fallback
                    user.setStatus("Rejected");

                    // Remove the item from the list
                    userArrayList.remove(user);

                    // Notify the adapter about the data change
                    notifyDataSetChanged();
                });  updateStatusInDatabase( user, status);
    }


    // Method to handle updating the status in the database
    private void updateStatusInDatabase(User user, String status) {
        db.collection("users")
                .document(user.getEmail())
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    user.setStatus(status);   // Update the local user object's status
                    userArrayList.remove(user);  // Remove the item from the list
                    notifyDataSetChanged();
                    Log.d("MyAdapter", "Item updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("MyAdapter", "Error updating status: " + e.getMessage());
                });
    }


    // Method to handle rejecting an item
    private void onRejectButtonClick(User user) {
        db.collection("users")
                .document(user.getEmail()) // Assuming email is a unique identifier
                .update("status", "Cancelled")
                .addOnSuccessListener(aVoid -> {
                    user.setStatus("Cancelled");   // Update the local user object's status
                    userArrayList.remove(user);  // Remove the item from the list
                    notifyDataSetChanged(); // Notify the adapter about the data change
                    // add further UI feedback or actions here
                })
                .addOnFailureListener(e -> {
                    Log.e("MyAdapter", "Error updating status: " + e.getMessage());   // Handle failure, log error, show toast, etc.
                });
    }



    //ALL USERS
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }



    //DINE ILALAGAY YUNG LAHAT NG ID
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
