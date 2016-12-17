package Manager.Model;

import Manager.ORMInterface;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by Iron on 2016/12/6.
 */
public class Admin {

    public static Admin selectAdmin(String id, String password) {
        SqlSession session = ORMInterface.getSession();
        AdminMapper mapper = session.getMapper(AdminMapper.class);
        Admin resultAdmin = mapper.selectAdminByID(id, password);
        session.close();
        return resultAdmin;
    }

    private String id, password;

    public Admin() {
    }

    ;

    public Admin(String id, String password) {
        this.id = id;
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
