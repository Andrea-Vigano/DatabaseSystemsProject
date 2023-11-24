package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationController extends Controller {
    private boolean isLogged = false;

    public AuthenticationController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }
    public boolean logIn(String username, String password) {
        String where = "username=" + username + " AND password=" + password;
        String statement = sqlManager.getSelectStatement("User", where);
        printStream.println(statement);
//        try {
//            ResultSet results = database.query(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        isLogged = true;
        return true;
    }

    public boolean singUp(String name, String username, String password, String email) {
        String[] columns = new String[]{ "name", "username", "password", "email" };
        String[] fields = new String[]{ name, username, password, email };
        String statement = sqlManager.getInsertStatement("User", columns, fields);
        printStream.println(statement);
//        try {
//            database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        isLogged = true;
        return true;
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public boolean logOut() {
        isLogged = false;
        return true;
    }
}
