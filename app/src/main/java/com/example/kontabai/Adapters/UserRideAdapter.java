package com.example.kontabai.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Classes.UserRide;
import com.example.kontabai.R;

import java.util.ArrayList;

public class UserRideAdapter extends RecyclerView.Adapter<UserRideAdapter.UserRideViewHolder>
{
    Context mContext;
    ArrayList<UserRide> userRides;

    public UserRideAdapter(Context mContext, ArrayList<UserRide> userRides) {
        this.mContext = mContext;
        this.userRides = userRides;
    }

    @NonNull
    @Override
    public UserRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_ride,null,false);
        return new UserRideViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserRideViewHolder holder, int position) {
        UserRide userRide= userRides.get(position);
        String pickLocation=userRide.getLocation();
        String date= userRide.getDate();
        String id=userRide.getId();
        String status=userRide.getStatus();

        switch (status) {
            case "Pending":
                holder.status.setText("Pending");
                holder.status.setTextColor(Color.RED);
                break;
            case "Rejected":
                holder.status.setText("Rejected");
                holder.status.setTextColor(Color.RED);
                break;
            case "Accepted":
                holder.status.setText("Accepted");
                holder.status.setTextColor(Color.GREEN);
                break;
        }
        holder.pickupLocation.setText("Pickup Location: "+pickLocation);
        holder.id.setText("Ride#"+id);
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return userRides.size();
    }

    static class UserRideViewHolder extends RecyclerView.ViewHolder
    {
        TextView pickupLocation,status,id,date;
        public UserRideViewHolder(@NonNull View itemView) {
            super(itemView);
            pickupLocation=itemView.findViewById(R.id.rideLocation);
            status=itemView.findViewById(R.id.rideStatus);
            id=itemView.findViewById(R.id.rideId);
            date=itemView.findViewById(R.id.rideDateTime);
        }
    }
}
