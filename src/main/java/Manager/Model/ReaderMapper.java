package Manager.Model;

import java.util.List;

/**
 * Created by Iron on 2016/12/12.
 */
public interface ReaderMapper {

    public List<Reader> getAllReaders();

    public void updateReader(Reader reader);

    public void addReader(Reader reader);

    public void deleteReader(Reader reader);
}
