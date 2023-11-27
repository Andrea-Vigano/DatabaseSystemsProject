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

    public String getSelectStatement(String table, String[] fields) {
        StringBuilder statement = new StringBuilder("SELECT ");
        for (int i = 0; i < fields.length; i++) {
            statement.append(fields[i]);
            if (i != fields.length - 1) statement.append(", ");
        }
        statement.append(" FROM ").append(table).append(";");
        return statement.toString();
    }

    public String getSelectStatement(String[] tables, String[] fields, String where) {
        StringBuilder statement = new StringBuilder("SELECT ");
        for (int i = 0; i < fields.length; i++) {
            statement.append(fields[i]);
            if (i != fields.length - 1) statement.append(", ");
        }
        statement.append(" FROM ");
        for (int i = 0; i < tables.length; i++) {
            statement.append(tables[i]);
            if (i != tables.length - 1) statement.append(", ");
        }
        statement.append(" WHERE ").append(where).append(";");
        return statement.toString();
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

    public String getDeleteStatement(String table, String where) {
        return "DELETE FROM " + table + " WHERE " + where + ";";
    }

    public String getUpdateStatement(String table, String[] columns, String[] fields, String where) {
        StringBuilder statement = new StringBuilder("UPDATE ").append(table).append(" SET ");
        for (int i = 0; i < columns.length || i < fields.length; i++) {
            statement.append(columns[i]).append("=").append(fields[i]);
            if (i != fields.length - 1 && i != columns.length - 1) statement.append(", ");
        }
        statement.append(" WHERE ").append(where).append(";");
        return statement.toString();
    }
}
