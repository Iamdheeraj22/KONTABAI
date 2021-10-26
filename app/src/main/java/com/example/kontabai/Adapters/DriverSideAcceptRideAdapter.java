package com.example.kontabai.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

;

public class DriverSideAcceptRideAdapter extends FirebaseRecyclerAdapter<DriverSideRideModel, DriverSideAcceptRideAdapter.DriverViewModel>
{

    public DriverSideAcceptRideAdapter(@NonNull FirebaseRecyclerOptions<DriverSideRideModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DriverViewModel holder, int position, @NonNull DriverSideRideModel model) {

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
