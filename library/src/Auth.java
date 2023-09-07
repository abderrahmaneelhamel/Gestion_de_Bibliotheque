import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Auth {
    // Method for user login
    public static User login() {
        User user = new User();
        System.out.println("Enter your email");
        String email = new Scanner(System.in).nextLine();

        // Validate user's email
        while (tools.isValidEmailFormat(email)) {
            System.out.println("Invalid email format. Please enter a valid email:");
            email = new Scanner(System.in).nextLine();
        }

        System.out.println("Enter your password");
        String password = new Scanner(System.in).nextLine();
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();

            // Query the database for user login
            ResultSet rs = statement.executeQuery("select * from users where email = '" + email + "' and password = '" + password + "'");
            if (rs.next()) {
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
                user.setRole(rs.getInt("role"));
            } else {
                System.out.println("Login failed. Invalid email or password.");
                return null;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method for user signup
    public static User signup() {
        User user = new User();
        System.out.println("Enter your Name: ");
        String name = new Scanner(System.in).nextLine();
        System.out.println("Enter your Email: ");
        String email = new Scanner(System.in).nextLine();

        // Validate user's email
        while (tools.isValidEmailFormat(email) || isValidEmail(email)) {
            if(isValidEmail(email)){
                System.out.println("Invalid email,this email is already registered. Please enter a valid email:");
            }
            if(tools.isValidEmailFormat(email)){
                System.out.println("Invalid email format. Please enter a valid email:");
            }
            email = new Scanner(System.in).nextLine();
        }
        System.out.println("Enter your Password: ");
        String password = new Scanner(System.in).nextLine();

        // Validate user's password
        while (tools.isValidPassword(password)) {
            System.out.println("Invalid password format. Password must be at least 8 characters long without spaces:");
            password = new Scanner(System.in).nextLine();
        }

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();

            // Insert user data into the database
            statement.executeUpdate("INSERT INTO users (name, email, password, role) VALUES ('" + user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + " ', 2)");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE email='" + user.getEmail() +"' AND password='" + user.getPassword() + "'");
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setRole(resultSet.getInt("role"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("User Added Successfully");
        return user;
    }
    public static boolean isValidEmail(String email) {
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT email FROM users");
            while (resultSet.next()) {
                if (resultSet.getString("email").equals(email)) {
                    return true;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
