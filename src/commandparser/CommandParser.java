package commandparser;

import controller.AuthenticationController;
import controller.MainController;
import controller.ReportingController;

import java.io.PrintStream;
import java.sql.Date;
import java.util.Objects;
import java.util.Scanner;

public class CommandParser {
    final private MainController controller;
    final private Scanner scanner;
    final private PrintStream printStream;
    public CommandParser(MainController controller, Scanner scanner, PrintStream printStream) {
        this.controller = controller;
        this.scanner = scanner;
        this.printStream = printStream;
    }

    public void parse(Command command) {
        if (command.isLogin()) {
            this.wrapWithLoginCheck(this::performLogin);
        } else if (command.isSignup()) {
            this.wrapWithLoginCheck(this::performSignUp);
        } else if (command.isLogout()) {
            this.performLogOut();
        } else if (command.isChangePassword()) {
            this.wrapWithAuth(this::performChangePassword);
        } else if (command.isShowShippingAddress()) {
            this.wrapWithAuth(this::performShowShippingAddress);
        } else if (command.isAddShippingAddress()) {
            this.wrapWithAuth(this::performAddShippingAddress);
        } else if (command.isDeleteShippingAddress()) {
            this.wrapWithAuth(this::performDeleteShippingAddress);
        } else if (command.isListProducts()) {
            this.wrapWithAuth(this::performList);
        } else if (command.isSearchProducts()) {
            this.wrapWithAuth(this::performSearch);
        } else if (command.isAdminLogin()) {
            this.wrapWithLoginCheck(this::performAdminLogin);
        } else if (command.isAddProduct()) {
            this.wrapWithAdminAuth(this::performAddProduct);
        } else if (command.isUpdateProduct()) {
            this.wrapWithAuth(this::performUpdateProduct);
        } else if (command.isDeleteProduct()) {
            this.wrapWithAuth(this::performDeleteProduct);
        } else if (command.isShowProduct()) {
            this.wrapWithAuth(this::performShowProduct);
        } else if (command.isAddToCart()) {
            this.wrapWithAuth(this::performAddToCart);
        } else if (command.isShowCart()) {
            this.wrapWithAuth(this::performShowCart);
        } else if (command.isCheckout()) {
            this.wrapWithAuth(this::performCheckout);
        } else if (command.isGenerateReport()) {
            this.wrapWithAdminAuth(this::performGenerateReport);
        } else if (command.isHelp()) {
            this.performHelp();
        } else if (command.isExit()) {
            this.printStream.println("Quitting");
            this.controller.trySmoothExit();
            // Main loop will break on its own in-App class
        } else {
            this.printStream.println("Unknown command, type 'help' for usage information");
        }
    }

    private void wrapWithLoginCheck(Runnable function) {
        if (this.controller.isLogged()) printStream.println("User already logged in");
        else function.run();
    }

    private void wrapWithAuth(Runnable function) {
        if (!this.controller.isLogged()) printStream.println("User not logged in");
        else function.run();
    }

    private void wrapWithAdminAuth(Runnable function) {
        if (!this.controller.isLogged()) printStream.println("User not logged in");
        else if (!this.controller.isAdmin()) printStream.println("User is not admin");
        else function.run();
    }

    private void performLogin() {
        printStream.print("Insert your username: ");
        String username = scanner.nextLine();
        while(username.length() == 0){
            printStream.println("Username cannot be null! Please insert your username again!");
            printStream.print("Insert your username: ");
            username = scanner.nextLine();
        }
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        while(password.length() == 0){
            printStream.println("Password cannot be null! Please insert your password again!");
            printStream.print("Insert your password: ");
            password = scanner.nextLine();
        }
        boolean result = this.controller.logIn(username, password);
        if (result) printStream.println("Successfully logged in");
        else printStream.println("Unable to log in");
    }

