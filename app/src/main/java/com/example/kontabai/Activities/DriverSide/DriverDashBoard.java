package com.example.kontabai.Activities.DriverSide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Activities.RegistrationActivity;
import com.example.kontabai.Activities.UserSide.UserSideActivity;
import com.example.kontabai.Adapters.DriverSideAcceptRideAdapter;
import com.example.kontabai.Adapters.DriverSidePendingRideAdapter;
import com.example.kontabai.Classes.RideModel;
import com.example.kontabai.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DriverDashBoard extends AppCompatActivity {
    RecyclerView rv_pending, rv_accepted;
    RelativeLayout rl_pending, rl_accepted;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    DriverSideAcceptRideAdapter driverSideAcceptRideAdapter;
    TextView tv_accepted, tv_pending, tv_logout;
    ArrayList<RideModel> pendingArrayList;
    ArrayList<RideModel> acceptArrayList;
    ItemTouchHelper itemTouchHelper1, itemTouchHelper2;
    DatabaseReference requestRef;
    double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentDriverId;
    private boolean asc=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);
        initViews();

        setListener();
        makeSelection("pending");
        tv_logout.setOnClickListener(v ->
                logOut());
    }

    private void logOut() {
        AlertDialog alertDialog = new AlertDialog.Builder(DriverDashBoard.this, R.style.verification_done).create();
        View view = LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.delete_item_alert_box, null, false);
        alertDialog.setView(view);
        alertDialog.show();
        alertDialog.setCancelable(true);
        TextView heading = view.findViewById(R.id.textHeading);
        heading.setText("Are you sure you want to logout ?");
        TextView yesButton = view.findViewById(R.id.yesButton);
        TextView noButton = view.findViewById(R.id.noButton);
        yesButton.setOnClickListener(v1 -> {
            //yesButton.setBackgroundResource(R.drawable.screen_background_2);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DriverDashBoard.this, RegistrationActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                alertDialog.dismiss();
            }, 3000);
        });
        noButton.setOnClickListener(v1 -> {
            //noButton.setBackgroundResource(R.drawable.screen_background_2);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                alertDialog.dismiss();
                noButton.setBackgroundColor(Color.WHITE);
            }, 2000);
        });
    }

    private void setListener() {
        rl_pending.setOnClickListener(v -> makeSelection("pending"));
        rl_accepted.setOnClickListener(v -> makeSelection("accepted"));
    }

    private void makeSelection(String type) {
        tv_accepted.setTextColor(getResources().getColor(R.color.black));
        tv_pending.setTextColor(getResources().getColor(R.color.black));
        rl_accepted.setBackground(null);
        rl_pending.setBackground(null);
        if (type.equalsIgnoreCase("accepted")) {
            rv_pending.setVisibility(View.GONE);
            rv_accepted.setVisibility(View.VISIBLE);
            tv_accepted.setTextColor(getResources().getColor(R.color.white));
            rl_accepted.setBackgroundResource(R.drawable.screen_background_1);
        } else {
            rv_pending.setVisibility(View.VISIBLE);
            rv_accepted.setVisibility(View.GONE);
            tv_pending.setTextColor(getResources().getColor(R.color.white));
            rl_pending.setBackgroundResource(R.drawable.screen_background_1);
        }
        setStaticData(type);
    }

    private void initViews() {
        rl_accepted = findViewById(R.id.rl_accepted);
        rl_pending = findViewById(R.id.rl_pending);
        rv_pending = findViewById(R.id.view_pager);
        rv_accepted = findViewById(R.id.view_pager2);
        tv_accepted = findViewById(R.id.tv_accepted);
        tv_pending = findViewById(R.id.tv_pending);
        tv_logout = findViewById(R.id.driver_logout_button);
        itemTouchHelper1 = new ItemTouchHelper(simpleCallback1);
        itemTouchHelper1.attachToRecyclerView(rv_pending);
        requestRef = FirebaseDatabase.getInstance().getReference().child("requests");
        itemTouchHelper2 = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper2.attachToRecyclerView(rv_accepted);
        currentDriverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void setStaticData(String type) {
        if (type.equalsIgnoreCase("pending")) {
            getAllPendingRequest();
        }
        if (type.equalsIgnoreCase("Accepted")) {
            getAllAcceptRequest();
        }
    }

    //String deletedMovie = null;
    ItemTouchHelper.SimpleCallback simpleCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            RideModel rideModel = pendingArrayList.get(position);
            Log.d("myRecycleid", "pos " + pendingArrayList.get(position).getRequest_order());
            if (direction == ItemTouchHelper.LEFT) {
                AlertDialog alertDialog = new AlertDialog.Builder(DriverDashBoard.this, R.style.verification_done).create();
                View view = LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.accept_request_dialogbox, null, false);
                alertDialog.setView(view);
                alertDialog.show();
                alertDialog.setCancelable(false);
                TextView textView = view.findViewById(R.id.pendingRequestButton);
                textView.setOnClickListener(v -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("requests").child(rideModel.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                databaseReference.child("requests").child(rideModel.getId()).removeValue();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("request_order", String.valueOf(rideModel.getRequest_order()));
                                hashMap.put("date", rideModel.getDate());
                                hashMap.put("status", "Accepted");
                                hashMap.put("id", rideModel.getId());
                                hashMap.put("user_id", rideModel.getUser_id());
                                hashMap.put("pickup_address", rideModel.getPickup_address());
                                databaseReference.child("driverRequest").child(currentDriverId).child(rideModel.getId()).setValue(hashMap);
                                databaseReference.child("userRequest").child(rideModel.getUser_id()).child(rideModel.getId()).child("status").setValue("Accepted");
                                driverSidePendingRideAdapter.notifyItemRemoved(position);
                                pendingArrayList.remove(position);
//                                getAllPendngiRequest();
//                                rv_pending.setVisibility(View.GONE);
//                                rv_accepted.setVisibility(View.VISIBLE);
//                                tv_accepted.setTextColor(getResources().getColor(R.color.white));
//                                rl_accepted.setBackgroundResource(R.drawable.screen_background_1);
                                alertDialog.dismiss();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DriverDashBoard.this, "Failed :-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
                });
            }
        }
    };
    String deletedMovie2 = null;
    ItemTouchHelper.SimpleCallback simpleCallback2 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            final int position = viewHolder.getAdapterPosition();
            RideModel rideModel1 = acceptArrayList.get(position);
            String status = rideModel1.getStatus();
            if (status.equalsIgnoreCase("Completed")) {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, 0);
            }
            return super.getMovementFlags(recyclerView, viewHolder);
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            RideModel rideModel1 = acceptArrayList.get(position);
            if (direction == ItemTouchHelper.LEFT) {
                deletedMovie2 = String.valueOf(acceptArrayList.get(position));
                AlertDialog alertDialog = new AlertDialog.Builder(DriverDashBoard.this, R.style.verification_done).create();
                View view = LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.delete_item_alert_box, null, false);
                alertDialog.setView(view);
                alertDialog.show();
                alertDialog.setCancelable(false);
                TextView heading = view.findViewById(R.id.textHeading);
                heading.setText("Are you sure the Job is completed ?");
                TextView yesButton = view.findViewById(R.id.yesButton);
                TextView noButton = view.findViewById(R.id.noButton);
                yesButton.setOnClickListener(v -> {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        String userId = rideModel1.getUser_id();
                        DatabaseReference changeRef = FirebaseDatabase.getInstance().getReference();
                        changeRef.child("driverRequest").child(currentDriverId).child(rideModel1.getId()).child("status").setValue("Completed");
                        changeRef.child("userRequest").child(userId).child(rideModel1.getId()).child("status").setValue("Completed");
                        alertDialog.dismiss();
                    }, 500);
                });
                noButton.setOnClickListener(v -> {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        alertDialog.dismiss();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("userRequest").child(rideModel1.getUser_id()).child(rideModel1.getId()).child("status").setValue("Accepted");
                        databaseReference.child("driverRequest").child(currentDriverId).child(rideModel1.getId()).child("status").setValue("Accepted");
                        driverSideAcceptRideAdapter.notifyItemChanged(position);
                        noButton.setBackgroundResource(R.color.white);
                    }, 500);
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        rv_accepted.setVisibility(View.GONE);
        rv_pending.setVisibility(View.VISIBLE);
        updateUserLocation();
        getAllPendingRequest();
        getAllAcceptRequest();
    }

    private void updateUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child("latitude").setValue(latitude);
                databaseReference.child("longitude").setValue(longitude);
            }
        });
    }

    private void getAllPendingRequest() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    pendingArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Object status = dataSnapshot.child("status").getValue();
                        if (status != null) {
                            if (status.toString().equalsIgnoreCase("Pending")) {
                                RideModel rideModel = dataSnapshot.getValue(RideModel.class);
                                pendingArrayList.add(rideModel);
                            }
                        }
                    }
                    Collections.reverse(pendingArrayList);
                    driverSidePendingRideAdapter = new DriverSidePendingRideAdapter(pendingArrayList, DriverDashBoard.this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DriverDashBoard.this);
                    rv_pending.setLayoutManager(layoutManager);
                    rv_pending.setAdapter(driverSidePendingRideAdapter);
                    driverSidePendingRideAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverDashBoard.this, "Failed :- " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllAcceptRequest() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("driverRequest").child(currentDriverId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                acceptArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RideModel rideModel = dataSnapshot.getValue(RideModel.class);
                    acceptArrayList.add(rideModel);
                }
                Collections.reverse(acceptArrayList);
                driverSideAcceptRideAdapter = new DriverSideAcceptRideAdapter(acceptArrayList, DriverDashBoard.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DriverDashBoard.this);
                rv_accepted.setLayoutManager(layoutManager);
                rv_accepted.setAdapter(driverSideAcceptRideAdapter);
                //sortData(asc, acceptArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverDashBoard.this, "Failed :-" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
