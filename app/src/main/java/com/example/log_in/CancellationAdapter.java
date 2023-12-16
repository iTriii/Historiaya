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
import java.util.Collection;

public class CancellationAdapter extends RecyclerView.Adapter<CancellationAdapter.MyViewHolder> {

    Context context;

    private final FirebaseFirestore db;

    private Collection<? extends User> updatedCancellationList;
    private ArrayList<User> CancellationlistUsers;
    private ArrayList<User> cancellationList;


    public CancellationAdapter(Context context, ArrayList<User> Cancellationlist, FirebaseFirestore db) {
        this.context = context;
        this.CancellationlistUsers = Cancellationlist;
        this.db = db;
    }

    @NonNull
    @Override
    public CancellationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cancellation_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CancellationAdapter.MyViewHolder holder, int position) {
        User cancellation = CancellationlistUsers.get(position);

        // Handle button clicks
        holder.approvedbtn.setOnClickListener(v -> onAcceptButtonClick(cancellation));
        holder.rejectdbtn.setOnClickListener(v -> onRejectButtonClick(cancellation));

        // Log the values for debugging
        Log.d("CancellationAdapter", "ReservedDate: " + cancellation.getReservedDate());
        Log.d("CancellationAdapter", "SelectedTour: " + cancellation.getSelectedTour());
        Log.d("CancellationAdapter", "Email: " + cancellation.getEmail());
        Log.d("CancellationAdapter", "SelectedTouristNum: " + cancellation.getSelectedTouristNum());
        Log.d("CancellationAdapter", "TotalAmount: " + cancellation.getTotalAmount());

        // Set the values to the TextView widgets
        holder.MonthCancelText.setText(cancellation.getReservedDate());
        holder.BahayPendingCancelText.setText(cancellation.getSelectedTour());
        holder.ArawPendingCancelText.setText(cancellation.getReservedDate());
        holder.bookebyNameCancel.setText(cancellation.getEmail());
        holder.TotalNumberCancel.setText(cancellation.getSelectedTouristNum());
        holder.SelectedHouseCancel.setText(cancellation.getSelectedTour());
        holder.AmountTextCancel.setText(String.valueOf(cancellation.getTotalAmount()));
    }

    private void onAcceptButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Approved)");
    }

    private void onRejectButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Rejected)");
    }

    private void updateStatusInDatabase(User cancellation, String status) {
        int position = CancellationlistUsers.indexOf(cancellation);
        if (position != -1) {
            db.collection("users")
                    .document(cancellation.getUid())
                    .update("StatusOfCancellation", status)
                    .addOnSuccessListener(aVoid -> {
                        cancellation.setStatus(status);

                        // Remove the user from the current list
                        CancellationlistUsers.remove(position);

                        // Add the user to the appropriate list
                        if ("Approved".equals(status)) {
                            // Move to Upcoming list
                         //   upcomingList.remove(cancellation);
                        } else if ("Cancelled".equals(status)) {
                            // Move to History list
                         //   historyList.add(cancellation);
                        }

                        notifyDataSetChanged();
                        Log.d("CancellationAdapter", "Item updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CancellationAdapter", "Error updating status: " + e.getMessage());
                    });
        } else {
            Log.e("CancellationAdapter", "Item not found in the list");
        } updateData(cancellationList);
    }


    @Override
    public int getItemCount() {
        return CancellationlistUsers.size();
    }

    public void updateData(ArrayList<User> cancellationList) {
        cancellationList.clear();

        // Add the new data to the list
        cancellationList.addAll(updatedCancellationList);

        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }

    public void setUsers(ArrayList<User> userList) {
        this.CancellationlistUsers = userList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MonthCancelText, BahayPendingCancelText, ArawPendingCancelText, TotalNumberCancel, bookebyNameCancel, SelectedHouseCancel, AmountTextCancel;
        Button approvedbtn, rejectdbtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

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
