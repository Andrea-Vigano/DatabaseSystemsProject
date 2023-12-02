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
            System.out.println("=========================================");
            System.out.println("          Welcome to our OSS System          ");
            System.out.println("=========================================");
            System.out.println("        Please login before proceeding       ");
            System.out.println("=========================================");

            System.out.println("_        ,");
            System.out.println("(_\\______/__________/");
            System.out.println("   \\-|-|/|-|-|-|-|//");
            System.out.println("    \\==/-|-|-|-|-//");
            System.out.println("     \\/|-|-|-|,-//");
            System.out.println("      \\_-|_|_|_//");
            System.out.println("       \\_j________");
            System.out.println("       (_)     (_)");
            System.out.println("Enter a command. (Type 'help' for a list of commands for system navigation )");
        }


        public void run() {
        printCommands();
        Command currentCommand;
        do {
            System.out.print(">> ");
            String rawCommand = App.scanner.nextLine();
            currentCommand = new Command(rawCommand);
            commandParser.parse(currentCommand);
        } while (!currentCommand.isExit());
    }
}