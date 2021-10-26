package com.example.kontabai.Classes;

public class DriverSideRideModel
{
    private String pickup_address;
    private String status;
    private String date;
    private String request_order;

    public DriverSideRideModel(String pickup_address, String status, String date, String request_order) {
        this.pickup_address = pickup_address;
        this.status = status;
        this.date = date;
        this.request_order = request_order;
    }
    public DriverSideRideModel() {
    }



    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getRequest_order() {
        return request_order;
    }
    public String getPickup_address() {
        return pickup_address;
    }
}
