package com.example.kontabai.Activities.DriverSide;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kontabai.Activities.RegistrationActivity;
import com.example.kontabai.Adapters.DriverSidePendingRideAdapter;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverDashBoard extends AppCompatActivity {
    RecyclerView rv_pending, rv_accepted;
    RelativeLayout rl_pending, rl_accepted;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    TextView tv_accepted, tv_pending, tv_logout;
    ArrayList<DriverSideRideModel> arrayList;
    ItemTouchHelper itemTouchHelper1, itemTouchHelper2;
    DatabaseReference requestRef;
    String currentDriverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);
        initViews();

        setListener();
        makeSelection("pending");
        tv_logout.setOnClickListener(v -> startActivity(new Intent(DriverDashBoard.this, RegistrationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
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
        arrayList = new ArrayList<>();
        tv_logout = findViewById(R.id.driver_logout_button);
        itemTouchHelper1 = new ItemTouchHelper(simpleCallback1);
        itemTouchHelper1.attachToRecyclerView(rv_pending);
        requestRef= FirebaseDatabase.getInstance().getReference().child("requests");
        itemTouchHelper2 = new ItemTouchHelper(simpleCallback2);
        itemTouchHelper2.attachToRecyclerView(rv_accepted);
        currentDriverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DriverDashBoard.this, DriverSideProfileCreation.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setStaticData(String type) {
        arrayList.clear();
        if (type.equalsIgnoreCase("pending")) {
            FirebaseRecyclerOptions<DriverSideRideModel> options=
                    new FirebaseRecyclerOptions.Builder<DriverSideRideModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("requests"),DriverSideRideModel.class)
                    .build();
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
            driverSidePendingRideAdapter=new DriverSidePendingRideAdapter(options);
            rv_pending.setLayoutManager(layoutManager);
            driverSidePendingRideAdapter.startListening();
            rv_pending.setAdapter(driverSidePendingRideAdapter);
        } else {
            FirebaseRecyclerOptions<DriverSideRideModel> options=
                    new FirebaseRecyclerOptions.Builder<DriverSideRideModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid()),DriverSideRideModel.class)
                            .build();
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
            driverSidePendingRideAdapter=new DriverSidePendingRideAdapter(options);
            rv_accepted.setLayoutManager(layoutManager);
            rv_accepted.setAdapter(driverSidePendingRideAdapter);
            driverSidePendingRideAdapter.startListening();
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

            if (direction == ItemTouchHelper.LEFT) {
                //deletedMovie = String.valueOf(arrayList.get(position));
                AlertDialog alertDialog = new AlertDialog.Builder(DriverDashBoard.this, R.style.verification_done).create();
                View view = LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.accept_request_dialogbox, null, false);
                alertDialog.setView(view);
                alertDialog.show();
                alertDialog.setCancelable(false);
                TextView textView = view.findViewById(R.id.pendingRequestButton);
                textView.setOnClickListener(v -> {
                    //arrayList.remove(position);
                    String id=arrayList.get(position).getId();
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("requests").child(id);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                databaseReference.child("driverId").setValue(currentDriverId);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DriverDashBoard.this, "Failed :-"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    driverSidePendingRideAdapter.notifyItemRemoved(position);
                    alertDialog.dismiss();
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
            DriverSideRideModel driverSideRideModel1 = arrayList.get(position);
            String status = driverSideRideModel1.getStatus();
            if (status.equalsIgnoreCase("Completed")) {
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, 0);
            }
            return super.getMovementFlags(recyclerView, viewHolder);
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
//            DriverSideRideModel driverSideRideModel1 = arrayList.get(position);
//            String status = driverSideRideModel1.getStatus();

           if (direction == ItemTouchHelper.LEFT) {
                deletedMovie2 = String.valueOf(arrayList.get(position));
                AlertDialog alertDialog = new AlertDialog.Builder(DriverDashBoard.this, R.style.verification_done).create();
                View view = LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.delete_item_alert_box, null, false);
                alertDialog.setView(view);
                alertDialog.show();
                alertDialog.setCancelable(false);
                TextView heading=view.findViewById(R.id.textHeading);
                heading.setText("Are you sure the Job is completed ?");
                TextView yesButton = view.findViewById(R.id.yesButton);
                TextView noButton = view.findViewById(R.id.noButton);
                yesButton.setOnClickListener(v -> {
                    yesButton.setBackgroundResource(R.drawable.screen_background_2);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {

                        //arrayList.get(position).setStatus("Completed");
                        driverSidePendingRideAdapter.notifyItemChanged(position);
                        alertDialog.dismiss();
                    }, 500);
                });
                noButton.setOnClickListener(v -> {
                    noButton.setBackgroundResource(R.drawable.screen_background_2);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        alertDialog.dismiss();
                        //DriverSideRideModel driverSideRideModel = arrayList.get(position);
                        driverSidePendingRideAdapter.notifyItemChanged(position);
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
    }
}