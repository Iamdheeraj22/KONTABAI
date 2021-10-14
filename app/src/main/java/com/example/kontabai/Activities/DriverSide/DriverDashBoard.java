package com.example.kontabai.Activities.DriverSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kontabai.Activities.Fragments.AcceptedRequestFragment;
import com.example.kontabai.Activities.Fragments.PendingRequestFragment;
import com.example.kontabai.Activities.RegistrationActivity;
import com.example.kontabai.Adapters.DriverSidePendingRideAdapter;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.Classes.ViewPageAdapter;
import com.example.kontabai.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DriverDashBoard extends AppCompatActivity {
    RecyclerView viewPager;
    ImageView backButtonImageView;
    ItemTouchHelper itemTouchHelper;
    RelativeLayout rl_pending,rl_accepted;
    DriverSidePendingRideAdapter driverSidePendingRideAdapter;
    TextView tv_accepted,tv_pending,tv_logout;
    ArrayList<DriverSideRideModel> driverSideRideModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);
        initViews();

        setListener();
        makeSelection("pending");
        backButtonImageView.setOnClickListener(v->{
            startActivity(new Intent(DriverDashBoard.this,DriverSideProfileCreation.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        tv_logout.setOnClickListener(v->{
            startActivity(new Intent(DriverDashBoard.this, RegistrationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        });
    }

    private void setListener()
    {
        rl_pending.setOnClickListener(v -> makeSelection("pending"));
        rl_accepted.setOnClickListener(v -> makeSelection("accepted"));
    }

    private void makeSelection(String type) {
        tv_accepted.setTextColor(getResources().getColor(R.color.black));
        tv_pending.setTextColor(getResources().getColor(R.color.black));
        rl_accepted.setBackground(null);
        rl_pending.setBackground(null);
        if(type.equalsIgnoreCase("accepted")){
            tv_accepted.setTextColor(getResources().getColor(R.color.white));
            rl_accepted.setBackgroundResource(R.drawable.screen_background);
        }else{
            tv_pending.setTextColor(getResources().getColor(R.color.white));
            rl_pending.setBackgroundResource(R.drawable.screen_background);
        }
        setStaticData(type);
    }

    private void initViews() {
        rl_accepted=findViewById(R.id.rl_accepted);
        rl_pending=findViewById(R.id.rl_pending);
        viewPager=findViewById(R.id.view_pager);
      tv_accepted=findViewById(R.id.tv_accepted);
        tv_pending=findViewById(R.id.tv_pending);
        driverSideRideModels=new ArrayList<>();
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(viewPager);
        tv_logout=findViewById(R.id.driver_logout_button);
        backButtonImageView=findViewById(R.id.backButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DriverDashBoard.this,DriverSideProfileCreation.class)
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void setStaticData(String type){
        driverSideRideModels.clear();
        if(type.equalsIgnoreCase("pending")){
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Pending","12-10-2021, 03:45pm","01120"));



        }else{
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Accepted","12-10-2021, 03:45pm","01120"));
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Accepted","12-10-2021, 03:45pm","01120"));
            driverSideRideModels.add(new DriverSideRideModel("Amritsar","Accepted","12-10-2021, 03:45pm","01120"));
        }
        driverSidePendingRideAdapter=new DriverSidePendingRideAdapter(this,driverSideRideModels);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        viewPager.setLayoutManager(layoutManager);
        viewPager.setAdapter(driverSidePendingRideAdapter);
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
                deletedMovie = String.valueOf(driverSideRideModels.get(position));
                AlertDialog alertDialog=new AlertDialog.Builder(DriverDashBoard.this,R.style.verification_done).create();
                View view= LayoutInflater.from(DriverDashBoard.this).inflate(R.layout.accept_request_dialogbox,null,false);
                alertDialog.setView(view);
                alertDialog.show();
                alertDialog.setCancelable(false);
                TextView textView=view.findViewById(R.id.pendingRequestButton);
                textView.setOnClickListener(v -> {
                    driverSideRideModels.remove(position);
                    driverSidePendingRideAdapter.notifyItemRemoved(position);
                    alertDialog.dismiss();
                });
            }
        }
    };
}