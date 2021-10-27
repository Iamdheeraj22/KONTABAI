package com.example.kontabai.Activities.UserSide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kontabai.Activities.RegistrationActivity;
import com.example.kontabai.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UserSideActivity extends AppCompatActivity {
    TextView btnRefresh, countStatus, logout_Button;
    RelativeLayout relativeNeedTaxi, relativeRequestStatus;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    String number, name,id;
    String userLocation,currentUid;
    double latitude, longitude,lat,log;
    LocationManager locationManager;
    long requestCounter,userRequestCounter;

    //LocationRequest locationRequest;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        relativeNeedTaxi.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UserSideActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                sendNeedTaxiRequest();
            }
        });
        relativeRequestStatus.setOnClickListener(v -> {
            relativeRequestStatus.setBackgroundResource(R.drawable.screen_background);
            relativeNeedTaxi.setEnabled(false);
            Handler handler = new Handler();
            handler.postDelayed(() -> startActivity(new Intent(UserSideActivity.this, UserDashboard.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)), 1000);
        });
        logout_Button.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(UserSideActivity.this, R.style.verification_done).create();
            View view = LayoutInflater.from(UserSideActivity.this).inflate(R.layout.delete_item_alert_box, null, false);
            alertDialog.setView(view);
            alertDialog.show();
            alertDialog.setCancelable(true);
            TextView heading = view.findViewById(R.id.textHeading);
            heading.setText("Are you sure you want to logout ?");
            TextView yesButton = view.findViewById(R.id.yesButton);
            TextView noButton = view.findViewById(R.id.noButton);
            yesButton.setOnClickListener(v1 -> {
                yesButton.setBackgroundResource(R.drawable.screen_background_2);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(UserSideActivity.this, RegistrationActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    alertDialog.dismiss();
                }, 3000);
            });
            noButton.setOnClickListener(v1 -> {
                noButton.setBackgroundResource(R.drawable.screen_background_2);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    alertDialog.dismiss();
                    noButton.setBackgroundColor(Color.WHITE);
                }, 2000);
            });
        });
    }
    private void initViews() {
        relativeNeedTaxi = findViewById(R.id.relativeNeedTaxi);
        relativeRequestStatus = findViewById(R.id.statusRelative);
        btnRefresh = findViewById(R.id.refreshButton);
        countStatus = findViewById(R.id.countRequest);
        logout_Button = findViewById(R.id.logout_button);
        currentUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        relativeRequestStatus.setBackgroundResource(R.drawable.black_corners);
        relativeNeedTaxi.setBackgroundResource(R.drawable.black_corners);
        updateUserLocation();
        getCurrentLocation();
        userRideRequest();
    }

    private void userRideRequest()
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("userRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    relativeNeedTaxi.setEnabled(false);
                    countStatus.setVisibility(View.VISIBLE);
                }else{
                    relativeNeedTaxi.setEnabled(true);
                    countStatus.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserSideActivity.this, "Failed :- "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseReference.child("latitude").setValue(latitude);
                databaseReference.child("longitude").setValue(longitude);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showAlertBox(){
        AlertDialog alertDialog=new AlertDialog.Builder(this,R.style.verification_done).create();
        View view= LayoutInflater.from(this).inflate(R.layout.confirmation_dialog,null,false);
        alertDialog.setView(view);
        alertDialog.show();
        TextView textView=view.findViewById(R.id.confirmationHeading);
        alertDialog.setCancelable(false);
        textView.setText("Your location has been sent to\n"+"drivers , please wait.");
        alertDialog.dismiss();

        Handler handler=new Handler();
        handler.postDelayed(() -> {
            countStatus.setVisibility(View.VISIBLE);
            relativeNeedTaxi.setBackgroundResource(R.drawable.black_corners);
            relativeRequestStatus.setBackgroundResource(R.drawable.black_corners);
            relativeRequestStatus.setEnabled(true);
            relativeNeedTaxi.setEnabled(true);
        },3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.d("location","Permission Granted");}
            else {
                Toast.makeText(UserSideActivity.this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location=task.getResult();
            if(location!=null){
                Geocoder geocoder=new Geocoder(UserSideActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    userLocation=Html.fromHtml(addresses.get(0).getAddressLine(0)).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendNeedTaxiRequest()
    {
        AlertDialog alertDialog=new AlertDialog.Builder(UserSideActivity.this,R.style.verification_done).create();
        View view=LayoutInflater.from(UserSideActivity.this).inflate(R.layout.progress_dialog,null,false);
        alertDialog.setView(view);
        alertDialog.show();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.child("requestcounter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String countReq=snapshot.child("count").getValue().toString();
                    requestCounter=Long.parseLong(countReq);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserSideActivity.this, "Failed :-"+error, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        mDatabaseRef.child("users").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id=snapshot.child("id").getValue().toString().trim();
                    lat=Double.parseDouble(snapshot.child("latitude").getValue().toString());
                    log=Double.parseDouble(snapshot.child("longitude").getValue().toString());

                    number=snapshot.child("number").getValue().toString().trim();
                    name=snapshot.child("").getValue().toString().trim();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserSideActivity.this, "Failed :- "+error, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf=new SimpleDateFormat("dd-M-yyyy hh:mm a");
        String dateTime= sdf.format(new Date());

        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("userRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String key =  mDatabaseRef.push().getKey();
        requestCounter=requestCounter+1;
        String order= String.valueOf(requestCounter);
        String status="pending";
        HashMap<String,Object> requestMap=new HashMap<>();
        requestMap.put("id",key);
        requestMap.put("date",dateTime);
        requestMap.put("userId",currentUid);
        requestMap.put("latitude",latitude);
        requestMap.put("longitude",longitude);
        requestMap.put("driverId","");
        requestMap.put("pickup_address",userLocation);
        requestMap.put("status",status);
        requestMap.put("request_order", order);
        assert key != null;
        databaseReference1.child(key).setValue(requestMap);
        mDatabaseRef.child("requestcounter").child("count").setValue(requestCounter);
        mDatabaseRef.child("requests").child(key).setValue(requestMap).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                alertDialog.dismiss();
                setTherequestInUserSide(key,dateTime,userLocation,status);
//                showAlertBox();
                Toast.makeText(UserSideActivity.this, "Request submitted..", Toast.LENGTH_SHORT).show();
                relativeNeedTaxi.setEnabled(false);
            }
        });
    }

    private void setTherequestInUserSide(String key, String dateTime, String userLocation, String status) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("userRequestCounter").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String countReq=snapshot.child("count").getValue().toString();
                    userRequestCounter=Long.parseLong(countReq);
                }else {
                    userRequestCounter=0;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserSideActivity.this, "Failed :- "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        userRequestCounter=userRequestCounter+1;
        String order= String.valueOf(userRequestCounter);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pickup_address",userLocation);
        hashMap.put("id",key);
        hashMap.put("date",dateTime);
        hashMap.put("status",status);
        hashMap.put("count",order);
        databaseReference.child("userRequestCounter").child(currentUid).child("count").setValue(order);
        databaseReference.child("userRequest").child(currentUid).child(key).setValue(hashMap);
    }
}