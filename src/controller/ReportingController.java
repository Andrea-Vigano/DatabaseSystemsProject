package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.Report;

import java.io.PrintStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

// Need to check

public class ReportingController extends Controller {
    public ReportingController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }
    public boolean generateReport(String adminID){
        ArrayList<Integer> arrayList = getProductLength();
        ArrayList<Integer> arrayList1 = getRecordLength();
        try {
            for (int i = arrayList.get(1); i <= arrayList.get(0); i++) {
                if(arrayList1.get(0) != null && i <= arrayList1.get(0)) continue;
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

    private ArrayList<Integer> getRecordLength() {
        String statementMax = sqlManager.getSelectStatement(
                "Report",
                new String[] { "MAX(reportID) AS maxReport" }
        );
        String statementMin = sqlManager.getSelectStatement(
                "Report",
                new String[] { "MIN(reportID) AS minReport" }
        );
        try {
            ResultSet resultMax = database.query(statementMax);
            ResultSet resultMin = database.query(statementMin);
            if (resultMax.next() && resultMin.next()) {
                ArrayList<Integer> result = new ArrayList<>();
                result.add(resultMax.getInt("maxReport"));
                result.add(resultMin.getInt("minReport"));
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
    // Product.price - Promotion.Discount * Product.price
    public boolean addPromotion(String id, int percentage, Date startDate, Date endDate){
        String productName = getProductName(id);
        String statement = "INSERT INTO Promotion (productID, productName, startDate, endDate, discountPercentage) " +
                "VALUES (" + convert(id) + ", " + convert(productName) +
                ", TO_DATE(" + convert(String.valueOf(startDate)) + ", 'YYYY-MM-DD'), " +
                "TO_DATE(" + convert(String.valueOf(endDate)) + ", 'YYYY-MM-DD'), " + percentage + ")";
        printStream.println(statement);
        try {
            database.update(statement);
            changePrice(id, -1);
        } catch (SQLException e) {
            database.abort();
            return false;
        }
        return true;
    }

    public boolean delete_promotion(String id){
        String statement = sqlManager.getDeleteStatement("Promotion", "productID=" + id);
        printStream.println(statement);
        try {
            for(Map.Entry<String, Double> entry : priceStore.entrySet()){
                if(entry.getKey().equals(id)){
                    changePrice(id, entry.getValue());
                }
            }
            database.update(statement);
        } catch (SQLException e) {
            database.abort();
            return false;
        }
        return true;
    }

    public int getDiscountPercentage(String id){
        String statementForQuantityCheck = sqlManager.getSelectStatement(
                "Promotion",
                new String[] { "discountPercentage" }, "productID = " + id
        );
        try {
            ResultSet result = database.query(statementForQuantityCheck);
            if (result.next()) {
                return result.getInt("discountPercentage");
            }
        } catch (SQLException ignored) { }
        return 1;
    }

    public void changePrice(String id, double currPrice){
        int currPercentage = getDiscountPercentage(id);
        String statement = sqlManager.getSelectStatement(
                new String[] {"Product", "Promotion"},
                new String[]{"Product.price", "Product.productID" ,"Promotion.productID"},
                "Promotion.productID = Product.productID"
        );
        printStream.println(statement);
        try {
           ResultSet resultSet = database.query(statement);
           if(resultSet.next()) {
               if (currPrice == -1) {
                   double price = resultSet.getDouble("price");
                   statement = sqlManager.getUpdateStatement(
                           "Product",
                           new String[]{"price"},
                           new String[]{convert(String.valueOf(price - (price * currPercentage / 100)))},
                           "productID=" + id
                   );
                   printStream.println(statement);
                   database.update(statement);
                   priceStore.put(id, price);
               } else{
                   statement = sqlManager.getUpdateStatement(
                           "Product",
                           new String[]{"price"},
                           new String[]{convert(String.valueOf(currPrice))},
                           "productID=" + id
                   );
                   printStream.println(statement);
                   database.update(statement);
                   priceStore.put(id, currPrice);
               }
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
