package  service;

import dao.BookDAOImpl;
import dao.BorrowDAOImpl;
import dao.DatabaseConnection;
import dao.UserDAOImpl;
import model.User;
import util.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryService {
    private final AuthenticationService authService;
    private final BookService bookService;
    private final LibrarianService librarianService;
    private final ReaderService readerService;

    private Connection connection;

    public LibraryService() throws SQLException {
        // Initialize services and dependencies here
        Connection connection = new DatabaseConnection().connection();
        authService = new AuthenticationService(new UserDAOImpl(connection));
        bookService = new BookService(new BookDAOImpl(connection));
        librarianService = new LibrarianService(connection);
        readerService = new ReaderService(new BookDAOImpl(connection), new BorrowDAOImpl(connection));
    }

    public void start() {
        tools.checkOutDatedBorrows();

        Scanner scanner = new Scanner(System.in);

        // Display a welcome message and options to log in or sign up
        System.out.println("Welcome to the Library Management System!");
        int choice;

        while (true) {
            System.out.println("1. Log In");
            System.out.println("2. Sign Up");
            System.out.print("Enter your choice (1 or 2): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (choice == 1) {
                    // User authentication
                    User authenticatedUser = authService.signIn(scanner);

                    if (authenticatedUser != null) {
                        // Determine user role (Librarian or Reader)
                        if (authenticatedUser.getRole() == 1) {
                            // Librarian menu
                            librarianService.showMenu(authenticatedUser);
                        } else if (authenticatedUser.getRole() == 2) {
                            // Reader menu
                            readerService.displayMenu(authenticatedUser);
                        } else {
                            System.out.println("Invalid user role. Exiting...");
                        }
                        break; // Exit the loop if authentication is successful
                    } else {
                        System.out.println("Authentication failed. Please try again.");
                    }
                } else if (choice == 2) {
                    // User registration (sign up)
                    User registeredUser = authService.signUp(scanner);

                    if (registeredUser != null) {
                        // Registration and login successful
                        System.out.println("Registration and login successful!");
                        readerService.displayMenu(registeredUser); // Assuming registered users are readers
                        break; // Exit the loop if registration and login are successful
                    } else {
                        System.out.println("Registration failed. Please try again.");
                    }
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Close resources
        scanner.close();
    }

}
