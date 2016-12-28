package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface BookInfoMapper {
    public List<BookInfo> selectAllBooks();

    public void updateBook(BookInfo book);

    public void addBook(BookInfo boo);

    public void deleteBook(BookInfo book);

    public BookInfo selectBookById(@Param("id") int id);
}
