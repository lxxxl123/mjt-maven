package com.chen.sap;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.util.TypeUtils;
import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import com.chen.sap.entity.impl.AufnrPre;
import com.chen.sap.sap.SapConnectionPool;
import com.chen.sap.sap.SapReturnObject;
import com.sap.mw.jco.JCO;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chenwh3
 */
@Slf4j
public class SapUtils {

    private static final String INTERFACE_ID = "1";


    public static final int MAP_DEF_LEN = 16;
    public static final String AUFNR = "AUFNR";


    private static String[] getDdbtoutTable(){
        return new String[]{"OT_DDBT", "OT_DDZY", "OT_RESB", "OT_AUFM"};
    }
    private static String[][] getDdbtOutputData() {
        //ddbt表字段 43个
        String[] ddbtParam = {"MANDT", AUFNR, "POSNR", "WERKS", "AUART", "GSTRP", "GLTRP", "GLTRS", "GETRI",
                "DISPO", "FEVOR", "PLNBEZ", "WEMNG", "WEMNG_TON", "STEXT", "CREATETIME", "PSMNG",
                "AMEIN", "PRDVERNO", "PRDVERNAME", "PLANBEGDAT", "DCRTDATE", "CCRTER", "ORDGROUPNO",
                "SYNCFLAG", "TECODATE", "GAMNG", "GMEIN", "PRUEFLOS", "IGMNG", "ERNAM",
                "AENAM", "AEDAT", "BOMQXGROUP", "GSTRI", "FTRMI", "GSUZI", "GEUZI", "GSTRS", "LGORT",
                "SUBWEMNG", "GLUZS", "GLUZP"};
        //ddzy表字段 9个
        String[] ddzyParam = {AUFNR, "VORNR", "ARBPL", "KOSTL", "KTEXT", "ISM02", "ISERH", "LTXA1", "ISMAX"};
        //resb表字段 23个
        String[] resbParam = {"RSNUM", "RSPOS", AUFNR, "BDTER", "MATNR", "WERKS", "LGORT", "BDMNG", "MEINS",
                "ENMNG", "ENWRT", "BWART", "SAKNR", "POSNR", "AUSCH", "GPREIS", "PEINH", "XLOEK",
                "XWAOK", "KZEAR", "VORNR", "CHARG", "RSNUM2"};
        //aufm表字段 23个
        String[] aufmParam = {"MATVORNO", "MATVORYEAR", "MATVORITEM", "POSTDATE", "ORDERNO", "MATERIALNO", "FACTORY",
                "STOCKNO", "BATCH", "QUANTITY", "MOVETYPE", "WEIGHT", "AMOUNT", "BASEUNIT", "ELIKZ",
                "ERFME", "ABLAD", "WEMPF", "KZBEW", "RSNUM", "RSPOS", "EBELN", "SOBKZ"};

        String[][] outData = new String[4][ddbtParam.length];
        outData[0] = ddbtParam;
        outData[1] = ddzyParam;
        outData[2] = resbParam;
        outData[3] = aufmParam;
        return outData;
    }

