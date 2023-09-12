package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection connection() throws SQLException {
//                    Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/library", "postgres", "admin"
//            );
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/library", "postgres", "admin"
            );
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
