package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.Product;
import model.User;

import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Date;

public class MainController extends Controller {
    private final AuthenticationController authenticationController;
    private final ProductsController productsController;
    private final UserController userController;
    private final CartController cartController;

    private final ReportingController reportingController;
    public MainController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
        this.authenticationController = new AuthenticationController(printStream, database, sqlManager);
        this.productsController = new ProductsController(printStream, database, sqlManager);
        this.userController = new UserController(printStream, database, sqlManager);
        this.cartController = new CartController(printStream, database, sqlManager);
        this.reportingController = new ReportingController(printStream, database, sqlManager);
    }

    public boolean logIn(String username, String password) {
        User user = authenticationController.logIn(username, password);
        if (user != null) {
            userController.setUser(user);
            return true;
        }
        return false;
    }

    public boolean adminLogIn(String username, String password) {
        return authenticationController.adminLogIn(username, password);
    }

    public boolean signUp(String name, String username, String password, String email, String phoneNumber) {
        User user = authenticationController.singUp(name, username, password, email, phoneNumber);
        if (user != null) {
            userController.setUser(user);
            return true;
        }
        return false;
    }

    public boolean logOut() {
        boolean result = authenticationController.logOut();
        if (result) {
            userController.setUser(null);
        }
        return result;
    }

    public boolean isLogged() {
        return authenticationController.getIsLogged();
    }

    public boolean isAdmin() {
        return authenticationController.getIsAdmin();
    }

    public boolean addShippingAddress(String address) {
        return userController.addShippingAddress(address);
    }

    public boolean removeShippingAddress(String id) {
        return userController.removeShippingAddress(id);
    }

    public void showShippingAddresses() {
        userController.showShippingAddresses();
    }

    public boolean comparePasswords(String password) {
        return userController.comparePasswords(password);
    }

    public boolean changePassword(String password) {
        return userController.changePassword(password);
    }

    public boolean listProducts() {
        return productsController.list();
    }

    public boolean searchProducts(String category, String brand, double lowestPrice, double highestPrice) {
        return productsController.search(category, brand, lowestPrice, highestPrice);
    }

    public void showProduct(String id) {
        productsController.show(id);
    }

    public String getCategoryId(String name) {
        return productsController.getCategory(name);
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
            String categoryId
    ) {
        Product product = productsController.add(name, description, price, brand, quantity, supplier, warehouse, review, categoryId, this.authenticationController.getAdminID());
        if(product != null){
            productsController.setProduct(product);
            return true;
        }
        return false;
    }

    public boolean updateProduct(
            String productID,
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
        return productsController.update(productID, name, description, price, brand, quantity, supplier, warehouse, review, null, null);
    }

    public boolean deleteProduct(String id) {
        return productsController.delete(id);
    }

    public boolean isCartEmpty() {
        return cartController.isEmpty();
    }

    public void showCart() {
        cartController.show();
    }

    public boolean checkout(String shippingAddressId) {
        return cartController.checkout(shippingAddressId, this.getUserId());
    }

    public boolean addToCart(String id, int quantity) {
        return cartController.add(id, quantity, this.getUserId());
    }

    public String getUserId() {
        return this.userController.getUser().getUserId();
    }

    public void trySmoothExit() {
        try {
            database.closeConnection();
        } catch (SQLException ignored) { }
    }
    public boolean generateRevenueReport() {
        return reportingController.generateRevenueReport(authenticationController.getAdminID());
    }

    public boolean generateSalesReport() {
        return reportingController.generateSalesReport(authenticationController.getAdminID());
    }

    public boolean generateOrderReport(Date startDate, Date endDate) {
        return reportingController.generateOrderReport(startDate, endDate);
    }
}
