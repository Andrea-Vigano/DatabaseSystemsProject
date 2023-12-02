import commandparser.Command;
import commandparser.CommandParser;
import controller.MainController;
import controller.database.Database;
import controller.database.SQLManager;
import io.IO;

import java.io.PrintStream;
import java.util.Scanner;

public class App {
    static private final Scanner scanner = IO.scanner;
    static private final PrintStream printStream = IO.printStream;
    private final CommandParser commandParser;

    public App(MainController mainController) {
        this.commandParser = new CommandParser(mainController, scanner, printStream);
    }

    static public void main(String[] args) {
        MainController MainController = new MainController(printStream, Database.get(), new SQLManager());
        App app = new App(MainController);
        app.run();
    }
    static private void printCommands(){
        System.out.println("Welcome to our OSS System. Please login before proceeding.");
        System.out.println("=========================");
        System.out.println("Enter a command. (type 'help' for list of commands)");

    }

    public void run() {
        Command currentCommand;
        printCommands();
        do {
            System.out.print(">> ");
            String rawCommand = App.scanner.nextLine();
            currentCommand = new Command(rawCommand);
            commandParser.parse(currentCommand);
        } while (!currentCommand.isExit());
    }
}