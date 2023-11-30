package model;

public class User {
    private String userID;
    private String name;
    private String username;
    private String passwordHash;
    private String email;
    private String address;
    private String phoneNumber;

    public User(String userID, String name, String username, String passwordHash, String email, String address, String phoneNumber) {
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userID;
    }
}