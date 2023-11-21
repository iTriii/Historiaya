package com.example.log_in.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.log_in.R;
import com.example.log_in.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView firstName, lastName, userEmail;
        private ImageView icon;

        UserViewHolder(View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.textName);
            userEmail = itemView.findViewById(R.id.textEmail);
            icon = itemView.findViewById(R.id.icon);
        }

        void setUserData(User user) {
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            userEmail.setText(user.getUserEmail());

            String iconBase64 = user.getImageBase64();
            if (!TextUtils.isEmpty(iconBase64)) {
                icon.setImageBitmap(getUserImage(iconBase64));
            } else {
                // Handle the case when the image is missing or empty
                // You can set a default image or show an error message.
                // For now, I'll show a Toast message for demonstration purposes.
                Toast.makeText(itemView.getContext(), "User image is missing or empty", Toast.LENGTH_SHORT).show();
            }
        }

        private Bitmap getUserImage(String iconBase64) {
            byte[] bytes = Base64.decode(iconBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }
}
