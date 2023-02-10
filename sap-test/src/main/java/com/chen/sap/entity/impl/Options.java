package com.chen.sap.entity.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.chen.sap.anno.SapField;
import com.chen.sap.anno.SapTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenwh3
 */
@Data
@NoArgsConstructor
public class Options<T> {

    @SapField(ignore = true)
    private String tableName;

    private String sign = "I";
    private String option ;
    private T low ;
    private T high ;

    public static <O> List<Options<O>> buildEqList(String tableName , List<O> list){
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(e -> {
            Options<O> options = new Options<>();
            options.setLow(e);
            options.setTableName(tableName);
            options.setOption("EQ");
            return options;
        }).collect(Collectors.toList());
    }

    public static <O> List<Options<O>> buildBetween(String tableName, O low, O high) {
        if (low == null) {
            return Collections.emptyList();
        }
        Options<O> options = new Options<>();
        options.setOption("EQ");
        options.setTableName(tableName);
        options.setLow(low);
        if (high != null) {
            options.setOption("BT");
            options.setHigh(high);
        }
        return ListUtil.of(options);
    }
}
