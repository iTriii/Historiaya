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
    private static final int TYPE_UPCOMING = 1;
    private static final int TYPE_HISTORY = 2;
    private static final int TYPE_PENDING = 3;

    private Context context;
    private ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the view type based on your logic
        // For example, you can check a property of the User object

        // Replace the following condition with your actual logic
        if (position % 3 == 0) {
            return TYPE_UPCOMING;
        } else if (position % 3 == 1) {
            return TYPE_HISTORY;
        } else {
            return TYPE_PENDING;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        switch (viewType) {
            case TYPE_UPCOMING:
                view = inflater.inflate(R.layout.upcoming_item, parent, false);
                return new UpcomingViewHolder(view);
            case TYPE_HISTORY:
                view = inflater.inflate(R.layout.history_item, parent, false);
                return new HistoryViewHolder(view);
            case TYPE_PENDING:
                view = inflater.inflate(R.layout.item, parent, false);
                return new PendingViewHolder(view);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = userArrayList.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_UPCOMING:
                // Bind data for the UpcomingViewHolder
                UpcomingViewHolder upcomingViewHolder = (UpcomingViewHolder) holder;
                upcomingViewHolder.bind(user);
                break;
            case TYPE_HISTORY:
                // Bind data for the HistoryViewHolder
                HistoryViewHolder historyViewHolder = (HistoryViewHolder) holder;
                historyViewHolder.bind(user);
                break;
            case TYPE_PENDING:
                // Bind data for the PendingViewHolder
                PendingViewHolder pendingViewHolder = (PendingViewHolder) holder;
                pendingViewHolder.bind(user);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + holder.getItemViewType());
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    // Define your ViewHolders here
    static class UpcomingViewHolder extends RecyclerView.ViewHolder {
        TextView MonthTourHeadText, BahayTourHeadText, ArawTourHeadText;

        UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            MonthTourHeadText = itemView.findViewById(R.id.MonthTourHeadText); //UPCOMING
            BahayTourHeadText = itemView.findViewById(R.id.BahayTourHeadText);
            ArawTourHeadText = itemView.findViewById(R.id.ArawTourHeadText);
        }

        void bind(User user) {
            // Bind data for UpcomingViewHolder
            MonthTourHeadText.setText(user.getReservedDate());
            BahayTourHeadText.setText(user.getSelectedTour());
            ArawTourHeadText.setText(user.getReservedDate());
        }
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView bookebyNameHistory, ArawHistoText, BahayHistoText, MontHistoText, TotalNumberHistory, SelectedHouseHistory;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            SelectedHouseHistory = itemView.findViewById(R.id.SelectedHouseHistory); // HISTORY
            TotalNumberHistory = itemView.findViewById(R.id.TotalNumberHistory);
            MontHistoText = itemView.findViewById(R.id.MontHistoText);
            BahayHistoText = itemView.findViewById(R.id.BahayHistoText);
            ArawHistoText = itemView.findViewById(R.id.ArawHistoText);
            bookebyNameHistory = itemView.findViewById(R.id.bookebyNameHistory);
        }


        void bind(User user) {
            // Bind data for HistoryViewHolder
            SelectedHouseHistory.setText(user.getSelectedTour()); //HISTORY
            TotalNumberHistory.setText(user.getSelectedTouristNum());
            MontHistoText.setText(user.getReservedDate());
            BahayHistoText.setText(user.getSelectedTour());
            ArawHistoText.setText(user.getReservedDate());
            bookebyNameHistory.setText(user.getEmail());
        }
    }

    static class PendingViewHolder extends RecyclerView.ViewHolder {
        TextView PendingMonthText, BahayPendingText, ArawPendingText, bookebyNamePending, TotalNumberPending, SelectedHousePending;

        PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            PendingMonthText = itemView.findViewById(R.id.PendingMonthText);
            BahayPendingText = itemView.findViewById(R.id.BahayPendingText);
            ArawPendingText = itemView.findViewById(R.id.ArawPendingText);
            bookebyNamePending = itemView.findViewById(R.id.bookebyNamePending);
            TotalNumberPending = itemView.findViewById(R.id.TotalNumberPending);
            SelectedHousePending = itemView.findViewById(R.id.SelectedHousePending);
        }

        void bind(User user) {
            // Bind data for PendingViewHolder
            PendingMonthText.setText(user.getReservedDate());
            BahayPendingText.setText(user.getSelectedTour());
            ArawPendingText.setText(user.getReservedDate());
            bookebyNamePending.setText(user.getEmail());
            TotalNumberPending.setText(user.getSelectedTouristNum());
            SelectedHousePending.setText(user.getSelectedTour());
        }
    }
}
