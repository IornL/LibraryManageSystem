package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface ReaderMapper {

    public List<Reader> getAllReaders();

    public void updateReader(Reader reader);

    public void addReader(Reader reader);

    public void deleteReader(Reader reader);

    public Reader selectReaderById(@Param("id")int id);

    public List<Book> getBorrowedBooks(@Param("borrower") int borrower);
}
