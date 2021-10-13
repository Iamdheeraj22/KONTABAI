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

;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;

import java.util.ArrayList;

public class DriverSideAcceptedRideAdapter extends RecyclerView.Adapter<DriverSideAcceptedRideAdapter.DriverSideRideViewHolder>
{
    Context context;
    ArrayList<DriverSideRideModel> arrayList;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    public DriverSideAcceptedRideAdapter(Context context, ArrayList<DriverSideRideModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DriverSideRideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view= LayoutInflater.from(context).inflate(R.layout.user_ride,null,false);
        return new DriverSideRideViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverSideRideViewHolder holder, @SuppressLint("RecyclerView") int position) {
//            int newPosition = holder. getAdapterPosition();
//            if(newPosition!=-1) {
//                DriverSideAcceptedRideModel driverSideAcceptRide = arrayList.get(position);
//                holder.location.setText(driverSideAcceptRide.getLocation());
//                holder.completeRide.setOnClickListener(v -> {
//                    AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.verification_done).create();
//                    View view = LayoutInflater.from(context).inflate(R.layout.delete_item_alert_box, null, false);
//                    alertDialog.setView(view);
//                    alertDialog.show();
//                    TextView btnYes, btnNo;
//                    btnYes = view.findViewById(R.id.yesButton);
//                    btnNo = view.findViewById(R.id.noButton);
//                    btnYes.setOnClickListener(v12 -> {
//                        arrayList.remove(newPosition);
//                        notifyItemRemoved(newPosition);
//                        notifyItemRangeChanged(newPosition, arrayList.size());
//                        alertDialog.dismiss();
//                    });
//                    btnNo.setOnClickListener(v1 -> alertDialog.dismiss());
//                });
//                holder.itemView.setOnClickListener(v -> {
//                    Intent intent = new Intent(context, UserRideDetails.class);
//                    intent.putExtra("name", "Dheeraj");
//                    intent.putExtra("number", "8290845089");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                });
//            }

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

    static class DriverSideRideViewHolder extends RecyclerView.ViewHolder
    {
        TextView location,Id,date,status;
        public DriverSideRideViewHolder(@NonNull View itemView) {
            super(itemView);
           location=itemView.findViewById(R.id.rideLocation);
           Id=itemView.findViewById(R.id.rideId);
           date=itemView.findViewById(R.id.rideDateTime);
           status=itemView.findViewById(R.id.rideStatus);
        }
    }
}
