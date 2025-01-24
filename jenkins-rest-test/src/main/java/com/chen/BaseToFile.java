package com.chen;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;

public class BaseToFile {

    public static void main(String[] args) {
        String base64 = FileUtil.readString("C:\\Users\\chenwh3\\Desktop\\base64.txt", "utf-8");
        byte[] decode = Base64.decode(base64);
        FileUtil.writeBytes(decode, "C:\\Users\\chenwh3\\Desktop\\base64.pdf");
    }
}
