package com.android.bentabasura.benta_basura;

/**
 * Created by Geno on 9/24/2017.
 */

public class Users {
    private String username,email,firstname,lastname,gender,imageUrl,userType;
    public Users(){

    }
    public Users(String username,String email,String firstname, String lastname,String gender,String imageUrl,String userType){
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.userType = userType;
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

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getimageUrl() {
        return imageUrl;
    }

    public String getUserType() {
        return userType;
    }
}
