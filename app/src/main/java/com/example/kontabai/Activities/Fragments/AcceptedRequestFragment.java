package com.example.kontabai.Activities.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kontabai.Adapters.DriverSideAcceptedRideAdapter;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;

import java.util.ArrayList;

public class AcceptedRequestFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<DriverSideRideModel> driverSideAcceptedRideModels;
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
        driverSideAcceptedRideModels =new ArrayList<>();
        driverSideAcceptedRideAdapter =new DriverSideAcceptedRideAdapter(getContext(), driverSideAcceptedRideModels);
        setStaticData();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(driverSideAcceptedRideAdapter);
    }
    private void setStaticData(){
        driverSideAcceptedRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));
        driverSideAcceptedRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));
        driverSideAcceptedRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));
    }
}