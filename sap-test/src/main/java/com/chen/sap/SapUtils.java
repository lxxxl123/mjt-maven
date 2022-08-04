package com.chen.sap;

import com.chen.sap.sap.SapConnectionPool;
import com.chen.sap.sap.SapReturnObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SapUtils {

    private static String interfaceId = "1";

    public static final String CHAR_X = "X";
    public static final String KEY_FLAG = "flag";
    public static final String KEY_MSG = "msg";
    public static final String KEY_HEAD = "head";
    public static final String KEY_DETAIl = "detail";
    public static final int MAP_DEF_LEN = 16;



    private static String[] getDdbtoutTable(){
        return new String[]{"OT_DDBT", "OT_DDZY", "OT_RESB", "OT_AUFM"};
    }
    private static String[][] getDdbtOutputData() {
        //输出tables
        String[] outTable = getDdbtoutTable();
        //ddbt表字段 43个
        String[] ddbtParam = {"MANDT", "AUFNR", "POSNR", "WERKS", "AUART", "GSTRP", "GLTRP", "GLTRS", "GETRI",
                "DISPO", "FEVOR", "PLNBEZ", "WEMNG", "WEMNG_TON", "STEXT", "CREATETIME", "PSMNG",
                "AMEIN", "PRDVERNO", "PRDVERNAME", "PLANBEGDAT", "DCRTDATE", "CCRTER", "ORDGROUPNO",
                "SYNCFLAG", "TECODATE", "GAMNG", "GMEIN", "PRUEFLOS", "IGMNG", "ERNAM",
                "AENAM", "AEDAT", "BOMQXGROUP", "GSTRI", "FTRMI", "GSUZI", "GEUZI", "GSTRS", "LGORT",
                "SUBWEMNG", "GLUZS", "GLUZP"};
        //ddzy表字段 9个
        String[] ddzyParam = {"AUFNR", "VORNR", "ARBPL", "KOSTL", "KTEXT", "ISM02", "ISERH", "LTXA1", "ISMAX"};
        //resb表字段 23个
        String[] resbParam = {"RSNUM", "RSPOS", "AUFNR", "BDTER", "MATNR", "WERKS", "LGORT", "BDMNG", "MEINS",
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
     * @param aufnr
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

            SapReturnObject obj = SapConnectionPool.getResultNew(interfaceId, functionName, null, inTable, inData, outTable, outData, null);
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

            SapReturnObject obj = SapConnectionPool.getResultNew(interfaceId, functionName, null, inTable, inData, outTable, outData, null);
            if (obj != null && obj.getTables() != null) {
                resultMap = obj.getTables();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * Z_QM_DDBT_INFO
     */
    public static void test1() {
        System.out.println(getDdbtInfo("20210101", "20220801"));
    }

    public static void test2() {
        System.out.println(getDdbtInfoByAufnr(Arrays.asList("000061000001", "000061000002")));
    }

    public static void main(String[] args) {
        test2();
    }
}