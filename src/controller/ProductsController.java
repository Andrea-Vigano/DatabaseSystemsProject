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

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product){
        this.product = product;
    }

    public boolean list() {
        String[] tables = new String[]{ "Product", "Category", "Admin" };
        String[] fields = new String[]{
                "Product.productID",
                "Product.name AS productName",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Category.name AS categoryName",
                "Admin.adminID"
        };
        String where = "Product.categoryID = Category.categoryID AND Product.adminID = Admin.adminID";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
        try {
            ResultSet results = database.query(statement);
            int i = 0;
            while (results.next()) {
                String id = results.getString("productID");
                i = printProduct(id, results, i);
            }
            if (i == 0) {
                printStream.println("No products to show");
            }
            results.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            double review,
            String categoryId,
            String adminId
    ) {
        int id = getLastedProductID();
        String statement = sqlManager.getInsertStatement(
                "Product",
                new String[] { "productID", "name", "description", "price", "brand", "quantity", "supplier", "warehouse", "review", "categoryID", "adminID" },
                new String[] {convert(Integer.toString(id)), convert(name), convert(description), Objects.toString(price),
                        convert(brand), Objects.toString(quantity), convert(supplier), convert(warehouse), Objects.toString(review),
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
            Double review,
            String categoryId,
            String adminId
    ) {
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> fields = new ArrayList<>();
        if (name != null) {
            columns.add("name");
            fields.add(convert(name));
        }
        if (description != null) {
            columns.add("description");
            fields.add(convert(description));
        }
        if (price != 0.0) {
            columns.add("price");
            fields.add(Objects.toString(price));
        }
        if (brand != null) {
            columns.add("brand");
            fields.add(convert(brand));
        }
        if (quantity != 0) {
            columns.add("quantity");
            fields.add(Objects.toString(quantity));
        }
        if (supplier != null) {
            columns.add("supplier");
            fields.add(convert(supplier));
        }
        if (warehouse != null) {
            columns.add("warehouse");
            fields.add(convert(warehouse));
        }
        if (review != 0.0) {
            columns.add("review");
            fields.add(Objects.toString(review));
        }
        if (categoryId != null) {
            columns.add("categoryID");
            fields.add(convert(categoryId));
        }
        if (adminId != null) {
            columns.add("adminID");
            fields.add(convert(adminId));
        }
        String statement = sqlManager.getUpdateStatement("Product",
                columns.toArray(new String[]{}),
                fields.toArray(new String[]{}),
                "productID=" + productID
        );
        printStream.println(statement);
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
                "Product.name AS productName",
                "Product.description",
                "Product.price",
                "Product.brand",
                "Product.quantity",
                "Product.supplier",
                "Product.warehouse",
                "Product.review",
                "Category.categoryID",
                "Category.name AS categoryName",
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
        String name = results.getString("productName");
        String description = results.getString("description");
        double price = results.getDouble("price");
        int quantity = results.getInt("quantity");
        String supplier = results.getString("supplier");
        String warehouse = results.getString("warehouse");
        double review = results.getDouble("review");
        String categoryID = results.getString("categoryID");
        String categoryName = results.getString("categoryName");
        printStream.printf(
                "%s. %s: %s\n\tPrice: %.1f\n\tQuantity: %d\n\tSupplier: %s\n\tWarehouse: %s\n\tReview: %.2f\n\tCategory: %s.%s\n\n",
                id, name, description, price, quantity, supplier, warehouse, review, categoryID, categoryName
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
