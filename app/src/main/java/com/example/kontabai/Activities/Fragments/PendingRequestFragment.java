package com.example.kontabai.Activities.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kontabai.Adapters.DriverSidePendingRideAdapter;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;

import java.util.ArrayList;

public class PendingRequestFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<DriverSideRideModel> arrayList;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    @Override   
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pending_request, container, false);
        initViews(view);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }
    String deletedMovie = null;
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.RIGHT) {
                deletedMovie = String.valueOf(arrayList.get(position));
                AlertDialog alertDialog=new AlertDialog.Builder(getContext(),R.style.verification_done).create();
                View view=LayoutInflater.from(getContext()).inflate(R.layout.accept_request_dialogbox,null,false);
                alertDialog.setView(view);
                alertDialog.show();
                TextView textView=view.findViewById(R.id.pendingRequestButton);
                textView.setOnClickListener(v -> {
                    arrayList.remove(position);
                    driverSidePendingRideAdapter.notifyItemRemoved(position);
                    alertDialog.dismiss();
                });
            }
        }
    };
    private void initViews(View view)
    {
        recyclerView=view.findViewById(R.id.pendingRecyclerView);
        arrayList=new ArrayList<>();
        driverSidePendingRideAdapter=new DriverSidePendingRideAdapter(getContext(),arrayList);
        setStaticData();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(driverSidePendingRideAdapter);
    }

    private void setStaticData(){
        arrayList.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","Pending"));
        arrayList.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","Pending"));
        arrayList.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","Pending"));

    }
}