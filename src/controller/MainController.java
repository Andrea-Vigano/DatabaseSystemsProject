package controller;

import controller.database.Database;
import controller.database.SQLManager;

import java.io.PrintStream;

public class MainController implements Controller {
    private final Database database;
    private final SQLManager sqlManager;
    private final AuthenticationController authenticationController;

    public MainController(PrintStream printStream, Database database, SQLManager sqlManager) {
        this.database = database;
        this.sqlManager = sqlManager;
        this.authenticationController = new AuthenticationController(printStream, database, sqlManager);
    }

    public boolean logIn(String username, String password) {
        return authenticationController.logIn(username, password);
    }

    public boolean signUp(String name, String username, String password, String email) {
        return authenticationController.singUp(name, username, password, email);
    }

    public boolean logOut() {
        return authenticationController.logOut();
    }

    public boolean isLogged() {
        return authenticationController.getIsLogged();
    }
}
