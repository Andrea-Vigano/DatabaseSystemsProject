package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.User;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController extends Controller {
    private User user;

    public UserController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean addShippingAddress(String address) {
        String userId = this.user.getUserID();
        String statement = sqlManager.getInsertStatement(
                "Address",
                new String[]{"user_id", "address"},
                new String[]{userId, address}
        );
        printStream.println(statement);
         try {
             database.update(statement);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
        return true;
    }

    public boolean removeShippingAddress(String id) {
        String statement = sqlManager.getDeleteStatement("Address", "address_id=" + id);
        printStream.println(statement);
         try {
             database.update(statement);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
        return true;
    }

    public List<String> showShippingAddresses() {
        List<String> addresses = new ArrayList<>();
        String userId = this.user.getUserID();
        String query = "SELECT address FROM Address WHERE user_id = '" + userId + "'";
        try {
            ResultSet resultSet = database.query(query);
            while (resultSet.next()) {
                String address = resultSet.getString("address");
                addresses.add(address);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addresses;
    }

    public boolean comparePasswords(String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String userId = this.user.getUserID();
        String query = "SELECT 1 FROM Users WHERE user_id = '" + userId + "' AND passwordhash = '" + passwordHash + "'";
        try {
            ResultSet resultSet = database.query(query);
            boolean passwordMatches = resultSet.next();
            resultSet.close();
            return passwordMatches;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changePassword(String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String userId = this.user.getUserID();
        String statement = "UPDATE Users SET passwordhash = '" + passwordHash + "' WHERE user_id = '" + userId + "'";
        printStream.println(statement);
         try {
             database.update(statement);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
        return true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}