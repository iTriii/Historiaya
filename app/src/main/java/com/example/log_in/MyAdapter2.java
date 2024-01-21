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

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {


    Context context;
    ArrayList<User> userArrayList1;
private final FirebaseFirestore db;

public MyAdapter2(Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList1 = userArrayList;
        this.db = firestore;
        }

@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item2, parent, false);
        return new MyViewHolder(v);
        }


@Override
public void onBindViewHolder(MyAdapter2.@NonNull MyViewHolder holder, int position) {
        User user = userArrayList1.get(position);

        // Check if the user is confirmed or cancelled, and skip displaying the item
        if (!isConfirmedOrCancelled(user)) {
        // Handle button clicks
        holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(user));
        holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(user));

        holder.pendingMonthText.setText(user.getReservedDate2());
        holder.bahayPendingText.setText(user.getSelectedTour2());
        holder.arawPendingText.setText(user.getReservedDate2());
        holder.bookedByPending.setText(user.getEmail());
        holder.totalNumberPending.setText(user.getSelectedTouristNum2());
        holder.selectedHousePending.setText(user.getSelectedTour2());
        holder.AmountTextTH.setText(String.valueOf(user.getTotalAmount2()));
        } else {
        // If the user is confirmed or cancelled, hide the itemView
        holder.itemView.setVisibility(View.GONE);
        }
        }

// Method to check if the user is confirmed or cancelled
private boolean isConfirmedOrCancelled(User user) {
        return user != null && user.getStatus2() != null &&
        (user.getStatus2().equals("Confirmed Booking") || user.getStatus2().equals("Cancelled Booking"));
        }


private void onAcceptButtonClick(User user) {
        updateStatusInDatabase(user, "Confirmed Booking ");
        }

private void onRejectButtonClick(User user) {
        updateStatusInDatabase(user, "Cancelled Booking");
        }

// Method to handle updating the status in the database and removing from the list
@SuppressLint("NotifyDataSetChanged")

private void updateStatusInDatabase(User user, String status2) {
        if (user != null && user.getUid() != null) {
        db.collection("users")
        .document(user.getUid())
        .update("status2", status2)
        .addOnSuccessListener(aVoid -> {
        // Update the user status in the local object
        user.setStatus2(status2);

        // Remove the user from the list
        userArrayList1.remove(user);

        // Notify the adapter on the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> notifyDataSetChanged());

        Log.d("MyAdapter2", "Item updated successfully");
        })
        .addOnFailureListener(e -> {
        Log.e("MyAdapter2", "Error updating status: " + e.getMessage());
        });
        } else {
        Log.e("MyAdapter2", "User or UID is null");
        }
        }


//ALL USERS
@Override
public int getItemCount() {
        return userArrayList1.size();
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