package com.android.bentabasura.benta_basura;

/**
 * Created by gd185082 on 10/6/2017.
 */

public class ActiveUser {

    public static  ActiveUser activeUserInstance = null;

    private String userId;
    private String email;
    private String fullname;
    private String gender;
    private String userType;

    public static ActiveUser getInstance()
    {
        if (activeUserInstance == null )
        {
            return activeUserInstance = new ActiveUser();
        }
        return activeUserInstance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getuserType() {
        return userType;
    }

    public void setuserType(String userType) {
        this.userType = userType;
    }

}
