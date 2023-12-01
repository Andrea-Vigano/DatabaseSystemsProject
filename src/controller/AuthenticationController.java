package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.User;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AuthenticationController extends Controller {
    private boolean isLogged = false;
    private boolean isAdmin = false;

    public AuthenticationController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public User logIn(String username, String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String where = "username=" + "'" + username + "'" + " AND passwordHash=" + "'" + passwordHash + "'";
        String statement = sqlManager.getSelectStatement(
                new String[]{"Users"},
                new String[]{"userID", "name", "username", "passwordHash", "email", "phoneNumber" },
                where
        );
        String checkStatement = sqlManager.getSelectStatement(
                new String[]{"Users"},
                new String[]{ "COUNT(*) AS count"},
                where
        );
        printStream.println(statement);
        printStream.println(checkStatement);
        try {
            ResultSet results = database.query(statement);
            ResultSet cntResults = database.query(checkStatement);

            if (results.next() && cntResults.next()) {
                int count = cntResults.getInt("count");
                if (count == 1) {
                    isLogged = true;
                    String userID = results.getString("userID");
                    String name = results.getString("name");
                    String _username = results.getString("username");
                    String _passwordHash = results.getString("passwordHash");
                    String email = results.getString("email");
                    String phoneNumber = results.getString("phoneNumber");
                    return new User(userID, name, _username, _passwordHash, email, phoneNumber);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean adminLogIn(String username, String password) {
        String passwordHash = AuthenticationController.sha256(password);
        String where = "username=" + "'" + username + "'" + " AND passwordHash=" + "'" + passwordHash + "'";
        String statement = sqlManager.getSelectStatement(
                new String[]{"Users"},
                new String[]{"userID", "name", "username", "passwordHash", "email", "phoneNumber" },
                where
        );
        String checkStatement = sqlManager.getSelectStatement(
                new String[]{"Admin"},
                new String[]{"COUNT(*) AS count"},
                where
        );
        printStream.println(statement);
        printStream.println(checkStatement);
        try {
            ResultSet results = database.query(statement);
            ResultSet cntResults = database.query(checkStatement);
            if (results.next() && cntResults.next()) {
                int count = results.getInt("count");
                if (count == 1) {
                    isLogged = true;
                    isAdmin = true;
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public User singUp(String name, String username, String password, String email, String phoneNumber) {
        int userID = getLatestUserID();
        String[] columns = new String[]{"userID" ,"name", "username", "passwordHash", "email", "phoneNumber" };
        String[] fields = new String[]{String.valueOf(userID), name, username, AuthenticationController.sha256(password), email, phoneNumber };
        String statement = sqlManager.getInsertStatement("Users", columns, fields);
        printStream.println(statement);
        try {
            database.update(statement);
            isLogged = true;
            return new User(Objects.toString(userID), name, username, AuthenticationController.sha256(password), email, phoneNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean logOut() {
        isLogged = false;
        isAdmin = false;
        return true;
    }

    private int getLatestUserID() {
        String statement = "SELECT NVL(MAX(userID), 0) AS maxUserID FROM Users";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxUserID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public static String sha256(final String base) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}