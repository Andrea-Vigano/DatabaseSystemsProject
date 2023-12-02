package model;

public class Report {
    private String reportID;
    private String productName;
    private double revenue;
    private int sales;
    private String adminID;

    public Report(String reportID, String productName, double revenue, int sales, String adminID) {
        this.reportID = reportID;
        this.productName = productName;
        this.revenue = revenue;
        this.sales = sales;
        this.adminID = adminID;
    }


    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getSales() { return sales; }

    public void setSales(int sales) { this.sales = sales; }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
}