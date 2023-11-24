package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;

public class ReportingController extends Controller {
    public ReportingController(PrintStream printStream, Database database, SQLManager sqlManager) {
        super(printStream, database, sqlManager);
    }
}
