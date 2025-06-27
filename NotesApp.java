import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NotesApp {
    private static final String USERS_FILE = "users.txt";
    private static String currentUsername = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Notes App");
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    if (loginUser()) {
                        notesMenu();
                    }
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty() || username.contains(" ")) {
            System.out.println("Invalid username (no spaces or empty).");
            return;
        }

        if (userExists(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        System.out.print("Choose a password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        try (FileWriter writer = new FileWriter(USERS_FILE, true)) {
            writer.write(username + ":" + hashedPassword + System.lineSeparator());
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to users file: " + e.getMessage());
        }
    }

    private static boolean loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(hashedPassword)) {
                    System.out.println("Login successful! Welcome, " + username + "!");
                    currentUsername = username;
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No users registered yet.");
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }

        System.out.println("Invalid username or password.");
        return false;
    }

    private static void notesMenu() {
        while (true) {
            showNotesMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    writeNotes(false);
                    break;
                case "2":
                    writeNotes(true);
                    break;
                case "3":
                    readNotes();
                    break;
                case "4":
                    searchNotes();
                    break;
                case "5":
                    deleteNotesFile();
                    break;
                case "6":
                    System.out.println("Logged out.");
                    currentUsername = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showNotesMenu() {
        System.out.println("\n📒 NOTES MENU for " + currentUsername + ":");
        System.out.println("1. Write Notes (Overwrite)");
        System.out.println("2. Append Notes");
        System.out.println("3. Read Notes");
        System.out.println("4. Search Notes");
        System.out.println("5. Delete Notes File");
        System.out.println("6. Logout");
        System.out.print("Choose an option (1-6): ");
    }

    private static String getNotesFileName() {
        return "notes_" + currentUsername + ".txt";
    }

    private static void writeNotes(boolean append) {
        System.out.print("Enter a title for this note: ");
        String title = scanner.nextLine();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        System.out.println("Enter your note content (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) break;
            content.append(line).append(System.lineSeparator());
        }

        try (FileWriter writer = new FileWriter(getNotesFileName(), append)) {
            writer.write("[" + timestamp + "] " + title + System.lineSeparator());
            writer.write(content.toString());
            writer.write("------------------------------------------------------------\n");
            System.out.println(append ? "Note appended successfully!" : "Note overwritten successfully!");
        } catch (IOException e) {
            System.out.println("Error writing notes: " + e.getMessage());
        }
    }

    private static void readNotes() {
        System.out.println("\n📖 Notes for " + currentUsername + ":");
        try (BufferedReader reader = new BufferedReader(new FileReader(getNotesFileName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("---- End of Notes ----");
        } catch (FileNotFoundException e) {
            System.out.println("No notes found. Start by writing some!");
        } catch (IOException e) {
            System.out.println("Error reading notes: " + e.getMessage());
        }
    }

    private static void searchNotes() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();

        try (BufferedReader reader = new BufferedReader(new FileReader(getNotesFileName()))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found) System.out.println("No notes matched your keyword.");
        } catch (FileNotFoundException e) {
            System.out.println("No notes file found.");
        } catch (IOException e) {
            System.out.println("Error searching notes: " + e.getMessage());
        }
    }

    private static void deleteNotesFile() {
        System.out.print("Are you sure you want to delete all your notes? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();
        if (confirmation.equals("yes")) {
            File file = new File(getNotesFileName());
            if (file.exists() && file.delete()) {
                System.out.println("All your notes deleted successfully.");
            } else {
                System.out.println("Failed to delete your notes file.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    private static boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // No users file yet, so no user exists
        } catch (IOException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return password;
        }
    }
}
