package dao;

import model.Reader;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAOImpl implements ReaderDAO {
    private final Connection connection;

    public ReaderDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addReader(Reader reader) {
        String sql = "INSERT INTO readers (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getEmail());
            preparedStatement.setString(3, reader.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Reader getReaderById(int readerId) {
        String sql = "SELECT * FROM readers WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Reader reader = new Reader(new User(0, null, null, null, 0));
                reader.setId(resultSet.getInt("id"));
                reader.setName(resultSet.getString("name"));
                reader.setEmail(resultSet.getString("email"));
                reader.setPassword(resultSet.getString("password"));
                return reader;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reader> getAllReaders() {
        List<Reader> readers = new ArrayList<>();
        String sql = "SELECT * FROM readers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Reader reader = new Reader( new User(0, null, null, null, 0));
                reader.setId(resultSet.getInt("id"));
                reader.setName(resultSet.getString("name"));
                reader.setEmail(resultSet.getString("email"));
                reader.setPassword(resultSet.getString("password"));
                readers.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return readers;
    }

    @Override
    public boolean updateReader(Reader reader) {
        String sql = "UPDATE readers SET name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getEmail());
            preparedStatement.setString(3, reader.getPassword());
            preparedStatement.setInt(4, reader.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteReader(int readerId) {
        String sql = "DELETE FROM readers WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, readerId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
