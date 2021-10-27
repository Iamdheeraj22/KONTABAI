package com.example.kontabai.Classes;

public class UserSideRideModel
{
    private String pickup_address;
    private String status;
    private String date;
    private String id;
    private String count;

    public UserSideRideModel(String pickup_address, String status, String date, String request_order,String id) {
        this.pickup_address = pickup_address;
        this.status = status;
        this.date=date;
        this.count = request_order;
        this.id=id;
    }
    public UserSideRideModel() {
    }


    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getDate() {
        return date;
    }
    public String getCount() {
        return count;
    }
    public String getPickup_address() {
        return pickup_address;
    }
}
