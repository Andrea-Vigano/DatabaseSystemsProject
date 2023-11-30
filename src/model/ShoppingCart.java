package model;

import java.util.Date;

public class ShoppingCart {
    private String id;
    private int quantity;
    private Date dateAdded;
    private String productName;
    private String productID;


    public ShoppingCart(String id, int quantity, Date dateAdded, String productName, String productID) {
        this.id = id;
        this.quantity = quantity;
        this.dateAdded = dateAdded;
        this.productName = productName;
        this.productID = productID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
