package com.example.kontabai.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kontabai.Activities.DriverSide.DriverDashBoard;
import com.example.kontabai.R;

public class UserRideDetails extends AppCompatActivity {
    TextView name,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ride_details);
        name=findViewById(R.id.userDetailsName);
        number=findViewById(R.id.userDetailsPhoneNumber);
        String Name=getIntent().getStringExtra("name");
        String Number=getIntent().getStringExtra("number");

        name.setText(Name);
        number.setText(Number);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserRideDetails.this, DriverDashBoard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}