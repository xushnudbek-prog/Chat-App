import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;
import java.util.regex.Pattern;

public class UserService {
    static {
        try {
            LogManager.getLogManager().readConfiguration(
                    Main.class.getResourceAsStream("logging.properties")
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Logger logger = Logger.getLogger(UserService.class.getSimpleName());
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static ArrayList<User> usersList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    private static User isUserFound(String email) {
        for (User user : usersList)
            if (user.getEmail().equals(email))
                return user;
        return null;
    }
    private static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w+.-]+@([\\w+]{2,}+.+[\\w+]{2,})$");
        return pattern.matcher(email).matches();
    }
    private static String takeUserInput() {
        System.out.print("Enter your email: ");
        return sc.nextLine();
    }



    public static void register() {
        System.out.println("Enter your name: ");
        String name = sc.nextLine();
        String email = takeUserInput();
        if (isValidEmail(email)) {
            executorService.execute(()-> {
                usersList.add(new User(name, email));
                logger.log(Level.INFO, "Registered ✅.");
            });
            executorService.execute(()-> {
                String filepath = "data/registeration_info.txt";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm MM.dd.yyyy");
                LocalDateTime now = LocalDateTime.now();
                try(BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
                    writer.write("%s %s%n".formatted(email, now.format(dtf)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }
        else
            System.out.println("You entered wrong email.");

    }

    public static void login() {
        User currentUser = isUserFound(takeUserInput());
        if (currentUser != null) {
            logger.log(Level.INFO, "Login successful.");
            userMenu(currentUser);
        }
        else {
            System.out.println("Email not found. Please register first.");
        }
    }

    private static void userMenu(User currentUser) {
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Send a message\n2. Exit");
            String userInput = sc.nextLine().trim();
            if (Pattern.matches("^\\d$", userInput))
                switch (userInput) {
                    case "1" -> sendMessage(currentUser);
                    case "2" -> exit = true;
                    default -> System.out.println("Wrong choice.");
                }
            else
                System.out.println("Wrong choice.");
        }
    }
    private static void sendMessage(User sender) {
        System.out.println("Enter the message: ");
        String messageText = sc.nextLine();
        System.out.println("Enter the receiver email: ");
        String email = sc.nextLine();
        while (true) {
            if (!isValidEmail(email)) {
                System.out.println("You entered wrong email, please enter correctly: ");
                email = sc.nextLine();
            }
            else break;
        }
        Message message = new Message(sender, messageText, email, LocalDateTime.now());
        System.out.println(message + " Succeed ✅");
        String filepath = "data/chat_history.txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(message.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
