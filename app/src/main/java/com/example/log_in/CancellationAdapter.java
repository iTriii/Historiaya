package com.example.log_in;

// Import necessary packages
import android.annotation.SuppressLint;
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

// Adapter class for the RecyclerView to display cancellation requests
public class CancellationAdapter extends RecyclerView.Adapter<CancellationAdapter.MyViewHolder> {

    // Variables to store context, cancellation data, and Firebase Firestore instance
    Context context;
    ArrayList<User>  cancellationList;
    ArrayList<User> historyList;
    private final FirebaseFirestore db;

    // Constructor to initialize the adapter with context, cancellation data, and Firestore instance
    public CancellationAdapter(Context context, ArrayList<User> cancellationList, FirebaseFirestore db) {
        this.context = context;
        this. cancellationList =  cancellationList;
        this.db = db;
    }

    // Method to create a new ViewHolder instance when needed
    @NonNull
    @Override
    public CancellationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each list item using the provided context
        View v = LayoutInflater.from(context).inflate(R.layout.cancellation_item, parent, false);
        return new MyViewHolder(v);
    }

    // Method to bind data to the ViewHolder at the given position
    @Override
    public void onBindViewHolder(@NonNull CancellationAdapter.MyViewHolder holder, int position) {
        // Get the cancellation data at the current position
        User cancellation = cancellationList.get(position);

        // Handle button clicks
        holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(cancellation));
        holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(cancellation));

        // Set the values to the TextView widgets
        holder.MonthCancelText.setText(cancellation.getReservedDate());
        holder.BahayPendingCancelText.setText(cancellation.getSelectedTour());
        holder.ArawPendingCancelText.setText(cancellation.getReservedDate());
        holder.bookebyNameCancel.setText(cancellation.getEmail());
        holder.TotalNumberCancel.setText(cancellation.getSelectedTouristNum());
        holder.SelectedHouseCancel.setText(cancellation.getSelectedTour());
        holder.AmountTextCancel.setText(String.valueOf(cancellation.getTotalAmount()));
    }

    // Method to handle the acceptance of a cancellation request
    private void onAcceptButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Approved)");
    }

    // Method to handle the rejection of a cancellation request
    private void onRejectButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Rejected)");
    }

    // Method to update the cancellation status in the database and handle list updates
    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusInDatabase(User cancellation, String status) {
        int position = cancellationList.indexOf(cancellation);
        if (position != -1) {
            db.collection("users")
                    .document(cancellation.getUid())
                    .update("cancellationStatus", status)
                    .addOnSuccessListener(aVoid -> {
                        cancellation.setStatus(status);

                        // Remove the user from the current list
                        cancellationList.remove(position);

                        // Add the user to the appropriate list (e.g., Upcoming or History)
                        if ("Cancellation (Approved)".equals(status)) {
                            // Move to Upcoming list
                            historyList.remove(cancellation);
                        } else if ("Cancellation (Rejected)".equals(status)) {
                            // Move to History list
                            historyList.add(cancellation);
                        }

                        notifyDataSetChanged();
                        Log.d("CancellationAdapter", "Item updated successfully");

                        // Call updateData method here if needed
                        updateData(cancellationList);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CancellationAdapter", "Error updating status: " + e.getMessage());
                    });
        } else {
            Log.e("CancellationAdapter", "Item not found in the list");
        }
    }

    // Method to get the total number of items in the adapter
    @Override
    public int getItemCount() {
        return cancellationList.size();
    }

    // Method to update the adapter data with a new cancellation list
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<User> cancellationList) {
        cancellationList.clear();

        // Add the new data to the list
        cancellationList.addAll(cancellationList);

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

    // Method to set the cancellation list data for the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(ArrayList<User> userCancellationList) {
        this.cancellationList = userCancellationList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Method to set the cancellation list data for the adapter
    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<User> userCancellationList) {
        this.cancellationList = userCancellationList;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold the views for each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button approvedbtn, rejectdbtn;
        TextView MonthCancelText, BahayPendingCancelText, ArawPendingCancelText, TotalNumberCancel, bookebyNameCancel, SelectedHouseCancel, AmountTextCancel;

        // Constructor to initialize the ViewHolder with the item layout views
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find and assign views from the layout to variables
            approvedbtn = itemView.findViewById(R.id.approvedbtn);
            rejectdbtn = itemView.findViewById(R.id.rejectdbtn);
            MonthCancelText = itemView.findViewById(R.id.MonthCancelText);
            BahayPendingCancelText = itemView.findViewById(R.id.BahayPendingCancelText);
            ArawPendingCancelText = itemView.findViewById(R.id.ArawPendingCancelText);
            bookebyNameCancel = itemView.findViewById(R.id.bookebyNameCancel);
            TotalNumberCancel = itemView.findViewById(R.id.TotalNumberCancel);
            SelectedHouseCancel = itemView.findViewById(R.id.SelectedHouseCancel);
            AmountTextCancel = itemView.findViewById(R.id.AmountTextCancel);
        }
    }
}