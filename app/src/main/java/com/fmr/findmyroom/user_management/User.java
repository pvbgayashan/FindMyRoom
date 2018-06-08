package com.fmr.findmyroom.user_management;

public class User {

    private String userType;
    private String name;
    private String country;
    private String city;

    // for regular user
    private String gender;
    private String age;

    // for advanced user
    private String contactEmail;
    private String contactNo;
    private String nic;

    // constructor for regular user
    public User(String userType, String name, String country, String city, String gender, String age) {
        this.userType = userType;
        this.name = name;
        this.country = country;
        this.city = city;
        this.gender = gender;
        this.age = age;
    }

    // constructor for advanced user
    public User(String userType, String name, String country, String city, String contactEmail, String contactNo, String nic) {
        this.userType = userType;
        this.name = name;
        this.country = country;
        this.city = city;
        this.contactEmail = contactEmail;
        this.contactNo = contactNo;
        this.nic = nic;
    }

    public String getUserType() {
        return userType;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getNic() {
        return nic;
    }
}
