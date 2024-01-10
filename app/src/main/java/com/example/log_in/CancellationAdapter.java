package com.example.log_in;

import android.annotation.SuppressLint;
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
//FOR UPDATE ONLY

// Adapter class for the RecyclerView to display cancellation requests
public class CancellationAdapter extends RecyclerView.Adapter<CancellationAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private final FirebaseFirestore db;
    private Object UpdatingtheTouristText;

    // Constructor to initialize the adapter with context, cancellation data, and Firestore instance
    public CancellationAdapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore db) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = db;
    }

    // Method to create a new ViewHolder instance when needed
    @Override
    public CancellationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each list item using the provided context
        View v = LayoutInflater.from(context).inflate(R.layout.cancellation_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CancellationAdapter.@NonNull MyViewHolder holder, int position) {
        // Get the cancellation data at the current position
        User cancellation = userArrayList.get(position);

        // Check if the user is already processed
        if (!isAlreadyProcessed(cancellation)) {
            // Handle button clicks
            holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(cancellation));
            holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(cancellation));

            // Set the values to the TextView widgets
            holder.MonthCancelText.setText(cancellation.getReservedDate());
            holder.BahayPendingCancelText.setText(cancellation.getSelectedTour());
            holder.ArawPendingCancelText.setText(cancellation.getReservedDate()); // Set the correct value here
            holder.bookebyNameCancel.setText(cancellation.getEmail());
            holder.TotalNumberCancel.setText(cancellation.getSelectedTouristNum());
            holder.SelectedHouseCancel.setText(cancellation.getSelectedTour());
            holder.AmountTextCancel.setText(String.valueOf(cancellation.getTotalAmount()));
        } else {
            // If the user is already processed, remove from the ArrayList
            userArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Method to handle the acptance of a cancellation request
    private void onAcceptButtonClick(User cancellation) {
        if (!isAlreadyProcessed(cancellation)) {
            updateStatusInDatabase(cancellation, "Cancellation (Approved)");
            updateStatusColor("red"); // Change status color to red
        } else {
            Log.d("CancellationAdapter", "User already processed");
        }
    }

    private void onRejectButtonClick(User cancellation) {
        if (!isAlreadyProcessed(cancellation)) {
            updateStatusInDatabase(cancellation, "Cancellation (Rejected)");
            updateStatusColor("red"); // Change status color to red
        } else {
            Log.d("CancellationAdapter", "User already processed");
        }
    }

    // Method to update status color
    private void updateStatusColor(String color) {

        if (UpdatingtheTouristText != null) {
            int colorRes = getColorRes(color); // Get the color resource based on the color name
            UpdatingtheTouristText.notify();
        }
    }

    // Method to get color resource based on color name
    private int getColorRes(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red":
                return R.color.salmon; // Replace with your actual red color resource
            // Add more cases for other colors if needed
            default:
                return R.color.black; // Replace with your default color resource
        }
    }


    private boolean isAlreadyProcessed(User user) {
        // Check if the user has already been approved or rejected
        return user != null && user.getStatus() != null &&
                (user.getStatus().equals("Cancellation (Approved)") || user.getStatus().equals("Cancellation (Rejected)"));
    }


    // Method to handle updating the status in the database and removing from the list
    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusInDatabase(User user, String status) {
        if (user != null && user.getUid() != null) {
            db.collection("users")
                    .document(user.getUid())
                    .update("status", status)
                    .addOnSuccessListener(aVoid -> {
                        user.setStatus(status);
                        userArrayList.remove(user);
                        notifyDataSetChanged();
                        Log.d("CancellationAdapter", "Item updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CancellationAdapter", "Error updating status: " + e.getMessage());
                    });
        } else {
            Log.e("CancellationAdapter", "User or UID is null");
        }
    }



    //ALL USERS
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button approvedbtn, rejectdbtn;
        TextView MonthCancelText, BahayPendingCancelText, ArawPendingCancelText, TotalNumberCancel, bookebyNameCancel, SelectedHouseCancel, AmountTextCancel;

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