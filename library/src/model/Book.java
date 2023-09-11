package model;
public class Book {
    private int id;
    private String title;
    private String author;
    private String ISBN;
    private int quantity;


    // Constructor with all fields
    public Book(String title, String author, String ISBN, int quantity) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.quantity = quantity;
    }

    // Getters and setters for all properties

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Additional getter methods

    public String getBookTitle() {
        return title;
    }

    public String getBookAuthor() {
        return author;
    }

    public String getBookISBN() {
        return ISBN;
    }

    public int getBookQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "-" + id +
                " | title='" + title + '\'' +
                " | author='" + author + '\'' +
                " | ISBN='" + ISBN + '\'' +
                " | quantity=" + quantity ;
    }
}
