package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Iron on 2016/12/11.
 */
public class Book extends RecursiveTreeObject<Book> {
    public static class STATUS{
        public static final int INSIDE = 0;
        public static final int OUTSIDE = 1;
        public static final int FORBIDDEN =2;
    }
    public static Book selectBookById(int id) {
        SqlSession session = ORMInterface.getSession();
        BookMapper mapper = session.getMapper(BookMapper.class);
        Book result = mapper.selectBookById(id);
        session.close();
        return result;
    }

    public static List<Book> getAllBooks() {
        SqlSession session = ORMInterface.getSession();
        BookMapper mapper = session.getMapper(BookMapper.class);
        List<Book> books = mapper.getAllBooks();
        session.close();
        return books;
    }


    private int id, status, borrower;
    private String title, publisher, borrowedDate, pubDate;

    public Book() {
    }

    ;

    public Book(int id, String title, String publisher, String borrowedDate, int borrower, String pubDate, int status) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.borrowedDate = borrowedDate;
        this.borrower = borrower;
        this.pubDate = pubDate;
        this.status = status;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setBorrower(int borrower) {
        this.borrower = borrower;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getBorrowedDate() {
        return this.borrowedDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDate() {
        return this.pubDate;
    }

    public int getBorrower() {
        return this.borrower;
    }

    public boolean isOverdue() throws ParseException {
        if (this.getBorrowedDate().equals("null"))
            return false;
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());

        Calendar returnCalender = getReturnCalendar();
        return now.before(returnCalender);
    }

    public Calendar getReturnCalendar() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat("YYYY-MM-DD").parse(this.borrowedDate));
        calendar.add(Calendar.MONTH, 1);
        return calendar;
    }

    public void save() {
        SqlSession session = ORMInterface.getSession();
        BookMapper mapper = session.getMapper(BookMapper.class);
        if (this.getId() != 0)
            mapper.updateBook(this);
        else
            mapper.addBook(this);
        session.commit();
        session.close();
    }

    public void delete() {
        SqlSession session = ORMInterface.getSession();
        BookMapper mapper = session.getMapper(BookMapper.class);
        mapper.deleteBook(this);
        session.commit();
        session.close();
    }
}
