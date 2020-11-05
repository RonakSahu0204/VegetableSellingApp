package com.example.vegetablessell.model;

public class UserModel {

    private String username;
    private String contactNo;
    private String email;
    private int cartCount;

    public UserModel() {

    }

    public UserModel(String username, String contactNo, String email, int cartCount) {
        this.username = username;
        this.contactNo = contactNo;
        this.email = email;
        this.cartCount = cartCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }
}
