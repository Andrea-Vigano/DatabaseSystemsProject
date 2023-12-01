package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.User;

import java.io.PrintStream;

public class UserController extends Controller {
    private User user;
    public UserController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean addShippingAddress(String address) {
        String statement = sqlManager.getInsertStatement(
            "Address",
            new String[] { "address", "user_id" },
            new String[] { address, this.user.getUserID()}
        );
        printStream.println(statement);
//        try {
//            database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean removeShippingAddress(String id) {
        String statement = sqlManager.getDeleteStatement("Address", "address_id=" + id);
        printStream.println(statement);
//        try {
//            database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean showShippingAddresses() {
        return true;
    }

    public boolean comparePasswords(String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String statement = "UPDATE Users ";
        return true;
    }

    public boolean changePassword(String password) {
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
