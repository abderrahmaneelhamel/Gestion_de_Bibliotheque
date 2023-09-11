package dao;

import model.Librarian;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDAOImpl implements LibrarianDAO {
    private final Connection connection;

    public LibrarianDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addLibrarian(Librarian librarian) {
        String sql = "INSERT INTO librarians (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, librarian.getName());
            preparedStatement.setString(2, librarian.getEmail());
            preparedStatement.setString(3, librarian.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Librarian getLibrarianById(int librarianId) {
        String sql = "SELECT * FROM librarians WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, librarianId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Librarian librarian = new Librarian( new User(0, null, null, null, 0));
                librarian.setId(resultSet.getInt("id"));
                librarian.setName(resultSet.getString("name"));
                librarian.setEmail(resultSet.getString("email"));
                librarian.setPassword(resultSet.getString("password"));
                return librarian;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Librarian> getAllLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT * FROM librarians";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Librarian librarian = new Librarian( new User(0, null, null, null, 0));
                librarian.setId(resultSet.getInt("id"));
                librarian.setName(resultSet.getString("name"));
                librarian.setEmail(resultSet.getString("email"));
                librarian.setPassword(resultSet.getString("password"));
                librarians.add(librarian);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return librarians;
    }

    @Override
    public boolean updateLibrarian(Librarian librarian) {
        String sql = "UPDATE librarians SET name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, librarian.getName());
            preparedStatement.setString(2, librarian.getEmail());
            preparedStatement.setString(3, librarian.getPassword());
            preparedStatement.setInt(4, librarian.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteLibrarian(int librarianId) {
        String sql = "DELETE FROM librarians WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, librarianId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
