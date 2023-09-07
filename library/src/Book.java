import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;

public class Book{
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private int quantity;

    public Book addBook(int id, String title, String author, String ISBN, int quantity){
        this.id = id;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.quantity = quantity;
        return  this;
    }

    // Getter and Setter for bookId
    public int getBookId(){
        return this.id;
    }

    public void setBookId(int id){
        this.id = id;
    }

    // Getter and Setter for bookTitle
    public String getBookTitle(){
        return this.title;
    }

    public void setBookTitle(String title){
        this.title = title;
    }

    // Getter and Setter for bookAuthor
    public String getBookAuthor(){
        return this.author;
    }

    public void setBookAuthor(String author){
        this.author = author;
    }

    // Getter and Setter for bookISBN
    public String getBookISBN(){
        return this.ISBN;
    }

    public void setBookISBN(String ISBN){
        this.ISBN = ISBN;
    }

    // Getter and Setter for bookStatus
    public int getBookQuantity(){
        return this.quantity;
    }

    public void setBookQuantity(int quantity){
        this.quantity = quantity;
    }

    // Continue creating getter and setter methods for the other fields...

    public String toString(){
        return "Book ID: " + this.id + "\n" +
                "Book Title: " + this.title + "\n" +
                "Book Author: " + this.author + "\n" +
                "Book ISBN: " + this.ISBN + "\n" +
                "Book quantity: " + this.quantity + "\n";
    }

    public static void checkOutDatedBorrows(){
        try {
            Connection connection = dataBase.connection();
            Statement statement = connection.createStatement();
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
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

