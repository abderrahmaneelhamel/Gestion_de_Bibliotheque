package service;
import dao.BookDAOImpl;
import dao.StatisticsDAO;
import model.Book;
import util.tools;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class BookService {
    private final BookDAOImpl bookDAO;
    private final StatisticsDAO statisticsDAO = new StatisticsDAO();

    public BookService(BookDAOImpl bookDAO) {
        this.bookDAO = bookDAO;
    }

    public void addBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter book title:");
        String title = scanner.nextLine();

        System.out.println("Enter book author:");
        String author = scanner.nextLine();

        System.out.println("Enter book ISBN:");
        String isbn = scanner.nextLine();

        int quantity;
        while (true) {
            try {
                System.out.println("Enter book quantity (must be >= 0):");
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity >= 0) {
                    break; // Exit the loop if the input is a valid non-negative integer
                } else {
                    System.out.println("Quantity must be greater than or equal to 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        Book newBook = new Book(title, author, isbn, quantity);

        if (bookDAO.addBook(newBook)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add book.");
        }
    }

    public void editBook() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book.toString());
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the book id to edit:");
        int id = scanner.nextInt();
        scanner.nextLine();

        Book book = bookDAO.getBookById(id);

        if (book != null) {
            System.out.println("Enter updated book title:");
            String title = scanner.nextLine();

            System.out.println("Enter updated book author:");
            String author = scanner.nextLine();

            int quantity;
            while (true) {
                try {
                    System.out.println("Enter updated book quantity (must be >= 0):");
                    quantity = Integer.parseInt(scanner.nextLine());
                    if (quantity >= 0) {
                        break; // Exit the loop if the input is a valid non-negative integer
                    } else {
                        System.out.println("Quantity must be greater than or equal to 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }

            book.setTitle(title);
            book.setAuthor(author);
            book.setQuantity(quantity);

            if (bookDAO.updateBook(book)) {
                System.out.println("Book updated successfully.");
            } else {
                System.out.println("Failed to update book.");
            }
        } else {
            System.out.println("Book not found with Id: " + id);
        }
    }

    public void deleteBook() {
        List<Book> books = bookDAO.getAllBooks();
        for (Book book : books) {
            System.out.println(book.toString());
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the book id to edit:");
        int id = scanner.nextInt();

        if (bookDAO.deleteBook(id)) {
            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Failed to delete book.");
        }
    }

    public void generateLibraryStatistics() {
        try {
            File file = new File("statistics.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();

            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            tools.printInFile("statistics\n", "statistics.txt");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");

            System.out.println("All the books in the library :");
            tools.printInFile("all the books in the library :", "statistics.txt");
            List<String> allBooksInfo = statisticsDAO.getAllBooksInfo();
            for (String bookInfo : allBooksInfo) {
                System.out.println(bookInfo + "\n");
                tools.printInFile(bookInfo + "\n", "statistics.txt");
            }

            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            System.out.println("All the borrowed books :");
            tools.printInFile("all the borrowed books :", "statistics.txt");
            List<String> borrowedBooksInfo = statisticsDAO.getAllBorrowedBooksInfo();
            for (String borrowedBookInfo : borrowedBooksInfo) {
                System.out.println(borrowedBookInfo + "\n");
                tools.printInFile(borrowedBookInfo + "\n", "statistics.txt");
            }

            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            System.out.print("Number of books in the library : ");
            tools.printInFile("Number of books in the library : ", "statistics.txt");
            int numberOfBooks = statisticsDAO.getNumberOfBooksInLibrary();
            System.out.println(numberOfBooks);
            tools.printInFile(numberOfBooks + "\n", "statistics.txt");

            System.out.print("Number of readers : ");
            tools.printInFile("Number of readers : ", "statistics.txt");
            int numberOfReaders = statisticsDAO.getNumberOfReaders();
            System.out.println(numberOfReaders);
            tools.printInFile(numberOfReaders + "\n", "statistics.txt");

            System.out.print("Number of books borrowed : ");
            tools.printInFile("Number of books borrowed : ", "statistics.txt");
            int numberOfBooksBorrowed = statisticsDAO.getNumberOfBooksBorrowed();
            System.out.println(numberOfBooksBorrowed);
            tools.printInFile(numberOfBooksBorrowed + "\n", "statistics.txt");

            System.out.print("Number of books returned : ");
            tools.printInFile("Number of books returned : ", "statistics.txt");
            int numberOfBooksReturned = statisticsDAO.getNumberOfBooksReturned();
            System.out.println(numberOfBooksReturned);
            tools.printInFile(numberOfBooksReturned + "\n", "statistics.txt");

            System.out.print("Number of books available : ");
            tools.printInFile("Number of books available : ", "statistics.txt");
            int numberOfBooksAvailable = statisticsDAO.getNumberOfBooksAvailable();
            System.out.println(numberOfBooksAvailable);
            tools.printInFile(numberOfBooksAvailable + "\n", "statistics.txt");

            System.out.print("Number of books lost : ");
            tools.printInFile("Number of books lost : ", "statistics.txt");
            int numberOfLostBooks = statisticsDAO.getNumberOfLostBooks();
            System.out.println(numberOfLostBooks);
            tools.printInFile(numberOfLostBooks + "\n", "statistics.txt");

            System.out.print("Number of ongoing borrowed books : ");
            tools.printInFile("Number of ongoing borrowed books : ", "statistics.txt");
            int numberOfOngoingBorrowedBooks = statisticsDAO.getNumberOfOngoingBorrowedBooks();
            System.out.println(numberOfOngoingBorrowedBooks);
            tools.printInFile(numberOfOngoingBorrowedBooks + "\n", "statistics.txt");

            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the book ISBN or title to search:");
        String input = scanner.nextLine();

        List<Book> books = bookDAO.searchBookByISBNOrTitle(input);

        if (books.isEmpty()) {
            System.out.println("No books found matching the search criteria.");
        } else {
            System.out.println("Books matching the search criteria:");
            for (Book book : books) {
                System.out.println(book.toString());
            }
        }
    }



    public void searchBooksByAuthor() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter author name to search books:");
        String author = scanner.nextLine();

        List<Book> books = bookDAO.searchBooksByAuthor(author);

        if (books != null && !books.isEmpty()) {
            System.out.println("Books found by author '" + author + "':");
            for (Book book : books) {
                System.out.println(book.toString());
            }
        } else {
            System.out.println("No books found by author '" + author + "'.");
        }
    }

    public void searchBooksByTitle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter book title to search books:");
        String title = scanner.nextLine();

        List<Book> books = bookDAO.searchBooksByTitle(title);

        if (books != null && !books.isEmpty()) {
            System.out.println("Books found with title '" + title + "':");
            for (Book book : books) {
                System.out.println(book.toString());
            }
        } else {
            System.out.println("No books found with title '" + title + "'.");
        }
    }
}

