package dao;
import model.Borrow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAOImpl implements BorrowDAO {
    private final Connection connection;

    public BorrowDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean borrowBook(Borrow borrow) {
        String sql = "INSERT INTO borrow (book_id, reader_id, borrow_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrow.getBookId());
            preparedStatement.setInt(2, borrow.getReaderId());
            preparedStatement.setString(3, borrow.getBorrowDate());
            preparedStatement.setString(4, borrow.getReturnDate());
            preparedStatement.setString(5, borrow.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Borrow getBorrowById(int borrowId) {
        String sql = "SELECT * FROM borrow WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrowId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Borrow borrow = new Borrow(0, 0, null, null, null);
                borrow.setId(resultSet.getInt("id"));
                // You may need to fetch associated book and reader here
                borrow.setBorrowDate(resultSet.getString("borrow_date"));
                borrow.setReturnDate(resultSet.getString("return_date"));
                borrow.setStatus(resultSet.getString("status"));
                return borrow;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Borrow> getAllBorrowsByReaderId(int readerId) {
        List<Borrow> borrows = new ArrayList<>();
        String sql = "SELECT * FROM borrow WHERE reader_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Borrow borrow = new Borrow(0, 0, null, null, null);;
                borrow.setId(resultSet.getInt("id"));
                // You may need to fetch associated book and reader here
                borrow.setBorrowDate(resultSet.getString("borrow_date"));
                borrow.setReturnDate(resultSet.getString("return_date"));
                borrow.setStatus(resultSet.getString("status"));
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    @Override
    public boolean returnBook(int borrowId) {
        String sql = "UPDATE borrow SET status = 'returned' WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrowId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBorrow(int borrowId) {
        String sql = "DELETE FROM borrow WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrowId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
