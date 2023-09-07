import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Librarian extends User {
    // Constructor for Librarian
    public Librarian(User user){
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setId(user.getId());
        this.setRole(user.getRole());
    }

    // Method to add a book to the library
    public void addBook() {
        Book book =  new Book();
        System.out.println("Enter book title");
        book.setBookTitle(new Scanner(System.in).nextLine());
        System.out.println("Enter book author");
        book.setBookAuthor(new Scanner(System.in).nextLine());
        System.out.println("Enter book ISBN");
        book.setBookISBN(new Scanner(System.in).nextLine());
        while (isValidISBN(book.getBookISBN())){
            System.out.println("Enter a valid book ISBN");
            book.setBookISBN(new Scanner(System.in).nextLine());
        }
        System.out.println("Enter book Quantity");
        book.setBookQuantity(tools.tryParse(new Scanner(System.in).nextLine()));
        while (book.getBookQuantity() < 0){
            System.out.println("Enter a valid book Quantity");
            book.setBookQuantity(tools.tryParse(new Scanner(System.in).nextLine()));
        }
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();

            // Insert book data into the database
            statement.executeUpdate("INSERT INTO books (title, author, ISBN,quantity) VALUES ('" + book.getBookTitle() + "', '" + book.getBookAuthor() + "', '" + book.getBookISBN() + "','"+ book.getBookQuantity() +"')");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE ISBN='" + book.getBookISBN() +"'");
            while (resultSet.next()) {
                book.setBookId(resultSet.getInt("id"));
                book.setBookQuantity(resultSet.getInt("quantity"));
            }
            System.out.println(book.getBookTitle()+" added successfully");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add an admin user
    public void addAdmin() {
        System.out.println("Enter admin name");
        String adminName = new Scanner(System.in).nextLine();
        System.out.println("Enter admin Email");
        String adminEmail = new Scanner(System.in).nextLine();

        // Validate admin's email
        while(tools.isValidEmailFormat(adminEmail)){
            System.out.println("Enter a valid Email : ");
            adminEmail = new Scanner(System.in).nextLine();
        }

        System.out.println("Enter your Password : ");
        String adminPassword = new Scanner(System.in).nextLine();

        // Validate admin's password
        while(!tools.isValidPassword(adminPassword)){
            System.out.println("Enter a valid Password : ");
            adminPassword = new Scanner(System.in).nextLine();
        }
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();

            // Insert admin user data into the database
            statement.executeUpdate("INSERT INTO users (name, email, password,role) VALUES ('" + adminName + "', '" + adminEmail + "', '" + adminPassword + "',1)");
            System.out.println("Admin added successfully");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to edit a book's details
    public void editBook() {
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity"));
            }
            int bookId;

            if(!resultSet.next()){
                System.out.println("Enter the book id");
                bookId = tools.tryParse(new Scanner(System.in).nextLine());
                Book book =  new Book();
                System.out.println("Enter the book title");
                book.setBookTitle(new Scanner(System.in).nextLine());
                System.out.println("Enter the book author");
                book.setBookAuthor(new Scanner(System.in).nextLine());
                System.out.println("Enter the book ISBN");
                book.setBookISBN(new Scanner(System.in).nextLine());
                System.out.println("Enter book Quantity");
                book.setBookQuantity(tools.tryParse(new Scanner(System.in).nextLine()));
                while (book.getBookQuantity() < 0){
                    System.out.println("Enter a valid book Quantity");
                    book.setBookQuantity(tools.tryParse(new Scanner(System.in).nextLine()));
                }
                statement.executeUpdate("UPDATE books SET  title='"+book.getBookTitle()+"', author='"+book.getBookAuthor()+"', ISBN='"+book.getBookISBN()+"', quantity='"+book.getBookQuantity()+"' WHERE id='"+bookId+"'");
                System.out.println(book.getBookTitle()+" updated successfully");
            }else{
                System.out.println("No book is available\nExiting...");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to delete a book
    public void deleteBook() {
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity"));
            }
            System.out.println("Enter the book id");
            int bookId = tools.tryParse(new Scanner(System.in).nextLine());

            try{
                statement.executeUpdate("DELETE FROM books WHERE id='"+bookId+"'");
                System.out.println("Book deleted successfully");
            }catch (SQLException e){
                try {
                    statement.executeUpdate("UPDATE books SET quantity = 0 WHERE id='" + bookId + "'");
                    System.out.println("This book is borrowed, so the quantity is set to 0");
                }catch (SQLException e1){
                    System.out.println("This book is not in the database");
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate library statistics
    public void statistics() {
        try {
            File file = new File("statistics.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            tools.printInFile("statistics\n", "statistics.txt");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            System.out.println("All the books in the library :");
            tools.printInFile("all the books in the library :", "statistics.txt");
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity")+"\n");
                tools.printInFile(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity")+"\n", "statistics.txt");
            }
            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            System.out.println("All the borrowed books :");
            tools.printInFile("all the borrowed books :", "statistics.txt");
            resultSet = statement.executeQuery("SELECT b.title , br.borrow_date , br.return_date, u.name , br.status FROM users AS u, borrow AS br , books AS b WHERE br.book_id = b.id AND u.id = br.reader_id;");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("title") + " | borrow date : " + resultSet.getString("borrow_date") + " | return date : " + resultSet.getString("return_date") + " | reader name : " + resultSet.getString("name") + " | status : " + resultSet.getString("status")+"\n");
                tools.printInFile(resultSet.getString("title") + " | borrow date : " + resultSet.getString("borrow_date") + " | return date : " + resultSet.getString("return_date") + " | reader name : " + resultSet.getString("name") + " | status : " + resultSet.getString("status")+"\n", "statistics.txt");
            }
            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            System.out.print("Number of books in the library : ");
            tools.printInFile("Number of books in the library : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.print("Number of readers : ");
            tools.printInFile("Number of readers : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1)-1); // 1 is the admin
                tools.printInFile(resultSet.getInt(1)-1+"\n", "statistics.txt");
            }
            System.out.print("Number of books borrowed : ");
            tools.printInFile("Number of books borrowed : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.print("Number of books returned : ");
            tools.printInFile("Number of books returned : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM returns");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.print("Number of books available : ");
            tools.printInFile("Number of books available : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM books WHERE quantity>0");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.print("Number of books lost : ");
            tools.printInFile("Number of books lost : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow WHERE status = 'lost'");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.print("Number of ongoing borrowed books : ");
            tools.printInFile("Number of ongoing borrowed books : ", "statistics.txt");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow WHERE status = 'ongoing'");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
                tools.printInFile(resultSet.getInt(1)+"\n", "statistics.txt");
            }
            System.out.println("==================================================================================================================");
            tools.printInFile("==================================================================================================================\n", "statistics.txt");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to search for a book by ISBN
    public void search(){
        try {
            System.out.println("Enter the book ISBN or title: ");
            String input = new Scanner(System.in).nextLine();
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE ISBN = '"+input+"' OR title LIKE '%"+input+"%'");
            while (resultSet.next()) {
                System.out.println("Title : "+resultSet.getString("title") + " | Author : " + resultSet.getString("author") + " | Quantity : " + resultSet.getString("quantity"));
            }
            connection.close();
        }
        catch (SQLException e) {
            System.out.println("Invalid ISBN or title");
        }
    }
    public static boolean isValidISBN(String isbn) {
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ISBN FROM books");
            while (resultSet.next()) {
                if (resultSet.getString("ISBN").equals(isbn)) {
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
