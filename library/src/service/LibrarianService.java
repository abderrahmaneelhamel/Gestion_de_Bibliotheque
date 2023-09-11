package service;
import dao.BookDAOImpl;
import dao.LibrarianDAOImpl;
import dao.UserDAOImpl;
import model.User;

import java.sql.Connection;
import java.util.Scanner;

public class LibrarianService {
    private final BookService bookService;
    private Connection connection;

    public LibrarianService(Connection connection) {
        this.connection = connection;
        LibrarianDAOImpl librarianDAO = new LibrarianDAOImpl(connection);
        this.bookService = new BookService(new BookDAOImpl(connection));
    }

    public void showMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Edit Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Generate Library Statistics");
            System.out.println("5. Search for a Book");
            System.out.println("6. Add a new User");
            System.out.println("7. Logout");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> bookService.addBook();
                    case 2 -> bookService.editBook();
                    case 3 -> bookService.deleteBook();
                    case 4 -> bookService.generateLibraryStatistics();
                    case 5 -> bookService.search();
                    case 6 -> new AuthenticationService(new UserDAOImpl(connection)).addAdmin(scanner);
                    case 7 -> {
                        return; // Logout
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private void generateLibraryStatistics() {

    }
}

