package com.example.kontabai.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kontabai.Activities.DriverSide.DriverDashBoard;
import com.example.kontabai.Activities.UserSide.UserSideActivity;
import com.example.kontabai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.v("SplashCheck","ok");
                    if (snapshot.exists()){
                        int role=Integer.parseInt(snapshot.child("userRole").getValue().toString());
                        if(role==1){
                            startActivity(new Intent(WelcomeActivity.this, UserSideActivity.class));
                            finish();
                        }
                        if(role==2){
                            startActivity(new Intent(WelcomeActivity.this, DriverDashBoard.class));
                            finish();
                        }
                    }else{
                        startActivity(new Intent(WelcomeActivity.this,RegistrationActivity.class));
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(WelcomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            startActivity(new Intent(WelcomeActivity.this,RegistrationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}