    /**
     * 根据订单号查找订单
     * @param aufnr 订单号
     * @return
     */
    private static Map<String, List<HashMap<String, String>>> getDdbtInfoByAufnr(List<String> aufnr) {
        Map<String, List<HashMap<String, String>>> resultMap = new HashMap<>(MAP_DEF_LEN);
        try {
            String functionName = "Z_QM_DDBT_INFO1";
            //输入tables
            String[] inTable = {"IT_AUFNR"};

            HashMap<String, String>[][] inData = null;
            if (CollectionUtils.isNotEmpty(aufnr)) {
                inData = new HashMap[inTable.length][aufnr.size()];
                for (int i = 0; i < aufnr.size(); i++) {
                    inData[0][i] = new HashMap<>(MAP_DEF_LEN);
                    inData[0][i].put("AUFNR1", aufnr.get(i));
                }
            }

            String[] outTable = getDdbtoutTable();
            String[][] outData = getDdbtOutputData();

            SapReturnObject obj = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, null, inTable, inData, outTable, outData, null);
            if (obj != null && obj.getTables() != null) {
                resultMap = obj.getTables();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 根据日期查找订单
     */
    public static Map<String, List<HashMap<String, String>>> getDdbtInfo(String stStr, String etStr) {
        Map<String, List<HashMap<String, String>>> resultMap = new HashMap<>(MAP_DEF_LEN);
        try {
            String functionName = "Z_QM_DDBT_INFO";
            //输入tables
            String[] inTable = {"IT_AEDAT"};

            HashMap<String, String>[][] inData = null;
            if (StringUtils.isNotBlank(stStr)) {
                inData = new HashMap[inTable.length][1];
                inData[0][0] = new HashMap<>(MAP_DEF_LEN);
                //开始日期
                inData[0][0].put("AEDAT", stStr);
                //结束日期
                if (StringUtils.isNotBlank(etStr)) {
                    inData[0][0].put("AEDAT2", etStr);
                }
            }
            String[] outTable = getDdbtoutTable();
            String[][] outData = getDdbtOutputData();

            SapReturnObject obj = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, null, inTable, inData, outTable, outData, null);
            if (obj != null && obj.getTables() != null) {
                resultMap = obj.getTables();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 获取变更的ddbt订单号
     */
    public static List<Map> getChangedDdbt(Date from, Date to) {
        Map<String, List<HashMap<String, String>>> resultMap = new HashMap<>(MAP_DEF_LEN);
        try {
            String functionName = "Z_QM_DDBT_MODIFY";
            //输入tables
            HashMap<String, String> inParam = new HashMap<>(4);
            inParam.put("F_DATE", DateUtil.format(from, "yyyyMMdd"));
            inParam.put("T_DATE", DateUtil.format(to, "yyyyMMdd"));
            inParam.put("F_TIME", DateUtil.format(from, "HHmmss"));
            inParam.put("T_TIME", DateUtil.format(to, "HHmmss"));

            HashMap<String, String>[][] inData = null;

            String[] outTable = new String[]{"IT_AUFK","OT_AUFM"};
//            String[] outParam = {"OT_RES"};
            String[][] outData = new String[][]{new String[]{AUFNR,"AEDAT","AEZEIT","ERDAT","ERFZEIT"},new String[]{"QUANTITY","WEIGHT"}};

            SapReturnObject obj = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, inParam, null, inData, outTable, outData, null);
            if (obj != null && obj.getTables() != null) {
                resultMap = obj.getTables();
                log.info("{}", (List) resultMap.get("OT_AUFM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (List)resultMap.get("IT_AUFK");
    }


    /**
     * Z_QM_DDBT_INFO
     */
    public static void test1() {
        log.info("{}",getDdbtInfo("20210101", "20220801"));
    }

    public static void test2() {
        log.info("{}",getDdbtInfoByAufnr(Arrays.asList("000061000001", "000061000002")));
    }

    public static void test3()  {
        Date from = TypeUtils.castToDate("2022-01-25 00:00:00");
        Date to = TypeUtils.castToDate("2022-01-28 00:00:00");
        log.info("{}",getChangedDdbt(from,to));
    }


    public static List<HashMap<String, String>> getAufnr2Sv(String aufnr) {
        SapReturnObject sapResult = new SapReturnObject();
        String functionName = "ZQCRFC_AUFNR2SV";

        HashMap<String, String> inParam = new HashMap<>();
        //单号
        inParam.put("PAUFNR", aufnr);

        String[] outTable = {"TB_RETURN"};
        String[] param = {"ARBID", "KTEXT", "MATNR", "MAKTX", "AUFNR", "VORNR", "WERKS"};
        String[][] outData = new String[2][param.length];
        outData[0] = param;

        SapReturnObject newResult = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, inParam, null, null, outTable, outData, null);
        return newResult.getTables().get("TB_RETURN");
    }

    /**
     * 根据注解创建sap的字段映射
     */
    private static Map<String, String> buildFieldMap(Class<?> clazz){
        Field[] fields = ReflectUtil.getFields(clazz);
        // sap : 内存中的字段名
        HashMap<String, String> fieldNameMap = new LinkedHashMap<>(fields.length);
        Arrays.stream(fields).forEach(e -> {
            SapField sapField = e.getAnnotation(SapField.class);
            String sapFieldName;
            if (sapField != null) {
                sapFieldName = sapField.name();
            } else {
                sapFieldName = e.getName().toUpperCase();
            }
            fieldNameMap.put(sapFieldName, e.getName());
        });
        return fieldNameMap;
    }

    private static List<List<?>> runFunc(String functionName , Map<?,?> paramMap, List<?> ins
            , List<Class<?>> outs){
        String[] inTable = null;
        HashMap<String,String>[][] inTableDatas = null;
        /**
         * 创建inTable
         */
        if (ins == null) {
            //todo 根据ins构建入参表
        }

        /**
         * 创建outTable
         */
        List<String> outTables = new ArrayList<>();
        String[][] outTableDatas = new String[outs.size()][];
        List<Map<String, String>> fieldNameMaps = new ArrayList<>();
        for (int i = 0; i < outs.size(); i++) {
            Class<?> out = outs.get(i);
            SapTable sapTable = out.getAnnotation(SapTable.class);
            outTables.add(sapTable.name().toUpperCase());
            Map<String, String> fieldNameMap = buildFieldMap(out);
            fieldNameMaps.add(fieldNameMap);
            outTableDatas[i] = fieldNameMap.keySet().toArray(new String[]{});
        }
        HashMap<String,String> map = new HashMap<>();
        for (Map.Entry<?, ?> entry : paramMap.entrySet()) {
            map.put(StrUtil.toString(entry.getKey()), StrUtil.toString(entry.getValue()));
        }
        SapReturnObject newResult = SapConnectionPool.getResultNew(INTERFACE_ID
                    , functionName, map, inTable, inTableDatas, outTables.toArray(new String[]{}), outTableDatas, null);

        /**
         * 获取返回值
         */
        HashMap<String, List<HashMap<String, String>>> tables = newResult.getTables();
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreCase(true);
        List<List<?>>  res = new ArrayList<>();
        for (int i = 0; i < outTables.size(); i++) {
            copyOptions.setFieldMapping(fieldNameMaps.get(i));
            String outTable = outTables.get(i);
            Class<?> clazz = outs.get(i);
            List<HashMap<String, String>> table = tables.get(outTable);
            List<Object> list = table.stream().map(e -> BeanUtil.mapToBean(e, clazz,true, copyOptions)).collect(Collectors.toList());
            res.add(list);
        }
        return res;
    }

    private static <T> List<T> runFuncWithParamForOne(String functionName, Map<?, ?> paramMap
            , Class<T> out){
        List<List<?>> res = runFunc(functionName, paramMap, null, ListUtil.of(out));
        return (List<T>)res.get(0);
    }

    public static void main(String[] args) {
//        JCO.createClient(new Properties());
//
//        test3();
        System.out.println(runFuncWithParamForOne("ZQCRFC_AUFNR2SV", MapBuilder.create().put("PAUFNR", "6100000").build(), AufnrPre.class));
//
//        System.out.println(getAufnr2Sv("61"));


    }
}