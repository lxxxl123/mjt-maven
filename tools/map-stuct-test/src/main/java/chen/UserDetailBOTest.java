package chen;

import chen.bo.UserDetailBO;
import chen.convert.UserDetailConvert;
import chen.dataobject.UserDO;

public class UserDetailBOTest {

    public static void main(String[] args) {
        // 创建 UserDO 对象
        UserDO userDO = new UserDO()
                .setId(1).setUsername("yudaoyuanma").setPassword("buzhidao");
        // 进行转换
        UserDetailBO userDetailBO = UserDetailConvert.INSTANCE.convert(userDO);
        System.out.println(userDetailBO.getUserId());
    }

}
