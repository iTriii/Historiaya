package com.example.log_in;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int USER_TYPE = 0;
    public  final int HISTORY_TYPE = 1;
    public final int UPCOMING_TYPE = 2;

    private Context context;
    public ArrayList<User> userArrayList;
    public ArrayList<historyList> historyList;
    public ArrayList<upcomingList> upcomingList;

    public MyAdapter(Context context, ArrayList<User> userArrayList, ArrayList<historyList> historyList, ArrayList<upcomingList> upcomingList) {
        this.context = context;
        this.userArrayList = userArrayList;
        this.historyList = historyList;
        this.upcomingList = upcomingList;
    }

    public MyAdapter(TourismHeadAdmin context, ArrayList<User> userArrayList) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == USER_TYPE) {
            v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            return new UserViewHolder(v);
        } else if (viewType == HISTORY_TYPE) {
            v = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
            return new HistoryViewHolder(v);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.upcoming_item, parent, false);
            return new UpcomingViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == USER_TYPE) {
            ((UserViewHolder) holder).bindUser(userArrayList.get(position));
        } else if (holder.getItemViewType() == HISTORY_TYPE) {
            ((HistoryViewHolder) holder).bindHistory(historyList.get(position - userArrayList.size()));
        } else {
            ((UpcomingViewHolder) holder).bindUpcoming(upcomingList.get(position - userArrayList.size() - historyList.size()));
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size() + historyList.size() + upcomingList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < userArrayList.size()) {
            return USER_TYPE;
        } else if (position < userArrayList.size() + historyList.size()) {
            return HISTORY_TYPE;
        } else {
            return UPCOMING_TYPE;
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView PendingMonthText, BahayPendingText, ArawPendingText, bookebyNamePending, TotalNumberPending, SelectedHousePending;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize views for user item
            PendingMonthText = itemView.findViewById(R.id.PendingMonthText);
            BahayPendingText = itemView.findViewById(R.id.BahayPendingText);
            ArawPendingText = itemView.findViewById(R.id.ArawPendingText);
            bookebyNamePending = itemView.findViewById(R.id.bookebyNamePending);
            TotalNumberPending = itemView.findViewById(R.id.TotalNumberPending);
            SelectedHousePending = itemView.findViewById(R.id.SelectedHousePending);
        }

        public void bindUser(User user) {
            // bind user data to views
            PendingMonthText.setText(user.getReservedDate());
            BahayPendingText.setText(user.getSelectedTour());
            ArawPendingText.setText(user.getReservedDate());
            bookebyNamePending.setText(user.getEmail());
            TotalNumberPending.setText(user.getSelectedTouristNum());
            SelectedHousePending.setText(user.getSelectedTour());
        }
    }

    //history
    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView bookebyNameHistory, ArawHistoText, BahayHistoText, MontHistoText, TotalNumberHistory, SelectedHouseHistory;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            SelectedHouseHistory = itemView.findViewById(R.id.SelectedHouseHistory); // HISTORY
            TotalNumberHistory = itemView.findViewById(R.id.TotalNumberHistory);
            MontHistoText = itemView.findViewById(R.id.MontHistoText);
            BahayHistoText = itemView.findViewById(R.id.BahayHistoText);
            ArawHistoText = itemView.findViewById(R.id.ArawHistoText);
            bookebyNameHistory = itemView.findViewById(R.id.bookebyNameHistory);
        }

        public void bindHistory(historyList history) {
            User user = historyList.get(getAdapterPosition() - userArrayList.size());

            SelectedHouseHistory.setText(user.getSelectedTour()); //HISTORY
            TotalNumberHistory.setText(user.getSelectedTouristNum());
            MontHistoText.setText(user.getReservedDate());
            BahayHistoText.setText(user.getSelectedTour());
            ArawHistoText.setText(user.getReservedDate());
            bookebyNameHistory.setText(user.getEmail());
        }
    }

    public static class UpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView MonthTourHeadText, BahayTourHeadText, ArawTourHeadText;

        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            MonthTourHeadText = itemView.findViewById(R.id.MonthTourHeadText); //UPCOMING
            BahayTourHeadText = itemView.findViewById(R.id.BahayTourHeadText);
            ArawTourHeadText = itemView.findViewById(R.id.ArawTourHeadText);
        }

        public void bindUpcoming(User upcoming) {
            MonthTourHeadText.setText(upcoming.getReservedDate());
            BahayTourHeadText.setText(upcoming.getSelectedTour());
            ArawTourHeadText.setText(upcoming.getReservedDate());
        }
    }
}
