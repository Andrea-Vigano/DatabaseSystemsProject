package controller;

import controller.database.Database;
import controller.database.SQLManager;
import model.Category;
import model.Product;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;


public class ProductsController extends Controller {

    private Product product;

    public ProductsController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public boolean list() {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.productID",
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Category.name",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
        try {
            ResultSet results = database.query("SELECT Product.productID FROM Product");
            int i = 0;
            while (results.next()) {
                String id = results.getString("Product.productID");
                i = printProduct(id, results, i);
            }
            if (i == 0) {
                printStream.println("No products to show");
            }
            results.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean search(String category, String brand, double lowestPrice, double highestPrice) {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.productID",
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.name",
                "Category.categoryID",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";

        if (!Objects.equals(category, "")) where += " AND Category.name = " + category;
        if (!Objects.equals(brand, "")) where += " AND Category.brand = " + brand;
        if (lowestPrice != 0.0) where += " AND Product.price > " + lowestPrice;
        if (highestPrice != 0.0) where += " AND Product.price < " + highestPrice;

        String statement = sqlManager.getSelectStatement(tables, fields, where);
        try {
            ResultSet results = database.query(statement);
            int i = 0;
            while (results.next()) {
                String id = results.getString("Product.productID");
                i = printProduct(id, results, i);
            }
            if (i == 0) {
                printStream.println("No products to show");
            }
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public Product add(
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
        int id = getLastedProductID();
        String statement = sqlManager.getInsertStatement(
                "Product",
                new String[] { "productID", "name", "description", "price", "brand", "quantity", "supplier", "warehouse", "review", "categoryID", "adminID" },
                new String[] {convert(Integer.toString(id)), convert(name), convert(description), Objects.toString(price),
                        convert(brand), Objects.toString(quantity), convert(supplier), convert(warehouse), convert(review),
                        convert(categoryId), convert(adminId) }
        );
        printStream.println(statement);
        try {
            database.update(statement);
            return new Product(Integer.toString(id), name, description, price, brand, quantity,
                    supplier, warehouse, review, categoryId, adminId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String productID) {
        String statement = sqlManager.getDeleteStatement("Product", "productID=" + productID);
        printStream.println(statement);
        try {
            database.update(statement);
        } catch (SQLException e) {
            database.abort();
            return false;
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
        try {
            database.update(statement);
        } catch (SQLException e) {
            database.abort();
            return false;
        }
        return true;
    }

    public void show(String id) {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.productID",
                "Product.name",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Category.name",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID AND Product.productID = " + id;
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        try {
            ResultSet results = database.query(statement);
            int i = 0;
            while (results.next()) {
                i = printProduct(id, results, i);
            }
            if (i == 0) {
                printStream.println("No products to show");
            }
        } catch (SQLException ignored) { }
    }

    private int printProduct(String id, ResultSet results, int i) throws SQLException {
        String name = results.getString("Product.name");
        String description = results.getString("Product.description");
        double price = results.getDouble("Product.price");
        int quantity = results.getInt("Product.quantity");
        String supplier = results.getString("Product.supplier");
        String warehouse = results.getString("Product.warehouse");
        String review = results.getString("Product.review");
        String categoryID = results.getString("Category.categoryID");
        String categoryName = results.getString("Category.name");
        String adminID = results.getString("Admin.adminID");
        printStream.printf(
                "%s. %s: %s\n\tPrice: .2%f\tQuantity: %d\n\tSupplier: %s\tWarehouse: %s\n\tReview: %s\n\tCategory: %s. %s\n\tAdminID: %s\n\n",
                id, name, description, price, quantity, supplier, warehouse, review, categoryID, categoryName, adminID
        );
        i++;
        return i;
    }

    public String getCategory(String name) {
        String statement = sqlManager.getSelectStatement("Category", new String[] { "categoryID" }, "name=" +  "'" + name + "'");
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getString("categoryID");
            } else {
                return null;
            }
        } catch (SQLException ignored) { }
        return null;
    }

    private int getLastedProductID() {
        String statement = "SELECT NVL(MAX(productID), 0) AS maxProductID FROM Product";
        try {
            ResultSet results = database.query(statement);
            if (results.next()) {
                return results.getInt("maxProductID") + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
