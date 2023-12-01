package controller;

import controller.database.Database;
import controller.database.SQLManager;
import java.sql.*;
import java.sql.Date;
import java.util.*;


import java.io.PrintStream;

public class AdminController extends Controller {
    private UserController userController;
    private ProductsController productsController;
    private ReportingController reportingController;

    public AdminController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
        this.userController = new UserController(printStream, database, sqlManager);
        this.productsController = new ProductsController(printStream, database, sqlManager);
        this.reportingController = new ReportingController(printStream, database, sqlManager);
    }

    // Admin-exclusive methods for managing users
    public boolean addShippingAddress(String address) {
        return userController.addShippingAddress(address);
    }

    public boolean removeShippingAddress(String id) {
        return userController.removeShippingAddress(id);
    }

    public List<String> showShippingAddresses() {
        return userController.showShippingAddresses();
    }

    // Admin-exclusive methods for managing products
    public boolean listProducts() {
        return productsController.list();
    }

    public boolean searchProducts(String category, String brand, double lowestPrice, double highestPrice) {
        return productsController.search(category, brand, lowestPrice, highestPrice);
    }

    public boolean addProduct(
            String name,
            String description,
            double price,
            String brand,
            int quantity,
            String supplier,
            String warehouse,
            String review,
            String categoryId,
            String adminId
    ) {
        return productsController.add(name, description, price, brand, quantity, supplier, warehouse, review, categoryId, adminId);
    }

    public boolean deleteProduct(String productId) {
        return productsController.delete(productId);
    }

    public boolean updateProduct(
            String productId,
            String name,
            String description,
            Double price,
            String brand,
            Integer quantity,
            String supplier,
            String warehouse,
            String review,
            String categoryId,
            String adminId
    ) {
        return productsController.update(productId, name, description, price, brand, quantity, supplier, warehouse, review, categoryId, adminId);
    }

    public boolean showProduct(String productId) {
        return productsController.show(productId);
    }

    public boolean getCategory(String name) {
        return productsController.getCategory(name);
    }

    // Admin-exclusive methods for generating reports
    public void generateRevenueReport(String adminID) {
        reportingController.generateRevenueReport(adminID);
    }

    public void generateSalesReport(String adminID) {
        reportingController.generateSalesReport(adminID);
    }

    public void generateOrderReport(Date startDate, Date endDate) {
        reportingController.generateOrderReport(startDate, endDate);
    }
}