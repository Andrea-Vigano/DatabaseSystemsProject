package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;
import java.util.HashMap;

public abstract class Controller {
    protected final Database database;
    protected final SQLManager sqlManager;
    protected final PrintStream printStream;

    protected HashMap<String, Double> priceStore = new HashMap<>();

    protected String convert(String val){
        return "'" + val + "'";
    }

    public Controller(PrintStream printStream, Database database, SQLManager sqlManager) {
        this.database = database;
        this.sqlManager = sqlManager;
        this.printStream = printStream;
    }
}


