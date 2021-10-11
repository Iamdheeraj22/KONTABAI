package com.example.kontabai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Classes.DriverSideAcceptRide;
import com.example.kontabai.Classes.DriverSidePendingRide;
import com.example.kontabai.R;

import java.util.ArrayList;

public class DriverSidePendingRideAdapter extends RecyclerView.Adapter<DriverSidePendingRideAdapter.PendingViewHolder>
{
    Context context;
    ArrayList<DriverSidePendingRide> arrayList;

    public DriverSidePendingRideAdapter(Context context, ArrayList<DriverSidePendingRide> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.driver_side_pending_ride,null,false);
        return new PendingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class PendingViewHolder extends RecyclerView.ViewHolder
    {
        public PendingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
