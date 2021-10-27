package com.example.kontabai.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;

import java.util.ArrayList;

public class DriverSidePendingRideAdapter extends RecyclerView.Adapter<DriverSidePendingRideAdapter.DriverViewModel> {
    ArrayList<DriverSideRideModel> arrayList;
    Context context;

    public DriverSidePendingRideAdapter(ArrayList<DriverSideRideModel> arrayList,Context context) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverViewModel holder, int position) {
        DriverSideRideModel model = arrayList.get(position);
        if (model.getStatus().equalsIgnoreCase("pending")) {
            holder.requestStatus.setText("Pending");
            holder.requestStatus.setTextColor(Color.RED);
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
        holder.requestTime.setText(model.getDate());
        holder.requestStatus.setText(model.getStatus());
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