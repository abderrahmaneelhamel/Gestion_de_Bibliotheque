package dao;
import model.User;

import java.util.List;

public interface UserDAO {

    User authenticate(String email, String password);
    // Create
    boolean addUser(User user);
}