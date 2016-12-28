package Manager.Model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

/**
 * Created by Iron on 2016/12/11.
 */
public class Book extends RecursiveTreeObject<Book> {
    public static class STATUS{
        public static final int INSIDE = 0;
        public static final int OUTSIDE = 1;
    }

    private int id, bookInfoId, readerId;
    private BookInfo bookInfo;
    private Reader reader;
    private String borrowedDate;
    private boolean returned;


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
