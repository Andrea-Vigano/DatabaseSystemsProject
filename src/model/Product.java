package model;

public class Product {
    private String productID;
    private String name;
    private String description;
    private double price;
    private String brand;
    private String supplier;
    private String warehouse;
    private double review;
    private String categoryID;
    private int quantity;
    private String adminID;

    public Product(
            String productID,
            String name,
            String description,
            double price,
            String brand,
            int quantity,
            String supplier,
            String warehouse,
            double review,
            String categoryId,
            String adminId
    ){
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.supplier = supplier;
        this.warehouse = warehouse;
        this.review = review;
        this.categoryID = categoryId;
        this.adminID = adminId;
    }

    // Getters and setters

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public double getReview() {
        return review;
    }

    public void setReview(double review) {
        this.review = review;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
}