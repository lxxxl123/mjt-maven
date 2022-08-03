package com.chen.sap.sap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * schedulejob
 *
 * @author yangzj2
 * @date 2019/9/7
 */

public class SapReturnObject implements Serializable {
    /**
     * 接口执行情况，true成功，false失败
     */
    private boolean isSuccess=false;//true成功，false失败
    /**
     * 错误信息
     */
    private String errorMsg="";
    /**
     * 返回tables
     */
    private HashMap<String,List<HashMap<String ,String>>> tables;

    /**
     * 返回tablesField 所有字段类型
     * 作者：易登科
     * 开发时间：2017-07-25
     */
    private HashMap<String,List<HashMap<String ,String>>> tablesField;
    /**
     * 返回exports
     */
    private HashMap<String, Object> exports;
    /**
     * 返回tables
     */
    public HashMap<String, List<HashMap<String, String>>> getTables() {
        return tables;
    }
    /**
     * 设置返回的tables
     */
    public void setTables(HashMap<String, List<HashMap<String, String>>> tables) {
        this.tables = tables;
    }
    /**
     * 返回exports
     */
    public HashMap<String, Object> getExports() {
        return exports;
    }
    /**
     * 设置返回的exports
     */
    public void setExports(HashMap<String, Object> exports) {
        this.exports = exports;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public boolean isSuccess() {
        return isSuccess;
    }
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    public HashMap<String,List<HashMap<String ,String>>> getTablesField() {
        return tablesField;
    }
    public void setTablesField(HashMap<String,List<HashMap<String ,String>>> tablesField) {
        this.tablesField = tablesField;
    }

}