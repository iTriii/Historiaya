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

public class UpcomingAdapter  extends RecyclerView.Adapter<UpcomingAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> UpcomingUsersArrayList;
    private final FirebaseFirestore db;

    public UpcomingAdapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore db) {
        this.context = context;
        this.UpcomingUsersArrayList = userArrayList;
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
        User UpcomingUsers = UpcomingUsersArrayList.get(position);

        holder.MonthTourHeadText.setText(UpcomingUsers.getReservedDate());
        holder.BahayTourHeadText.setText(UpcomingUsers.getSelectedTour());
        holder.ArawTourHeadText.setText(UpcomingUsers.getReservedDate());

    }

    @Override
    public int getItemCount() {
        return UpcomingUsersArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MonthTourHeadText, BahayTourHeadText, ArawTourHeadText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthTourHeadText = itemView.findViewById(R.id.MonthTourHeadText);
            BahayTourHeadText = itemView.findViewById(R.id.BahayTourHeadText);
            ArawTourHeadText= itemView.findViewById(R.id.ArawTourHeadText);

        }
    }
}
