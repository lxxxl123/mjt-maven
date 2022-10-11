package com.chen;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extrict {
    private static String s = "{CHARG=, ENWRT=69948109543.07, BWART=531, WERKS=1200, LGORT=2168, BDMNG=325.421, PEINH=1, MEINS=M3, AUFNR=000063471740, ENMNG=63472055.700, KZEAR=X, RSNUM2=8405794, RSNUM=0008405794, SAKNR=5001800001, GPREIS=1102.03, POSNR=40, RSPOS=0004, XLOEK=X, VORNR=0010, BDTER=2021-11-22, XWAOK=X, MATNR=6019805, AUSCH=0}";
    private static Pattern compile = Pattern.compile("(\\w+)=(.*?),");

    public static void main(String[] args) {
        Matcher matcher = compile.matcher(s);
        List<String> keys = new ArrayList<>();
        List<String> vals = new ArrayList<>();

        while (matcher.find()) {
            keys.add(matcher.group(1));
            vals.add("'" + matcher.group(2) + "'");
        }
        System.out.printf("insert into pp_resb (%s) values (%s)", String.join(",", keys), String.join(",", vals));


    }
}
