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

import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;

import java.util.ArrayList;

public class DriverSidePendingRideAdapter extends RecyclerView.Adapter<DriverSidePendingRideAdapter.PendingViewHolder>
{
    Context context;
    ArrayList<DriverSideRideModel> arrayList;

    public DriverSidePendingRideAdapter(Context context, ArrayList<DriverSideRideModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_ride,null,false);
        return new PendingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
        DriverSideRideModel userSideRideModel = arrayList.get(position);
        String pickLocation= userSideRideModel.getLocation();
        String date= userSideRideModel.getDate();
        String id= userSideRideModel.getId();
        String status= userSideRideModel.getStatus();

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
        holder.location.setText("Pickup Location: "+pickLocation);
        holder.Id.setText("Ride#"+id);
        holder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class PendingViewHolder extends RecyclerView.ViewHolder
    {
        TextView location,Id,date,status;
        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
            location=itemView.findViewById(R.id.rideLocation);
            Id=itemView.findViewById(R.id.rideId);
            date=itemView.findViewById(R.id.rideDateTime);
            status=itemView.findViewById(R.id.rideStatus);
        }
    }
}
