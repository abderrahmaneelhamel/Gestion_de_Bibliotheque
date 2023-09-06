import java.util.Date;
public class Borrow {
    private int id;
    private int book_id;
    private Reader reader;
    private String borrowDate;

    private String returnDate;

    public Borrow(int book_id, Reader reader, String borrowDate,String returnDate){
        this.book_id = book_id;
        this.reader = reader;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getId(){
        return this.id;
    }

    public int getBook(){
        return this.book_id;
    }

    public Reader getReader(){
        return this.reader;
    }

    public String getBorrowDate(){
        return this.borrowDate;
    }
    public String getReturnDate(){
        return this.returnDate;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setBook(int book_id){
        this.book_id = book_id;
    }

    public void setReader(Reader reader){
        this.reader = reader;
    }

    public void setBorrowDate(String borrowDate){
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(String returnDate){
        this.returnDate = returnDate;
    }


    // Continue creating getter and setter methods for these fields...
}

