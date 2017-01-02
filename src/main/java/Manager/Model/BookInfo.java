package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Iron on 2016/12/27.
 */
public class BookInfo extends RecursiveTreeObject<BookInfo> {
    private int id, amount, categoryId, rest;
    private String title, pubDate, ISBN, publisher;
    private BookCategory category;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public BookCategory getCategory() {
        return category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
        this.categoryId = category.getId();
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public static List<BookInfo> selectAllBookInfos() {
        try(SqlSession session = ORMInterface.getSession()) {
            BookInfoMapper mapper = session.getMapper(BookInfoMapper.class);
            return mapper.selectAllBookInfos();
        }
    }

    public void save() {
        try(SqlSession session = ORMInterface.getSession()) {
            BookInfoMapper mapper = session.getMapper(BookInfoMapper.class);
            if(getId() == 0) {
                mapper.insertBookInfo(this);
                session.commit();
                setId(mapper.selectLastRowId());
            } else {
                mapper.updateBookInfo(this);
                session.commit();
            }

        }
    }

    public static Integer selectSumOfBookByCategory(BookCategory category) {
        try(SqlSession session = ORMInterface.getSession()){
            BookInfoMapper mapper = session.getMapper(BookInfoMapper.class);
            return mapper.selectSumOfBookByCategory(category);
        }
    }

    public void delete() {
        try(SqlSession session = ORMInterface.getSession()) {
            BookInfoMapper mapper = session.getMapper(BookInfoMapper.class);
            mapper.deleteBookInfo(this);
            session.commit();
        }
    }
}
