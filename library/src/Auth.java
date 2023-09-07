import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Auth {
    public static User login() {
        User user =  new User();
        System.out.println("Enter your email");
        String email = new Scanner(System.in).nextLine();
        while(!tools.isValidEmail(email)){
            System.out.println("enter a valid Email : ");
            email = new Scanner(System.in).nextLine();
        }
        System.out.println("Enter your password");
        String password = new Scanner(System.in).nextLine();
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from users where email = '" + email + "' and password = '" + password + "'");
            if (rs.next()) {
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
                user.setRole(rs.getInt("role"));

            } else {
                System.out.println("Login Failed");
                return  null;
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    public static User signup() {
        User user =  new User();
        System.out.println("enter your Name : ");
        String name = new Scanner(System.in).nextLine();
        System.out.println("enter your Email : ");
        String email = new Scanner(System.in).nextLine();
        while(!tools.isValidEmail(email)){
            System.out.println("enter a valid Email : ");
            email = new Scanner(System.in).nextLine();
        }
        System.out.println("enter your Password : ");
        String password = new Scanner(System.in).nextLine();
        while(!tools.isValidPassword(password)){
            System.out.println("enter a valid Password : ");
            password = new Scanner(System.in).nextLine();
        }
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO users (name, email, password,role) VALUES ('" + user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + " ', 2)");
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
}
