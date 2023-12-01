package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.sql.*;
import java.sql.Date;

// Need to check

public class ReportingController extends Controller {
    public ReportingController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean generateRevenueReport(String adminID){
        try{
            String query = "SELECT SUM(revenue) AS totalRevenue FROM Report WHERE adminID = ?";
            PreparedStatement preparedStatement = sqlManager.prepareStatement(query);
            preparedStatement.setString(1, adminID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                double totalRevenue = resultSet.getDouble("totalRevenue");
                printStream.println("Revenue report for admin " + adminID + " : $" + totalRevenue);
            }
            else{
                printStream.println("No revenue data found for admin " + adminID);
            }

        }
        catch(SQLException e){
            printStream.println("Error occurred while generating revenue report: " + e.getMessage());
        }
        return false;
    }

    public boolean generateSalesReport(String adminID){
        try{
            String query = "SELECT COUNT(*) AS totalSales FROM OrderLineItems WHERE adminID = ?";
            PreparedStatement preparedStatement = sqlManager.prepareStatement(query);
            preparedStatement.setString(1, adminID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int totalSales = resultSet.getInt("totalSales");
                printStream.println("Sales report for admin " + adminID + " : " + totalSales + " sales");
            }
            else{
                printStream.println("No sales data found for admin " + adminID);
            }

        }
        catch(SQLException e){
            printStream.println("Error occurred while generating sales report: " + e.getMessage());
        }
        return false;
    }
// For report within date range
public boolean generateOrderReport(Date startDate, Date endDate) {
    try {
        String query = "SELECT COUNT(*) AS totalOrders FROM Orders WHERE dateCreated BETWEEN ? AND ?";
        PreparedStatement preparedStatement = sqlManager.prepareStatement(query);
        preparedStatement.setDate(1, (java.sql.Date) startDate);
        preparedStatement.setDate(2, (java.sql.Date) endDate);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int totalOrders = resultSet.getInt("totalOrders");
            printStream.println("Orders report for period " + startDate + " to " + endDate + ": " + totalOrders + " orders placed");
        } else {
            printStream.println("No orders data found for the specified period");
        }

        resultSet.close();
        preparedStatement.close();
    } catch (SQLException e) {
        printStream.println("Error occurred while generating order report: " + e.getMessage());
    }
    return false;
}
}
