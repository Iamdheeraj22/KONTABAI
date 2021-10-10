package com.example.kontabai.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.kontabai.Activities.Fragments.AcceptedRequestFragment;
import com.example.kontabai.Activities.Fragments.PendingRequestFragment;
import com.example.kontabai.Classes.ViewPageAdapter;
import com.example.kontabai.R;
import com.google.android.material.tabs.TabLayout;

public class DriverDashBoard extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dash_board);
        //initViews();
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
        setUpTablayout();
    }
    private void setUpTablayout(){
        ViewPageAdapter viewPagerAdapter= new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AcceptedRequestFragment(),"Accepted");
        viewPagerAdapter.addFragment(new PendingRequestFragment(),"Pending");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}