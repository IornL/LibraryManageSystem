package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface BookMapper {
    public List<Book> getAllBooks();

    public void updateBook(Book book);

    public void addBook(Book book);

    public void deleteBook(Book book);

    public Book selectBookById(@Param("id") int id);
}