    private void performSignUp() {
        printStream.print("Insert your name: ");
        String name = scanner.nextLine();
        while(name.length() == 0){
            printStream.println("First name cannot be null! Please insert your first name again!");
            printStream.print("Insert your name: ");
            name = scanner.nextLine();
        }
        printStream.print("Insert your username: ");
        String username = scanner.nextLine();
        while(username.length() == 0){
            printStream.println("Username cannot be null! Please insert your username again!");
            printStream.print("Insert your username: ");
            username = scanner.nextLine();
        }
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        while(password.length() == 0){
            printStream.println("Password cannot be null! Please insert your password again!");
            printStream.print("Insert your password: ");
            password = scanner.nextLine();
        }
        printStream.print("Insert your email: ");
        String email = scanner.nextLine();
        printStream.print("Insert your phone number: ");
        String phoneNumber = scanner.nextLine();
        boolean result = this.controller.signUp(name, username, password, email, phoneNumber);
        if (result) printStream.println("Successfully signed up");
        else printStream.println("Unable to sign up");
    }

    private void performLogOut() {
        boolean result = this.controller.logOut();
        if (result) printStream.println("Successfully logged out");
        else printStream.println("Unable to log out");
    }

    private void performChangePassword() {
        printStream.println("Insert your current password: ");
        String password = scanner.nextLine();
        if (this.controller.comparePasswords(password)) {
            printStream.println("Insert your new password: ");
            String newPassword = scanner.nextLine();
            boolean result = this.controller.changePassword(newPassword);
            if (result) printStream.println("Successfully changed password");
            else printStream.println("Unable to change password");
        } else {
            printStream.println("Invalid password");
        }
    }

    private void performShowShippingAddress() {
        this.controller.showShippingAddresses();
    }

    private void performAddShippingAddress() {
        printStream.print("Insert address: ");
        String address = scanner.nextLine();
        boolean result = this.controller.addShippingAddress(address);
        if (result) printStream.println("Successfully added address");
        else printStream.println("Unable to add address");
    }

    private void performDeleteShippingAddress() {
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        boolean result = this.controller.removeShippingAddress(id);
        if (result) printStream.println("Successfully deleted shipping address with id: " + id);
        else printStream.println("Unable to delete shipping address with id: " + id);
    }

    private void performList() {
        boolean result = this.controller.listProducts();
        if (result) printStream.println("Successfully listed products");
        else printStream.println("Unable to list products");
    }

