package controller.database;

public class SQLManager {
    public String getSelectStatement(String table, String[] fields, String where) {
        StringBuilder statement = new StringBuilder("SELECT ");
        for (int i = 0; i < fields.length; i++) {
            statement.append(fields[i]);
            if (i != fields.length - 1) statement.append(", ");
        }
        statement.append(" FROM ").append(table).append(" WHERE ").append(where).append(";");
        return statement.toString();
    }

    public String getSelectStatement(String table, String where) {
        return "SELECT * FROM " + table + " WHERE " + where + ";";
    }

    public String getInsertStatement(String table, String[] columns, String[] fields) {
        StringBuilder statement = new StringBuilder("INSERT INTO ").append(table).append(" (");
        for (int i = 0; i < columns.length; i++) {
            statement.append(columns[i]);
            if (i != columns.length - 1) statement.append(", ");
        }
        statement.append(") VALUES (");
        for (int i = 0; i < fields.length; i++) {
            statement.append(fields[i]);
            if (i != fields.length - 1) statement.append(", ");
        }
        statement.append(");");
        return statement.toString();
    }
}
