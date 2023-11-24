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
            performLogin();
        } else if (command.isSignup()) {
            performSignUp();
        } else if (command.isLogout()) {
            performLogOut();
        } else if (command.isList()) {
            this.wrapWithAuth(this::performList);
        } else if (command.isSearch()) {
            this.wrapWithAuth(this::performSearch);
        }
    }

    private void wrapWithAuth(Runnable function) {
        if (!this.controller.isLogged()) printStream.println("User not logged in");
        else function.run();
    }

    private void performLogin() {
        printStream.print("Insert you username: ");
        String username = scanner.nextLine();
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        boolean result = this.controller.logIn(username, password);
        if (result) printStream.println("Successfully logged in");
        else printStream.println("Unable to log in");
    }

    private void performSignUp() {
        printStream.print("Insert you name: ");
        String name = scanner.nextLine();
        printStream.print("Insert you username: ");
        String username = scanner.nextLine();
        printStream.print("Insert your password: ");
        String password = scanner.nextLine();
        printStream.print("Insert you email: ");
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
        String isCategory = scanner.next();
        if (Objects.equals(isCategory, "Y")) {
            printStream.println("Insert category: ");
            category = scanner.nextLine();
        }
        printStream.print("Do you want to search products by brand [Y/N]? ");
        String isBrand = scanner.next();
        if (Objects.equals(isBrand, "Y")) {
            printStream.println("Insert brand: ");
            brand = scanner.nextLine();
        }
        printStream.print("Do you want to search products by priceRange [Y/N]? ");
        String isPriceRange = scanner.next();
        if (Objects.equals(isPriceRange, "Y")) {
            printStream.println("Insert lowest price: ");
            lowestPrice = Double.parseDouble(scanner.nextLine());
            printStream.println("Insert highest price: ");
            highestPrice = Double.parseDouble(scanner.nextLine());
        }
        boolean result = this.controller.searchProducts(category, brand, lowestPrice, highestPrice);
        if (result) printStream.println("Successfully searched products");
        else printStream.println("Unable to search products");
    }
}
