package dao;
import model.Librarian;

import java.util.List;

public interface LibrarianDAO {
    // Create
    boolean addLibrarian(Librarian librarian);

    // Read
    Librarian getLibrarianById(int librarianId);
    List<Librarian> getAllLibrarians();

    // Update
    boolean updateLibrarian(Librarian librarian);

    // Delete
    boolean deleteLibrarian(int librarianId);
}
