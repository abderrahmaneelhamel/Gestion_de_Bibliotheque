package service;
import dao.BookDAOImpl;
import dao.BorrowDAOImpl;
import model.User;

import java.util.Scanner;

public class ReaderService {
    private final BookDAOImpl bookDAO;
    private final BorrowDAOImpl borrowDAO;

    public ReaderService(BookDAOImpl bookDAO, BorrowDAOImpl borrowDAO) {
        this.bookDAO = bookDAO;
        this.borrowDAO = borrowDAO;
    }

    public void displayMenu(User authenticatedUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome, " + authenticatedUser.getName() + "!");

        while (true) {
            System.out.println("Reader Menu:");
            System.out.println("1. Borrow a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. Search for a Book by Title");
            System.out.println("4. Search for a Book by Author");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> new BorrowReturnService(borrowDAO, bookDAO, authenticatedUser).borrowBook();
                    case 2 -> new BorrowReturnService(borrowDAO, bookDAO, authenticatedUser).returnBook();
                    case 3 -> new BookService(bookDAO).searchBooksByTitle();
                    case 4 -> new BookService(bookDAO).searchBooksByAuthor();
                    case 5 -> {
                        System.out.println("Thank you for using the library service. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }


}

