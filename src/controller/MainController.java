package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;

public class MainController extends Controller {
    private final AuthenticationController authenticationController;
    private final ProductsController productsController;

    public MainController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
        this.authenticationController = new AuthenticationController(printStream, database, sqlManager);
        this.productsController = new ProductsController(printStream, database, sqlManager);
    }

    public boolean logIn(String username, String password) {
        return authenticationController.logIn(username, password);
    }

    public boolean adminLogIn(String username, String password) {
        return authenticationController.adminLogIn(username, password);
    }

    public boolean signUp(String name, String username, String password, String email) {
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

    public boolean listProducts() {
        return productsController.list();
    }

    public boolean searchProducts(String category, String brand, double lowestPrice, double highestPrice) {
        return productsController.search(category, brand, lowestPrice, highestPrice);
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
}
