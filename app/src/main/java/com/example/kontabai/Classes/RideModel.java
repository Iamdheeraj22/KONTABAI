package com.example.kontabai.Classes;

public class RideModel
{
    private String pickup_address;
    private String status;
    private String date;
    private String driver_id;
    private String id;
    private String user_id;
    private String request_order;

    public RideModel(String pickup_address, String status, String date, String request_order, String driver_id, String id, String user_id) {
        this.pickup_address = pickup_address;
        this.status = status;
        this.user_id=user_id;
        this.date=date;
        this.request_order = request_order;
        this.driver_id=driver_id;
        this.id=id;
    }
    public RideModel() {
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
    public String getRequest_order() {
        return request_order;
    }
    public String getPickup_address() {
        return pickup_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }
}
