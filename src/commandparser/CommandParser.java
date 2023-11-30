package commandparser;

import controller.MainController;

import java.io.PrintStream;
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
            // Main loop will break on its own in App class
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
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        boolean result = this.controller.logIn(username, password);
        if (result) printStream.println("Successfully logged in");
        else printStream.println("Unable to log in");
    }

    private void performSignUp() {
        printStream.print("Insert your name: ");
        String name = scanner.nextLine();
        printStream.print("Insert your username: ");
        String username = scanner.nextLine();
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        printStream.print("Insert your email: ");
        String email = scanner.nextLine();
        boolean result = this.controller.signUp(name, username, password, email);
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
        // TODO Add non-mandatory fields options
        printStream.print("Insert product name: ");
        String name = scanner.nextLine();
        printStream.print("Insert product description: ");
        String description = scanner.nextLine();
        printStream.print("Insert product specifications: ");
        String specifications = scanner.nextLine();
        printStream.print("Insert product price: ");
        double price = Double.parseDouble(scanner.nextLine());
        printStream.print("Insert product brand: ");
        String brand = scanner.nextLine();
        printStream.print("Insert product category name: ");
        String category = scanner.nextLine();
        printStream.print("Insert product warehouse name: ");
        String warehouse = scanner.nextLine();
        printStream.print("Insert product supplier name: ");
        String supplier = scanner.nextLine();
        printStream.print("Insert product rating: ");
        String rating = scanner.nextLine();
        // change return type ...
        boolean categoryId = this.controller.getCategoryId(category);
        // ...
    }

    private void performUpdateProduct() {
        // TODO enhance update options
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        String name = null, description = null, specifications = null, brand = null;
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
        printStream.print("Do you want to update the product specifications [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert specifications: ");
            specifications = scanner.nextLine();
        }
        printStream.print("Do you want to update the product brand [Y/N]? ");
        isYes = scanner.nextLine().toLowerCase();
        if (Objects.equals(isYes, "y")) {
            printStream.print("Insert brand: ");
            brand = scanner.nextLine();
        }
        boolean result = this.controller.updateProduct(id, name, description, specifications, brand);
        if (result) printStream.println("Successfully updated product with id: " + id);
        else printStream.println("Unable to update product with id: " + id);
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
