package dao;
import model.Borrow;

import java.util.List;

public interface BorrowDAO {
    boolean borrowBook(Borrow borrow);

    List<Borrow> getAllBorrowsByReaderId(int readerId);
    public void returnBook(Borrow borrow,String currentDate);
}
