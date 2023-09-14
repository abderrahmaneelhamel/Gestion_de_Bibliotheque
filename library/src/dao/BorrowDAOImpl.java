package dao;
import model.Borrow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAOImpl implements BorrowDAO {
    private final Connection connection;

    public BorrowDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void borrowBook(Borrow borrow) {
        String sql = "INSERT INTO borrow (book_id, reader_id, borrow_date, return_date, status) VALUES (?, ?, '"+borrow.getBorrowDate()+"', '"+borrow.getReturnDate()+"', ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, borrow.getBookId());
            preparedStatement.setInt(2, borrow.getReaderId());
            preparedStatement.setString(3, borrow.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Borrow> getAllBorrowsByReaderId(int readerId) {
        List<Borrow> borrows = new ArrayList<>();
        String sql = "SELECT * FROM borrow WHERE reader_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Borrow borrow = new Borrow(resultSet.getInt("book_id"), resultSet.getInt("reader_id"), resultSet.getString("borrow_date"), resultSet.getString("return_date"), resultSet.getString("status"));
                borrow.setId(resultSet.getInt("id"));
                // You may need to fetch associated book and reader here
                borrows.add(borrow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrows;
    }

    @Override
    public void returnBook(Borrow borrow,String currentDate) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM borrow WHERE id = " + borrow.getId());
            statement.executeUpdate("INSERT INTO returns (book_id, reader_id, return_date) VALUES ("
                    + borrow.getBookId() + ","
                    + borrow.getReaderId() + ",'"
                    + currentDate + "')");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
