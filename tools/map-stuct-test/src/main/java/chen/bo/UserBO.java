package chen.bo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserBO {

    /** 用户编号 **/
    private Integer userId;
    /** 用户名 **/
    private String username;
    /** 密码 **/
    private String password;

}
