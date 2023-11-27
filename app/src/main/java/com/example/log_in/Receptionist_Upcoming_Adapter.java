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

public class Receptionist_Upcoming_Adapter extends RecyclerView.Adapter< Receptionist_Upcoming_Adapter.MyViewHolder> {
    Context context;
    ArrayList<User> userArrayList;
    private FirebaseFirestore db;

    public  Receptionist_Upcoming_Adapter(Context context, ArrayList<User> userArrayList, FirebaseFirestore firestore) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.db = firestore;
    }


    @NonNull
    @Override
    public Receptionist_Upcoming_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.receptionist_upcoming_item, parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Receptionist_Upcoming_Adapter.MyViewHolder holder, int position) {
        User UpcomingUsers_Recep = userArrayList.get(position);

        holder.MonthTextRecep_Upcoming.setText(UpcomingUsers_Recep.getReservedDate());
        holder.BahayRecepUpcoming_Text.setText(UpcomingUsers_Recep.getSelectedTour());
        holder.ArawRecepUpcoming_Text.setText(UpcomingUsers_Recep.getReservedDate());

    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView MonthTextRecep_Upcoming, BahayRecepUpcoming_Text, ArawRecepUpcoming_Text;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MonthTextRecep_Upcoming = itemView.findViewById(R.id.MonthTextRecep_Upcoming);
            BahayRecepUpcoming_Text = itemView.findViewById(R.id.BahayRecepUpcoming_Text);
            ArawRecepUpcoming_Text = itemView.findViewById(R.id.ArawRecepUpcoming_Text);

        }
    }
}
