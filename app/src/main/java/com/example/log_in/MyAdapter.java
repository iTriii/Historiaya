package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    private final FirebaseFirestore db;

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

        // Check if the user is confirmed or cancelled, and skip displaying the item
        if (!isConfirmedOrCancelled(user)) {
            // Handle button clicks
            holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(user));
            holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(user));

            holder.pendingMonthText.setText(user.getReservedDate1());
            holder.bahayPendingText.setText(user.getSelectedTour1());
            holder.arawPendingText.setText(user.getReservedDate1());
            holder.bookedByPending.setText(user.getEmail());
            holder.totalNumberPending.setText(user.getSelectedTouristNum1());
            holder.selectedHousePending.setText(user.getSelectedTour1());
            holder.AmountTextTH.setText(String.valueOf(user.getTotalAmount1()));
        } else {
            // If the user is confirmed or cancelled, hide the itemView
            holder.itemView.setVisibility(View.GONE);
        }
    }

    // Method to check if the user is confirmed or cancelled
    private boolean isConfirmedOrCancelled(User user) {
        return user != null && user.getStatus1() != null &&
                (user.getStatus1().equals("Confirmed Booking") || user.getStatus1().equals("Cancelled Booking"));
    }


    private void onAcceptButtonClick(User user) {
        updateStatusInDatabase(user, "Confirmed Booking ");
    }

    private void onRejectButtonClick(User user) {
        updateStatusInDatabase(user, "Cancelled Booking");
    }

    // Method to handle updating the status in the database and removing from the list
    @SuppressLint("NotifyDataSetChanged")

    private void updateStatusInDatabase(User user, String status1) {
        if (user != null && user.getUid() != null) {
            db.collection("users")
                    .document(user.getUid())
                    .update("status1", status1)
                    .addOnSuccessListener(aVoid -> {
                        // Update the user status in the local object
                        user.setStatus1(status1);

                        // Remove the user from the list
                        userArrayList.remove(user);

                        // Notify the adapter on the main thread
                        Handler mainHandler = new Handler(Looper.getMainLooper());
                        mainHandler.post(() -> notifyDataSetChanged());

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
        TextView pendingMonthText, bahayPendingText, arawPendingText, bookedByPending, totalNumberPending, selectedHousePending, AmountTextTH;

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
            AmountTextTH = itemView.findViewById(R.id.AmountTextTH);
        }
    }
}