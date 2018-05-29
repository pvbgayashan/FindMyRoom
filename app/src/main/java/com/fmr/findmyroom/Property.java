package com.fmr.findmyroom;

import java.util.Map;

public class Property {

    private String name;
    private String price;
    private String country;
    private String city;
    private String address;
    private String postalCode;
    private String downloadUrl;
    private Map<String, Boolean> propPrefMapper;

    public Property() {
        // empty constructor required
    }

    public Property(String name, String price, String country, String city, String address,
                    String postalCode, String downloadUrl, Map<String, Boolean> propPrefMapper) {
        this.name = name;
        this.price = price;
        this.country = country;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.downloadUrl = downloadUrl;
        this.propPrefMapper = propPrefMapper;
    }

    public String getPropName() {
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public Map<String, Boolean> getPropertyPrefMapper() {
        return propPrefMapper;
    }
}
