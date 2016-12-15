package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface BookMapper {
    public List<Book> getAllBooks();
    public void addBook(Book book);
}
