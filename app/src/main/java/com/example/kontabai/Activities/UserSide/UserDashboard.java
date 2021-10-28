package com.example.kontabai.Activities.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kontabai.Adapters.UserRideAdapter;
import com.example.kontabai.Classes.RideModel;
import com.example.kontabai.Classes.UserSideRideModel;
import com.example.kontabai.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UserDashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    UserRideAdapter userRideAdapter;
    ArrayList<RideModel> arrayList;
    String currentUser;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        initViews();
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
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("userRequest").child(currentUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList=new ArrayList<>();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    RideModel rideModel =dataSnapshot.getValue(RideModel.class);
                    arrayList.add(rideModel);
                }
                Collections.reverse(arrayList);
                userRideAdapter=new UserRideAdapter(UserDashboard.this,arrayList);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(UserDashboard.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(userRideAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserDashboard.this, "Faild :-"+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        recyclerView=findViewById(R.id.userRecyclerView);
        backButton=findViewById(R.id.userBackButton);
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheValuesInRecyclerView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserDashboard.this,UserSideActivity.class));
        finish();
    }
}