package com.example.kontabai.Activities.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kontabai.Adapters.DriverSideAcceptedRideAdapter;
import com.example.kontabai.Classes.DriverSideAcceptRide;
import com.example.kontabai.R;

import java.util.ArrayList;

public class AcceptedRequestFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<DriverSideAcceptRide> driverSideAcceptRides;
    DriverSideAcceptedRideAdapter driverSideAcceptedRideAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_accepted_request, container, false);
        // Inflate the layout for this fragment
        initViews(view);
        return view;
    }
    private void initViews(View view)
    {
        recyclerView=view.findViewById(R.id.acceptedRecyclerView);
        driverSideAcceptRides =new ArrayList<>();
        driverSideAcceptedRideAdapter =new DriverSideAcceptedRideAdapter(getContext(), driverSideAcceptRides);
        setStaticData();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(driverSideAcceptedRideAdapter);
    }
    private void setStaticData(){
        driverSideAcceptRides.add(new DriverSideAcceptRide("Amritsar"));
        driverSideAcceptRides.add(new DriverSideAcceptRide("Delhi"));
        driverSideAcceptRides.add(new DriverSideAcceptRide("Chandigarh"));
    }
}