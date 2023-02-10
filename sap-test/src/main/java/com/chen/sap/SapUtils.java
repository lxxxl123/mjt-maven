package com.chen.sap;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.util.TypeUtils;
import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import com.chen.sap.entity.impl.*;
import com.chen.sap.ex.ServiceException;
import com.chen.sap.sap.SapConnectionPool;
import com.chen.sap.sap.SapReturnObject;
import com.chen.sap.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        String[][] outData = new String[1][param.length];
        outData[0] = param;

        SapReturnObject newResult = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, inParam, null, null, outTable, outData, null);
        return newResult.getTables().get("TB_RETURN");
    }


    public static List<HashMap<String, String>> getMatnr(String matnr) {
        String functionName = "ZQM_MATNR_EXP";


        String[] inTable = {"IT_MATNR"};
        String[] inParam = {"MATNR"};
        String[] outTable = {"OT_RETURN"};
        String[] param = {"MATNR","ZPDAT8"};

        HashMap<String, String>[][] inData = new HashMap[1][inParam.length];
        HashMap<String, String> map = new HashMap<>();
        map.put("MATNR", matnr);
        inData[0] = new HashMap[1];
        inData[0][0] = map;

        String[][] outData = new String[2][param.length];
        outData[0] = param;

        SapReturnObject newResult = SapConnectionPool.getResultNew(INTERFACE_ID, functionName, null, inTable, inData, outTable, outData, null);
        return newResult.getTables().get("OT_RETURN");
    }

    /**
     * 根据注解创建sap的字段映射
     */
    private static Map<String, String> buildFieldNameMap(Class<?> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        // sap : 内存中的字段名
        HashMap<String, String> fieldNameMap = new LinkedHashMap<>(fields.length);
        Arrays.stream(fields).forEach(e -> {
            SapField sapField = e.getAnnotation(SapField.class);
            String sapFieldName;
            if (sapField != null && StringUtils.isNotBlank(sapField.name())) {
                sapFieldName = sapField.name();
            } else {
                sapFieldName = StrUtil.toUnderlineCase(e.getName()).toUpperCase();
            }
            fieldNameMap.put(sapFieldName, e.getName());
        });
        return fieldNameMap;
    }

    /**
     * 根据注解创建sap的字段映射
     */
    private static Map<String, SapField> buildFieldMap(Class<?> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        // sap : 内存中的字段名
        HashMap<String, SapField> fieldNameMap = new LinkedHashMap<>(fields.length);
        Arrays.stream(fields).forEach(e -> {
            SapField sapField = e.getAnnotation(SapField.class);
            if (sapField != null) {
                fieldNameMap.put(e.getName(), sapField);
            }
        });
        return fieldNameMap;
    }

    private static String getTableName(Object o) {
        Class<?> clazz;
        if (o instanceof Class) {
            clazz = (Class<?>) o;
        } else {
            if (o instanceof Options) {
                return ((Options<?>) o).getTableName();

            }
            clazz = o.getClass();
        }
        SapTable sapTable = clazz.getAnnotation(SapTable.class);
        return sapTable.name().toUpperCase();
    }

    private static boolean isSapTable(Object o) {
        Class<?> clazz;
        if (o instanceof Class) {
            clazz = (Class<?>) o;
        } else {
            clazz = o.getClass();
        }
        SapTable sapTable = clazz.getAnnotation(SapTable.class);
        return o == Options.class || sapTable != null;
    }

    private static String[] getIgnoreField(Map<String, SapField> map) {
        List<String> ignoreField = new ArrayList<>();
        for (Map.Entry<String, SapField> entry : map.entrySet()) {
            if (entry.getValue().ignore()) {
                ignoreField.add(entry.getKey());
            }
        }
        return ignoreField.toArray(new String[]{});
    }

    private static final ConcurrentHashMap<Class<?>, CopyOptions> COPY_OPTIONS_MAP = new ConcurrentHashMap<>(64);


    /**
     * 使用缓存获取对象的copyOption
     */
    private static CopyOptions getCopyOptionMap(Object o) {
        if (COPY_OPTIONS_MAP.size() > 64) {
            log.error("警告!!! 缓存对象过多");
            COPY_OPTIONS_MAP.clear();
        }
        return COPY_OPTIONS_MAP.computeIfAbsent(o.getClass(), (clazz) -> {
            Map<String, String> fieldNameMap = buildFieldNameMap(clazz);
            Map<String, SapField> fieldMap = buildFieldMap(clazz);
            CopyOptions copyOptions = new CopyOptions();
            // 忽略空值
            copyOptions.setIgnoreNullValue(true);
            // 忽略指定字段
            copyOptions.setIgnoreProperties(getIgnoreField(fieldMap));
            copyOptions.setFieldValueEditor((type, value) -> value == null ? "" : StrUtil.toString(value));
            copyOptions.setFieldMapping(MapUtil.reverse(fieldNameMap));
            return copyOptions;
        });

    }

    private static HashMap<String, String> objToMap(Object o) {
        CopyOptions copyOptions = getCopyOptionMap(o);
        Map<String, Object> map = new HashMap<>();
        BeanUtil.beanToMap(o, map, copyOptions);
        return (HashMap) map;
    }

    private static <T> SapRes<T> runFuncFor(String functionName, Map<?, ?> paramMap, List<List<?>> ins
            , List<Class<?>> outs , Class<T> outParamClazz) {
        String[] inTable = null;
        HashMap<String, String>[][] inTableDatas = null;
        if (CollUtil.isEmpty(outs)) {
            outs = Collections.emptyList();
        }
        /**
         * 创建inTable
         */
        if (CollUtil.isNotEmpty(ins)) {
            ins = ins.stream().filter(CollUtil::isNotEmpty)
                    .collect(Collectors.toList());

            // 生成表名
            inTable = new String[ins.size()];
            inTableDatas = new HashMap[ins.size()][];
            for (int i = 0; i < ins.size(); i++) {
                Object o = ins.get(i).get(0);
                String tableName = getTableName(o);
                inTable[i] = tableName;
            }
            // 生成map数据
            for (int i = 0; i < ins.size(); i++) {
                List<?> list = ins.get(i);
                inTableDatas[i] = new HashMap[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    inTableDatas[i][j] = objToMap(list.get(j));
                }
            }
        }

        /**
         * 创建inParam
         */
        HashMap<String, String> strParam = new HashMap<>(4);
        Map<String, HashMap<String, String>> objParam = new HashMap<>(4);
        if (paramMap != null) {
            for (Map.Entry<?, ?> entry : paramMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (isSapTable(entry.getValue())) {
                    objParam.put(entry.getKey().toString(), objToMap(objParam));
                } else {
                    strParam.put(entry.getKey().toString(), StringUtil.toNotNullStr(entry.getValue()));
                }
            }
        }

        /**
         * 创建outTable
         */
        List<String> outTables = new ArrayList<>(outs.size());
        String[][] outTableDatas = new String[outs.size()][];
        List<Map<String, String>> fieldNameMaps = new ArrayList<>(outs.size());
        for (int i = 0; i < outs.size(); i++) {
            Class<?> out = outs.get(i);
            outTables.add(getTableName(out));
            Map<String, String> fieldNameMap = buildFieldNameMap(out);
            fieldNameMaps.add(fieldNameMap);
            outTableDatas[i] = fieldNameMap.keySet().toArray(new String[]{});
        }

        String[] outTable = outTables.toArray(new String[]{});
        if (log.isDebugEnabled()) {
            log.debug("function: {}", functionName);
            log.debug("inParam : {}", strParam);
            log.debug("inParamObj : {}", objParam);
            log.debug("inTable : {}", Arrays.toString(inTable));
            log.debug("inTableDatas : {}", Arrays.deepToString(inTableDatas));
            log.debug("outTableDatas : {}", Arrays.toString(outTable));
            log.debug("outTableDatas : {}", Arrays.deepToString(outTableDatas));
        }

        /**
         * 执行函数
         */
        SapReturnObject newResult = SapConnectionPool.getResultNew(INTERFACE_ID
                , functionName
                , strParam
                , inTable, inTableDatas
                , outTable, outTableDatas, null);

        if (log.isDebugEnabled()) {
            log.debug("sap resTable = [{}]", newResult.getTables());
            log.debug("sap resParam = [{}]", newResult.getExports());
        }

        /**
         * 获取返回值
         */
        T t = null;
        if (outParamClazz != null && newResult.getExports() != null) {
            HashMap<String, Object> outParam = newResult.getExports();
            Map<String, String> map = buildFieldNameMap(outParamClazz);
            t = ReflectUtil.newInstance(outParamClazz);
            for (Map.Entry<String, Object> entry : outParam.entrySet()) {
                String key = entry.getKey();
                String fieldName = map.get(key);
                if (fieldName == null) {
                    continue;
                }
                Object obj = entry.getValue();
                Class<?> aClass = obj.getClass();
                CopyOptions copyOptions = buildCopyOption(aClass);
                copyOptions.setFieldMapping(buildFieldNameMap(aClass));
                Object o = BeanUtil.mapToBean(outParam, aClass, true, copyOptions);
                ReflectUtil.setFieldValue(t, fieldName, o);
            }
        }

        /**
         * 获取返回表
         */
        HashMap<String, List<HashMap<String, String>>> tables = newResult.getTables();
        if (StringUtil.isNotBlank(newResult.getErrorMsg())) {
            throw new ServiceException(newResult.getErrorMsg());
        }
        List<List<?>> res = new ArrayList<>();
        for (int i = 0; i < outTables.size(); i++) {
            String ot = outTables.get(i);
            Class<?> clazz = outs.get(i);
            CopyOptions copyOptions = buildCopyOption(clazz);
            copyOptions.setFieldMapping(fieldNameMaps.get(i));
            List<Object> list;
            if (tables == null || !tables.containsKey(ot)) {
                list = Collections.emptyList();
            } else {
                List<HashMap<String, String>> table = tables.get(ot);
                list = table.stream().map(e -> BeanUtil.mapToBean(e, clazz, true, copyOptions)).collect(Collectors.toList());
            }
            res.add(list);
        }
        return new SapRes<>(res,  t);
    }



    private static CopyOptions buildCopyOption(Class<?> clazz) {
        SapTable sapTable = clazz.getAnnotation(SapTable.class);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.setIgnoreCase(true);
        if (sapTable.trim()) {
            copyOptions.setFieldValueEditor((name, val) -> {
                if (val instanceof String) {
                    val = StrUtil.trim((String) val);
                }
                return val;
            });
        }
        return copyOptions;
    }



    private static List<List<?>> runFuncForTables(String functionName, Map<?, ?> paramMap, List<List<?>> ins
            , List<Class<?>> outs) {
        return runFuncFor(functionName, paramMap, ins, outs, null).getTables();
    }

    private static <T> List<T> runFuncForOne(String functionName, Map<?, ?> paramMap, List<List<?>> ins
            , Class<T> out) {
        return (List<T>)runFuncFor(functionName, paramMap, ins, ListUtil.of(out), null).getTables().get(0);
    }

    /**
     * 根据入参获取单个表的数据
     */
    private static <T> List<T> runFuncWithParamForOne(String functionName, Map<?, ?> paramMap
            , Class<T> out) {
        List<List<?>> res = runFuncForTables(functionName, paramMap, null, ListUtil.of(out));
        return (List<T>) res.get(0);
    }

    /**
     * 获取单个表的数据 , 入参为一个表
     */
    private static <T> List<T> runFuncWithOneTableForOne(String functionName, List<? extends Object> talbeObj
            , Class<T> out) {
        if (CollUtil.isEmpty(talbeObj)) {
            return Collections.emptyList();
        }
        List<List<?>> res = runFuncForTables(functionName, null, ListUtil.of(talbeObj), ListUtil.of(out));
        return (List<T>) res.get(0);
    }

    private static <T> List<T> runFuncWithTables(String functionName, List<List<? extends Object>> talbeObj
            , Class<T> out) {
        List<List<?>> res = runFuncForTables(functionName, null, talbeObj, ListUtil.of(out));
        return (List<T>) res.get(0);
    }


    public static void main(String[] args) {
        System.out.println(runFuncWithParamForOne("ZQCRFC_AUFNR2SV", MapBuilder.create().put("PAUFNR", "6100000").build(), AufnrPre.class));

//        System.out.println(runFuncWithOneTableForOne("ZQM_MATNR_EXP", ListUtil.of(new MatnrExpIn("000000025500013782")), MatnrExp.class));
//        System.out.println(runFuncWithTables("Z_QM_GET_ZQALSLIST"
//                , ListUtil.of(
//                        Options.buildEqList("T_PRUEFLOS", ListUtil.of("010000885831")),
//                        Options.buildBetween("T_ART", "01", "03"),
//                        Options.buildBetween("T_ERDAT",DateUtil.parse("2021-03-11 00:00:00"), null )
//                )
//                , SapYdQals.class));


//
//        System.out.println(getAufnr2Sv("61"));


    }
}