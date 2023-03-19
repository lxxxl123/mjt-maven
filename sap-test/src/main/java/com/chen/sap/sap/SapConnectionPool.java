package com.chen.sap.sap;

import com.chen.sap.utils.CommonManage;
import com.chen.sap.utils.DesUtil;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.IMetaData;
import com.sap.mw.jco.IRepository;
import com.sap.mw.jco.JCO;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

/**
 * schedulejob
 *
 * @author yangzj2
 * @date 2019/9/7
 */


public class SapConnectionPool {

    private static final String DEFAULT_POOL_NAME = "SAP_POOL";

    private static String SAP_client = "";
    private static String SAP_user = "";
    private static String SAP_passwd = "";
    private static String SAP_mshost = "";
    private static String SAP_r3name = "";
    private static String SAP_group = "";
    private static boolean groupFlag = false;  //true:组登录，false：ip登录
    private static Properties properties = null;

    private final static String prefsfile = "config-saphttp.properties";

    /**
     * 构造函数
     */
    public static synchronized void init(String interfaceId,String pool_name) {
        Properties logonProperties = new Properties();
        int maxConnection = 10;
        try {
            if (properties == null) {
                properties = loadProperties("config-htwy.properties");
            }
            if ("0".equals(interfaceId)) {
                String config = CommonManage.toNotNullString(properties.get("jco.config"));
                if ("".equals(config) || "ip".equals(config)) {//ip登录
                    logonProperties.put("jco.client.ashost", properties.get("jco.client.ashost"));// 服务器IP
                    logonProperties.put("jco.client.client", properties.get("jco.client.client"));// 客户端
                    logonProperties.put("jco.client.sysnr", properties.get("jco.client.sysnr"));// 系统编号
                    logonProperties.put("jco.client.user", DesUtil.decrypt((String) properties.get("jco.client.user"))); // 用户名
                    logonProperties.put("jco.client.passwd", DesUtil.decrypt((String) properties.get("jco.client.passwd"))); // 密码
                    maxConnection = Integer.parseInt(properties.get("jco.maxConnection").toString());
                    createConnectionPool(pool_name, maxConnection, logonProperties);
                } else if ("group".equals(config)) {//组登录
                    groupFlag = true;
                    SAP_client = CommonManage.toNotNullString(properties.get("jco.client.client"));
                    SAP_user = DesUtil.decrypt((String) properties.get("jco.client.user"));
                    SAP_passwd = DesUtil.decrypt((String) properties.get("jco.client.passwd"));
                    SAP_mshost = CommonManage.toNotNullString(properties.get("jco.client.ashost"));
                    SAP_r3name = CommonManage.toNotNullString(properties.get("jco.client.r3name"));
                    SAP_group = CommonManage.toNotNullString(properties.get("jco.client.group"));
                }
            } else if ("1".equals(interfaceId)) {
                String config = CommonManage.toNotNullString(properties.get("jco1.config"));
                if ("".equals(config) || "ip".equals(config)) {//ip登录
                    logonProperties.put("jco.client.ashost", properties.get("jco1.client.ashost"));// 服务器IP
                    logonProperties.put("jco.client.client", properties.get("jco1.client.client"));// 客户端
                    logonProperties.put("jco.client.sysnr", properties.get("jco1.client.sysnr"));// 系统编号
                    logonProperties.put("jco.client.user", DesUtil.decrypt((String) properties.get("jco1.client.user"))); // 用户名
                    logonProperties.put("jco.client.passwd", DesUtil.decrypt((String) properties.get("jco1.client.passwd"))); // 密码
                    maxConnection = Integer.parseInt(properties.get("jco1.maxConnection").toString());
                    createConnectionPool(pool_name, maxConnection, logonProperties);
                } else if ("group".equals(config)) {//组登录
                    groupFlag = true;
                    SAP_client = CommonManage.toNotNullString(properties.get("jco1.client.client"));
                    SAP_user = DesUtil.decrypt((String) properties.get("jco1.client.user"));
                    SAP_passwd = DesUtil.decrypt((String) properties.get("jco1.client.passwd"));
                    SAP_mshost = CommonManage.toNotNullString(properties.get("jco1.client.ashost"));
                    SAP_r3name = CommonManage.toNotNullString(properties.get("jco1.client.r3name"));
                    SAP_group = CommonManage.toNotNullString(properties.get("jco1.client.group"));
                }
            } else if ("2".equals(interfaceId)) {
                String config = CommonManage.toNotNullString(properties.get("jco2.config"));
                if ("".equals(config) || "ip".equals(config)) {//ip登录
                    logonProperties.put("jco.client.ashost", properties.get("jco2.client.ashost"));// 服务器IP
                    logonProperties.put("jco.client.client", properties.get("jco2.client.client"));// 客户端
                    logonProperties.put("jco.client.sysnr", properties.get("jco2.client.sysnr"));// 系统编号
                    logonProperties.put("jco.client.user", DesUtil.decrypt((String) properties.get("jco2.client.user"))); // 用户名
                    logonProperties.put("jco.client.passwd", DesUtil.decrypt((String) properties.get("jco2.client.passwd"))); // 密码
                    maxConnection = Integer.parseInt(properties.get("jco2.maxConnection").toString());
                    createConnectionPool(pool_name, maxConnection, logonProperties);
                } else if ("group".equals(config)) {//组登录
                    groupFlag = true;
                    SAP_client = CommonManage.toNotNullString(properties.get("jco2.client.client"));
                    SAP_user = DesUtil.decrypt((String) properties.get("jco2.client.user"));
                    SAP_passwd = DesUtil.decrypt((String) properties.get("jco2.client.passwd"));
                    SAP_mshost = CommonManage.toNotNullString(properties.get("jco2.client.ashost"));
                    SAP_r3name = CommonManage.toNotNullString(properties.get("jco2.client.r3name"));
                    SAP_group = CommonManage.toNotNullString(properties.get("jco2.client.group"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param interfaceId       要调用接口id(0:200，1:120)
     * @param functionName      要调用的SAP函数名称
     * @param param             参数import
     * @param selectTable       查询的表
     * @param selectParam       查询的表的字段
     * @param resultTable       返回接口的表
     * @param resultSelectParam 返回表的字段
     * @param resultParam       返回字段
     * @return SapReturnObject 返回值，包含export和tables
     */
    @SuppressWarnings("deprecation")
    public static SapReturnObject getResultNew(String interfaceId, String functionName, HashMap<String, String> param, String[] selectTable, HashMap<String, String>[][] selectParam, String[] resultTable, String[][] resultSelectParam, String[] resultParam) {
        SapReturnObject obj = new SapReturnObject();
        Properties p = loadProperties(prefsfile);
        String control = String.valueOf(p.get("useQueue"));
        String[] functionlist = String.valueOf(p.get("supprotFunction")).split(",");
        if ("on".equalsIgnoreCase(control)) {
            boolean inlist = false;
            for (int i = 0; i < functionlist.length; i++) {
                if (functionName.equalsIgnoreCase(functionlist[i].trim())) {
                    inlist = true;
                    break;
                }
            }
            if (!inlist) {
                control = "off";
            }
        }
        if ("on".equalsIgnoreCase(control)) {
            try {
                obj = SapHttpClient.doRequest(interfaceId, functionName, param, selectTable, selectParam, resultTable, resultSelectParam, resultParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            HashMap<String, List<HashMap<String, String>>> tables = new HashMap<String, List<HashMap<String, String>>>();
            HashMap<String, Object> exports = new HashMap<String, Object>();
            JCO.Client connection = null;
            try {
                String poolName = DEFAULT_POOL_NAME;
                if ("0".equals(interfaceId)) {
                    poolName = "SAP_POOL0";
                } else if ("1".equals(interfaceId)) {
                    poolName = "SAP_POOL1";
                } else if ("2".equals(interfaceId)) {
                    poolName = "SAP_POOL2";
                }

                if (functionName != null && !"".equals(functionName)) {
                    if (groupFlag) {//组登录
                        init(interfaceId,poolName);
                        connection = getConnectionInPool("");
                    } else {//ip登录
                        connection = getConnectionInPool(poolName);
                        if (connection == null) {
                            init(interfaceId,poolName);
                            connection = getConnectionInPool(poolName);
                        }
                    }
                    connection.connect();
                    if (connection != null && connection.isAlive()) {
                        IRepository repository = null;
                        if (groupFlag) {//组登录
                            repository = JCO.createRepository("MYRepository", connection);
                        } else {//ip登录
                            repository = JCO.createRepository("MYRepository", poolName);
                        }
                        // 获得一个指定函数名的函数模板
                        IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
                        JCO.Function function = ft.getFunction();
                        JCO.ParameterList input = function.getImportParameterList();
                        if (param != null && param.size() > 0) {
                            Iterator<?> it = param.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                input.setValue(value, key);
                            }
                        }
                        if (selectTable != null && selectTable.length > 0) {
                            for (int i = 0; i < selectTable.length; i++) {
                                if (selectParam != null && selectParam.length > 0) {
                                    JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
                                    for (int j = 0; j < selectParam[i].length; j++) {
                                        // 设定该行对应变量
                                        if (selectParam[i] != null && selectParam[i].length > 0 && selectParam[i][j] != null && selectParam[i][j].size() > 0) {
                                            // 新增一条空行
                                            tDateRange.appendRow();
                                            Iterator<?> it = selectParam[i][j].entrySet().iterator();
                                            while (it.hasNext()) {
                                                Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                                String key = (String) entry.getKey();
                                                String value = (String) entry.getValue();
                                                if (value != null && !"".equals(value)) {
                                                    // 定位到第j行
                                                    tDateRange.setRow(j);
                                                    tDateRange.setValue(value, key);
                                                    //System.out.println(tDateRange.getValue(key));
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        connection.execute(function);
                        // 输出参数1
                        if (resultParam != null && resultParam.length > 0) {
                            JCO.ParameterList output = function.getExportParameterList();
                            for (String tempParam : resultParam) {
                                exports.put(tempParam, output.getValue(tempParam));
                            }
                        }
                        // 输出参数2
                        if (resultTable != null && resultTable.length > 0) {
                            for (int s = 0; s < resultTable.length; s++) {
                                JCO.Table flights = function.getTableParameterList().getTable(resultTable[s]);
                                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < flights.getNumRows(); i++) {
                                    flights.setRow(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    for (int x = 0; x < resultSelectParam[s].length; x++) {
                                        String key = resultSelectParam[s][x];
                                        String value = flights.getString(resultSelectParam[s][x]);
                                        if (key != null) {
                                            key = key.trim();
                                        }
                                        if (value != null) {
                                            value = value.trim();
                                        }
                                        map.put(key, value);
                                    }
                                    list.add(map);
                                }
                                tables.put(resultTable[s], list);
                            }
                        }

                    } else {
                        obj.setErrorMsg("sap连接失败");
                    }
                } else {
                    obj.setErrorMsg("参数为空");
                }
                if (tables.size() == 0) {
                    tables = null;
                } else {
                    obj.setTables(tables);
                }
                if (exports.size() == 0) {
                    exports = null;
                } else {
                    obj.setExports(exports);
                }
            } catch (Exception e) {
                e.printStackTrace();
                obj.setErrorMsg(e.getMessage());
            } finally {    //add by chenl	2015.04
                releaseConnection(connection);
            }
        }
        return obj;

    }

    /**
     * @param interfaceId       要调用接口id(0:200，1:120)
     * @param functionName      要调用的SAP函数名称
     * @param param             参数import
     * @param selectTable       查询的表
     * @param selectParam       查询的表的字段
     * @param resultTable       返回接口的表
     * @param resultSelectParam 返回表的字段 为空
     * @param resultParam       返回字段
     * @return SapReturnObject 返回值，包含export和tables
     * 同步SAP 获取所有字段
     * 作者：易登科
     * 开发时间：2017-07-25
     * (interfaceId, functionName,null,inTable, inData , outTable,outData,null);
     */
    @SuppressWarnings("deprecation")
    public static SapReturnObject getResultNewAllField(String interfaceId, String functionName, HashMap<String, String> param, String[] selectTable, HashMap<String, String>[][] selectParam, String[] resultTable, String[][] resultSelectParam, String[] resultParam) {

        SapReturnObject obj = new SapReturnObject();
        Properties p = loadProperties(prefsfile);
        String control = String.valueOf(p.get("useQueue"));
        String[] functionlist = String.valueOf(p.get("supprotFunction")).split(",");
        if ("on".equalsIgnoreCase(control)) {
            boolean inlist = false;
            for (int i = 0; i < functionlist.length; i++) {
                if (functionName.equalsIgnoreCase(functionlist[i].trim())) {
                    inlist = true;
                    break;
                }
            }
            if (!inlist) {
                control = "off";
            }
        }
        if ("on".equalsIgnoreCase(control)) {
            try {
                obj = SapHttpClient.doRequest(interfaceId, functionName, param, selectTable, selectParam, resultTable, resultSelectParam, resultParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            HashMap<String, List<HashMap<String, String>>> tables = new HashMap<String, List<HashMap<String, String>>>();
            HashMap<String, List<HashMap<String, String>>> tablesField = new HashMap<String, List<HashMap<String, String>>>();
            HashMap<String, Object> exports = new HashMap<String, Object>();
            JCO.Client connection = null;
            try {
                String poolName = DEFAULT_POOL_NAME;
                if ("0".equals(interfaceId)) {
                    poolName = "SAP_POOL0";
                } else if ("1".equals(interfaceId)) {
                    poolName = "SAP_POOL1";
                } else if ("2".equals(interfaceId)) {
                    poolName = "SAP_POOL2";
                }

                if (functionName != null && !"".equals(functionName)) {
                    if (groupFlag) {//组登录
                        init(interfaceId,poolName);
                        connection = getConnectionInPool("");
                    } else {//ip登录
                        connection = getConnectionInPool(poolName);
                        if (connection == null) {
                            init(interfaceId,poolName);
                            connection = getConnectionInPool(poolName);
                        }
                    }
                    connection.connect();
                    if (connection != null && connection.isAlive()) {
                        IRepository repository = null;
                        if (groupFlag) {//组登录
                            repository = JCO.createRepository("MYRepository", connection);
                        } else {//ip登录
                            repository = JCO.createRepository("MYRepository", poolName);
                        }
                        // 获得一个指定函数名的函数模板
                        IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
                        JCO.Function function = ft.getFunction();
                        JCO.ParameterList input = function.getImportParameterList();
                        if (param != null && param.size() > 0) {
                            Iterator<?> it = param.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                input.setValue(value, key);
                            }
                        }
                        if (selectTable != null && selectTable.length > 0) {
                            for (int i = 0; i < selectTable.length; i++) {
                                if (selectParam != null && selectParam.length > 0) {
                                    JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
                                    for (int j = 0; j < selectParam[i].length; j++) {
                                        // 设定该行对应变量
                                        if (selectParam[i] != null && selectParam[i].length > 0 && selectParam[i][j] != null && selectParam[i][j].size() > 0) {
                                            // 新增一条空行
                                            tDateRange.appendRow();
                                            Iterator<?> it = selectParam[i][j].entrySet().iterator();
                                            while (it.hasNext()) {
                                                Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                                String key = (String) entry.getKey();
                                                String value = (String) entry.getValue();
                                                if (value != null && !"".equals(value)) {
                                                    // 定位到第j行
                                                    tDateRange.setRow(j);
                                                    tDateRange.setValue(value, key);
                                                    //System.out.println(tDateRange.getValue(key));
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        connection.execute(function);
                        // 输出参数1
                        if (resultParam != null && resultParam.length > 0) {
                            JCO.ParameterList output = function.getExportParameterList();
                            for (String tempParam : resultParam) {
                                exports.put(tempParam, output.getValue(tempParam));
                            }
                        }
                        // 输出参数2
                        if (resultTable != null && resultTable.length > 0) {
                            for (int s = 0; s < resultTable.length; s++) {
                                JCO.Table flights = function.getTableParameterList().getTable(resultTable[s]);
                                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                                List<HashMap<String, String>> listField = new ArrayList<HashMap<String, String>>();
                                IMetaData mFieldName = flights.getMetaData();

                                HashMap<String, String> mapTableType = new HashMap<String, String>();
                                for (int i = 0; i < mFieldName.getFieldCount(); i++) {
                                    //System.out.println("------------>第" + i + "个字段名：" + mFieldName.getName(i) + " / 字段类型：" + flights.getTypeAsString(mFieldName.getName(i)));

                                    String key = mFieldName.getName(i);
                                    String cType = flights.getTypeAsString(mFieldName.getName(i));
                                    mapTableType.put(key, cType);

                                    listField.add(mapTableType);
                                }
                                tablesField.put(resultTable[s], listField);
                                tables.put("###" + resultTable[s], listField);

                                for (int i = 0; i < flights.getNumRows(); i++) {
                                    flights.setRow(i);

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    for (int inKey = 0; inKey < mFieldName.getFieldCount(); inKey++) {
                                        String key = mFieldName.getName(inKey);
                                        String value = flights.getString(key);
                                        if (key != null) {
                                            key = key.trim();
                                        }
                                        if (value != null) {
                                            value = value.trim();
                                        }
                                        map.put(key, value);
                                    }

                                    list.add(map);
                                }
                                tables.put(resultTable[s], list);
                            }
                        }
                    } else {
                        obj.setErrorMsg("sap连接失败");
                    }
                } else {
                    obj.setErrorMsg("参数为空");
                }
                if (tables.size() == 0) {
                    tables = null;
                } else {
                    obj.setTables(tables);
                }
                if (tablesField.size() == 0) {
                    tablesField = null;
                } else {
                    obj.setTablesField(tablesField);
                }

                if (exports.size() == 0) {
                    exports = null;
                } else {
                    obj.setExports(exports);
                }
            } catch (Exception e) {
                e.printStackTrace();
                obj.setErrorMsg(e.getMessage());
            } finally {
                //System.out.println("SAP连接失败!");
                releaseConnection(connection);
            }
        }
        return obj;
    }

    /**
     * @param interfaceId  要调用接口id(0:查询余额，1:参考DN单)
     * @param functionName 要调用的SAP函数名称
     * @param selectTable  查询的表
     * @param selectParam  查询的表的字段
     * @param resultTable  返回接口的表
     * @param resultParam  返回字段
     * @return List<HashMap<String ,String>>
     */
    @SuppressWarnings({"deprecation", "unchecked"})
    public static List<HashMap<String, String>> getResult(String interfaceId, String functionName, String[] selectTable, HashMap<String, String[]>[] selectParam, String resultTable, String[] resultParam) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Properties p = loadProperties(prefsfile);
        String control = String.valueOf(p.get("useQueue"));
        if ("on".equalsIgnoreCase(control)) {
            try {
                list = SapHttpClient.doRequestForList(interfaceId, functionName, selectTable, selectParam, resultTable, resultParam);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            JCO.Client connection = null;
            try {
                String poolName = DEFAULT_POOL_NAME;
                if ("0".equals(interfaceId)) {
                    poolName = "SAP_POOL0";
                } else if ("1".equals(interfaceId)) {
                    poolName = "SAP_POOL1";
                }
                if (functionName != null && !"".equals(functionName) && selectTable != null && resultTable != null && !"".equals(resultTable) && resultParam != null && resultParam.length > 0) {
                    if (groupFlag) {//组登录
                        init(interfaceId,poolName);
                        connection = getConnectionInPool("");
                    } else {//ip登录
                        connection = getConnectionInPool(poolName);
                        if (connection == null) {
                            init(interfaceId,poolName);
                            connection = getConnectionInPool(poolName);
                        }
                    }
                    connection.connect();
                    if (connection != null && connection.isAlive()) {
                        IRepository repository = null;
                        if (groupFlag) {//组登录
                            repository = JCO.createRepository("MYRepository", connection);
                        } else {//ip登录
                            repository = JCO.createRepository("MYRepository", poolName);
                        }
                        // 获得一个指定函数名的函数模板
                        IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
                        JCO.Function function = ft.getFunction();
                        if (selectTable != null && selectTable.length > 0) {
                            for (int i = 0; i < selectTable.length; i++) {
                                if (selectParam != null && selectParam.length > 0 && selectParam[i] != null) {
                                    JCO.Table tDateRange = function.getTableParameterList().getTable(selectTable[i]);
                                    // 设定该行对应变量
                                    if (selectParam[i] != null && selectParam[i].size() > 0) {
                                        Iterator<?> it = selectParam[i].entrySet().iterator();
                                        while (it.hasNext()) {
                                            Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                            String key = (String) entry.getKey();
                                            String[] value = (String[]) entry.getValue();
                                            if (value != null && value.length > 0) {
                                                for (int k = 0; k < value.length; k++) {
                                                    // 新增一条空行
                                                    tDateRange.appendRow();
                                                    // 定位到第k行
                                                    tDateRange.setRow(k);
                                                    tDateRange.setValue(value[k], key);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        connection.execute(function);
                        // 输出参数
                        JCO.Table flights = function.getTableParameterList().getTable(resultTable);
                        for (int i = 0; i < flights.getNumRows(); i++) {
                            flights.setRow(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            for (int x = 0; x < resultParam.length; x++) {
                                String key = resultParam[x];
                                String value = flights.getString(resultParam[x]);
                                if (key != null) {
                                    key = key.trim();
                                }
                                if (value != null) {
                                    value = value.trim();
                                }
                                map.put(key, value);
                            }
                            list.add(map);
                        }
                    } else {
                        System.out.println("======error:sap连接失败");
                    }
                } else {
                    System.out.println("======error:参数为空");
                }
                if (list.size() == 0) {
                    list = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {    //add by chenl	2015.04
                releaseConnection(connection);
            }
        }
        return list;

    }

    /**
     * @param interfaceId  要调用接口id(0:查询余额，1:参考DN单)
     * @param functionName 要调用的SAP函数名称
     * @param selectParam  查询参数
     * @param resultParam  返回字段
     * @return HashMap<String ,String>
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, Object> getParamResult(String interfaceId, String functionName, HashMap<String, String> selectParam, String[] resultParam) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Properties p = loadProperties(prefsfile);
        String control = String.valueOf(p.get("useQueue"));
        if ("on".equalsIgnoreCase(control)) {
            try {
                map = SapHttpClient.doRequestForMap(interfaceId, functionName, selectParam, resultParam);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            JCO.Client connection = null;
            try {
                String poolName = DEFAULT_POOL_NAME;
                if ("0".equals(interfaceId)) {
                    poolName = "SAP_POOL0";
                } else if ("1".equals(interfaceId)) {
                    poolName = "SAP_POOL1";
                }
                if (functionName != null && !"".equals(functionName) && resultParam != null && resultParam.length > 0) {
                    if (groupFlag) {//组登录
                        init(interfaceId,poolName);
                        connection = getConnectionInPool("");
                    } else {//ip登录
                        connection = getConnectionInPool(poolName);
                        if (connection == null) {
                            init(interfaceId,poolName);
                            connection = getConnectionInPool(poolName);
                        }
                    }
                    connection.connect();
                    if (connection != null && connection.isAlive()) {
                        IRepository repository = null;
                        if (groupFlag) {//组登录
                            repository = JCO.createRepository("MYRepository", connection);
                        } else {//ip登录
                            repository = JCO.createRepository("MYRepository", poolName);
                        }
                        // 获得一个指定函数名的函数模板
                        IFunctionTemplate ft = repository.getFunctionTemplate(functionName.toUpperCase());
                        JCO.Function function = ft.getFunction();
                        JCO.ParameterList input = function.getImportParameterList();
                        if (selectParam != null && selectParam.size() > 0) {
                            Iterator<?> it = selectParam.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<?, ?> entry = (Entry<?, ?>) it.next();
                                String key = (String) entry.getKey();
                                String value = (String) entry.getValue();
                                input.setValue(value, key);
                            }
                        }
                        connection.execute(function);
                        // 输出参数
                        JCO.ParameterList output = function.getExportParameterList();
                        for (String param : resultParam) {
                            map.put(param, output.getString(param));
                        }
                    } else {
                        System.out.println("======error:sap连接失败");
                    }
                } else {
                    System.out.println("======error:参数为空");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                releaseConnection(connection);
            }
        }
        return map;
    }

    @SuppressWarnings("deprecation")
    public static void createConnectionPool(String poolName, int maxConnection, Properties logonProperties) {
        JCO.Pool pool = JCO.getClientPoolManager().getPool(poolName);
        if (pool == null) {
            JCO.addClientPool(poolName,         // 连接池名
                    maxConnection,    // 最大连接数
                    logonProperties); // 登录参数
        }
    }

    @SuppressWarnings("deprecation")
    public static JCO.Client getConnectionInPool(String poolName) {
        JCO.Client connection = null;
        if (groupFlag) { //组登录
            connection = JCO.createClient(SAP_client, SAP_user, SAP_passwd, "zh", SAP_mshost, SAP_r3name, SAP_group);
        } else { //ip登录
            JCO.Pool pool = JCO.getClientPoolManager().getPool(poolName);
            if (pool != null) {
                connection = JCO.getClient(poolName);
                //pool.setAbapDebug(true);
            }
        }
        return connection;
    }

    @SuppressWarnings("deprecation")
    public static void releaseConnection(JCO.Client connection) {
        JCO.releaseClient(connection);
    }

    @SuppressWarnings("deprecation")
    public static void removeConnectionPool(String poolName) {
        JCO.removeClientPool(poolName);
    }

    /**
     * 加载配置文件
     */
    public static Properties loadProperties(String fileName) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            properties = null;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }


    @SuppressWarnings("unchecked")
    public static void test1() {
        String functionName = "Z_PROPLAN_CKJH_BZL";
        HashMap<String, String> param = new HashMap<String, String>();
        String[] selectTable = {"IT_WERKS", "IT_DISPO", "IT_LGORT", "IT_MATNR"};
        HashMap<String, String>[][] selectParam = new HashMap[selectTable.length][2];
        selectParam[3][0] = new HashMap<String, String>();
        selectParam[3][0].put("MATNR", "79100200");
        param.put("BDTER", "20100929");
        String[] resultTable = {"OT_PU"};
        String[] resultSelectParam1 = {"LGORT", "MATNR", "MAKTX", "MEINS", "BDMNG"};
        String[][] resultSelectParam = new String[1][resultSelectParam1.length];
        resultSelectParam[0] = resultSelectParam1;
        SapReturnObject obj = SapConnectionPool.getResultNew("0", functionName, param, selectTable, selectParam, resultTable, resultSelectParam, null);
        HashMap<String, List<HashMap<String, String>>> resultList = obj.getTables();
        if (resultList != null && resultList.size() > 0) {
            for (int k = 0; k < resultTable.length; k++) {
                for (int i = 0; i < resultList.get(resultTable[k]).size(); i++) {
                    for (int j = 0; j < resultSelectParam[k].length; j++) {
                        System.out.println("======[" + resultSelectParam[k][j] + "][" + resultTable[k] + "][" + k + "][" + j + "]:" + resultList.get(resultTable[k]).get(i).get(resultSelectParam[k][j]));
                    }
                }
            }
        }

    }


    public static void test2() {
        String functionName = "ZSDID_CREDIT2SQL";
        String[] selectTable = {"T_CUSTOM"};
        HashMap<String, String[]>[] selectParam = new HashMap[1];
        selectParam[0] = new HashMap<String, String[]>();
        selectParam[0].put("KUNNR", new String[]{"1010700180".substring(0, 9) + "0"});
        selectParam[0].put("KKBER", new String[]{"HT01"});
        String resultTable = "T_CUSTOM_CREDIT";
        String[] resultParam = {"KUNNR", "OBLIG"};
        List<HashMap<String, String>> resultList;
        try {
            resultList = SapHttpClient.doRequestForList("0", functionName, selectTable, selectParam, resultTable, resultParam);
            if (resultList != null && resultList.size() > 0) {
                for (int i = 0; i < resultList.size(); i++) {
                    for (int j = 0; j < resultParam.length; j++) {
                        System.out.println("======[" + resultParam[j] + "][" + i + "]:" + resultList.get(i).get(resultParam[j]));
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void test3() {


    }

    public static void main(String[] args) {
        //test1();
        test2();


    }

}