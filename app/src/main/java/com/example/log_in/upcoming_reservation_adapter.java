package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
//FOR UPDATE ONLY

public class upcoming_reservation_adapter extends RecyclerView.Adapter<upcoming_reservation_adapter.MyViewHolder> {

    Context context;
    ArrayList<User>  userArrayList;
    private final FirebaseFirestore db;

    public upcoming_reservation_adapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore db) {
        this.context = context;
        this. userArrayList = userArrayList;
        this.db = db;
    }


    @NonNull
    @Override
    public upcoming_reservation_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.upcoming_reservation,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull upcoming_reservation_adapter.MyViewHolder holder, int position) {

        User upcoming_reservation_of_user = userArrayList.get(position);

        holder. MonthHouseManagerText.setText(upcoming_reservation_of_user.getReservedDate1());
        holder.BahayHouseManagerText.setText(upcoming_reservation_of_user.getSelectedTour1());
        holder.ArawHouseManagerText.setText(upcoming_reservation_of_user.getReservedDate1());
//        holder.AmountHouseManagerText.setText(String.valueOf(upcoming_reservation_of_user.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView MonthHouseManagerText, BahayHouseManagerText, ArawHouseManagerText,AmountHouseManagerText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthHouseManagerText = itemView.findViewById(R.id.MonthHouseManagerText);
            BahayHouseManagerText = itemView.findViewById(R.id.BahayHouseManagerText);
            ArawHouseManagerText = itemView.findViewById(R.id.ArawHouseManagerText);
//            AmountHouseManagerText = itemView.findViewById(R.id.AmountHouseManagerText);
        }
    }
}