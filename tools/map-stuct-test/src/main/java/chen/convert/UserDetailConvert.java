package chen.convert;

import chen.bo.UserDetailBO;
import chen.dataobject.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDetailConvert {

    UserDetailConvert INSTANCE = Mappers.getMapper(UserDetailConvert.class);

    @Mappings(
            @Mapping(source = "id", target = "userId" , expression = "")
    )
    UserDetailBO convert(UserDO userDO);

}
