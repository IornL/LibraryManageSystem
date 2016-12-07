package Manager.Model;
/**
 * Created by Iron on 2016/12/6.
 */
public class Admin {

    private String id, password;

    public Admin() {};

    public Admin(String id,String password) {
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

    public void setPassword(String password ) {
        this.password = password;
    }

}
