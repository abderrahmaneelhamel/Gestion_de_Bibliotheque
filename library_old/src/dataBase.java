import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dataBase {
    public static Connection connection() throws SQLException {
        //            Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/library", "postgres", "admin"
//            );
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library", "root", ""
        );
    }
}
