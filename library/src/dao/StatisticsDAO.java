package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsDAO {
    private Connection connection;

    public StatisticsDAO() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllBooksInfo() {
        List<String> bookInfoList = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String bookInfo = resultSet.getInt("id") + " - title : " + resultSet.getString("title") +
                        " | author : " + resultSet.getString("author") + " | ISBN : " + resultSet.getString("ISBN") +
                        " | quantity : " + resultSet.getInt("quantity");
                bookInfoList.add(bookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookInfoList;
    }

    public List<String> getAllBorrowedBooksInfo() {
        List<String> borrowedBooksInfoList = new ArrayList<>();
        String sql = "SELECT b.title, br.borrow_date, br.return_date, u.name, br.status " +
                "FROM users AS u, borrow AS br, books AS b " +
                "WHERE br.book_id = b.id AND u.id = br.reader_id;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String borrowedBookInfo = resultSet.getString("title") + " | borrow date : " + resultSet.getString("borrow_date") +
                        " | return date : " + resultSet.getString("return_date") + " | reader name : " + resultSet.getString("name") +
                        " | status : " + resultSet.getString("status");
                borrowedBooksInfoList.add(borrowedBookInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooksInfoList;
    }

    public int getNumberOfBooksInLibrary() {
        int numberOfBooks = 0;
        String sql = "SELECT COUNT(*) FROM books";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfBooks = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfBooks;
    }

    public int getNumberOfReaders() {
        int numberOfReaders = 0;
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfReaders = resultSet.getInt(1) - 1; // Exclude the admin
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfReaders;
    }

    public int getNumberOfBooksBorrowed() {
        int numberOfBooksBorrowed = 0;
        String sql = "SELECT COUNT(*) FROM borrow";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfBooksBorrowed = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfBooksBorrowed;
    }

    public int getNumberOfBooksReturned() {
        int numberOfBooksReturned = 0;
        String sql = "SELECT COUNT(*) FROM returns";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfBooksReturned = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfBooksReturned;
    }

    public int getNumberOfBooksAvailable() {
        int numberOfBooksAvailable = 0;
        String sql = "SELECT COUNT(*) FROM books WHERE quantity > 0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfBooksAvailable = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfBooksAvailable;
    }

    public int getNumberOfLostBooks() {
        int numberOfLostBooks = 0;
        String sql = "SELECT COUNT(*) FROM borrow WHERE status = 'lost'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfLostBooks = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfLostBooks;
    }

    public int getNumberOfOngoingBorrowedBooks() {
        int numberOfOngoingBorrowedBooks = 0;
        String sql = "SELECT COUNT(*) FROM borrow WHERE status = 'ongoing'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                numberOfOngoingBorrowedBooks = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfOngoingBorrowedBooks;
    }
}
