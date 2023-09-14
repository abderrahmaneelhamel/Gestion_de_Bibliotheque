package dao;
import model.Borrow;

import java.util.List;

public interface BorrowDAO {
    void borrowBook(Borrow borrow);
    List<Borrow> getAllBorrowsByReaderId(int readerId);
    void returnBook(Borrow borrow,String currentDate);
}
