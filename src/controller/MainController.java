package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.sql.SQLException;

public class MainController extends Controller {
    private final AuthenticationController authenticationController;
    private final ProductsController productsController;
    private final UserController userController;
    private final CartController cartController;

    public MainController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
        this.authenticationController = new AuthenticationController(printStream, database, sqlManager);
        this.productsController = new ProductsController(printStream, database, sqlManager);
        this.userController = new UserController(printStream, database, sqlManager);
        this.cartController = new CartController(printStream, database, sqlManager);
    }
    // TODO add User to UserController after login, remove after logout and keep in sync after each update

    public boolean logIn(String username, String password) {
        return authenticationController.logIn(username, password);
    }

    public boolean adminLogIn(String username, String password) {
        return authenticationController.adminLogIn(username, password);
    }

    public boolean signUp(String name, String username, String password, String email, String address) {
        return authenticationController.singUp(name, username, password, email);
    }

    public boolean logOut() {
        return authenticationController.logOut();
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

    public boolean getCategoryId(String name) {
        return productsController.getCategory(name);
    }

    public boolean addProduct(
            String name,
            String description,
            String specification,
            double price,
            String brand,
            String categoryId,
            String warehouseId,
            String supplierId,
            String rating
    ) {
        return productsController.add(name, description, specification, price, brand, categoryId, warehouseId, supplierId, rating);
    }

    public boolean updateProduct(
            String id,
            String name,
            String description,
            String specification,
            String brand
    ) {
        return productsController.update(id, name, description, specification, null, brand, null, null, null);
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
}
