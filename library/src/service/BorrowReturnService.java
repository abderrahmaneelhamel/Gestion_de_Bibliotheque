package service;
import dao.BookDAOImpl;
import dao.BorrowDAOImpl;
import dao.DatabaseConnection;
import model.Book;
import model.Borrow;
import model.User;
import util.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Scanner;

public class BorrowReturnService {
    private final BorrowDAOImpl borrowDAO;
    private final BookDAOImpl bookDAO;
    private final User authenticatedReader;

    public BorrowReturnService(BorrowDAOImpl borrowDAO, BookDAOImpl bookDAO, User authenticatedReader) {
        this.borrowDAO = borrowDAO;
        this.bookDAO = bookDAO;
        this.authenticatedReader = authenticatedReader;
    }

    public void borrowBook() {
        try {
            Connection connection = new DatabaseConnection().connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE quantity > 0");

            System.out.println("Available Books:");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "- " + resultSet.getString("title"));
            }

            int bookId;
            System.out.println("Enter the book id you want to borrow (or 0 to exit):");
            bookId = tools.tryParse(new Scanner(System.in).nextLine());

            if (bookId == 0) {
                System.out.println("Exiting...");
                return;
            }

            if (bookDAO.checkIfBookIsAvailable(bookId)) {
                System.out.println("Enter the number of days you plan to borrow the book for:");
                int duration = tools.tryParse(new Scanner(System.in).nextLine());

                Date currentDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String borrowDate = formatter.format(currentDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(currentDate);
                cal.add(Calendar.DATE, duration);
                Date returnDate = cal.getTime();
                String returnDateStr = formatter.format(returnDate);

                Borrow borrow = new Borrow(bookId, authenticatedReader.getId(), borrowDate, returnDateStr,"ongoing");
                borrowDAO.borrowBook(borrow);

                Book borrowedBook = bookDAO.getBookById(bookId);
                System.out.println("Book borrowed successfully!");
                System.out.println("You should return it by: " + returnDateStr);
                System.out.println("The book ISBN is: " + borrowedBook.getBookISBN());
            } else {
                System.out.println("No book is available with this id or it's not in stock.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void returnBook() {
        try {
            Connection connection = new DatabaseConnection().connection();
            Statement statement = connection.createStatement();

            System.out.println("Enter the book ISBN to return:");
            String ISBN = new Scanner(System.in).nextLine();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM borrow WHERE reader_id = '" + authenticatedReader.getId() + "' AND status = 'ongoing'");
            HashMap<String, String> borrowedBook = new HashMap<>();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                Book book = bookDAO.getBookById(bookId);
                if (book.getBookISBN().equals(ISBN)) {
                    borrowedBook.put("id", resultSet.getString("id"));
                    borrowedBook.put("book_id", resultSet.getString("book_id"));
                    borrowedBook.put("borrow_date", resultSet.getString("borrow_date"));
                    borrowedBook.put("return_date", resultSet.getString("return_date"));
                    break;
                }
            }

            if (!borrowedBook.isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                LocalDate today = LocalDate.now();
                Date currentDate = new Date();
                String currentDateStr = formatter.format(currentDate);
                LocalDate returnDate = LocalDate.parse(borrowedBook.get("return_date"));

                if (today.isAfter(returnDate)) {
                    statement.executeUpdate("DELETE FROM borrow WHERE id = " + borrowedBook.get("id"));
                    statement.executeUpdate("INSERT INTO returns (book_id, reader_id, return_date) VALUES ("
                            + borrowedBook.get("book_id") + ","
                            + authenticatedReader.getId() + ",'"
                            + currentDateStr + "')");
                    System.out.println("You are late to return the book.");
                } else {
                    statement.executeUpdate("DELETE FROM borrow WHERE id = " + borrowedBook.get("id"));
                    statement.executeUpdate("INSERT INTO returns (book_id, reader_id, return_date) VALUES ("
                            + borrowedBook.get("book_id") + ","
                            + authenticatedReader.getId() + ",'"
                            + currentDateStr + "')");
                    System.out.println("Thank you for returning the book.");
                }
            } else {
                System.out.println("No records found for the provided ISBN or the book is not currently borrowed.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
