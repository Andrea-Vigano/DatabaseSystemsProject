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
import java.util.Arrays;
import java.util.Objects;

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

    private ArrayList<ShoppingCart> list(String userId) throws SQLException {
        String statement = sqlManager.getSelectStatement(
                new String[] { "ShoppingCart", "Product", "Users" },
                new String[] { "ShoppingCart.cartID", "ShoppingCart.quantity", "ShoppingCart.dateAdded", "Product.name", "Product.price", "ShoppingCart.productID", "ShoppingCart.userID" },
                "ShoppingCart.productID = Product.productID AND ShoppingCart.userID = Users.userID AND ShoppingCart.userID = " + userId
        );
        printStream.println(statement);
        ArrayList<ShoppingCart> list = new ArrayList<>();
        ResultSet result = database.query(statement);
        while (result.next()) {
            String id = result.getString("cartID");
            int quantity = result.getInt("quantity");
            Date dateAdded = result.getDate("dateAdded");
            String productName = result.getString("name");
            double productPrice = result.getDouble("price");
            String productID = result.getString("productID");
            list.add(new ShoppingCart(id, quantity, dateAdded, productName, productPrice, productID));
        }
        return list;
    }

    private String compareWarehouse(String userId) {
        String statement = sqlManager.getSelectStatement(
                new String[] { "ShoppingCart", "Product", "Users" },
                new String[] { "ShoppingCart.cartID", "ShoppingCart.quantity", "ShoppingCart.productID", "ShoppingCart.userID", "Product.warehouse" },
                "ShoppingCart.productID = Product.productID AND ShoppingCart.userID = Users.userID AND ShoppingCart.userID = " + userId
        );
        printStream.println(statement);
        try {
            int max = 0;
            String res = "";
            ResultSet result = database.query(statement);
            while (result.next()) {
                int quantity = result.getInt("quantity");
                if(max <= quantity){
                    String warehouse = result.getString("warehouse");
                    max = quantity;
                    res = warehouse;
                }
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void show(String userId) {
        try {
            ArrayList<ShoppingCart> list = this.list(userId);
            for (ShoppingCart item : list) {
                printStream.printf(
                        "\n%s.%s\n  Quantity: %d\n  Calculated Price: %.1f\n  Added on: %s%n\n",
                        item.getId(),
                        item.getProductName(),
                        item.getQuantity(),
                        (item.getProductPrice() * item.getQuantity()),
                        item.getDateAdded()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkout(String shippingAddressId, String userId) {
        int newID = getLastedOrderID();
        String warehouse = this.compareWarehouse(userId);
        printStream.println(warehouse);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String insertStatement = "INSERT INTO Orders (orderID, dateCreated, totalprice, warehouse, userID, addressID) " +
                "VALUES (" + convert(Objects.toString(newID)) + ", " +
                "TO_DATE(" + convert(formattedDateTime) + ", 'YYYY-MM-DD HH24:MI:SS'), " + 0.0 + ", " + convert(warehouse) +
                ", " + convert(userId) + ", " + convert(shippingAddressId) + ")";
        printStream.println(insertStatement);
        try {
            database.update(insertStatement);
            if (newID == 0) {
                database.abort();
                return false;
            }
            String orderId = String.valueOf(newID);
            ArrayList<ShoppingCart> list = this.list(userId);

            String[][] fields = new String[list.size()][5];
            int orderLineItemsID = getLastedOrderLineItemsID();
            double totalprice = 0.0;

            for (int i = 0; i < list(userId).size(); i++) {
                fields[i][0] = convert(String.valueOf(orderLineItemsID));
                fields[i][1] = String.valueOf(list.get(i).getProductPrice() * list.get(i).getQuantity());
                fields[i][2] = String.valueOf(list.get(i).getQuantity());
                fields[i][3] = convert(list.get(i).getProductID());
                fields[i][4] = convert(orderId);

                totalprice += (list.get(i).getProductPrice() * list.get(i).getQuantity());
                orderLineItemsID++;
            }
            printStream.println(Arrays.deepToString(fields));
            for(int i = 0; i < fields.length; i++){
                String statement = sqlManager.getInsertStatement(
                        "OrderLineItems",
                        new String[]{"orderLineItemID", "price" ,"quantity", "productID", "orderID"},
                        fields[i]
                );
                database.update(statement);
            }

            insertStatement = "UPDATE ORDERS SET totalprice=" + totalprice + "WHERE orderID=" + orderId;
            printStream.println(insertStatement);
            database.update(insertStatement);
            if (flush()) {
                //database.commit();
                return true;
            } else {
                //database.abort();
                return false;
            }
        } catch (SQLException e) {
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
        double currentPrice = getCurrentPrice(id);
        if (currentQuantity < quantity || currentQuantity == -1 || currentPrice == -1) return false;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String insertStatement = "INSERT INTO ShoppingCart (cartID, quantity, price, dateAdded, userID, productID) " +
                "VALUES (" + convert(Objects.toString(newID)) + ", " + quantity + ", " + (currentPrice * quantity) +
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
            database.abort();
            return false;
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

    private double getCurrentPrice(String id) {
        String statementForQuantityCheck = sqlManager.getSelectStatement(
                "Product",
                new String[] { "price" }, "productID = " + id
        );
        try {
            ResultSet result = database.query(statementForQuantityCheck);
            if (result.next()) {
                return result.getDouble("price");
            }
        } catch (SQLException ignored) { }
        return -1;
    }

    private int getLastedCartID() {
        String statement = "SELECT NVL(MAX(cartID), 0) AS maxCartID FROM ShoppingCart";
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
    private int getLastedOrderID() {
        String statement = "SELECT NVL(MAX(orderID), 0) AS maxOrderID FROM Orders";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxOrderID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
    private int getLastedOrderLineItemsID() {
        String statement = "SELECT NVL(MAX(orderLineItemID), 0) AS maxOrderLineItemID FROM OrderLineItems";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxOrderLineItemID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
