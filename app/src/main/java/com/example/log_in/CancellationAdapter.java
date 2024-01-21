package com.example.log_in;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
//FOR UPDATE ONLY

// Adapter class for the RecyclerView to display cancellation requests
public class CancellationAdapter extends RecyclerView.Adapter<CancellationAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> cancellationList;
    private final FirebaseFirestore db;
    private static final int VIEW_TYPE_1 = 1;
    private static final int VIEW_TYPE_2 = 2;
    private static final int VIEW_TYPE_3 = 3;
    private static final int VIEW_TYPE_4 = 4;

    // Constructor to initialize the adapter with context, cancellation data, and Firestore instance
    public CancellationAdapter(Context context, ArrayList<User> cancellationList, FirebaseFirestore db) {
        this.context = context;
        this.cancellationList = cancellationList;
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
        User cancellation = cancellationList.get(position);

        // Handle button clicks
        holder.approvedbtn1.setOnClickListener(v -> onAcceptButtonClick(cancellation));
        holder.rejectdbtn1.setOnClickListener(v -> onRejectButtonClick(cancellation));
        holder.MonthCancelText.setText(cancellation.getReservedDate1());
        holder.BahayPendingCancelText.setText(cancellation.getSelectedTour1());
        holder.ArawPendingCancelText.setText(cancellation.getReservedDate1());
        holder.bookebyNameCancel.setText(cancellation.getEmail());
        holder.TotalNumberCancel.setText(cancellation.getSelectedTouristNum1());
        holder.SelectedHouseCancel.setText(cancellation.getSelectedTour1());
        holder.AmountTextCancel.setText(String.valueOf(cancellation.getTotalAmount1()));
        // Set the visibility of LLCANCEL1 based on selectRefundOption1


        // Handle button clicks
        holder.approvedbtn2.setOnClickListener(v -> onAcceptButtonClick(cancellation));
        holder.rejectdbtn2.setOnClickListener(v -> onRejectButtonClick(cancellation));

        // Set the values to the TextView widgets
        holder.MonthCancelText2.setText(cancellation.getReservedDate2());
        holder.BahayPendingCancelText2.setText(cancellation.getSelectedTour2());
        holder.ArawPendingCancelText2.setText(cancellation.getReservedDate2());
        holder.bookebyNameCancel2.setText(cancellation.getEmail());
        holder.TotalNumberCancel2.setText(cancellation.getSelectedTouristNum2());
        holder.SelectedHouseCancel2.setText(cancellation.getSelectedTour2());
        holder.AmountTextCancel2.setText(String.valueOf(cancellation.getTotalAmount2()));
        // Set the visibility of LLCANCEL2 based on selectRefundOption2



        // Handle button clicks
        holder.approvedbtn3.setOnClickListener(v -> onAcceptButtonClick(cancellation));
        holder.rejectdbtn3.setOnClickListener(v -> onRejectButtonClick(cancellation));

        // Set the values to the TextView widgets
        holder.MonthCancelText3.setText(cancellation.getReservedDate3());
        holder.BahayPendingCancelText3.setText(cancellation.getSelectedTour3());
        holder.ArawPendingCancelText3.setText(cancellation.getReservedDate3());
        holder.bookebyNameCancel3.setText(cancellation.getEmail());
        holder.TotalNumberCancel3.setText(cancellation.getSelectedTouristNum3());
        holder.SelectedHouseCancel3.setText(cancellation.getSelectedTour3());
        holder.AmountTextCancel3.setText(String.valueOf(cancellation.getTotalAmount3()));
        // Set the visibility of LLCANCEL3 based on selectRefundOption3


        // Set the values to the TextView widgets
        holder.MonthCancelText4.setText(cancellation.getReservedDate4());
        holder.BahayPendingCancelText4.setText(cancellation.getSelectedTour4());
        holder.ArawPendingCancelText4.setText(cancellation.getReservedDate4());
        holder.bookebyNameCancel4.setText(cancellation.getEmail());
        holder.TotalNumberCancel4.setText(cancellation.getSelectedTouristNum4());
        holder.SelectedHouseCancel4.setText(cancellation.getSelectedTour4());
        holder.AmountTextCancel4.setText(String.valueOf(cancellation.getTotalAmount4()));


    }



    // Method to handle the acceptance of a cancellation request
    private void onAcceptButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Approved)");
    }

    // Method to handle the rejection of a cancellation request
    private void onRejectButtonClick(User cancellation) {
        updateStatusInDatabase(cancellation, "Cancellation (Rejected)");
    }

    //
    // Method to handle updating the status in the database and removing from the list
    @SuppressLint("NotifyDataSetChanged")
    private void updateStatusInDatabase(User user, String status) {
        if (user != null && user.getUid() != null) {
            db.collection("users")
                    .document(user.getUid())
                    .update(
                            "status1", status,
                            "Cancelled1", true,
                            "Cancelled2", false,
                            "Cancelled3", false,
                            "Cancelled4", false,
                            "Cancelled5", false

                            // Add the 'Cancelled' field and set it to true
                    )
                    .addOnSuccessListener(aVoid -> {
                        user.setStatus1(status);
                        user.setCancelled1(true);
                        cancellationList.remove(user);
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
        return cancellationList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button approvedbtn1, rejectdbtn1;
        Button approvedbtn2, rejectdbtn2;
        Button approvedbtn3, rejectdbtn3;
        Button approvedbtn4, rejectdbtn4;
        LinearLayout LLCANCEL1, LLCANCEL2, LLCANCEL3, LLCANCEL4;
        TextView MonthCancelText, BahayPendingCancelText, ArawPendingCancelText, TotalNumberCancel, bookebyNameCancel, SelectedHouseCancel, AmountTextCancel;
        TextView MonthCancelText2, BahayPendingCancelText2, ArawPendingCancelText2, TotalNumberCancel2, bookebyNameCancel2, SelectedHouseCancel2, AmountTextCancel2;
        TextView MonthCancelText3, BahayPendingCancelText3, ArawPendingCancelText3, TotalNumberCancel3, bookebyNameCancel3, SelectedHouseCancel3, AmountTextCancel3;

        TextView MonthCancelText4, BahayPendingCancelText4, ArawPendingCancelText4, TotalNumberCancel4, bookebyNameCancel4, SelectedHouseCancel4, AmountTextCancel4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find and assign views from the layout to variables
            approvedbtn1 = itemView.findViewById(R.id.approvedbtn1);
            rejectdbtn1 = itemView.findViewById(R.id.rejectdbtn1);
            MonthCancelText = itemView.findViewById(R.id.MonthCancelText);
            BahayPendingCancelText = itemView.findViewById(R.id.BahayPendingCancelText);
            ArawPendingCancelText = itemView.findViewById(R.id.ArawPendingCancelText);
            bookebyNameCancel = itemView.findViewById(R.id.bookebyNameCancel);
            TotalNumberCancel = itemView.findViewById(R.id.TotalNumberCancel);
            SelectedHouseCancel = itemView.findViewById(R.id.SelectedHouseCancel);
            AmountTextCancel = itemView.findViewById(R.id.AmountTextCancel);
            LLCANCEL1 = itemView.findViewById(R.id.LLCANCEL1);

            // Find and assign views from the layout to variables
            approvedbtn2 = itemView.findViewById(R.id.approvedbtn2);
            rejectdbtn2 = itemView.findViewById(R.id.rejectdbtn2);
            MonthCancelText2 = itemView.findViewById(R.id.MonthCancelText2);
            BahayPendingCancelText2 = itemView.findViewById(R.id.BahayPendingCancelText2);
            ArawPendingCancelText2 = itemView.findViewById(R.id.ArawPendingCancelText2);
            bookebyNameCancel2 = itemView.findViewById(R.id.bookebyNameCancel2);
            TotalNumberCancel2 = itemView.findViewById(R.id.TotalNumberCancel2);
            SelectedHouseCancel2 = itemView.findViewById(R.id.SelectedHouseCancel2);
            AmountTextCancel2 = itemView.findViewById(R.id.AmountTextCancel2);
            LLCANCEL2 = itemView.findViewById(R.id.LLCANCEL2);

            // Find and assign views from the layout to variables
            approvedbtn3 = itemView.findViewById(R.id.approvedbtn3);
            rejectdbtn3 = itemView.findViewById(R.id.rejectdbtn3);
            MonthCancelText3 = itemView.findViewById(R.id.MonthCancelText3);
            BahayPendingCancelText3 = itemView.findViewById(R.id.BahayPendingCancelText3);
            ArawPendingCancelText3 = itemView.findViewById(R.id.ArawPendingCancelText3);
            bookebyNameCancel3 = itemView.findViewById(R.id.bookebyNameCancel3);
            TotalNumberCancel3 = itemView.findViewById(R.id.TotalNumberCancel3);
            SelectedHouseCancel3 = itemView.findViewById(R.id.SelectedHouseCancel3);
            AmountTextCancel3 = itemView.findViewById(R.id.AmountTextCancel3);
            LLCANCEL3 = itemView.findViewById(R.id.LLCANCEL3);

            // Find and assign views from the layout to variables
            approvedbtn4 = itemView.findViewById(R.id.approvedbtn4);
            rejectdbtn4 = itemView.findViewById(R.id.rejectdbtn4);
            MonthCancelText4 = itemView.findViewById(R.id.MonthCancelText4);
            BahayPendingCancelText4 = itemView.findViewById(R.id.BahayPendingCancelText4);
            ArawPendingCancelText4 = itemView.findViewById(R.id.ArawPendingCancelText4);
            bookebyNameCancel4 = itemView.findViewById(R.id.bookebyNameCancel4);
            TotalNumberCancel4 = itemView.findViewById(R.id.TotalNumberCancel4);
            SelectedHouseCancel4 = itemView.findViewById(R.id.SelectedHouseCancel4);
            AmountTextCancel4 = itemView.findViewById(R.id.AmountTextCancel4);
            LLCANCEL4 = itemView.findViewById(R.id.LLCANCEL4);


        }

    }
}