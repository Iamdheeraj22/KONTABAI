package com.example.kontabai.Adapters;

import android.annotation.SuppressLint;
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

public class DriverSidePendingRideAdapter extends FirebaseRecyclerAdapter<DriverSideRideModel, DriverSidePendingRideAdapter.DriverViewModel>
{
    public DriverSidePendingRideAdapter(@NonNull FirebaseRecyclerOptions<DriverSideRideModel> options) {
        super(options);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DriverViewModel holder, int position, @NonNull DriverSideRideModel model) {
        String status=model.getStatus();
        if(status.equalsIgnoreCase("pending")){
            holder.requestStatus.setText("Pending");
            holder.requestStatus.setTextColor(Color.RED);}
        else if(status.equalsIgnoreCase("accepted")){
            holder.requestStatus.setText("Rejected");
            holder.requestStatus.setTextColor(Color.RED);
        }else if (status.equalsIgnoreCase("rejected")){
            holder.requestStatus.setText("Accepted");
            holder.requestStatus.setTextColor(Color.GREEN);
        }
        holder.requestLocation.setText("Pickup Location: "+model.getPickup_address());
        holder.requestOrder.setText("RIDE#"+model.getRequest_order());
        holder.requestTime.setText(model.getDate());
        holder.requestStatus.setText(model.getStatus());
    }

    @NonNull
    @Override
    public DriverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ride,null,false);
        return new DriverViewModel(view);
    }

    static class DriverViewModel extends RecyclerView.ViewHolder
    {
        TextView requestOrder,requestTime,requestLocation,requestStatus;
        public DriverViewModel(@NonNull View itemView) {
            super(itemView);
            requestLocation=itemView.findViewById(R.id.rideLocation);
            requestOrder=itemView.findViewById(R.id.rideId);
            requestTime=itemView.findViewById(R.id.rideDateTime);
            requestStatus=itemView.findViewById(R.id.rideStatus);
        }
    }
}
