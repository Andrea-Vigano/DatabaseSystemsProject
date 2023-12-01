package model;

public class OrderLineItems {
    private String orderLineItemID;
    private int quantity;
    private String productID;
    private String orderID;

    public OrderLineItems(String orderLineItemID, int quantity, String productID, String orderID) {
        this.orderLineItemID = orderLineItemID;
        this.quantity = quantity;
        this.productID = productID;
        this.orderID = orderID;
    }

    // Getters and setters

    public String getOrderLineItemID() {
        return orderLineItemID;
    }

    public void setOrderLineItemID(String orderLineItemID) {
        this.orderLineItemID = orderLineItemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}