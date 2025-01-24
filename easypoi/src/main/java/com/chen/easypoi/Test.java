package com.chen.easypoi;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Test {

    /**
     * 输入列名 ,
     */
    public static void export() throws IOException {
        String title = "";
        String sheetName = "";
        List<ExcelExportEntity> entries = JSONObject.parseArray("[{\"key\":\"name\",\"name\":\"名字\"},{\"key\":\"sex\",\"name\":\"性别\"}]", ExcelExportEntity.class);
        List<Map> dataList = JSONObject.parseArray("[{\"name\":\"chenwh\",\"sex\":\"boy\"},{\"name\":\"ywj\",\"sex\":\"girl\"}]", Map.class);
        Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), entries, dataList);
        sheets.write(new FileOutputStream("./excel.xls"));


    }

}
