package service;
import dao.BookDAOImpl;
import dao.BorrowDAOImpl;
import model.Book;
import model.Borrow;
import model.User;
import util.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
            List<Book> books = bookDAO.getAllAvailableBooks();

            System.out.println("Available Books:");
            for (Book book : books) {
                System.out.println(book.getId() + "- " + book.getBookTitle());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook() {
        try {
            List<Borrow> borrows = borrowDAO.getAllBorrowsByReaderId(authenticatedReader.getId());
            if(borrows.size() > 0) {
                Borrow borrowedBook = null;

                System.out.println("Enter the book ISBN to return:");
                String ISBN = new Scanner(System.in).nextLine();

                for (Borrow borrow : borrows) {
                    Book book = bookDAO.getBookById(borrow.getBookId());
                    if (book.getBookISBN().equals(ISBN)) {
                        borrowedBook = borrow;
                        break;
                    }
                }

                if (borrowedBook != null) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    LocalDate today = LocalDate.now();
                    Date currentDate = new Date();
                    String currentDateStr = formatter.format(currentDate);
                    LocalDate returnDate = LocalDate.parse(borrowedBook.getReturnDate());

                    if (today.isAfter(returnDate)) {
                        System.out.println("You are late to return the book.");
                    } else {
                        borrowDAO.returnBook(borrowedBook, currentDateStr);
                        System.out.println("Thank you for returning the book.");
                    }
                } else {
                    System.out.println("No records found for the provided ISBN or the book is not currently borrowed.");
                }
            } else {
                System.out.println("You didn't borrow any book.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
