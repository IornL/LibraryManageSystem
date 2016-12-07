package Manager; /**
 * Created by Iron on 2016/12/7.
 */

import Manager.Model.Admin;
import Manager.Model.AdminMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;

/**
 * Created by Iron on 2016/12/7.
 */
public class ORMInterface {
    private static SqlSessionFactory sessionFactory;



    static public void init() throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatisConfig.xml");
        sessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    public static Admin selectAdmin(String id, String password) {
        SqlSession session = sessionFactory.openSession();
        AdminMapper mapper = session.getMapper(AdminMapper.class);
        Admin resultAdmin = mapper.selectAdminByID(id, password);
        session.close();
        return resultAdmin;
    }
}
