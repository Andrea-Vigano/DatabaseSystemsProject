package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.ShoppingCart;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;

public class CartController extends Controller {
    public CartController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean isEmpty() {
        String statement = sqlManager.getSelectStatement("ShoppingCart", new String[] { "cartID" });
        try {
            ResultSet result = database.query(statement);
            int i = 0;
            while (result.next()) i++;
            return i == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ShoppingCart> list() throws SQLException {
        String statement = sqlManager.getSelectStatement(
                new String[] { "ShoppingCart", "Product" },
                new String[] { "cartID", "quantity", "dateAdded", "name", "ShoppingCart.productID" },
                "ShoppingCart.productID = Product.productID"
        );
        ArrayList<ShoppingCart> list = new ArrayList<>();
        ResultSet result = database.query(statement);
        while (result.next()) {
            String id = result.getString("cartID");
            int quantity = result.getInt("quantity");
            Date dateAdded = result.getDate("dateAdded");
            String productName = result.getString("name");
            String productID = result.getString("productID");
            list.add(new ShoppingCart(id, quantity, dateAdded, productName, productID));
        }
        return list;
    }

    public void show() {
        try {
            ArrayList<ShoppingCart> list = this.list();
            for (ShoppingCart item : list)
                printStream.printf(
                        "%s. %s. Quantity: %d. Added on: %s%n\n",
                        item.getId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getDateAdded()
                );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkout(String shippingAddressId, String userId) {
        String statement = sqlManager.getInsertStatement(
                "Orders",
                new String[] { "dateCreated", "userID", "addressID" },
                new String[] { convert(String.valueOf(new Date(System.currentTimeMillis()))), convert(userId), convert(shippingAddressId) }
        );
        try {
            ArrayList<String> keys = database.insertAndGetKeys(statement);
            if (keys.isEmpty()) {
                database.abort();
                return false;
            }

            String orderId = keys.get(0);
            ArrayList<ShoppingCart> list = this.list();

            String[][] fields = new String[list.size()][3];

            for (int i = 0; i < list().size(); i++) {
                fields[i][0] = String.valueOf(list.get(i).getQuantity());
                fields[i][1] = list.get(i).getProductID();
                fields[i][2] = orderId;
            }
            statement = sqlManager.getInsertStatement(
                    "OrderLineItems",
                    new String[] { "quantity", "productID", "orderID" },
                    fields
            );

            database.update(statement);

            if (flush()) {
                database.commit();
                return true;
            } else {
                database.abort();
                return false;
            }
        } catch (SQLException ignored) {
            database.abort();
            return false;
        }
    }

    private boolean flush() {
        String statement = sqlManager.getFlushStatement("ShoppingCart");
        try {
            database.update(statement);
        } catch (SQLException ignored) {
            return false;
        }
        return true;
    }

    public boolean add(String id, int quantity, String userId) {
        int newID = getLastedCartID();
        int currentQuantity = getCurrentQuantity(id);
        if (currentQuantity < quantity || currentQuantity == -1) return false;
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Define the Oracle SQL date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time
        String formattedDateTime = now.format(formatter);

        String insertStatement = "INSERT INTO ShoppingCart (cartID, quantity, dateAdded, userID, productID) " +
                "VALUES (" + convert(String.valueOf(newID)) + ", " + Integer.toString(quantity) +
                ", TO_DATE(" + convert(formattedDateTime) + ", 'YYYY-MM-DD HH24:MI:SS'), " +
                convert(userId) + ", " + convert(id) + ")";

        String updateStatement = sqlManager.getUpdateStatement(
                "Product",
                new String[] { "quantity" },
                new String[] { String.valueOf(currentQuantity - quantity) },
                "productID = " + id
        );
        printStream.println(insertStatement);
        printStream.println(updateStatement);
        try {
            database.update(insertStatement);
            database.update(updateStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private int getCurrentQuantity(String id) {
        String statementForQuantityCheck = sqlManager.getSelectStatement(
                "Product",
                new String[] { "quantity" }, "productID = " + id
        );
        try {
            ResultSet result = database.query(statementForQuantityCheck);
            if (result.next()) {
                return result.getInt("quantity");
            }
        } catch (SQLException ignored) { }
        return -1;
    }

    private int getLastedCartID() {
        String statement = "SELECT NVL(MAX(productID), 0) AS maxCartID FROM ShoppingCart";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxCartID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
