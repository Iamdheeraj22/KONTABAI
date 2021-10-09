package com.example.kontabai.Activities.UserSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kontabai.Adapters.UserRideAdapter;
import com.example.kontabai.Classes.UserRide;
import com.example.kontabai.R;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    UserRideAdapter userRideAdapter;
    ArrayList<UserRide> userRides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initViews();
        setTheValuesInRecyclerView();
    }

    private void setTheValuesInRecyclerView()
    {
            userRides.add(new UserRide("Amritsar","Pending"));
            userRides.add(new UserRide("Jaipur","Rejected"));
            userRides.add(new UserRide("Delhi","Accepted"));
            userRides.add(new UserRide("Sector 21 ,Chandigarh","Accepted"));
            userRides.add(new UserRide("Phase 6 , Chandigarh","Accepted"));
    }

    private void initViews() {
        recyclerView=findViewById(R.id.userRecyclerView);
        userRides=new ArrayList<>();
        userRideAdapter=new UserRideAdapter(getApplicationContext(),userRides);
        setRecyclerView();
    }

    private void  setRecyclerView(){
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userRideAdapter);
    }
}