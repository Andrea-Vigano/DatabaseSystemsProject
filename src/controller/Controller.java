package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;

public abstract class Controller {
    protected final Database database;
    protected final SQLManager sqlManager;
    protected final PrintStream printStream;

    public Controller(PrintStream printStream, Database database, SQLManager sqlManager) {
        this.database = database;
        this.sqlManager = sqlManager;
        this.printStream = printStream;
    }

}
