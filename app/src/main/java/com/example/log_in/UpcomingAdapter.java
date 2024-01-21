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

public class UpcomingAdapter  extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder> {

    Context context;
    ArrayList<User>  upcomingList;
    private final FirebaseFirestore db;

    public UpcomingAdapter(Context context, ArrayList<User> upcomingList, FirebaseFirestore db) {
        this.context = context;
        this. upcomingList = upcomingList;
        this.db = db;
    }



    @NonNull
    @Override
    public UpcomingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.upcoming_item, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.MyViewHolder holder, int position) {
        User UpcomingUsers =  upcomingList.get(position);

        holder.MonthTourHeadText.setText(UpcomingUsers.getReservedDate1());
        holder.BahayTourHeadText.setText(UpcomingUsers.getSelectedTour1());
        holder.ArawTourHeadText.setText(UpcomingUsers.getReservedDate1());

    }

    @Override
    public int getItemCount() {
        return  upcomingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MonthTourHeadText, BahayTourHeadText, ArawTourHeadText,TotalTourHeadText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthTourHeadText = itemView.findViewById(R.id.MonthTourHeadText);
            BahayTourHeadText = itemView.findViewById(R.id.BahayTourHeadText);
            ArawTourHeadText= itemView.findViewById(R.id.ArawTourHeadText);


        }
    }
}