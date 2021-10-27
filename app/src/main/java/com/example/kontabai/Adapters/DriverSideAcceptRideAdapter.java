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

public class DriverSideAcceptRideAdapter extends RecyclerView.Adapter<DriverSideAcceptRideAdapter.DriverViewModel> {
    ArrayList<DriverSideRideModel> arrayList;
    Context context;

    public DriverSideAcceptRideAdapter(ArrayList<DriverSideRideModel> arrayList, Context context) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverViewModel holder, int position) {
        DriverSideRideModel model = arrayList.get(position);
        if (model.getStatus().equalsIgnoreCase("completed")) {
            holder.requestStatus.setText("completed");
            holder.requestStatus.setTextColor(Color.GREEN);
        } else if (model.getStatus().equalsIgnoreCase("accepted")) {
            holder.requestStatus.setText("Accepted");
            holder.requestStatus.setTextColor(Color.GREEN);
        } else if (model.getStatus().equalsIgnoreCase("rejected")) {
            holder.requestStatus.setText("Rejected");
            holder.requestStatus.setTextColor(Color.RED);
        }
        holder.requestLocation.setText("Pickup Location: " + model.getPickup_address());
        holder.requestOrder.setText("RIDE#" + model.getRequest_order());
        holder.requestTime.setText(model.getDate());
    }

    @NonNull
    @Override
    public DriverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ride, null, false);
        return new DriverViewModel(view);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class DriverViewModel extends RecyclerView.ViewHolder {
        TextView requestOrder, requestTime, requestLocation, requestStatus;

        public DriverViewModel(@NonNull View itemView) {
            super(itemView);
            requestLocation = itemView.findViewById(R.id.rideLocation);
            requestOrder = itemView.findViewById(R.id.rideId);
            requestTime = itemView.findViewById(R.id.rideDateTime);
            requestStatus = itemView.findViewById(R.id.rideStatus);
        }
    }
}