package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;

public class CartController extends Controller {
    public CartController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }

    public boolean isEmpty() {
        return true;
    }

    public void show() {
    }

    public boolean checkout() {
        return true;
    }

    public boolean add(String id) {
        return true;
    }
}
