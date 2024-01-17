package chen.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.HashMap;
import java.util.List;

/**
 * @author chenwh3
 */
@Slf4j
public class FindRepeatFromTxt {
    public static void main(String[] args) {
        HashMap<Object, Object> map = new HashMap<>();
        List<String> strings = FileUtil.readUtf8Lines("C:\\Users\\chenwh3\\Desktop\\Temp\\left.txt");
        for (String s : strings) {
            if (StrUtil.isBlank(s)) {
                continue;
            }
            String[] split = s.split("\\s+");
            String cno = split[0];
            String vtcode = split.length > 1 ? split[1] : "";
            MultiKey<String> key = new MultiKey<String>(cno, vtcode);
            if (map.containsKey(key)) {
                log.error("重复值 {} : {}", cno, vtcode);
            } else {
                map.put(key, null);
            }
        }

    }
}
