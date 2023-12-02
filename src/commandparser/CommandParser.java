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
        } else if (command.isListCategory()) {
            this.wrapWithAuth(this::performList_Category);
        } else if(command.isListReports()){
            this.wrapWithAuth(this::performListReports);
        }else if (command.isSearchProducts()) {
            this.wrapWithAuth(this::performSearch);
        } else if (command.isAdminLogin()) {
            this.wrapWithLoginCheck(this::performAdminLogin);
        } else if (command.isAddProduct()) {
            this.wrapWithAdminAuth(this::performAddProduct);
        } else if (command.isUpdateProduct()) {
            this.wrapWithAuth(this::performUpdateProduct);
        } else if (command.isAddCategory()) {
            this.wrapWithAdminAuth(this::performAddCategory);
        } else if (command.isDeleteProduct()) {
            this.wrapWithAdminAuth(this::performDeleteProduct);
        } else if (command.isShowProduct()) {
            this.wrapWithAuth(this::performShowProduct);
        } else if (command.isShowCategory()) {
            this.wrapWithAuth(this::performShowCategory);
        } else if (command.isDeleteCategory()) {
            this.wrapWithAdminAuth(this::performDeleteCategory);
        } else if (command.isDeletePromotion()){
            this.wrapWithAuth(this::performDeletePromotion);
        }else if (command.isAddToCart()) {
            this.wrapWithAuth(this::performAddToCart);
        } else if(command.isAddPromotion()){
            this.wrapWithAuth(this::performAddPromotion);
        }else if (command.isShowCart()) {
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
    private void performList_Category() {
        boolean result = this.controller.listCategory();
        if (result) printStream.println("Successfully listed Categories");
        else printStream.println("Unable to list categories");
    }

    private void performListReports(){
        boolean result = this.controller.listReports();
        if (result) printStream.println("Successfully listed reports");
        else printStream.println("Unable to list reports");
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
        double review = Double.parseDouble(scanner.nextLine());
        printStream.print("Insert category name: ");
        String category = scanner.nextLine();
        String categoryID = this.controller.getCategoryId(category);
        boolean result = this.controller.addProduct(name, description, price, brand, quantity, supplier, warehouse, review, categoryID);
        if (result) printStream.println("Successfully added product");
        else printStream.println("Unable to add product");
    }
    private void performAddCategory() {
        printStream.print("Insert Category name: ");
        String name = scanner.nextLine();

        boolean result = this.controller.addCategory(name);
        if (result) printStream.println("Successfully added category");
        else printStream.println("Unable to add category ");
    }

    private void performUpdateProduct() {
        // TODO enhance update options
        printStream.print("Insert product product id: ");
        String productID = scanner.nextLine();
        String name = null, description = null, brand = null, supplier = null, warehouse = null;
        double price = 0.0, review = 0.0;
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
            review = Double.parseDouble(scanner.nextLine());
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
    private void performDeleteCategory() {
        printStream.print("Insert category id: ");
        String id = scanner.nextLine();
        boolean result = this.controller.deleteCategory(id);
        if (result) printStream.println("Successfully deleted category with id: " + id);
        else printStream.println("Unable to delete category with id: " + id);
    }

    private void performDeletePromotion(){
        printStream.println("Insert product id: ");
        String id = scanner.nextLine();
        boolean result = this.controller.deletePromotion(id);
        if (result) printStream.println("Successfully deleted promotion with product id: " + id);
        else printStream.println("Unable to delete category with product id: " + id);
    }

    private void performShowProduct() {
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        this.controller.showProduct(id);
    }
    private void performShowCategory() {
        printStream.print("Insert category id: ");
        String id = scanner.nextLine();
        this.controller.showCategory(id);
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

    private void performAddPromotion(){
        printStream.print("Insert product id: ");
        String id = scanner.nextLine();
        printStream.print("Insert discount percentage: ");
        int percentage = Integer.parseInt(scanner.nextLine());
        if(percentage > 100){
            printStream.println("Percentage cannot be more than 100%. Please enter your percentage again!");
            printStream.print("Insert discount percentage: ");
            percentage = Integer.parseInt(scanner.nextLine());
        }
        printStream.print("Insert the start of active date for your promotion in (YYYY-MM-DD) format: ");
        Date startDate = Date.valueOf(scanner.nextLine());
        printStream.print("Insert the end of active date for your promotion in (YYYY-MM-DD) format: ");
        Date endDate = Date.valueOf(scanner.nextLine());
        boolean result = this.controller.addPromotion(id, percentage, startDate, endDate);
        if (result) printStream.println("Promotion is successfully added");
        else printStream.println("Unable to add promotion");
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
        boolean result = this.controller.generateReport();
        if (result) printStream.println("Report is successfully generated");
        else printStream.println("Unable to generate report");
    }


    private void performHelp() {
        if (this.controller.isAdmin()) {
            System.out.println(" Hello I am your personal assistant, here's a small guide for you  ");
            System.out.println("   ___");
            System.out.println("  (o o)");
            System.out.println(" /  V  \\");
            System.out.println("/(  _  )\\");
            System.out.println("_^^__^^__ ");
            System.out.println(" ");

            System.out.println("1. login");
            System.out.println("2. signup");
            System.out.println("3. logout");
            System.out.println("4. change-password");
            System.out.println("5. list-products");
            System.out.println("6. search-product");
            System.out.println("7. admin-login");
            System.out.println("8. add-product");
            System.out.println("9. update-product");
            System.out.println("10. delete-product");
            System.out.println("11. show-product");
            System.out.println("12. generate-report");
            System.out.println("13. list-category");
            System.out.println("14. add-category");
            System.out.println("15. delete-category");
            System.out.println("16. show-category");
            System.out.println("17. generate-report");
            System.out.println("18. list-report");
            System.out.println("19. add-promotion");
            System.out.println("20. delete-promotion");
            System.out.println("21. exit");
        } else if (this.controller.isLogged()) {
            System.out.println(" Hello I am your personal assistant, here's a small guide for you  ");
            System.out.println("   ___");
            System.out.println("  (o o)");
            System.out.println(" /  V  \\");
            System.out.println("/(  _  )\\");
            System.out.println("_^^__^^__ ");
            System.out.println(" ");

            System.out.println("1. logout");
            System.out.println("2. change-password");
            System.out.println("3. show-shipping-addresses");
            System.out.println("4. add-shipping-addresses");
            System.out.println("5. delete-shipping-addresses");
            System.out.println("6. list-products");
            System.out.println("7. list-category");
            System.out.println("8. search-product");
            System.out.println("9. show-product");
            System.out.println("10. show-category");
            System.out.println("11. add-to-cart");
            System.out.println("12. show-cart");
            System.out.println("13. checkout");
        } else {
            System.out.println(" Hello I am your personal assistant, here's a small guide for you  ");
            System.out.println("   ___");
            System.out.println("  (o o)");
            System.out.println(" /  V  \\");
            System.out.println("/(  _  )\\");
            System.out.println("_^^__^^__ ");
            System.out.println(" ");

            System.out.println("1. login");
            System.out.println("2. signup");
        }
    }
}
