package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

public class ProductsController extends Controller {

    public ProductsController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean list() {
        String[] tables = new String[]{ "Product", "Category", "Supplier" };
        String[] fields = new String[]{
                "Product.name",
                "Product.description",
                "Product.specifications",
                "Product.price",
                "Category.brand",
                "Category.name",
                "Supplier.name",
                "Product.review"
        };
        String where = "Product.category_id = Category.category_id AND Product.supplier_id = Supplier.supplier_id";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
//        try {
//            ResultSet results = database.query(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean search(String category, String brand, double lowestPrice, double highestPrice) {
        String[] tables = new String[]{ "Product", "Category", "Supplier" };
        String[] fields = new String[]{
                "Product.name",
                "Product.description",
                "Product.specifications",
                "Product.price",
                "Category.brand",
                "Category.name",
                "Supplier.name",
                "Product.review"
        };
        String where = "Product.category_id = Category.category_id AND Product.supplier_id = Supplier.supplier_id";

        if (!Objects.equals(category, "")) where += " AND Category.name = " + category;
        if (!Objects.equals(brand, "")) where += " AND Category.brand = " + brand;
        if (lowestPrice != 0.0) where += " AND Product.price > " + lowestPrice;
        if (highestPrice != 0.0) where += " AND Product.price < " + highestPrice;

        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
//        try {
//            ResultSet results = database.query(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean adminList() {
        String[] tables = new String[]{ "Product", "Category", "Supplier" };
        String[] fields = new String[]{
                "Product.product_id",
                "Product.name",
                "Product.description",
                "Product.specifications",
                "Product.price",
                "Category.brand",
                "Category.name",
                "Supplier.name",
                "Product.review"
        };
        String where = "Product.category_id = Category.category_id AND Product.supplier_id = Supplier.supplier_id";
        String statement = sqlManager.getSelectStatement(tables, fields, where);
        printStream.println(statement);
//        try {
//            ResultSet results = database.query(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean add(
            String name,
            String description,
            String specification,
            double price,
            String brand,
            String categoryId,
            String warehouseId,
            String supplierId,
            String rating
    ) {
        String statement = sqlManager.getInsertStatement(
                "Product",
                new String[] { "name", "description", "specification", "price", "brand", "category_id", "warehouse_id", "supplier_id", "rating" },
                new String[] { name, description, specification, Objects.toString(price), brand, categoryId, warehouseId, supplierId, rating }
        );
        printStream.println(statement);
//        try {
//            ResultSet results = database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean delete(String id) {
        String statement = sqlManager.getDeleteStatement("Product", "product_id=" + id);
        printStream.println(statement);
        //        try {
//            ResultSet results = database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean update(
            String id,
            String name,
            String description,
            String specification,
            Double price,
            String categoryId,
            String warehouseId,
            String supplierId,
            String review
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
        if (specification != null) {
            columns.add("specification");
            fields.add(specification);
        }
        if (price != null) {
            columns.add("price");
            fields.add(Objects.toString(price));
        }
        if (categoryId != null) {
            columns.add("category_id");
            fields.add(categoryId);
        }
        if (warehouseId != null) {
            columns.add("warehouse_id");
            fields.add(warehouseId);
        }
        if (supplierId != null) {
            columns.add("supplier_id");
            fields.add(supplierId);
        }
        if (review != null) {
            columns.add("review");
            fields.add(review);
        }
        String statement = sqlManager.getUpdateStatement("Product",
                columns.toArray(new String[]{}),
                fields.toArray(new String[]{}),
                "product_id=" + id
        );
        printStream.println(statement);
//        try {
//            ResultSet results = database.update(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }

    public boolean getCategory(String name) {
        String statement = sqlManager.getSelectStatement("Category", new String[] { "category_id" }, "name=" + name);
        printStream.println(statement);
//        try {
//            ResultSet results = database.query(statement);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        return true;
    }
}
