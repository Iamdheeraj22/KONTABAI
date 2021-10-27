package com.example.kontabai.Classes;

public class DriverSideRideModel
{
    private String pickup_address;
    private String status;
    private String date;
    private String driverId;
    private String id;
    private String userId;
    private String request_order;

    public DriverSideRideModel(String pickup_address, String status, String date, String request_order,String driverId,String id,String userId) {
        this.pickup_address = pickup_address;
        this.status = status;
        this.userId=userId;
        this.date=date;
        this.request_order = request_order;
        this.driverId=driverId;
        this.id=id;
    }
    public DriverSideRideModel() {
    }


    public String getUserId() {
        return userId;
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
    public String getDriverId() {
        return driverId;
    }
}
