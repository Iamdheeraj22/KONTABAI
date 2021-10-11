package com.example.kontabai.Classes;

public class DriverSidePendingRide
{
    private String pendinglocation;

    public DriverSidePendingRide(String pendinglocation) {
        this.pendinglocation = pendinglocation;
    }

    public DriverSidePendingRide() {
    }

    public String getPendinglocation() {
        return pendinglocation;
    }

    public void setPendinglocation(String pendinglocation) {
        this.pendinglocation = pendinglocation;
    }
}
