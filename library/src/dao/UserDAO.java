package dao;
import model.User;

import java.util.List;

public interface UserDAO {

    User authenticate(String email, String password);
    // Create
    boolean addUser(User user);

    // Read
    User getUserById(int userId);
    List<User> getAllUsers();

    // Update
    boolean updateUser(User user);

    // Delete
    boolean deleteUser(int userId);
}