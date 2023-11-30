package model;

public class Address {
    private String addressID;
    private String userID;
    private String name;

    public Address(String addressID, String userID, String name) {
        this.addressID = addressID;
        this.userID = userID;
        this.name = name;
    }


    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

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
}