package chen.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author chenwh3
 */
@Slf4j
public class DistinctTxt {
    public static void main(String[] args) {
        HashSet<Object> set = new HashSet<>();
        List<String> strings = FileUtil.readUtf8Lines("C:\\Users\\chenwh3\\Desktop\\Temp\\distinct.txt");
        for (String s : strings) {
            set.add(s);
        }
        System.out.println(set);


    }
}
