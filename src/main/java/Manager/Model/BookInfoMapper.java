package Manager.Model;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface BookInfoMapper {
    public List<BookInfo> selectAllBookInfos();

    public void updateBookInfo(BookInfo bookInfo);

    public void insertBookInfo(BookInfo bookInfo);

    public void deleteBookInfo(BookInfo bookInfo);

    public BookInfo selectBookInfoById(@Param("id") int id);

    public Integer selectLastRowId();

    public Integer selectSumOfBookByCategory(BookCategory category);
}
