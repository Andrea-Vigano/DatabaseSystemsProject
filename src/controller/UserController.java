package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.User;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class UserController extends Controller {
    private User user;
    public UserController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean addShippingAddress(String address) {
        int newID = getLastedAddressID();
        String statement = sqlManager.getInsertStatement(
            "Address",
            new String[] { "name", "userID", "addressID" },
            new String[] { convert(address), convert(this.user.getUserID()), convert(String.valueOf(newID)) }
        );
        printStream.println(statement);
        try {
            database.update(statement);
            // database.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean removeShippingAddress(String id) {
        String statement = sqlManager.getDeleteStatement("Address", "addressID=" + id);
        try {
            database.update(statement);
            // database.commit();
        } catch (SQLException e) {
            database.abort();
            return false;
        }
        return true;
    }

    public void showShippingAddresses() {
        String statement = sqlManager.getSelectStatement(
                "Address",
                new String[] { "name", "addressID" },
                "userID = " + this.user.getUserId()
        );
        try {
            ResultSet results = database.query(statement);
            int i = 0;
            while (results.next()) {
                String address = results.getString("name");
                String id = results.getString("addressID");
                printStream.printf("%s. %s\n", id, address);
                i++;
            }
            if (i == 0) { // No addresses printed
                printStream.println("No address to show for current user");
            }
        } catch (SQLException ignored) { }
    }

    public boolean comparePasswords(String password) {
        String passwordHash = AuthenticationController.sha256(password);
        return Objects.equals(this.user.getPasswordHash(), passwordHash);
    }

    public boolean changePassword(String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String statement = sqlManager.getUpdateStatement(
                "Users",
                new String[] { "passwordHash" },
                new String[] { "'" + passwordHash + "'" },
                "userID = " + this.user.getUserId()
        );
        printStream.println(statement);
        try {
            database.update(statement);
            //database.commit();
        } catch (SQLException e) {
            printStream.println(e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
            database.abort();
            return false;
        }
        return true;
    }

    private int getLastedAddressID() {
        String statement = "SELECT NVL(MAX(addressID), 0) AS maxAddressID FROM Address";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxAddressID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
