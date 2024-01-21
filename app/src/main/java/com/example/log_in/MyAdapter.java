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



            holder.approvedbtn2.setOnClickListener(v -> onAcceptButtonClick(user));
            holder.rejectdbtn2.setOnClickListener(v -> onRejectButtonClick(user));

            holder.pendingMonthText2.setText(user.getReservedDate2());
            holder.bahayPendingText2.setText(user.getSelectedTour2());
            holder.arawPendingText2.setText(user.getReservedDate2());
            holder.bookedByPending2.setText(user.getEmail());
            holder.totalNumberPending2.setText(user.getSelectedTouristNum2());
            holder.selectedHousePending2.setText(user.getSelectedTour2());
            holder.AmountTextTH2.setText(String.valueOf(user.getTotalAmount2()));


            holder.approvedbtn3.setOnClickListener(v -> onAcceptButtonClick(user));
            holder.rejectdbtn3.setOnClickListener(v -> onRejectButtonClick(user));

            holder.pendingMonthText3.setText(user.getReservedDate3());
            holder.bahayPendingText3.setText(user.getSelectedTour3());
            holder.arawPendingText3.setText(user.getReservedDate3());
            holder.bookedByPending3.setText(user.getEmail());
            holder.totalNumberPending3.setText(user.getSelectedTouristNum3());
            holder.selectedHousePending3.setText(user.getSelectedTour3());
            holder.AmountTextTH3.setText(String.valueOf(user.getTotalAmount3()));


            holder.approvedbtn4.setOnClickListener(v -> onAcceptButtonClick(user));
            holder.rejectdbtn4.setOnClickListener(v -> onRejectButtonClick(user));

            holder.pendingMonthText4.setText(user.getReservedDate4());
            holder.bahayPendingText4.setText(user.getSelectedTour4());
            holder.arawPendingText4.setText(user.getReservedDate4());
            holder.bookedByPending4.setText(user.getEmail());
            holder.totalNumberPending4.setText(user.getSelectedTouristNum4());
            holder.selectedHousePending4.setText(user.getSelectedTour4());
            holder.AmountTextTH4.setText(String.valueOf(user.getTotalAmount4()));



            holder.approvedbtn5.setOnClickListener(v -> onAcceptButtonClick(user));
            holder.rejectdbtn5.setOnClickListener(v -> onRejectButtonClick(user));

            holder.pendingMonthText5.setText(user.getReservedDate5());
            holder.bahayPendingText5.setText(user.getSelectedTour5());
            holder.arawPendingText5.setText(user.getReservedDate5());
            holder.bookedByPending5.setText(user.getEmail());
            holder.totalNumberPending5.setText(user.getSelectedTouristNum5());
            holder.selectedHousePending5.setText(user.getSelectedTour5());
            holder.AmountTextTH5.setText(String.valueOf(user.getTotalAmount5()));


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


        Button approvedbtn2, rejectdbtn2;
        TextView pendingMonthText2, bahayPendingText2, arawPendingText2, bookedByPending2, totalNumberPending2, selectedHousePending2, AmountTextTH2;


        Button approvedbtn3, rejectdbtn3;
        TextView pendingMonthText3, bahayPendingText3, arawPendingText3, bookedByPending3, totalNumberPending3, selectedHousePending3, AmountTextTH3;


        Button approvedbtn4, rejectdbtn4;
        TextView pendingMonthText4, bahayPendingText4, arawPendingText4, bookedByPending4, totalNumberPending4, selectedHousePending4, AmountTextTH4;

        Button approvedbtn5, rejectdbtn5;
        TextView pendingMonthText5, bahayPendingText5, arawPendingText5, bookedByPending5, totalNumberPending5, selectedHousePending5, AmountTextTH5;



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



            pendingMonthText2 = itemView.findViewById(R.id.PendingMonthText2);
            bahayPendingText2 = itemView.findViewById(R.id.BahayPendingText2);
            arawPendingText2 = itemView.findViewById(R.id.ArawPendingText2);
            bookedByPending2 = itemView.findViewById(R.id.bookebyNamePending2);
            totalNumberPending2 = itemView.findViewById(R.id.TotalNumberPending2);
            selectedHousePending2 = itemView.findViewById(R.id.SelectedHousePending2);
            approvedbtn2 = itemView.findViewById(R.id.approvedbtn2);
            rejectdbtn2 = itemView.findViewById(R.id.rejectdbtn2);
            AmountTextTH2 = itemView.findViewById(R.id.AmountTextTH2);


            pendingMonthText3 = itemView.findViewById(R.id.PendingMonthText3);
            bahayPendingText3 = itemView.findViewById(R.id.BahayPendingText3);
            arawPendingText3 = itemView.findViewById(R.id.ArawPendingText3);
            bookedByPending3 = itemView.findViewById(R.id.bookebyNamePending3);
            totalNumberPending3 = itemView.findViewById(R.id.TotalNumberPending3);
            selectedHousePending3 = itemView.findViewById(R.id.SelectedHousePending3);
            approvedbtn3 = itemView.findViewById(R.id.approvedbtn3);
            rejectdbtn3 = itemView.findViewById(R.id.rejectdbtn3);
            AmountTextTH3 = itemView.findViewById(R.id.AmountTextTH3);

            pendingMonthText4 = itemView.findViewById(R.id.PendingMonthText4);
            bahayPendingText4 = itemView.findViewById(R.id.BahayPendingText4);
            arawPendingText4 = itemView.findViewById(R.id.ArawPendingText4);
            bookedByPending4 = itemView.findViewById(R.id.bookebyNamePending4);
            totalNumberPending4 = itemView.findViewById(R.id.TotalNumberPending4);
            selectedHousePending4 = itemView.findViewById(R.id.SelectedHousePending4);
            approvedbtn4 = itemView.findViewById(R.id.approvedbtn4);
            rejectdbtn4 = itemView.findViewById(R.id.rejectdbtn4);
            AmountTextTH4 = itemView.findViewById(R.id.AmountTextTH4);



            pendingMonthText5 = itemView.findViewById(R.id.PendingMonthText5);
            bahayPendingText5 = itemView.findViewById(R.id.BahayPendingText5);
            arawPendingText5 = itemView.findViewById(R.id.ArawPendingText5);
            bookedByPending5 = itemView.findViewById(R.id.bookebyNamePending5);
            totalNumberPending5 = itemView.findViewById(R.id.TotalNumberPending5);
            selectedHousePending5 = itemView.findViewById(R.id.SelectedHousePending5);
            approvedbtn5 = itemView.findViewById(R.id.approvedbtn5);
            rejectdbtn5 = itemView.findViewById(R.id.rejectdbtn5);
            AmountTextTH5 = itemView.findViewById(R.id.AmountTextTH5);


        }
    }
}