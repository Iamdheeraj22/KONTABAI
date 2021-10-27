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

import com.example.kontabai.Classes.UserSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class UserRideAdapter extends FirebaseRecyclerAdapter<UserSideRideModel,UserRideAdapter.UserRideViewHolder>
{

    public UserRideAdapter(@NonNull FirebaseRecyclerOptions<UserSideRideModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull UserRideViewHolder holder, int position, @NonNull UserSideRideModel model) {
        String status=model.getStatus();
        if(status.equalsIgnoreCase("pending")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.RED);
        }else if(status.equalsIgnoreCase("accepted")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.GREEN);
        }else if(status.equalsIgnoreCase("rejected")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.RED);
        }
        holder.requestLocation.setText("Pickup Location: "+model.getPickup_address());
        holder.requestOrder.setText("RIDE#"+model.getCount());
        holder.requestTime.setText(model.getDate());
        holder.requestStatus.setText(model.getStatus());
    }

    @NonNull
    @Override
    public UserRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ride,null,false);
        return new UserRideViewHolder(view);
    }

    static class UserRideViewHolder extends RecyclerView.ViewHolder
    {
        TextView requestOrder,requestTime,requestLocation,requestStatus;
        public UserRideViewHolder(@NonNull View itemView) {
            super(itemView);
            requestLocation=itemView.findViewById(R.id.rideLocation);
            requestOrder=itemView.findViewById(R.id.rideId);
            requestTime=itemView.findViewById(R.id.rideDateTime);
            requestStatus=itemView.findViewById(R.id.rideStatus);
        }
    }
}
