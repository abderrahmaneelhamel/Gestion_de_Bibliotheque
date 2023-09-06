import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        checkOutDatedBorrows();
        System.out.println("Welcome to the library management system!");
        System.out.println("Please choose from the following options:");
        System.out.println("1 : login \n2 : signup");
        String input = new Scanner(System.in).nextLine();
        int choice = tryParse(input);
        while (!(choice == 2 || choice == 1))  {
            System.out.println("Invalid input");
            System.out.println("\n1 : login \n2 : signup");
            input = new Scanner(System.in).nextLine();
            choice = tryParse(input);
        }
        if (choice == 1) {
            User current = login();
            int state = 2;
            while (state == 2) {
                if(current == null){
                    break;
                }
            if(current.getRole() == 1) {
                Librarian Librarian = new Librarian(current);
                librarianMenu(Librarian);
            }
            else if(current.getRole() == 2) {
                Reader reader = new Reader(current);
                readerMenu(reader);
            }
            System.out.println("\n1 : exit \n2 : restart");
            String test = new Scanner(System.in).nextLine();
            state = tryParse(test);
            while (!(state == 2 || state == 1))  {
                    System.out.println("Invalid input");
                    System.out.println("\n1 : exit \n2 : restart");
                    test = new Scanner(System.in).nextLine();
                    state = tryParse(test);
            }
            cls();
        }
        } else if (choice == 2) {
            User current = signup();
            int state = 2;
            while (state == 2) {
            Reader reader = new Reader(current);
            readerMenu(reader);
            System.out.println("\n1 : exit \n2 : restart");
            String test = new Scanner(System.in).nextLine();
            state = tryParse(test);
            while (!(state == 2 || state == 1))  {
                System.out.println("Invalid input");
                System.out.println("\n1 : exit \n2 : restart");
                test = new Scanner(System.in).nextLine();
                state = tryParse(test);
            }
            cls();
        }
        }
    }

    public static Connection connection() throws  SQLException{
        //            Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/library", "postgres", "admin"
//            );
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library", "root", ""
        );
    }
    public static void endConnection(Connection connection){
        try {
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Error closing connection");
        }
    }
    public static User login() {
        User user =  new User();
        System.out.println("Enter your email");
        String email = new Scanner(System.in).nextLine();
        while(!isValidEmail(email)){
            System.out.println("enter a valid Email : ");
            email = new Scanner(System.in).nextLine();
        }
        System.out.println("Enter your password");
        String password = new Scanner(System.in).nextLine();

        try {
            Connection connection = connection();
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
            endConnection(connection);
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
        while(!isValidEmail(email)){
             System.out.println("enter a valid Email : ");
            email = new Scanner(System.in).nextLine();
        }
        System.out.println("enter your Password : ");
        String password = new Scanner(System.in).nextLine();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            Connection connection = connection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO users (name, email, password,role) VALUES ('" + user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + " ', 2)");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE email='" + user.getEmail() +"' AND password='" + user.getPassword() + "'");
            while (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setRole(resultSet.getInt("role"));
            }
            endConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("User Added Successfully");
        return user;
    }
    public static void readerMenu(Reader reader) {
        System.out.println("hello "+reader.getName());
        System.out.println("1 : borrow a book \n2 : return a book\n3 : search a book by title\n4 : search book by author\n5 : log out");
        int choice = tryParse(new Scanner(System.in).nextLine());
        while (!(choice == 2 || choice == 1 || choice == 3 ||  choice == 4 ||  choice == 5))  {
            System.out.println("Invalid input");
            System.out.println("1 : borrow a book \n2 : return a book\n3 : search a book by title\n4 : search book by author\n5 : log out");
            choice = tryParse(new Scanner(System.in).nextLine());
        }
        switch (choice) {
            case 1 -> reader.borrowBook();
            case 2 -> reader.returnBook();
            case 3 -> reader.searchByTitle();
            case 4 -> reader.searchByAuthor();
            case 5 -> System.out.println("Logging out");
            default -> System.out.println("Invalid choice");
        }
    }

    public static void librarianMenu(Librarian librarian) {
        System.out.println("Welcome Mr Librarian");
        System.out.println("1 : add a book \n2 : edit a book\n3 : delete a book\n4 : statistics \n5 : search\n6: log out");
        int choice = tryParse(new Scanner(System.in).nextLine());
        while (!(choice == 2 || choice == 1 || choice == 3 || choice == 4  || choice == 5 ||  choice == 6))  {
            System.out.println("Invalid input");
            System.out.println("1 : add a book \n2 : edit a book\n3 : delete a book\n4 : statistics \n5 : search\n6: log out");
            choice = tryParse(new Scanner(System.in).nextLine());
        }
        switch (choice) {
            case 1 -> librarian.addBook();
            case 2 -> librarian.editBook();
            case 3 -> librarian.deleteBook();
            case 4 -> librarian.statistics();
            case 5 -> librarian.search();
            case 6 -> System.out.println("Logging out");
            default -> System.out.println("Invalid choice");
        }
    }

    public static void checkOutDatedBorrows(){
        try {
            Connection connection = connection();
            Statement  statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM borrow");
            HashMap<String, String> borrowedBooks = new HashMap<>();
            while (resultSet.next()) {
                borrowedBooks.put(resultSet.getString("id"), resultSet.getString("return_date"));
            }
            for (String key : borrowedBooks.keySet()) {
                String returnDate = borrowedBooks.get(key);
                LocalDate today = LocalDate.now();
                LocalDate return_date = LocalDate.parse(returnDate);
                if (today.isAfter(return_date)) {
                    statement.executeUpdate("UPDATE borrow SET status = 'lost' WHERE id = '" + key + "'");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void cls() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}