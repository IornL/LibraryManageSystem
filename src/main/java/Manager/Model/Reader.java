package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.apache.ibatis.session.SqlSession;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public class Reader extends RecursiveTreeObject<Reader> {

    private int id, borrowCount;
    private String name, address;
    private boolean frozen;

    public Reader() {};
    public  Reader(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public  Reader(int id, String name, String address, int borrowCount, boolean frozen) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.borrowCount = borrowCount;
        this.frozen = frozen;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public static List<Reader> getAllReaders() {
        try (SqlSession session = ORMInterface.getSession()) {
            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
            return mapper.getAllReaders();
        }
    }

    public static Reader selectReaderById(int id) {
        try (SqlSession session = ORMInterface.getSession()) {
            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
            return mapper.selectReaderById(id);
        }
    }

    public List<BookLog> getBorrowedBooks() {
        try (SqlSession session = ORMInterface.getSession()) {
            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
            return mapper.getBorrowedBooks(getId());
        }
    }
    public void save() {
        try (SqlSession session = ORMInterface.getSession()) {
            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
            if (getId() == 0)
                mapper.addReader(this);
            else
                mapper.updateReader(this);
            session.commit();
        }
    }
    public void delete() {
        try (SqlSession session = ORMInterface.getSession()) {
            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
            mapper.deleteReader(this);
            session.commit();
        }
    }

    public boolean checkOverDue() throws ParseException {
//        try (SqlSession session = ORMInterface.getSession()) {
//            ReaderMapper mapper = session.getMapper(ReaderMapper.class);
//            List<BookLog> bookLogs = mapper.getBorrowedBooks(getId());
//            for(BookLog bookLog : bookLogs) {
//            if (bookLog.isOverdue())
//                return true;
//            }
            return false;
    }

}
