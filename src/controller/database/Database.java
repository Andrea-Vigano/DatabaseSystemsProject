package controller.database;

import oracle.jdbc.OracleDriver;
import oracle.jdbc.driver.OracleConnection;

import java.sql.*;

public class Database {
    private static OracleConnection connection;

    private static final String username = "";
    private static final String password = "";
    private static final String url = "jdbc:oracle:thin:@csdoor.comp.polyu.edu.hk:FUCK:dbms";
    private static final Database instance;
    static {
        try {
            instance = new Database();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Database() throws SQLException {
        // connect();
    }

    private void connect() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        connection = (OracleConnection) DriverManager.getConnection(url, username, password);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public static Database get() {
        return Database.instance;
    }

    public ResultSet query(String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sqlStatement);
    }

    public void update(String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlStatement);
    }

    public void commit() throws SQLException {
        connection.commit();
    }
}
