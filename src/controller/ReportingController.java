package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.Report;

import java.io.PrintStream;
import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;

// Need to check

public class ReportingController extends Controller {
    public ReportingController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }
    public boolean generateReport(String adminID){
        ArrayList<Integer> arrayList = getProductLength();
        try {
            for (int i = arrayList.get(1); i <= arrayList.get(0); i++) {
                String currentProductName = getProductName(String.valueOf(i));
                String statement = sqlManager.getInsertStatement(
                        "Report",
                        new String[]{"reportID", "name", "revenue", "sales", "adminID"},
                        new String[]{convert(String.valueOf(i)), convert(currentProductName),
                                String.valueOf(generateRevenueReport(String.valueOf(i))),
                                String.valueOf(generateSalesReport(String.valueOf(i))), adminID
                        }
                );
                database.update(statement);
            }
            list();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private double generateRevenueReport(String id){
        String statement = sqlManager.getSelectStatement(
                new String[] { "OrderLineItems" },
                new String[] { "SUM(price) AS totalRevenue" },
                "productID=" + convert(id)
        );
        printStream.println(statement);
        try{
            ResultSet result = database.query(statement);
            if(result.next()){
                double totalRevenue = result.getDouble("totalRevenue");
                return totalRevenue;
            }
        }
        catch(SQLException e){
            printStream.println("Error occurred while generating revenue report: " + e.getMessage());
        }
        return -1;
    }

    private int generateSalesReport(String id){
        String statement = sqlManager.getSelectStatement(
                new String[] { "OrderLineItems" },
                new String[] { "SUM(quantity) AS totalSum" },
                "productID=" + convert(id)
        );
        printStream.println(statement);
        try{
            ResultSet result = database.query(statement);
            if(result.next()){
                int totalQuantity = result.getInt("totalSum");
                return totalQuantity;
            }
        }
        catch(SQLException e){
            printStream.println("Error occurred while generating sales report: " + e.getMessage());
        }
        return -1;
    }

    private ArrayList<Integer> getProductLength() {
        String statementMax = sqlManager.getSelectStatement(
                "OrderLineItems",
                new String[] { "MAX(productID) AS maxProduct" }
        );
        String statementMin = sqlManager.getSelectStatement(
                "OrderLineItems",
                new String[] { "MIN(productID) AS minProduct" }
        );
        try {
            ResultSet resultMax = database.query(statementMax);
            ResultSet resultMin = database.query(statementMin);
            if (resultMax.next() && resultMin.next()) {
                ArrayList<Integer> result = new ArrayList<>();
                result.add(resultMax.getInt("maxProduct"));
                result.add(resultMin.getInt("minProduct"));
                return result;
            }
        } catch (SQLException ignored) { }
        return null;
    }

    private String getProductName(String id) {
        String statementForQuantityCheck = sqlManager.getSelectStatement(
                "Product",
                new String[] { "name" }, "productID = " + id
        );
        try {
            ResultSet result = database.query(statementForQuantityCheck);
            if (result.next()) {
                return result.getString("name");
            }
        } catch (SQLException ignored) { }
        return null;
    }

    public boolean list() {
        String statement = sqlManager.getSelectStatement(
                new String[] { "Report", "Admin"},
                new String[] { "Report.reportID", "Report.name", "Report.revenue", "Report.sales", "Admin.adminID" },
                "Report.adminID = Admin.adminID"
        );
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
            int i = 0;
            while (results.next()) {
                String id = results.getString("reportID");
                i = printProduct(id, results, i);
            }
            if (i == 0) {
                printStream.println("No reports to show");
            }
            results.close();
        } catch (SQLException e) {
            database.abort();
            return false;
        }
        return true;
    }
    private int printProduct(String id, ResultSet results, int i) throws SQLException {
        String name = results.getString("name");
        double revenue = results.getDouble("revenue");
        int sales = results.getInt("sales");
        printStream.printf(
                "\n%s.%s:\n  Revenue: %.1f\n  Sales: %d\n\n",
                id, name, revenue, sales
        );
        i++;
        return i;
    }
}
