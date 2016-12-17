package Manager.Model;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Iron on 2016/12/7.
 */
public interface AdminMapper {
    public Admin selectAdminByID(@Param("id") String id, @Param("password") String password);
}
