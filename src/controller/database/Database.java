package controller.database;

import oracle.jdbc.OracleDriver;
import oracle.jdbc.driver.OracleConnection;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static OracleConnection connection;

    private static final String username = "\"22096786d\"";
    private static final String password = "vutgbdgo";
    private static final String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
    private static final Database instance;
    static {
        try {
            instance = new Database();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Database() throws SQLException {
        connect();
        System.out.println("Connected to DB");
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
        statement.execute("COMMIT");

    }

    public ArrayList<String> insertAndGetKeys(String sqlStatement) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlStatement, Statement.RETURN_GENERATED_KEYS);
        ResultSet result = statement.getGeneratedKeys();
        ArrayList<String> keys = new ArrayList<>();
        while (result.next()) keys.add(String.valueOf(result.getLong(1)));
        return keys;
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void abort() {
        try {
            connection.abort();
        } catch (SQLException ignored) { }
    }
}
