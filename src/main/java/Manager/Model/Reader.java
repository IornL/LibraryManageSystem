package Manager.Model;

import Manager.ORMInterface;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
class Reader extends RecursiveTreeObject<Reader> {

    private int id, borrowCount;
    private String name, address;
    private boolean frozen;

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

    public String getName() {
        return name;
    }

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

    public List<Reader> getAllReaders() {
        SqlSession session = ORMInterface.getSession();
        ReaderMapper mapper = session.getMapper(ReaderMapper.class);
        List<Reader> readers = mapper.getAllReaders();
        session.close();
        return readers;
    }

    public void save() {
        SqlSession session = ORMInterface.getSession();
        ReaderMapper mapper = session.getMapper(ReaderMapper.class);
        if (getId() == 0)
            mapper.addReader(this);
        else
            mapper.updateReader(this);
        session.close();
    }

    public void delete() {
        SqlSession session = ORMInterface.getSession();
        ReaderMapper mapper = session.getMapper(ReaderMapper.class);
        mapper.deleteReader(this);
        session.close();
    }

}