    private void performSearch() {
        String category = "", brand = "";
        String isYes;
        double lowestPrice = 0.0, highestPrice = 0.0;
        printStream.print("Do you want to search products by category [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert category: ");
            category = scanner.nextLine();
        }
        printStream.print("Do you want to search products by brand [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            brand = scanner.nextLine();
        }
        printStream.print("Do you want to search products by priceRange [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert lowest price: ");
            lowestPrice = Double.parseDouble(scanner.nextLine());
            printStream.print("Insert highest price: ");
            highestPrice = Double.parseDouble(scanner.nextLine());
        }
        boolean result = this.controller.searchProducts(category, brand, lowestPrice, highestPrice);
        if (result) printStream.println("Successfully searched products");
        else printStream.println("Unable to search products");
    }

    private void performAdminLogin() {
        printStream.print("Insert your username: ");
        String username = scanner.nextLine();
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        boolean result = this.controller.adminLogIn(username, password);
        if (result) printStream.println("Successfully logged in");
        else printStream.println("Unable to log in");
    }

    private void performAddProduct() {
        printStream.print("Insert product name: ");
        String name = scanner.nextLine();
        printStream.print("Insert product description: ");
        String description = scanner.nextLine();
        printStream.print("Insert product price: ");
        double price = Double.parseDouble(scanner.nextLine());
        printStream.print("Insert product brand: ");
        String brand = scanner.nextLine();
        printStream.print("Insert product quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        printStream.print("Insert product supplier name: ");
        String supplier = scanner.nextLine();
        printStream.print("Insert product warehouse name: ");
        String warehouse = scanner.nextLine();
        printStream.print("Insert product review: ");
        String review = scanner.nextLine();
        printStream.print("Insert category name: ");
        String category = scanner.nextLine();
        String categoryID = this.controller.getCategoryId(category);
        boolean result = this.controller.addProduct(name, description, price, brand, quantity, supplier, warehouse, review, categoryID);
        if (result) printStream.println("Successfully added product");
        else printStream.println("Unable to add product");
    }

    private void performUpdateProduct() {
        // TODO enhance update options
        printStream.print("Insert product product id: ");
        String productID = scanner.nextLine();
        String name = null, description = null, brand = null, supplier = null, warehouse = null, review = null;
        double price = 0.0;
        int quantity = 0;
        String isYes;
        printStream.print("Do you want to update the product name [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert name: ");
            name = scanner.nextLine();
        }
        printStream.print("Do you want to update the product description [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert description: ");
            description = scanner.nextLine();
        }
        printStream.print("Do you want to update the product price [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert specifications: ");
            price = Double.parseDouble(scanner.nextLine());
        }
        printStream.print("Do you want to update the product brand [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            brand = scanner.nextLine();
        }
        printStream.print("Do you want to update the product quantity [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            quantity = Integer.parseInt(scanner.nextLine());
        }
        printStream.print("Do you want to update the product supplier [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            supplier = scanner.nextLine();
        }
        printStream.print("Do you want to update the product warehouse [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            warehouse = scanner.nextLine();
        }
        printStream.print("Do you want to update the product review [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            review = scanner.nextLine();
        }
        boolean result = this.controller.updateProduct(productID, name, description, price, brand, quantity, supplier, warehouse, review, null, null);
        if (result) printStream.println("Successfully updated product with id: " + productID);
        else printStream.println("Unable to update product with id: " + productID);
    }

    private void performDeleteProduct() {
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        boolean result = this.controller.deleteProduct(id);
        if (result) printStream.println("Successfully deleted product with id: " + id);
        else printStream.println("Unable to delete product with id: " + id);
    }

    private void performShowProduct() {
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        this.controller.showProduct(id);
    }

    private void performAddToCart() {
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        printStream.print("Insert quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        boolean result = this.controller.addToCart(id, quantity);
        if (result) printStream.println("Successfully added to cart product with id: " + id);
        else printStream.println("Unable to add to cart product with id: " + id);
    }

    private void performShowCart() {
        if (this.controller.isCartEmpty()) printStream.println("Cart is empty");
        else this.controller.showCart();
    }

    private void performCheckout() {
        this.performShowShippingAddress();
        printStream.println("Insert shipping address id: ");
        String shippingAddressId = scanner.nextLine();
        // TODO payment method
        boolean result = this.controller.checkout(shippingAddressId);
        if (result) printStream.println("Successfully checked out");
        else printStream.println("Unable to checkout");
    }

    private void performGenerateReport() {
        printStream.println("Insert the start date (YYYY-MM-DD): ");
        Date startDate = Date.valueOf(scanner.nextLine());
        printStream.println("Insert the end date (YYYY-MM-DD): ");
        Date endDate = Date.valueOf(scanner.nextLine());


        printStream.println("Select the type of report:\n" +
                "1. Revenue Report \n" +
                "2. Sales Report \n" +
                "3. Order Report ");
        printStream.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine());
        String adminID="1";
        switch (choice) {
            case 1:
                controller.generateRevenueReport();
                break;
            case 2:
                controller.generateSalesReport();
                break;
            case 3:
                controller.generateOrderReport(startDate, endDate);
                break;
            default:
                printStream.println("Invalid choice. Please select a valid option.");
                break;
        }


    }

    private void performHelp() {
        if (this.controller.isAdmin()) {
            printStream.println("ADMIN HELP MESSAGE\n\n...");
        } else if (this.controller.isLogged()) {
            printStream.println("USER HELP MESSAGE\n\n...");
        } else {
            printStream.println("GUEST USER HELP MESSAGE\n\n...");
        }
    }
}
