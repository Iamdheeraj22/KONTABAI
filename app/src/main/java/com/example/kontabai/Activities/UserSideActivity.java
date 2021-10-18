package com.example.kontabai.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kontabai.Activities.UserSide.UserDashboard;
import com.example.kontabai.Activities.UserSide.UserSideProfileCreation;
import com.example.kontabai.R;

public class UserSideActivity extends AppCompatActivity {
    TextView btnRefresh,countStatus;
    RelativeLayout relativeNeedTaxi,relativeRequestStatus;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        relativeNeedTaxi.setOnClickListener(v -> {
            relativeNeedTaxi.setBackgroundResource(R.drawable.screen_background);
            relativeRequestStatus.setEnabled(false);
            Handler handler=new Handler();
            handler.postDelayed(() -> showAlertBox(),100);
        });
        relativeRequestStatus.setOnClickListener(v -> {
            relativeRequestStatus.setBackgroundResource(R.drawable.screen_background);
            relativeNeedTaxi.setEnabled(false);
            Handler handler=new Handler();
            handler.postDelayed(() -> startActivity(new Intent(UserSideActivity.this, UserDashboard.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)),1000);
        });
    }

    private void initViews() {
        relativeNeedTaxi=findViewById(R.id.relativeNeedTaxi);
        relativeRequestStatus=findViewById(R.id.statusRelative);
        btnRefresh=findViewById(R.id.refreshButton);
        countStatus=findViewById(R.id.countRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        relativeRequestStatus.setBackgroundResource(R.drawable.black_corners);
        relativeNeedTaxi.setBackgroundResource(R.drawable.black_corners);
        countStatus.setVisibility(View.GONE);
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
        Handler handler=new Handler();
        handler.postDelayed(() -> {
            countStatus.setVisibility(View.VISIBLE);
            alertDialog.dismiss();
            relativeNeedTaxi.setBackgroundResource(R.drawable.black_corners);
            relativeRequestStatus.setBackgroundResource(R.drawable.black_corners);
            relativeRequestStatus.setEnabled(true);
            relativeNeedTaxi.setEnabled(true);
        },3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserSideActivity.this, UserSideProfileCreation.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}