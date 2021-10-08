package com.example.kontabai.Classes;

public class UserRide
{
    private String location;
    private String status;

    public UserRide(String location, String status) {
        this.location = location;
        this.status = status;
    }

    public UserRide() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
