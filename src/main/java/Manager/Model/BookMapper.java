package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/28.
 */
public interface BookMapper {
    public void insertBook(Book book);
    public Book selectBookById(@Param("id") String id);
    public List<Book> selectBookByBookInfo(BookInfo bookInfo);
    public void deleteBookById(Book book);
    public void updateBookById(Book book);
    public void deleteBookByBookInfo(BookInfo info);
    public List<Book> selectBookByReader(Reader reader);
}
