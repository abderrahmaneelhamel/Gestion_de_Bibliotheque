import java.util.Date;

public class Return{
    private String id;
    private Book book;
    private Reader reader;
    private Date returnDate;

    public Return(String id, Book book, Reader reader, Date returnDate){
        this.id = id;
        this.book = book;
        this.reader = reader;
        this.returnDate = returnDate;
    }

    public String getId(){
        return id;
    }

    public Book getBook(){
        return book;
    }

    public Reader getReader(){
        return reader;
    }

    public Date getReturnDate(){
        return returnDate;
    }

    // Continue creating getter and setter methods for these fields...
}

