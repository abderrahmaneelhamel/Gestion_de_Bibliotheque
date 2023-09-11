package dao;
import model.Borrow;

import java.util.List;

public interface BorrowDAO {
    // Create
    boolean borrowBook(Borrow borrow);

    // Read
    Borrow getBorrowById(int borrowId);
    List<Borrow> getAllBorrowsByReaderId(int readerId);

    // Update
    boolean returnBook(int borrowId);

    // Delete
    boolean deleteBorrow(int borrowId);
}
