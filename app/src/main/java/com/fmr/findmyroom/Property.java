package com.fmr.findmyroom;

/**
 * Created by pvbgayashan on 4/19/18.
 */

public class Property {

    private String propName;
    private String country;
    private String city;
    private String price;
    private String downloadUrl;

    public Property() {
        // empty constructor required
    }

    public Property(String propName, String country, String city, String price, String downloadUrl) {
        this.propName = propName;
        this.country = country;
        this.city = city;
        this.price = price;
        this.downloadUrl = downloadUrl;
    }

    public String getPropName() {
        return propName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPrice() {
        return price;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
