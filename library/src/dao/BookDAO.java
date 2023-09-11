package dao;
import model.Book;

import java.util.List;

public interface BookDAO {
    // Create
    boolean addBook(Book book);

    // Read
    Book getBookById(int bookId);
    List<Book> getAllBooks();

    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);

    List<Book> searchBookByISBNOrTitle(String input);
    // Update
    boolean updateBook(Book book);

    // Delete
    boolean deleteBook(int bookId);

    boolean checkIfBookIsAvailable(int bookId);
}
