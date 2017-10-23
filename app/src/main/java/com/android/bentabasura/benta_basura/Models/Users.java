package com.android.bentabasura.benta_basura.Models;

/**
 * Created by Geno on 9/24/2017.
 */

public class Users {
    private String username,email,firstname,lastname,gender,profile_picture,userType,address,contact_number;
    public Users(){

    }
    public Users(String username,String email,String firstname, String lastname,String gender,String profile_picture,String userType, String address, String contact_number){
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.profile_picture = profile_picture;
        this.userType = userType;
        this.address = address;
        this.contact_number = contact_number;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setprofile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }

    public String getprofile_picture() {
        return profile_picture;
    }

    public String getUserType() {
        return userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getcontact_number() {
        return contact_number;
    }

    public void setcontact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}