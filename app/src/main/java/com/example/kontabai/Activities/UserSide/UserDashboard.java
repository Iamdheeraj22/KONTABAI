package com.example.kontabai.Activities.UserSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kontabai.Adapters.DriverSidePendingRideAdapter;
import com.example.kontabai.Adapters.UserRideAdapter;
import com.example.kontabai.Classes.DriverSideRideModel;
import com.example.kontabai.Classes.UserSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    UserRideAdapter userRideAdapter;
    String currentUser;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initViews();
        setTheValuesInRecyclerView();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this, UserSideActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }
    private void setTheValuesInRecyclerView()
    {
        FirebaseRecyclerOptions<UserSideRideModel> options=
                new FirebaseRecyclerOptions.Builder<UserSideRideModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("userRequest").child(currentUser),UserSideRideModel.class)
                        .build();
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        userRideAdapter=new UserRideAdapter(options);
        recyclerView.setLayoutManager(layoutManager);
        userRideAdapter.startListening();
        recyclerView.setAdapter(userRideAdapter);
    }

    private void initViews() {
        recyclerView=findViewById(R.id.userRecyclerView);
        backButton=findViewById(R.id.userBackButton);
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserDashboard.this,UserSideActivity.class));
        finish();
    }
}