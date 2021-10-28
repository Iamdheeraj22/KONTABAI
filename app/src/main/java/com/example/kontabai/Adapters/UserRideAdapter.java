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

import com.example.kontabai.Classes.RideModel;
import com.example.kontabai.R;

import java.util.ArrayList;

public class UserRideAdapter extends RecyclerView.Adapter<UserRideAdapter.UserRideViewHolder>
{
    private Context context;
    private ArrayList<RideModel> arrayList;


    public UserRideAdapter(Context context, ArrayList<RideModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public UserRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.user_ride,null,false);
        return new UserRideViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserRideViewHolder holder, int position) {
        RideModel model=arrayList.get(position);
        String status=model.getStatus();
        //position=position+1;
        if(status.equalsIgnoreCase("pending")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.RED);
        }else if(status.equalsIgnoreCase("accepted") || status.equalsIgnoreCase("completed")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.GREEN);
        }else if(status.equalsIgnoreCase("rejected")){
            holder.requestStatus.setText(status);
            holder.requestStatus.setTextColor(Color.RED);
        }
        holder.requestLocation.setText("Pickup Location: "+model.getPickup_address());
        holder.requestOrder.setText("RIDE#"+model.getId());
        holder.requestTime.setText(model.getDate());
        holder.requestStatus.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
