package com.example.kontabai.Activities.DriverSide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.kontabai.Activities.Fragments.AcceptedRequestFragment;
import com.example.kontabai.Activities.Fragments.PendingRequestFragment;
import com.example.kontabai.Classes.ViewPageAdapter;
import com.example.kontabai.R;
import com.google.android.material.tabs.TabLayout;

public class DriverDashBoard extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    Handler handler;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);
        //initViews();
        tabLayout=findViewById(R.id.tab_layout);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        viewPager=findViewById(R.id.view_pager);
        setUpTablayout();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler=new Handler();
            handler.postDelayed(() -> swipeRefreshLayout.setRefreshing(false),3000);
        });
    }
    private void setUpTablayout(){
        ViewPageAdapter viewPagerAdapter= new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PendingRequestFragment(),"Pending");
        viewPagerAdapter.addFragment(new AcceptedRequestFragment(),"Accepted");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DriverDashBoard.this,DriverSideProfileCreation.class)
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}