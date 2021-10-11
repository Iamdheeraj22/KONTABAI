package com.example.kontabai.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Classes.DriverSideAcceptRide;
import com.example.kontabai.R;

import java.util.ArrayList;

public class DriverSideAcceptedRideAdapter extends RecyclerView.Adapter<DriverSideAcceptedRideAdapter.DriverSideRideViewHolder>
{
    Context context;
    ArrayList<DriverSideAcceptRide> arrayList;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    public DriverSideAcceptedRideAdapter(Context context, ArrayList<DriverSideAcceptRide> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DriverSideRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view= LayoutInflater.from(context).inflate(R.layout.driver_side_accepted_ride,null,false);
        return new DriverSideRideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverSideRideViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DriverSideAcceptRide driverSideAcceptRide =arrayList.get(position);
        holder.location.setText(driverSideAcceptRide.getLocation());
        holder.completeRide.setOnClickListener(v -> {
            AlertDialog alertDialog=new AlertDialog.Builder(context,R.style.verification_done).create();
            View view=LayoutInflater.from(context).inflate(R.layout.delete_item_alert_box,null,false);
            alertDialog.setView(view);
            alertDialog.show();
            TextView btnYes,btnNo;
            btnYes=view.findViewById(R.id.yesButton);
            btnNo=view.findViewById(R.id.noButton);
            btnYes.setOnClickListener(v12 -> {
                arrayList.remove(position);
                notifyItemRemoved(position);
                alertDialog.dismiss();
            });
            btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class DriverSideRideViewHolder extends RecyclerView.ViewHolder
    {
        TextView location,completeRide;
        public DriverSideRideViewHolder(@NonNull View itemView) {
            super(itemView);
            location=itemView.findViewById(R.id.driverSideLocation);
            completeRide=itemView.findViewById(R.id.completeRide);
        }
    }
}
