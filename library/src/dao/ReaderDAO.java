package dao;
import model.Reader;

import java.util.List;

public interface ReaderDAO {
    // Create
    boolean addReader(Reader reader);

    // Read
    Reader getReaderById(int readerId);
    List<Reader> getAllReaders();

    // Update
    boolean updateReader(Reader reader);

    // Delete
    boolean deleteReader(int readerId);
}
