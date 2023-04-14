package com.chen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TextDealer {
    public static void main(String[] args) {
        String s = FileUtil.readString("C:\\Users\\chenwh3\\Desktop\\t1.txt", "utf-8");
        String[] union_alls = s.split("union all");
        List<Object> list = new ArrayList<>();
        for (String union_all : union_alls) {
            HashMap<Object, Object> map = new HashMap<>();
            list.add(map);
            String[] split = union_all.split("\n");
            for (String s1 : split) {
                if (s1.contains(" as ")) {
                    s1 = StrUtil.removeSuffix(s1.trim(), ",");
                    String[] split1 = s1.split(" +as +");
                    String val= ReUtil.get("'(.*)'", split1[0], 1);
                    String key = split1[1];
                    map.put(key, val);
                }
            }
        }
        System.out.println(JSONUtil.toJsonStr(list));
    }
}
