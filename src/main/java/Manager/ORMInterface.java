package Manager;

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
    public static SqlSession getSession() {
        return sessionFactory.openSession();

    }

}
