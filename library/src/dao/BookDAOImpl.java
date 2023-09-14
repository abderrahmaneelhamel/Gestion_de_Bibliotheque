package dao;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {
    private final Connection connection;

    public BookDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, ISBN, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getBookTitle());
            preparedStatement.setString(2, book.getBookAuthor());
            preparedStatement.setString(3, book.getBookISBN());
            preparedStatement.setInt(4, book.getBookQuantity());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getAllAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE quantity > 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%"+title+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%"+author+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
            return books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Book> searchBookByISBNOrTitle(String input) {
        String sql = "SELECT * FROM books WHERE ISBN=? OR title LIKE ?";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, input);
            preparedStatement.setString(2, "%" + input + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book(resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("ISBN"), resultSet.getInt("quantity"));
                book.setId(resultSet.getInt("id"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    @Override
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title=?, author=?, ISBN=?, quantity=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getBookTitle());
            preparedStatement.setString(2, book.getBookAuthor());
            preparedStatement.setString(3, book.getBookISBN());
            preparedStatement.setInt(4, book.getBookQuantity());
            preparedStatement.setInt(5, book.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("UPDATE books SET quantity = 0 WHERE id='" + bookId + "'");
                System.out.println("This book is borrowed, so the quantity is set to 0");
            }catch (SQLException e1){
                return false;
            }
        }
        return true;
    }

    // Method to check if a book is available
    public boolean checkIfBookIsAvailable(int bookId) {
        boolean isAvailable = false;

        try {
            Connection connection = new DatabaseConnection().connection();
            String sql = "SELECT quantity from books where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                isAvailable = quantity > 0;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to check if book is available");
        }
        return isAvailable;
    }
}
