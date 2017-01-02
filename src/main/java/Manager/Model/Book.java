package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionException;
import org.sqlite.SQLiteException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Iron on 2016/12/11.
 */
public class Book extends RecursiveTreeObject<Book> {
    public void save() {
        try (SqlSession session = ORMInterface.getSession()){
            BookMapper mapper = session.getMapper(BookMapper.class);
            try{

                mapper.insertBook(this);
            } catch (PersistenceException e) {
                mapper.updateBookById(this);
            }
            session.commit();
        };
    }
    public static List<Book> selectBookByReader(Reader reader) {
        try(SqlSession session = ORMInterface.getSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.selectBookByReader(reader);
        }
    }
    public static void deleteBookByBookInfo(BookInfo bookInfo) {
        try(SqlSession session = ORMInterface.getSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.deleteBookByBookInfo(bookInfo);
            session.commit();
        }
    }
    public static Book selectBookById(String id) {
        try(SqlSession session = ORMInterface.getSession()){
            BookMapper mapper = session.getMapper(BookMapper.class);
            return mapper.selectBookById(id);
        }
    }
    public Calendar getReturnCalendar() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(this.borrowedDate));
        if(calendar.get(Calendar.MONTH) == 11){
            calendar.add(Calendar.YEAR ,1);
            calendar.set(Calendar.MONTH, 0);
        }
        else {
            calendar.add(Calendar.MONTH, 1);
        }
        return calendar;
    }

    public static List<Book> selectBookByBookInfo(BookInfo info) {
        try(SqlSession session = ORMInterface.getSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            return  mapper.selectBookByBookInfo(info);
        }
    }

    public void delete() {
        try(SqlSession session = ORMInterface.getSession()) {
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.deleteBookById(this);
            session.commit();
        }
    }

    public boolean isInLibrary() {
        return reader == null;
    }

    public boolean isOverdue() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.after(getReturnCalendar());
    }

    private int bookInfoId, readerId;
    private BookInfo bookInfo;
    private Reader reader;
    private String borrowedDate, id;


    public int getReaderId() {
        return readerId;
    }

    public void setBookInfoId(int bookInfoId) {
        this.bookInfoId = bookInfoId;
    }

    public int getBookInfoId() {
        return bookInfoId;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        this.bookInfoId = bookInfo.getId();
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        setReaderId(reader != null ? reader.getId() : 0);
    }
}
