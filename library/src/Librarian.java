import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Librarian extends User {
    public Librarian(User user){
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setId(user.getId());
        this.setRole(user.getRole());
    }

    public void addBook() {
        Book book =  new Book();
        System.out.println("Enter book title");
        book.setBookTitle(new Scanner(System.in).nextLine());
        System.out.println("Enter book author");
        book.setBookAuthor(new Scanner(System.in).nextLine());
        System.out.println("Enter book ISBN");
        book.setBookISBN(new Scanner(System.in).nextLine());
        System.out.println("Enter book Quantity");
        book.setBookQuantity(new Scanner(System.in).nextInt());

        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO books (title, author, ISBN,quantity) VALUES ('" + book.getBookTitle() + "', '" + book.getBookAuthor() + "', '" + book.getBookISBN() + "','"+ book.getBookQuantity() +"')");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE ISBN='" + book.getBookISBN() +"'");
            while (resultSet.next()) {
                book.setBookId(resultSet.getInt("id"));
                book.setBookQuantity(resultSet.getInt("quantity"));
            }
            System.out.println(book.getBookTitle()+" added successfully");
            Main.endConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editBook() {
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity"));
            }
            int bookId;
            if(!resultSet.next()){
                System.out.println("Enter the book id");
                bookId = new Scanner(System.in).nextInt();
                Book book =  new Book();
                System.out.println("Enter the book title");
                book.setBookTitle(new Scanner(System.in).nextLine());
                System.out.println("Enter the book author");
                book.setBookAuthor(new Scanner(System.in).nextLine());
                System.out.println("Enter the book ISBN");
                book.setBookISBN(new Scanner(System.in).nextLine());
                System.out.println("Enter the book Quantity");
                book.setBookQuantity(new Scanner(System.in).nextInt());
                statement.executeUpdate("UPDATE books SET  title='"+book.getBookTitle()+"', author='"+book.getBookAuthor()+"', ISBN='"+book.getBookISBN()+"', quantity='"+book.getBookQuantity()+"' WHERE id='"+bookId+"'");
                System.out.println(book.getBookTitle()+" updated successfully");
            }else{
                System.out.println("No book is available\nexiting...");
            }
            Main.endConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deleteBook() {
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity"));
            }
            System.out.println("Enter the book id");
            int bookId = new Scanner(System.in).nextInt();
            try{
                statement.executeUpdate("DELETE FROM books WHERE id='"+bookId+"'");
                System.out.println("Book deleted successfully");
            }catch (SQLException e){
                statement.executeUpdate("UPDATE books SET quantity = 0 WHERE id='"+bookId+"'");
                System.out.println("this book is borrowed, so the quantity is set to 0");
            }
            Main.endConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void statistics() {
        try {
            System.out.println("all the books in the library :");
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + " - title : " + resultSet.getString("title")+ " | author : "+resultSet.getString("author")+" | ISBN : "+resultSet.getString("ISBN")+" | quantity : "+resultSet.getInt("quantity"));
            }
            System.out.print("Number of books in the library : ");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM books");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
            System.out.print("Number of books borrowed : ");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
            System.out.print("Number of books returned : ");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM returns");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
            System.out.print("Number of books available : ");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM books WHERE quantity>0");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
            System.out.print("Number of books lost : ");
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM borrow WHERE status = 'lost'");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1));
            }
            Main.endConnection(connection);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void search(){
        try {
            System.out.println("Enter the book ISBN : ");
            String isbn = new Scanner(System.in).nextLine();
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE ISBN = '"+isbn+"'");
            while (resultSet.next()) {
                System.out.println("title : "+resultSet.getString("title") + " | author : " + resultSet.getString("author") + " | quantity : " + resultSet.getString("quantity"));
            }
            Main.endConnection(connection);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}
