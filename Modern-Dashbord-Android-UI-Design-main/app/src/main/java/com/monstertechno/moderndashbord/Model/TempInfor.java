package com.monstertechno.moderndashbord.Model;


import java.util.ArrayList;

public class TempInfor {
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String image;
    private String token;
    private ArrayList<String> listRoles;

    public TempInfor() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getListRoles() {
        return listRoles;
    }

    public void setListRoles(ArrayList<String> listRoles) {
        this.listRoles = listRoles;
    }
}
