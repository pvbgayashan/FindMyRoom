package com.fmr.findmyroom.common;

import java.util.Map;

public class Property {

    private String id;
    private String name;
    private String price;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private String phone;
    private String downloadUrl;
    private String userName;
    private float rating;
    private int rateCount;
    private Map<String, Boolean> preferences;
    int pax;

    public Property() {
        // empty constructor required
    }

    public Property(String id, String name, String price, String country, String city, String address,
                    String postalCode, String phone, String downloadUrl, String userName,
                    float rating, int rateCount, Map<String, Boolean> preferences, int pax) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.country = country;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.downloadUrl = downloadUrl;
        this.userName = userName;
        this.rating = rating;
        this.rateCount = rateCount;
        this.preferences = preferences;
        this.pax = pax;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public float getRating() {
        return rating;
    }

    public int getRateCount() {
        return rateCount;
    }

    public Map<String, Boolean> getPreferences() {
        return preferences;
    }

    public int getPax() {
        return pax;
    }
}
