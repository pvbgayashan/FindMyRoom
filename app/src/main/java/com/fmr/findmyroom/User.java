package com.fmr.findmyroom;

public class User {

    private String name;
    private String age;
    private String gender;
    private String country;
    private String city;

    public User(String name, String age, String gender, String country, String city) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
