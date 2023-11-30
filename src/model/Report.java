package model;

public class Report {
    private String reportID;
    private String description;
    private double revenue;
    private String adminID;

    public Report(String reportID, String description, double revenue, String adminID) {
        this.reportID = reportID;
        this.description = description;
        this.revenue = revenue;
        this.adminID = adminID;
    }


    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
}