package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
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
                "Product.brand",
                "Category.name",
                "Supplier.name",
                "Product.rating"
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
                "Product.brand",
                "Category.name",
                "Supplier.name",
                "Product.rating"
        };
        String where = "Product.category_id = Category.category_id AND Product.supplier_id = Supplier.supplier_id";

        if (!Objects.equals(category, "")) where += " AND Category.name = " + category;
        if (!Objects.equals(brand, "")) where += " AND Product.brand = " + brand;
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
}
