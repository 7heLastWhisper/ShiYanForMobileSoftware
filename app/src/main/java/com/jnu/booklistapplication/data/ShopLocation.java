package com.jnu.booklistapplication.data;

public class ShopLocation {
    private String name;
    private double latitude;
    private double longitude;
    private String memo;

    ShopLocation(String name, double latitude, double longitude, String memo) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
