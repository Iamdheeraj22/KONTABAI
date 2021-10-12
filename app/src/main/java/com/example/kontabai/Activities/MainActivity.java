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

import com.example.kontabai.Activities.DriverSide.DriverDashBoard;
import com.example.kontabai.Activities.UserSide.UserDashboard;
import com.example.kontabai.R;

public class MainActivity extends AppCompatActivity {
    TextView btnRefresh,btnNeedTaxi,btnStatus,countStatus;
    RelativeLayout needTaxi,requestStatus;
    String type;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        type= getIntent().getStringExtra("type");
//        if(type.equals("driver")){
//            startActivity(new Intent(MainActivity.this, DriverDashBoard.class));
//        }
        btnNeedTaxi.setOnClickListener(v -> {showAlertBox();});
        btnStatus.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserDashboard.class)
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
        needTaxi.setOnClickListener(view -> showAlertBox());
        requestStatus.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, UserDashboard.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)));
    }

    private void initViews() {
        btnNeedTaxi=findViewById(R.id.needtaxi);
        btnRefresh=findViewById(R.id.refreshButton);
        btnStatus=findViewById(R.id.requeststatus);
        countStatus=findViewById(R.id.countRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        countStatus.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void showAlertBox(){
        AlertDialog alertDialog=new AlertDialog.Builder(this,R.style.verification_done).create();
        View view= LayoutInflater.from(this).inflate(R.layout.confirmation_dialog,null,false);
        alertDialog.setView(view);
        alertDialog.show();
        TextView textView=view.findViewById(R.id.confirmationHeading);
        textView.setText("Your location has been sent to\n"+"drivers , please wait.");
        Handler handler=new Handler();
        handler.postDelayed(() -> {
            countStatus.setVisibility(View.VISIBLE);
            alertDialog.dismiss();
        },3000);
    }
}