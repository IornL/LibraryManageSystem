package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Iron on 2016/12/11.
 */
public class Book extends RecursiveTreeObject<Book> {

      public static List<Book> getAllBooks() {
            SqlSession session =  ORMInterface.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            List<Book> books = mapper.getAllBooks();
            session.close();
            return books;
      }




      private int id, status, borrower;
      private String title, publisher, borrowedDate, pubDate;

      public Book() {};
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

      public String getBorrowedDate() { return this.borrowedDate; }

      public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
      }

      public String getPubDate() {
            return this.pubDate;
      }

      public int getBorrower() { return this.borrower; }


      public void save() {
            SqlSession session = ORMInterface.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            mapper.addBook(this);
            session.commit();
            session.close();
      }

}
