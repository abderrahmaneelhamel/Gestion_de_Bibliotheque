import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Reader extends User{
    public Reader(User user){
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        this.setId(user.getId());
        this.setRole(user.getRole());
    }
    public void borrowBook() {
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books Where quantity  > 0");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "- " + resultSet.getString("title"));
            }
            int bookId;
            if(!resultSet.next()){
                System.out.println("Enter the book id");
                bookId = Main.tryParse(new Scanner(System.in).nextLine());
                if(checkIfBookIsAvailable(bookId)) {
                    System.out.println("Enter in how many days you're going to return it : ");
                    int duration = new Scanner(System.in).nextInt();
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String borrow_date = formatter.format(date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    cal.add(Calendar.DATE, duration);
                    date = cal.getTime();
                    String returnDate = formatter.format(date);
                    Borrow borrow = new Borrow(bookId, this, borrow_date, returnDate);
                    statement.executeUpdate("INSERT INTO borrow (book_id,reader_id,borrow_date,return_date,status) VALUES ('" + borrow.getBook() + "','" + borrow.getReader().getId() + "' ,'" + borrow.getBorrowDate() + "' ,'" + borrow.getReturnDate() + "','ongoing')");
                    ResultSet resultSet1 = statement.executeQuery("SELECT * FROM books WHERE id = '" + bookId + "'");
                    String ISBN = "";
                    while (resultSet1.next()) {
                        ISBN = resultSet1.getString("ISBN");
                    }
                    System.out.printf("Book borrowed successfully\nyou should return it by the : %s", returnDate);
                    System.out.printf("\nthe book ISBN is : %s , you should provide it when you want to return the book", ISBN);
                    statement.close();
                }else {
                    System.out.println("No book is available with this id\nexiting...");
                }
            }else{
                System.out.println("No book is available\nexiting...");
            }
            Main.endConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook() {
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            System.out.println("Enter the book ISBN");
            String ISBN = new Scanner(System.in).nextLine();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books WHERE ISBN = '" + ISBN + "'");
            int id = 0;
            while(resultSet.next()) {
                id = resultSet.getInt("id");
            }
            ResultSet resultSet1 = statement.executeQuery("SELECT br.id, b.ISBN, b.title , br.return_date, br.reader_id , br.borrow_date  FROM borrow AS br , books AS b WHERE br.book_id = b.id AND b.id = " + id);
            HashMap<String, String> borrowedBook = new HashMap<>();
            while(resultSet1.next()) {
                borrowedBook.put("ISBN", resultSet1.getString("ISBN"));
                borrowedBook.put("id", resultSet1.getString("id"));
                borrowedBook.put("title", resultSet1.getString("title"));
                borrowedBook.put("return_date", resultSet1.getString("return_date"));
                borrowedBook.put("reader_id", resultSet1.getString("reader_id"));
                borrowedBook.put("borrow_date", resultSet1.getString("borrow_date"));
            }
            if(!borrowedBook.isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                LocalDate today = LocalDate.now();
                Date date = new Date();
                String theDate = formatter.format(date);
                LocalDate return_date = LocalDate.parse(borrowedBook.get("return_date"));
                if (today.isAfter(return_date)) {
                    statement.executeUpdate("DELETE FROM borrow WHERE id = " + borrowedBook.get("id"));
                    statement.executeUpdate("INSERT INTO returns (book_id,reader_id,return_date) VALUES (" + id + "," + borrowedBook.get("reader_id") + ",'" + theDate + "')");
                    System.out.println("You are late to return the book");
                }else {
                    statement.executeUpdate("DELETE FROM borrow WHERE id = " + borrowedBook.get("id"));
                    statement.executeUpdate("INSERT INTO returns (book_id,reader_id,return_date) VALUES (" + id + "," + borrowedBook.get("reader_id") + ",'" + theDate + "')");
                    System.out.println("Thank you for returning the book");
                }
            }else {
                System.out.println("please enter valide data");
            }
            Main.endConnection(connection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void searchByTitle(){
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            System.out.println("Enter the title of the book");
            String title = new Scanner(System.in).nextLine();
            String query = "SELECT * FROM books WHERE title LIKE '%" + title + "%'";
            ResultSet resultSet = statement.executeQuery(query);
            int check = 0;
            while (resultSet.next()) {
                check++;
                System.out.println("title : "+resultSet.getString("title") + " | author : " + resultSet.getString("author") + " | ISBN : " + resultSet.getString("ISBN") + " | quantity : " + resultSet.getString("quantity"));
            }
            if (check == 0) {
                System.out.println("No book found");
            }
            Main.endConnection(connection);
            System.out.println("1- borrow a book? \n 2- exit");
            int choice = Main.tryParse(new Scanner(System.in).nextLine());
            if (choice == 1) {
                borrowBook();
            }
            else if (choice == 2) {
                System.out.println("thank you for using our service");
            }
            else {
                System.out.println("please enter a valid choice");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void searchByAuthor(){
        try {
            Connection connection = Main.connection();
            Statement statement = connection.createStatement();
            System.out.println("Enter the author name");
            String author = new Scanner(System.in).nextLine();
            String query = "SELECT * FROM books WHERE author LIKE '%" + author + "%'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println("title : "+resultSet.getString("title") + " | author : " + resultSet.getString("author") + " | ISBN : " + resultSet.getString("ISBN") + " | quantity : " + resultSet.getString("quantity"));
            }
            Main.endConnection(connection);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkIfBookIsAvailable(int bookId) {
        boolean isAvailable = false;

        try {
            Connection connection = Main.connection();
            String sql = "SELECT quantity from books where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                isAvailable = quantity > 0;
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("failed to check if book is available");
        }
        return isAvailable;
    }
}
