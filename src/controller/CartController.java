package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.ShoppingCart;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
                new String[] { String.valueOf(new Date()), convert(userId), convert(shippingAddressId) }
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
        int currentQuantity = getCurrentQuantity(id);
        if (currentQuantity < quantity || currentQuantity == -1) return false;
        String insertStatement = sqlManager.getInsertStatement(
                "ShoppingCart",
                new String[] { "quantity", "dateAdded", "userID", "productID" },
                new String[] { Integer.toString(quantity), String.valueOf(new Date()), convert(userId), convert(id) }
        );
        String updateStatement = sqlManager.getUpdateStatement(
                "Product",
                new String[] { "quantity" },
                new String[] { String.valueOf(currentQuantity - quantity) },
                "productID = " + id
        );
        try {
            database.update(insertStatement);
            database.update(updateStatement);
            database.commit();
        } catch (SQLException ignored) {
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
}
