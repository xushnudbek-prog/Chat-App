import java.io.IOException;
import java.util.Scanner;
import java.util.logging.LogManager;
import java.util.regex.Pattern;

public class Main {
    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        menu();
    }

    private static void menu() {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Register\n2. Login\n3. Exit");
            String userInput = sc.nextLine().trim();
            if (Pattern.matches("^\\d$", userInput))
                switch (userInput) {
                    case "1" -> UserService.register();
                    case "2" -> UserService.login();
                    case "3" -> exit = true;
                    default -> System.out.println("Wrong choice.");
                }
            else
                System.out.println("Wrong choice.");
        }
    }
}