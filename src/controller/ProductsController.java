package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

public class ProductsController extends Controller {

    private int productCnt = 1;

    public ProductsController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean list() {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean search(String category, String brand, double lowestPrice, double highestPrice) {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";

        if (!Objects.equals(category, "")) where += " AND Category.name = " + category;
        if (!Objects.equals(brand, "")) where += " AND Category.brand = " + brand;
        if (lowestPrice != 0.0) where += " AND Product.price > " + lowestPrice;
        if (highestPrice != 0.0) where += " AND Product.price < " + highestPrice;

        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean adminList() {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean add(
            String name,
            String description,
            double price,
            String brand,
            int quantity,
            String supplier,
            String warehouse,
            String review,
            String categoryId,
            String adminId
    ) {
        String statement = sqlManager.getInsertStatement(
                "Product",
                new String[] { "productID", "name", "description", "price", "brand", "quantity", "supplier", "warehouse", "review", "categoryID", "adminID" },
                new String[] {String.valueOf(productCnt++), name, description, Objects.toString(price), brand, Objects.toString(quantity), supplier, warehouse, review, categoryId, adminId }
        );
        printStream.println(statement);
        try {
            ResultSet results = database.update(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean delete(String productID) {
        String statement = sqlManager.getDeleteStatement("Product", "productID=" + productID);
        printStream.println(statement);
                try {
            ResultSet results = database.update(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean update(
            String productID,
            String name,
            String description,
            Double price,
            String brand,
            Integer quantity,
            String supplier,
            String warehouse,
            String review,
            String categoryId,
            String adminId
    ) {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> fields = new ArrayList<>();
        if (name != null) {
            columns.add("name");
            fields.add(name);
        }
        if (description != null) {
            columns.add("description");
            fields.add(description);
        }
        if (price != null) {
            columns.add("price");
            fields.add(Objects.toString(price));
        }
        if (brand != null) {
            columns.add("brand");
            fields.add(brand);
        }
        if (quantity != null) {
            columns.add("quantity");
            fields.add(Objects.toString(quantity));
        }
        if (supplier != null) {
            columns.add("supplier_id");
            fields.add(supplier);
        }
        if (warehouse != null) {
            columns.add("warehouseID");
            fields.add(warehouse);
        }
        if (review != null) {
            columns.add("review");
            fields.add(review);
        }
        if (categoryId != null) {
            columns.add("categoryID");
            fields.add(categoryId);
        }
        if (adminId != null) {
            columns.add("adminID");
            fields.add(adminId);
        }
        String statement = sqlManager.getUpdateStatement("Product",
                columns.toArray(new String[]{}),
                fields.toArray(new String[]{}),
                "productID=" + productID
        );
        printStream.println(statement);
        try {
            ResultSet results = database.update(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean show(String id) {
        return true;
    }

    public boolean getCategory(String name) {
        String statement = sqlManager.getSelectStatement("Category", new String[] { "category_id" }, "name=" + name);
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
