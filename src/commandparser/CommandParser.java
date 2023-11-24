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
            performLogOut();
        } else if (command.isList()) {
            this.wrapWithAuth(this::performList);
        } else if (command.isSearch()) {
            this.wrapWithAuth(this::performSearch);
        } else if (command.isAdminLogin()) {
            this.wrapWithLoginCheck(this::performAdminLogin);
        } else if (command.isAddProduct()) {
            this.wrapWithAdminAuth(this::performAddProduct);
        } else {
            this.printStream.println("Unknown command");
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

    private void performList() {
        boolean result = this.controller.listProducts();
        if (result) printStream.println("Successfully listed products");
        else printStream.println("Unable to list products");
    }

    private void performSearch() {
        String category = "", brand = "";
        double lowestPrice = 0.0, highestPrice = 0.0;
        printStream.print("Do you want to search products by category [Y/N]? ");
        String isCategory = scanner.nextLine().toLowerCase();
        if (Objects.equals(isCategory, "y")) {
            printStream.print("Insert category: ");
            category = scanner.nextLine();
        }
        printStream.print("Do you want to search products by brand [Y/N]? ");
        String isBrand = scanner.nextLine().toLowerCase();
        if (Objects.equals(isBrand, "y")) {
            printStream.print("Insert brand: ");
            brand = scanner.nextLine();
        }
        printStream.print("Do you want to search products by priceRange [Y/N]? ");
        String isPriceRange = scanner.nextLine().toLowerCase();
        if (Objects.equals(isPriceRange, "y")) {
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
}
