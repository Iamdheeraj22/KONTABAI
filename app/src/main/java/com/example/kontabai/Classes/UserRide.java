package com.example.kontabai.Classes;

public class UserRide
{
    private String location;
    private String status;
    private String date;
    private String id;

    public UserRide(String location, String status, String date, String id) {
        this.location = location;
        this.status = status;
        this.date = date;
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
