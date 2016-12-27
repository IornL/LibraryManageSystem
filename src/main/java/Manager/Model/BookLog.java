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
public class BookLog extends RecursiveTreeObject<BookLog> {
    public static class STATUS{
        public static final int INSIDE = 0;
        public static final int OUTSIDE = 1;
    }

    private int id, bookId, readerId;
    private String borrowedDate;

    public int getReaderId() {
        return readerId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}
